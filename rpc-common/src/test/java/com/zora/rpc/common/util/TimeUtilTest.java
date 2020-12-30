package com.zora.rpc.common.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

/**
 * <h3>rpc-framework</h3>
 * <h4>com.zora.rpc.common.util</h4>
 * <p>时间戳工具测试</p>
 *
 * @author zora
 * @since 2020.12.30
 */
@Slf4j
public class TimeUtilTest {
    @Test
    public void loadTest() {
        log.info("方法加载");
        try {
            Thread.sleep(50);
        } catch (InterruptedException ignore) {
        }
        log.info("类加载");
        try {
            Thread.sleep(50);
        } catch (InterruptedException ignore) {
        }
        long current = TimeUtil.currentTimestamp();
        log.info("时间戳获取");
        try {
            Thread.sleep(100);
        } catch (InterruptedException ignore) {
        }
        Assert.assertNotEquals(current, TimeUtil.currentTimestamp());
    }
}
