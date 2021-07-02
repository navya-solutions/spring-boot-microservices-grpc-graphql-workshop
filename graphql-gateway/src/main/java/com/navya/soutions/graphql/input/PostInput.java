package com.navya.soutions.graphql.input;


import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
public class PostInput implements Serializable {
    private String title;
    private PostDetailInput detail;
    private Set<CommentInput> comments;
    private Set<TagInput> tags;
}

