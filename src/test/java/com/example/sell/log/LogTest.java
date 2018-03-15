package com.example.sell.log;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class LogTest {
    @Test
    public void testDefault() {
        log.trace("trace...");
        log.debug("debug...");
        log.info("info...");
        log.error("error...");
        log.warn("warn...");
    }
}
