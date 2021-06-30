package com.navya.soutions.proxy;

import com.navya.soutions.graphql.type.AppDetail;

import java.util.Set;

public interface GrpcServiceProxy {

    Set<AppDetail> getAppDetails();
}
