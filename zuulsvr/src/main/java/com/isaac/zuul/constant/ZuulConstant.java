package com.isaac.zuul.constant;

public interface ZuulConstant {
    String CORRELATION_ID = "tmx-correlation-id";
    String AUTH_TOKEN     = "tmx-auth-token";
    String USER_ID        = "tmx-user-id";
    String ORG_ID         = "tmx-org-id";
    String SERVICE_ID     = "serviceId";

    String PRE_FILTER_TYPE = "pre";
    String POST_FILTER_TYPE = "post";
    String ROUTE_FILTER_TYPE = "route";
    int FILTER_ORDER = 1;
    boolean SHOULD_FILTER = true;
}
