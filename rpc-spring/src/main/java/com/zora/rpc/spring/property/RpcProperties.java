package com.zora.rpc.spring.property;

import com.zora.rpc.common.model.ServiceMetaData;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * <h3>rpc-framework</h3>
 * <h4>com.zora.rpc.spring.properties</h4>
 * <p>全局配置</p>
 *
 * @author zora
 * @since 2021.02.05
 */
@Data
@ConfigurationProperties(prefix = "rpc")
public class RpcProperties {
    boolean provider;
    boolean consumer;
    List<ServiceMetaData> listen;
    String appName;
    int port;
    int coreThread;
    int maxThread;
    int queueLength;
}
