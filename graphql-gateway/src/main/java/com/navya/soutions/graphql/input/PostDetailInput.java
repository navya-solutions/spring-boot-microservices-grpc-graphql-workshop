package com.navya.soutions.graphql.input;

import lombok.Data;

import java.io.Serializable;


@Data
public class PostDetailInput implements Serializable {
    private Integer createdOn;
    private String createdBy;
}
