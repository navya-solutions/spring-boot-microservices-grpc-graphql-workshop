package com.navya.soutions.grpc.controller;

import com.google.protobuf.Empty;
import com.navya.solutions.grpc.proto.service.*;
import com.navya.soutions.domain.PostCommentEntity;
import com.navya.soutions.domain.PostEntity;
import com.navya.soutions.domain.PostTagEntity;
import com.navya.soutions.exception.CustomException;
import com.navya.soutions.mapper.AppDetailMapper;
import com.navya.soutions.mapper.PostMapper;
import com.navya.soutions.service.AppDetailService;
import com.navya.soutions.service.PostService;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@GRpcService
@AllArgsConstructor
@Transactional
@Slf4j(topic = "SAMPLE_GRPC_CONTROLLER")
public class SampleGrpcController extends com.navya.solutions.grpc.proto.service.SampleServiceGrpc.SampleServiceImplBase {

    final private AppDetailService appDetailService;
    final private AppDetailMapper appDetailMapper;
    final private PostService postService;
    final private PostMapper postMapper;


    @Override
    public void getAppDetail(AppResourceRequest request, StreamObserver<AppResourcesResponse> responseObserver) {
        log.info(">>>>>>>>>>>>>>>> GET APP DETAILS REQUEST {}", request);
        final Set<AppResourceResponse> appResourceResponses = appDetailMapper.mapJpaDataModelToGrpcApi(appDetailService.getAppDetails());
        responseObserver.onNext(AppResourcesResponse
                .newBuilder()
                .addAllAppResources(appResourceResponses)
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void createPost(PostRequest request, StreamObserver<PostResponse> responseObserver) {
        log.info(">>>>>>>>>>>>>>>> CREATE POST REQUEST {}", request);
        final PostEntity post = postService.createPost(postMapper.mapGrpcApiToJpaDataModel(request));
        responseObserver.onNext(postMapper.mapJpaDataModelToGrpcApi(post));
        responseObserver.onCompleted();
    }

    @Override
    public void getPost(GetPostRequest request, StreamObserver<PostResponse> responseObserver) {
        log.info(">>>>>>>>>>>>>>>> GET POST REQUEST {}", request);
        postService.getPost(request.getExternalIdentifier()).ifPresentOrElse((p -> {
            responseObserver.onNext(postMapper.mapJpaDataModelToGrpcApi(p));
            responseObserver.onCompleted();
        }), () -> {
            String format = String.format("Post not found for %s", request.toString());
            throw new CustomException(
                    format,
                    Status.NOT_FOUND.getCode().value());
        });
    }

    @Override
    public void deletePost(DeletePostRequest request, StreamObserver<Empty> responseObserver) {
        log.info(">>>>>>>>>>>>>>>> DELETE POST REQUEST {}", request);
        postService.deletePost(request.getExternalIdentifier());
        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    public void addComment(AddCommentRequest request, StreamObserver<PostResponse> responseObserver) {
        log.info(">>>>>>>>>>>>>>>> ADD POST COMMENT REQUEST {}", request);
        PostCommentEntity postComment = new PostCommentEntity();
        postComment.setReview(request.getPostComment().getReview());
        final PostEntity post = postService.addComment(request.getExternalIdentifier(), postComment);
        responseObserver.onNext(postMapper.mapJpaDataModelToGrpcApi(post));
        responseObserver.onCompleted();
    }

    @Override
    public void addTag(AddTagRequest request, StreamObserver<PostResponse> responseObserver) {
        log.info(">>>>>>>>>>>>>>>> ADD POST TAG REQUEST {}", request);
        PostTagEntity tagEntity = new PostTagEntity();
        tagEntity.setName(request.getPostTag().getName());
        final PostEntity postEntity = postService.addTag(request.getExternalIdentifier(), tagEntity);
        responseObserver.onNext(postMapper.mapJpaDataModelToGrpcApi(postEntity));
        responseObserver.onCompleted();
    }
}
