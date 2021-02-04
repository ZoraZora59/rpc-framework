package com.zora.rpc.common.model;

import lombok.Data;

/**
 * <h3>rpc-framework</h3>
 * <h4>com.zora.rpc.common.model</h4>
 * <p>远程服务元信息</p>
 *
 * @author zora
 * @since 2021.02.04
 */
@Data
public class ServiceMetaData {
    private String appName;
    private String ipAddress;
    private int port;
    private int connCount;
}
