package com.navya.soutions.graphql.type;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Builder
@ToString
@EqualsAndHashCode
public class AppDetail {
    private String identifier;
    private String appName, message, version;
}
