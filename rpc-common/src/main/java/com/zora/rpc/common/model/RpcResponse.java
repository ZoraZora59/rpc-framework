package com.zora.rpc.common.model;

import com.zora.rpc.common.exception.RpcException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <h3>rpc-framework</h3>
 * <h4>com.zora.rpc.common.model</h4>
 * <p>rpc 响应体</p>
 *
 * @author zora
 * @since 2021.01.05
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RpcResponse<T> implements Serializable {
    private String requestId;
    private Boolean success;
    private Integer code;
    private String message;
    private T data;
    private RpcException exception;
}
