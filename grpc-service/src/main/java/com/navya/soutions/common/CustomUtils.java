package com.navya.soutions.common;

import org.springframework.core.NestedExceptionUtils;

import java.util.Locale;
import java.util.UUID;

public final class CustomUtils {

    private CustomUtils() throws UnsupportedOperationException {
        throw new UnsupportedOperationException(
                "Do not instantiate this class, use statically.");
    }

    public static Long getEpochCurrentTime() {
        return System.currentTimeMillis() / 1000; //Returns epoch in seconds.
    }

    public static String createExternalIdentifier() {
        return UUID.randomUUID().toString().toUpperCase(Locale.ROOT).replace("-", "");
    }

    public static Throwable getRootException(final Throwable exception) {
        return NestedExceptionUtils.getMostSpecificCause(exception);
    }


}
