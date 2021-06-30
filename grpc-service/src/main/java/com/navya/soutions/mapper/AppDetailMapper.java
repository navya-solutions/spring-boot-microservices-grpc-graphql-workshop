package com.navya.soutions.mapper;

import com.navya.soutions.dto.AppDetail;
import org.mapstruct.*;

import java.util.Set;

@Mapper(componentModel = "spring", collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface AppDetailMapper {

    // Mappings from Grpc Objects ---> JPA Objects


    // Mappings from JPA Objects ---> Grpc Objects

    com.navya.solutions.grpc.proto.service.AppResourceResponse mapJpaDataModelToGrpcApi(AppDetail source);

    @Mappings({
            @Mapping(target = "appResourcesList", source = "."),
    })
    Set<com.navya.solutions.grpc.proto.service.AppResourceResponse> mapJpaDataModelToGrpcApi(Set<AppDetail> source);

}
