package com.navya.soutions.mapper;

import com.navya.solutions.grpc.proto.service.PostRequest;
import com.navya.solutions.grpc.proto.service.PostResponse;
import com.navya.soutions.graphql.input.PostInput;
import com.navya.soutions.graphql.type.PostType;
import org.mapstruct.*;

@Mapper(componentModel = "spring", collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface PostMapper {

    // Mappings from GraphQL --> Grpc
    @Mappings({
            @Mapping(target = "post.postCommentList", source = "comments"),
            @Mapping(target = "post.postTagList", source = "tags"),
            @Mapping(target = "post.postDetail", source = "detail"),
            @Mapping(target = "post.title", source = "title"),
    })
    PostRequest mapGraphQLApiToGrpcApiDataModel(PostInput source);

    // Mappings from  Grpc --> GraphQL
    @Mappings({
            @Mapping(target = "comments", source = "post.postCommentList"),
            @Mapping(target = "tags", source = "post.postTagList"),
            @Mapping(target = "detail", source = "post.postDetail"),
            @Mapping(target = "title", source = "post.title"),
            @Mapping(target = "id", source = "externalIdentifier"),
    })
    PostType mapGrpcApiToGraphQLApiDataModel(PostResponse source);


}
