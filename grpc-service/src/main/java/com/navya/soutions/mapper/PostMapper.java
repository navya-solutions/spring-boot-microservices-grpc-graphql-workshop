package com.navya.soutions.mapper;

import com.navya.solutions.grpc.proto.service.PostRequest;
import com.navya.solutions.grpc.proto.service.PostResponse;
import com.navya.soutions.domain.PostEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring", collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface PostMapper {

    // Mappings from Grpc Objects ---> JPA Objects
    @Mappings({
            @Mapping(target = "comments", source = "post.postCommentList"),
            @Mapping(target = "tags", source = "post.postTagList"),
            @Mapping(target = "detail", source = "post.postDetail"),
            @Mapping(target = "title", source = "post.title"),
    })
    PostEntity mapGrpcApiToJpaDataModel(PostRequest source);

    // Mappings from JPA Objects ---> Grpc Objects
    @Mappings({
            @Mapping(target = "post.postCommentList", source = "comments"),
            @Mapping(target = "post.postTagList", source = "tags"),
            @Mapping(target = "post.postDetail", source = "detail"),
            @Mapping(target = "post.title", source = "title"),
    })
    PostResponse mapJpaDataModelToGrpcApi(PostEntity source);


}
