package com.orhanararat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class IpFilterRuleEntityProjectApplicationTests {

    @Test
    void mainMethodStartsApplication() {
        assertDoesNotThrow(() -> IpFilterRuleProjectApplication.main(new String[]{}));
    }

    @Test
    void applicationHasEnableCachingAnnotation() {
        EnableCaching annotation = IpFilterRuleProjectApplication.class.getAnnotation(EnableCaching.class);
        assertNotNull(annotation, "The IpFilterRuleProjectApplication should have @EnableCaching annotation");
    }

}
