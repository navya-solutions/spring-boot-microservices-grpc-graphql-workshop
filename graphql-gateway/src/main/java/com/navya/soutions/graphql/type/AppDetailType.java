package com.navya.soutions.graphql.type;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Builder
@ToString
@EqualsAndHashCode
public class AppDetailType implements Serializable {
    private String identifier;
    private String appName, message, version;
}
