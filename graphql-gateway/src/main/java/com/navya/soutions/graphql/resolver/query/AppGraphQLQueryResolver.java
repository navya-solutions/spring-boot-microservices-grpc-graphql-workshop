package com.navya.soutions.graphql.resolver.query;

import com.navya.soutions.common.CustomUtils;
import com.navya.soutions.graphql.type.AppDetailType;
import com.navya.soutions.graphql.type.PostType;
import com.navya.soutions.proxy.GrpcServiceProxyClient;
import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Slf4j(topic = "QUERY_RESOLVER")
@RequiredArgsConstructor
public class AppGraphQLQueryResolver implements GraphQLQueryResolver {

    final private GrpcServiceProxyClient grpcServiceProxyClient;

    @Timed(value = "getAppDetails.time", description = "Time taken to return getAppDetails")
    public Set<AppDetailType> getAppDetails(DataFetchingEnvironment environment) {
        return grpcServiceProxyClient.getAppDetails();
    }

    @Timed(value = "getPost.time", description = "Time taken to return getPost")
    public PostType getPost(final String postId, DataFetchingEnvironment environment) {
        try {
            return grpcServiceProxyClient.getPost(postId);
        } catch (Exception e) {
            CustomUtils.exceptionHandler(e);
        }
        return null;
    }


}

