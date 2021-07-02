package com.navya.soutions;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.event.annotation.BeforeTestClass;
//import static org.springframework.core.env.AbstractEnvironment.DEFAULT_PROFILES_PROPERTY_NAME;

@SpringBootTest
@ActiveProfiles("test")
class GrpcServiceApplicationTests {

    @BeforeTestClass
    public static void setupTest() {
        //System.setProperty(DEFAULT_PROFILES_PROPERTY_NAME, "test");
    }

    @Test
    void contextLoads() {

    }

}
