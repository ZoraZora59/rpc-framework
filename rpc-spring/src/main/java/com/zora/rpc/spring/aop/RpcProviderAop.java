package com.zora.rpc.spring.aop;

import com.zora.rpc.spring.annotion.RpcServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * <h3>rpc-framework</h3>
 * <h4>com.zora.rpc.spring.aop</h4>
 * <p></p>
 *
 * @author zora
 * @since 2021.02.05
 */
@Slf4j
@Configuration
public class RpcProviderAop {
    @Autowired
    ApplicationContext context;

//    @Pointcut("@annotation(com.zora.rpc.spring.annotion.RpcServiceImpl)") // 切入点表达式
//    private void pointCutSignature() {
//    }// 方法签名
//
//    @Around("pointCutSignature()")
//    public void watchPerformance(ProceedingJoinPoint jp) {
//        try {
//            jp.proceed();
//        } catch (Throwable e) {
//            System.out.println("Demanding a refund");
//        }
//    }


}
