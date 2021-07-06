package com.navya.soutions.proxy;

import brave.grpc.GrpcTracing;
import com.navya.solutions.grpc.proto.service.*;
import com.navya.soutions.common.CustomUtils;
import com.navya.soutions.graphql.input.CommentInput;
import com.navya.soutions.graphql.input.PostInput;
import com.navya.soutions.graphql.input.TagInput;
import com.navya.soutions.graphql.type.AppDetailType;
import com.navya.soutions.graphql.type.PostType;
import com.navya.soutions.mapper.AppDetailMapper;
import com.navya.soutions.mapper.PostMapper;
import io.grpc.ClientInterceptor;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Slf4j(topic = "GRPC_SERVICE_PROXY_CLIENT")
public class GrpcServiceProxyClient implements GrpcServiceProxy {
    private final ManagedChannel channel;
    private final SampleServiceGrpc.SampleServiceBlockingStub sampleServiceBlockingStub;
    private final AppDetailMapper appDetailMapper;
    private final PostMapper postMapper;
    private final GrpcTracing grpcTracing;

    //private Tracer tracer;

    public GrpcServiceProxyClient(final String host, int port, final AppDetailMapper appDetailMapper,
                                  final PostMapper postMapper, final GrpcTracing grpcTracing) {
        this.grpcTracing = grpcTracing;
        final ClientInterceptor clientInterceptor = grpcTracing
                .newClientInterceptor();

        /*TracingClientInterceptor tracingInterceptor = TracingClientInterceptor
                .newBuilder()
                .withTracer(this.tracer)
                .build();
*/

        this.channel = ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext()
                .intercept(clientInterceptor)
                .build();
        this.sampleServiceBlockingStub = SampleServiceGrpc
                //.newBlockingStub(tracingInterceptor.intercept(channel));
                .newBlockingStub(channel);
        this.appDetailMapper = appDetailMapper;
        this.postMapper = postMapper;

    }

    @Override
    public Set<AppDetailType> getAppDetails() {
        final String identifier = CustomUtils.createIdentifier();
        log.info(">>>>>>>>>>>>>>>> GET APP DETAILS REQUEST for {}", identifier);
        final AppResourcesResponse appDetail = sampleServiceBlockingStub.getAppDetail(AppResourceRequest
                .newBuilder()
                .setIdentifier(identifier)
                .build());
        return appDetailMapper.mapJpaDataModelToGrpcApi(appDetail.getAppResourcesList());

    }

    @Override
    public PostType createPost(PostInput postInput) {
        log.info(">>>>>>>>>>>>>>>> CREATE POST REQUEST for {}", postInput);
        final PostResponse post = sampleServiceBlockingStub.createPost(postMapper.mapGraphQLApiToGrpcApiDataModel(postInput));
        final PostType postType = postMapper.mapGrpcApiToGraphQLApiDataModel(post);
        return postType;
    }

    @Override
    public void deletePost(String postId) {
        log.info(">>>>>>>>>>>>>>>> DELETE POST  REQUEST for {}", postId);
        sampleServiceBlockingStub.deletePost(DeletePostRequest
                .newBuilder()
                .setExternalIdentifier(postId)
                .build());

    }

    @Override
    public PostType addComment(String postId, CommentInput commentInput) {
        log.info(">>>>>>>>>>>>>>>> ADD COMMENT REQUEST for post id is {} and comment is {}", postId, commentInput);
        final PostResponse postResponse = sampleServiceBlockingStub.addComment(AddCommentRequest
                .newBuilder()
                .setExternalIdentifier(postId)
                .setPostComment(PostComment
                        .newBuilder()
                        .setReview(commentInput.getReview())
                        .build())
                .build());
        return postMapper.mapGrpcApiToGraphQLApiDataModel(postResponse);
    }

    @Override
    public PostType addTag(String postId, TagInput tagInput) {
        log.info(">>>>>>>>>>>>>>>> ADD TAG REQUEST for post id is {} and tag is {}", postId, tagInput);
        final PostResponse postResponse = sampleServiceBlockingStub.addTag(AddTagRequest
                .newBuilder()
                .setExternalIdentifier(postId)
                .setPostTag(PostTag
                        .newBuilder()
                        .setName(tagInput.getName())
                        .build())
                .build());
        return postMapper.mapGrpcApiToGraphQLApiDataModel(postResponse);
    }

    @Override
    public PostType getPost(String postId) {
        log.info(">>>>>>>>>>>>>>>> ADD POST REQUEST for post id {} ", postId);
        return postMapper.mapGrpcApiToGraphQLApiDataModel(sampleServiceBlockingStub.getPost(GetPostRequest
                .newBuilder()
                .setExternalIdentifier(postId)
                .build()));
    }
}
