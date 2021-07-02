package com.navya.soutions.graphql.type;

import lombok.Data;

import java.io.Serializable;


@Data
public class PostDetailType implements Serializable {
    private Integer createdOn;
    private String createdBy;
}
