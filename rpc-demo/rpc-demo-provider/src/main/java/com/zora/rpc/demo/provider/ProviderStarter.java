package com.zora.rpc.demo.provider;

import com.zora.rpc.client.server.ServerSocketManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <h3>rpc-framework</h3>
 * <h4>com.zora.rpc.demo.provider</h4>
 * <p>生产者启动器</p>
 *
 * @author zora
 * @since 2021.01.05
 */
@Slf4j
@SpringBootApplication
public class ProviderStarter implements CommandLineRunner {
    @Autowired
    ServerSocketManager manager;
    public static void main(String[] args) {
        SpringApplication.run(ProviderStarter.class);
    }
// TODO:优雅停机、nio、客户端调用实例

    @Override
    public void run(String... args) throws Exception {
        System.out.println(manager);
    }
}
