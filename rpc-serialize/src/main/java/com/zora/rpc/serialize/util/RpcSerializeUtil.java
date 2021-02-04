package com.zora.rpc.serialize.util;

import com.alibaba.fastjson.JSON;
import com.zora.rpc.common.model.RpcRequest;
import com.zora.rpc.common.model.RpcResponse;
import com.zora.rpc.common.util.ByteConvertUtil;

import java.io.IOException;
import java.io.InputStream;

/**
 * <h3>rpc-framework</h3>
 * <h4>com.zora.rpc.serialize.util</h4>
 * <p>序列化工具</p>
 *
 * @author zora
 * @since 2021.01.05
 */
public class RpcSerializeUtil {
    public static RpcResponse<?> deserializeResponse(byte[] responseBytes) {
        return JSON.parseObject(responseBytes, RpcResponse.class);
    }

    public static RpcRequest deserializeRequest(byte[] responseBytes) {
        return JSON.parseObject(responseBytes, RpcRequest.class);
    }

    public static RpcResponse<?> deserializeResponse(InputStream inputStream) throws IOException {
        return JSON.parseObject(inputStream, RpcResponse.class);
    }

    public static RpcRequest deserializeRequest(InputStream inputStream) throws IOException, InterruptedException {
        byte[] bytes = new byte[4];
        int readCount = inputStream.read(bytes);
        if (readCount == -1) {
            throw new InterruptedException();
        }
        int range = ByteConvertUtil.bytesToInt(bytes);
        byte[] inputBytes = new byte[range];
        inputStream.read(inputBytes,0,range);
        return JSON.parseObject(inputBytes, RpcRequest.class);
    }

    public static byte[] serialize(RpcRequest request) {
        return JSON.toJSONBytes(request);
    }

    public static byte[] serialize(RpcResponse<?> request) {
        return JSON.toJSONBytes(request);
    }

}
