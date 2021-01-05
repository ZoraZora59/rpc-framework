package com.zora.rpc.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <h3>rpc-framework</h3>
 * <h4>com.zora.rpc.common.model</h4>
 * <p>rpc 请求体</p>
 *
 * @author zora
 * @since 2021.01.05
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RpcRequest implements Serializable {
    private String requestId;
    private String requestTarget;
    private Object[] requestBody;
}
