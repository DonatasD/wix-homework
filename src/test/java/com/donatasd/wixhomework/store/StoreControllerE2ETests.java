package com.donatasd.wixhomework.store;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StoreControllerE2ETests {

    @LocalServerPort
    private Integer port;

    @Test
    shouldCreateNewStore() {

    }
}
