package com.navya.soutions.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Builder
@Data
public class AppDetail implements Serializable {
    private String identifier;
    private String appName, message, version;
}
