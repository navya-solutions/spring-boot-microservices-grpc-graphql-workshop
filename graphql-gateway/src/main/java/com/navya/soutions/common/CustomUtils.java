package com.navya.soutions.common;

import com.navya.soutions.graphql.error.GraphQLErrorHandler;
import io.grpc.Metadata;
import io.grpc.StatusRuntimeException;
import org.springframework.core.NestedExceptionUtils;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static io.grpc.Metadata.ASCII_STRING_MARSHALLER;
import static io.grpc.Metadata.BINARY_BYTE_MARSHALLER;

public final class CustomUtils {

    public static final Metadata.Key<String> ERROR_CODE = Metadata.Key.of("errorcode", ASCII_STRING_MARSHALLER);
    public static final Metadata.Key<byte[]> ERROR_MESSAGE = Metadata.Key.of("errormessage-bin", BINARY_BYTE_MARSHALLER);
    public static final Metadata.Key<byte[]> ERROR_DETAIL = Metadata.Key.of("errordetail-bin", BINARY_BYTE_MARSHALLER);
    public static final Predicate<String> isEmpty = x -> x == "";
    public static final Predicate<String> isNull = x -> x == null;

    private CustomUtils() throws UnsupportedOperationException {
        throw new UnsupportedOperationException(
                "Do not instantiate this class, use statically.");
    }

    public static String createIdentifier() {
        return UUID.randomUUID()
                .toString()
                .toUpperCase(Locale.ROOT)
                .replace("-", "");
    }

    public static <T> void safeSet(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }

    public static Boolean isEmptyOrNull(String value) {
        return isEmpty.test(value) || isNull.test(value);
    }

    public static ZonedDateTime getDateInUTC(long date) {
        return ZonedDateTime.ofInstant(Instant.ofEpochSecond(date), ZoneOffset.UTC);
    }

    public static Throwable getRootException(Throwable exception) {
        return NestedExceptionUtils.getMostSpecificCause(exception);

    }

    public static void exceptionHandler(Exception e) {
        String message = e.getLocalizedMessage();
        String details = "";
        String code = "";
        if (e instanceof StatusRuntimeException) {
            StatusRuntimeException statusRuntimeException = (StatusRuntimeException) e;
            Metadata trailers = statusRuntimeException.getTrailers();
            if (trailers != null && trailers.keys().size() > 0) {
                byte[] msg = trailers.get(CustomUtils.ERROR_MESSAGE);
                if (msg != null)
                    message = new String(msg);
                byte[] detail = trailers.get(CustomUtils.ERROR_DETAIL);
                if (detail != null)
                    details = new String(detail);
                String error = trailers.get(CustomUtils.ERROR_CODE);
                if (error != null)
                    code = error;

            } else {
                code = statusRuntimeException.getStatus().getDescription();
                message = statusRuntimeException.getLocalizedMessage();
                details = statusRuntimeException.getMessage();

            }
        } else if (e instanceof GraphQLErrorHandler) {
            code = String.valueOf(((GraphQLErrorHandler) e).getExtensions().get("code"));
            details = (String) ((GraphQLErrorHandler) e).getExtensions().get("details");
            message = (String) ((GraphQLErrorHandler) e).getExtensions().get("message");
        } else {
            Throwable rootException = CustomUtils.getRootException(e);
            code = "500";
            details = rootException.getLocalizedMessage() == null ? "java.lang.NullPointerException"
                    : rootException.getLocalizedMessage();
            message = rootException.getLocalizedMessage() == null ? "java.lang.NullPointerException"
                    : rootException.getLocalizedMessage();
        }
        if (details != null && details.isBlank())
            details = message;
        throw new GraphQLErrorHandler(code, message, details);
    }


}
