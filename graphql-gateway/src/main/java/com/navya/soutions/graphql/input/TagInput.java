package com.navya.soutions.graphql.input;

import lombok.Data;

import java.io.Serializable;


@Data
public class TagInput implements Serializable {
    private String name;
}
