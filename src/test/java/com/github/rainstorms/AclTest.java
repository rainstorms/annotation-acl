package com.github.rainstorms;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfiguration.class)
public class AclTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") @Autowired
    private TestClass testClass;

    @Test
    public void testSample() {
        assertNotNull(testClass);
    }
}
