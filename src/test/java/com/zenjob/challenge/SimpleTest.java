package com.zenjob.challenge;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SimpleTest {

    @Test
    public void shouldDoSomething() {
        int x = 1;
        int y = 1;
        Assertions.assertEquals(x, y);
    }

}
