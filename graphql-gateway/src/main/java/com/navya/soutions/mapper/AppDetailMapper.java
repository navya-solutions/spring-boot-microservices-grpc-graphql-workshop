package com.navya.soutions.mapper;

import com.navya.solutions.grpc.proto.service.AppResourceResponse;
import com.navya.soutions.graphql.type.AppDetailType;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface AppDetailMapper {

    // Mappings from Grpc Objects ---> GraphQL Objects

    AppResourceResponse mapJpaDataModelToGrpcApi(AppDetailType source);

    @Mappings({
            @Mapping(target = ".", source = "appResourcesList"),
    })
    Set<AppDetailType> mapJpaDataModelToGrpcApi(List<AppResourceResponse> source);

}
