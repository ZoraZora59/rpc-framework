package com.zora.rpc.spring;

import com.zora.rpc.client.handler.IRpcHandler;
import com.zora.rpc.client.server.ServerSocketManager;
import com.zora.rpc.spring.annotion.RpcServiceImpl;
import com.zora.rpc.spring.handler.RpcHandler;
import com.zora.rpc.spring.property.RpcProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * <h3>rpc-framework</h3>
 * <h4>com.zora.rpc.spring</h4>
 * <p>spring结合使用</p>
 *
 * @author zora
 * @since 2021.02.05
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(RpcProperties.class)
public class RpcAutoConfiguration {
    @Autowired
    private RpcProperties properties;

    @Autowired
    ApplicationContext context;

    public Map<String,Object> beanMap() {
        Map<String, Object> sourceMap = context.getBeansWithAnnotation(RpcServiceImpl.class);
        Map<String, Object> beanMap = new HashMap<>(sourceMap.size());
        sourceMap.values().forEach(clz -> {
            Class<?> anInterface = clz.getClass().getInterfaces()[0];
            String interfaceName = anInterface.getName();
            beanMap.put(interfaceName,clz);
        });
        return Collections.unmodifiableMap(beanMap);
    }
    public Map<String, Method> methodMap(Map<String,Object> beanMap) {
        Map<String, Object> sourceMap = context.getBeansWithAnnotation(RpcServiceImpl.class);
        Map<String, Method> methodMap = new HashMap<>();
        beanMap.forEach((name,bean) -> {
            for (Method method : bean.getClass().getInterfaces()[0].getMethods()) {
                methodMap.put(name + method.getName(), method);
            }
        });
        log.info("初始化RPC接口\nBeanMap=【{}】\nMethodMap=【{}】", beanMap, methodMap);
        return  Collections.unmodifiableMap(methodMap);
    }

    @Bean
    public ServerSocketManager buildManager(){
        Map<String,Object> beanMap =  beanMap();
        IRpcHandler handler = new RpcHandler(beanMap,methodMap(beanMap));
        return new ServerSocketManager(handler,properties.getPort(),properties.getCoreThread(),properties.getMaxThread(),properties.getQueueLength());
    }
}
