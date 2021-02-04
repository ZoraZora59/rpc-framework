package com.zora.rpc.client.conn;

import com.zora.rpc.common.model.RpcRequest;
import com.zora.rpc.common.model.RpcResponse;

public interface IConnector {

    RpcResponse<?> call(RpcRequest request);

    RpcResponse<?> callAsync(RpcRequest request);
}
