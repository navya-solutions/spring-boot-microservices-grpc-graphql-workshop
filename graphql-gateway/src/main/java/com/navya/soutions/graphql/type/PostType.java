package com.navya.soutions.graphql.type;

import com.navya.soutions.graphql.input.CommentInput;
import com.navya.soutions.graphql.input.PostDetailInput;
import com.navya.soutions.graphql.input.TagInput;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;


@Data
public class PostType implements Serializable {
    private String id, title;
    private PostDetailInput detail;
    private Set<CommentInput> comments;
    private Set<TagInput> tags;

}
