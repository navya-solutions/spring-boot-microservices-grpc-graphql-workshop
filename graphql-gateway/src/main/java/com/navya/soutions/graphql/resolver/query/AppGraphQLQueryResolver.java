package com.navya.soutions.graphql.resolver.query;

import com.navya.soutions.common.CustomUtils;
import com.navya.soutions.graphql.type.AppDetailType;
import com.navya.soutions.graphql.type.PostType;
import com.navya.soutions.proxy.GrpcServiceProxyClient;
import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Slf4j(topic = "QUERY_RESOLVER")
@RequiredArgsConstructor
public class AppGraphQLQueryResolver implements GraphQLQueryResolver {

    final private GrpcServiceProxyClient grpcServiceProxyClient;

    public Set<AppDetailType> getAppDetails(DataFetchingEnvironment environment) {
        return grpcServiceProxyClient.getAppDetails();
    }

    public PostType getPost(final String postId, DataFetchingEnvironment environment) {
        try {
            return grpcServiceProxyClient.getPost(postId);
        } catch (Exception e) {
            CustomUtils.exceptionHandler(e);
        }
        return null;
    }


}

