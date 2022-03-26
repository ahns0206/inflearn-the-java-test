package me.whiteship.inflearnthejavatest.study;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.RegisterExtension;

@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StudyOrderAndExtensionTest {
    int value = 0;

    @RegisterExtension
    static FindSlowTestExtension findSlowTestExtension = new FindSlowTestExtension(1000L);

    @Order(10)
    @FastTest
//    @SlowTest
    void testInstance1() throws InterruptedException {
        Thread.sleep(1005L);
        log.debug(this + ":" + value++); // 0
    }

    @Order(11)
    @Test
    void testInstance2() {
        log.debug(this + ":" + value++); // 1
    }

    @Order(12)
    @Test
    void testInstance3() {
        log.debug(this + ":" + value++); // 2
    }
}
