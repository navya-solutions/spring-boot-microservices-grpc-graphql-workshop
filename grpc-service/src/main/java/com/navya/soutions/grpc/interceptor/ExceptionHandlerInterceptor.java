package com.navya.soutions.grpc.interceptor;

import com.navya.soutions.exception.CustomException;
import io.grpc.*;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcGlobalInterceptor;

import static io.grpc.Metadata.ASCII_STRING_MARSHALLER;

@Slf4j

@GRpcGlobalInterceptor
public class ExceptionHandlerInterceptor implements ServerInterceptor {


    public static final Metadata.Key<String> TRACE_ID = Metadata.Key.of("x-b3-traceid", ASCII_STRING_MARSHALLER);

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall, Metadata metadata,
                                                                 ServerCallHandler<ReqT, RespT> serverCallHandler) {
        // ... get trace info from metadata
        String traceId = metadata.get(TRACE_ID);
        metadata.keys().stream().forEach(s -> {
            log.info(">>>>>>>>>>>>> {}", s);

        });
        log.info(">>>>>>>>>>>>>traceId {} ", traceId);
        final ServerCall.Listener<ReqT> listener = serverCallHandler.startCall(serverCall, metadata);
        return new ExceptionHandlingServerCallListener<>(listener, serverCall, metadata);
    }

    private class ExceptionHandlingServerCallListener<ReqT, RespT>
            extends ForwardingServerCallListener.SimpleForwardingServerCallListener<ReqT> {
        private final ServerCall<ReqT, RespT> serverCall;
        private final Metadata metadata;

        ExceptionHandlingServerCallListener(ServerCall.Listener<ReqT> listener, ServerCall<ReqT, RespT> serverCall,
                                            Metadata metadata) {
            super(listener);
            this.serverCall = serverCall;
            this.metadata = metadata;
        }

        @Override
        public void onHalfClose() {
            try {
                super.onHalfClose();
            } catch (RuntimeException ex) {
                handleException(ex, serverCall, metadata);
                throw ex;
            }
        }


        @Override
        public void onReady() {
            try {
                super.onReady();
            } catch (RuntimeException ex) {
                handleException(ex, serverCall, metadata);
                throw ex;
            }
        }


        private void handleException(RuntimeException exception, ServerCall<ReqT, RespT> serverCall, Metadata metadata) {
            if (exception instanceof CustomException) {
                serverCall.close(Status.fromCodeValue(((CustomException) exception).getCode()).withDescription(exception.getMessage()), metadata);
            } else if (exception instanceof IllegalArgumentException) {
                serverCall.close(Status.INVALID_ARGUMENT.withDescription(exception.getMessage()), metadata);
            } else if (exception instanceof StatusRuntimeException) {
                serverCall.close(Status.INTERNAL.withDescription(exception.getMessage()).withCause(exception.getCause()), metadata);
            } else {
                serverCall.close(Status.UNKNOWN.withDescription(exception.getMessage()).withCause(exception.getCause()), metadata);
            }
        }

    }
}
