package com.navya.soutions.graphql.resolver.mutation;

import com.navya.soutions.common.CustomUtils;
import com.navya.soutions.graphql.input.CommentInput;
import com.navya.soutions.graphql.input.PostInput;
import com.navya.soutions.graphql.input.TagInput;
import com.navya.soutions.graphql.type.PostType;
import com.navya.soutions.proxy.GrpcServiceProxyClient;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j(topic = "POST_MUTATION_RESOLVER")
@AllArgsConstructor
@Component
public class PostMutationResolver implements GraphQLMutationResolver {

    final private GrpcServiceProxyClient grpcServiceProxyClient;

    public PostType createPost(PostInput postInput, DataFetchingEnvironment environment) {
        try {
            return grpcServiceProxyClient.createPost(postInput);
        } catch (Exception e) {
            CustomUtils.exceptionHandler(e);
        }
        return null;
    }

    public Boolean deletePost(String postId, DataFetchingEnvironment environment) {
        try {
            grpcServiceProxyClient.deletePost(postId);
        } catch (Exception e) {
            CustomUtils.exceptionHandler(e);
        }
        return true;
    }

    public PostType addComment(String postId, CommentInput commentInput, DataFetchingEnvironment environment) {
        try {
            return grpcServiceProxyClient.addComment(postId, commentInput);
        } catch (Exception e) {
            CustomUtils.exceptionHandler(e);
        }
        return null;
    }

    public PostType addTag(String postId, TagInput tagInput, DataFetchingEnvironment environment) {
        try {
            return grpcServiceProxyClient.addTag(postId, tagInput);
        } catch (Exception e) {
            CustomUtils.exceptionHandler(e);
        }
        return null;
    }

}
