package com.zora.rpc.common.exception;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * <h3>rpc-framework</h3>
 * <h4>com.zora.rpc.common.exception</h4>
 * <p>调用异常</p>
 *
 * @author zora
 * @since 2021.01.05
 */
public class RpcException extends RuntimeException {
    static final long serialVersionUID = -8345829929738856817L;
    @Getter
    @Setter
    private int rpcCode;
    @Getter
    @Setter
    private String rpcMessage;
    public RpcException(){}
    public RpcException(int code){
        this.rpcCode = code;
    }
    public RpcException(int code,String message){
        this.rpcCode=code;
        this.rpcMessage=message;
    }
}
