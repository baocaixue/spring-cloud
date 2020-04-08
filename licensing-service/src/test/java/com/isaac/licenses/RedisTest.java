package com.isaac.licenses;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisTest {
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Test
    @Ignore
    public void testRedis() {
        redisConnectionFactory.getConnection().flushDb();
    }
}
