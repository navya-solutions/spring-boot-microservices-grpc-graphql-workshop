package com.navya.soutions.graphql.input;

import lombok.Data;

import java.io.Serializable;


@Data
public class CommentInput implements Serializable {
    private String review;
}
