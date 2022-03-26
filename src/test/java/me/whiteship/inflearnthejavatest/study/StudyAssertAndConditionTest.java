package me.whiteship.inflearnthejavatest.study;

import lombok.extern.slf4j.Slf4j;
import me.whiteship.inflearnthejavatest.domain.Study;
import me.whiteship.inflearnthejavatest.domain.StudyStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.*;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

@Slf4j
class StudyAssertAndConditionTest {
    @Test
    @DisplayName("Assertion")
    void assertion() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Study(-10));
        String message = exception.getMessage();
        assertEquals("limit은 0보다 커야 한다.", message);

        Study study = new Study(10);
        assertAll(
                () -> assertTimeout(Duration.ofMillis(100), () -> {
                    new Study(10);
                    Thread.sleep(300);
                }),
                () -> assertTimeoutPreemptively(Duration.ofMillis(100), () -> {
                    new Study(10);
                    Thread.sleep(300);
                }),
                () -> assertNotNull(study),
                () -> assertEquals(StudyStatus.DRAFT, study.getStatus(),
                    () -> "스터디를 처음 만들면 상태값이 "+ StudyStatus.DRAFT +" 이다."),
                () -> assertTrue(study.getLimit() > 0, "스터디 최대 참석 가능 인원은 0보다 커야 한다.")
        );
    }

    @Test
    @DisplayName("조건에 따라 테스트 실행")
    @EnabledOnOs({OS.MAC, OS.LINUX, OS.WINDOWS})
    @EnabledOnJre({JRE.JAVA_8, JRE.JAVA_9, JRE.JAVA_10, JRE.JAVA_11})
    @DisabledOnJre({JRE.OTHER})
    @EnabledIfEnvironmentVariable(named = "TEST_ENV", matches = "LOCAL")
    void condition() {
        String test_env = System.getenv("TEST_ENV");
        log.debug(test_env);

        assumeTrue("LOCAL".equalsIgnoreCase(test_env));

        assumingThat("LOCAL".equalsIgnoreCase(test_env), ()->{
            log.debug("local");
            Study actual = new Study(100);
            assertEquals(actual.getLimit(), 100);
        });

        assumeTrue("History".equalsIgnoreCase(test_env));

        assumingThat("History".equalsIgnoreCase(test_env), ()->{
            log.debug("history");
            Study actual = new Study(10);
            assertEquals(actual.getLimit(), 10);
        });
    }
}