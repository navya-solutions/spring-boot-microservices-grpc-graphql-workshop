package com.navya.soutions.proxy;

import com.navya.soutions.graphql.input.CommentInput;
import com.navya.soutions.graphql.input.PostInput;
import com.navya.soutions.graphql.input.TagInput;
import com.navya.soutions.graphql.type.AppDetailType;
import com.navya.soutions.graphql.type.PostType;

import java.util.Set;

public interface GrpcServiceProxy {

    Set<AppDetailType> getAppDetails();

    PostType createPost(PostInput postInput);

    PostType addComment(String postId, CommentInput commentInput);

    void deletePost(String postId);

    PostType addTag(String postId, TagInput tagInput);

    PostType getPost(String postId);
}
