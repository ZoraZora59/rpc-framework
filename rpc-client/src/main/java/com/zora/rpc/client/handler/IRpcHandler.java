package com.zora.rpc.client.handler;

import com.zora.rpc.common.model.RpcRequest;
import com.zora.rpc.common.model.RpcResponse;

public interface IRpcHandler {
    RpcResponse<?> handle(RpcRequest request);
}
