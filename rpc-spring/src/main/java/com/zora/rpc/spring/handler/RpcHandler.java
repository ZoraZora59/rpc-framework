package com.zora.rpc.spring.handler;

import com.zora.rpc.client.handler.IRpcHandler;
import com.zora.rpc.common.model.RpcRequest;
import com.zora.rpc.common.model.RpcResponse;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * <h3>rpc-framework</h3>
 * <h4>com.zora.rpc.spring.handler</h4>
 * <p></p>
 *
 * @author zora
 * @since 2021.02.05
 */
public class RpcHandler implements IRpcHandler {
    public RpcHandler( Map<String, Object> beanMap,Map<String, Method> methodMap){
        this.beanMap=beanMap;
        this.methodMap=methodMap;
    }
    private final Map<String, Method> methodMap;
    private final Map<String, Object> beanMap;

    @Override
    public RpcResponse<?> handle(RpcRequest request) {
        String[] target = request.getRequestTarget().split("//");
        Object bean = beanMap.get(target[0]);
        if (Objects.isNull(bean)){
            return notFoundResponse(request.getRequestId());
        }
        Method method = methodMap.get(target[1]);
        if (Objects.isNull(method)){
            return notFoundResponse(request.getRequestId());
        }
        try {
            RpcResponse<?> response= (RpcResponse<?>) method.invoke(bean,request.getRequestBody());
            response.setRequestId(request.getRequestId());
            response.setCode(200);
            return response;
        }catch (IllegalAccessException | InvocationTargetException ignore){
        }

        return notFoundResponse(request.getRequestId());
    }
    static RpcResponse<Object> notFoundResponse(String reqId){
        RpcResponse<Object> response =  new RpcResponse<>();
        response.setCode(-1);
        response.setData(null);
        response.setRequestId(reqId);
        response.setMessage("no response for this call");
        return response;
    }
}
