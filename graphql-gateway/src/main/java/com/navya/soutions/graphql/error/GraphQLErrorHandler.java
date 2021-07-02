package com.navya.soutions.graphql.error;

import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GraphQLErrorHandler extends RuntimeException implements GraphQLError {
    private final Map<String, Object> extensions = new ConcurrentHashMap<>(3);

    public GraphQLErrorHandler(String code, String message, String details) {
        super(message);
        extensions.put("code", code);
        extensions.put("message", message);
        extensions.put("details", ObjectUtils.isEmpty(details) ? message : details);
    }

    public GraphQLErrorHandler(HttpStatus code, String details) {
        super(code.name());
        extensions.put("code", code.value());
        extensions.put("message", code.name());
        extensions.put("details", details);
    }

    @Override
    public Map<String, Object> getExtensions() {
        return extensions;
    }

    @Override
    public List<SourceLocation> getLocations() {
        return null;
    }

    @Override
    public ErrorType getErrorType() {
        return null;
    }
}
