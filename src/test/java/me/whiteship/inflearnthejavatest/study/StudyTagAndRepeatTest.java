package me.whiteship.inflearnthejavatest.study;

import lombok.extern.slf4j.Slf4j;
import me.whiteship.inflearnthejavatest.domain.Study;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class StudyTagAndRepeatTest {

    @Test
    @Tag("fast")
    @DisplayName("태그")
    void tag() {
        log.debug("현재 태그는 fast 이다.");
    }

    @FastTest
    @DisplayName("커스텀 태그")
    void customTag(){
        log.debug("현재 커스텀 태그는 fast 이다.");
    }

    // 1. RepeatFastTest 커스텀 annotation 사용
    @RepeatFastTest
    @DisplayName("테스트 반복하기")
    void repeatTest(RepetitionInfo repetitionInfo){
        log.debug("test" + repetitionInfo.getCurrentRepetition() + "/" + repetitionInfo.getTotalRepetitions());
    }

    // 2. ParameterizedTest + ValueSource 조합
    @ParameterizedTest(name = "{index}. {displayName}, message={0}")
    @ValueSource(strings = {"2022년", "3월", "22일", "목요일"})
    @NullAndEmptySource
    @DisplayName("테스트 반복하기 1")
    void parameterizedTest(String message){
        log.debug(message);
    }

    @ParameterizedTest(name = "{index}. {displayName}, message={0}")
    @ValueSource(ints = {10, 20, 30})
    @NullSource
    @DisplayName("테스트 반복하기 2")
    void parameterizedTest2(Integer limit){
        log.debug(String.valueOf(limit));
    }

    // 3. ConvertWith 사용해 ValueSource 인자 1개 Study 객체에 넣어 받음
    @ParameterizedTest(name = "{index}. {displayName}, message={0}")
    @ValueSource(ints = {10, 20, 30})
    @DisplayName("테스트 반복하기 3")
    void parameterizedTest3(@ConvertWith(StudyConverter.class) Study study){
        log.debug(String.valueOf(study.getLimit()));
    }

    static class StudyConverter extends SimpleArgumentConverter {
        @Override
        protected Object convert(Object source, Class<?> targetType) throws ArgumentConversionException {
            assertEquals(Study.class, targetType, "Can only convert to Study");
            return new Study(Integer.parseInt(source.toString()));
        }
    }

    // 4. CsvSource 인자 2개 Study 객체에 넣어 받음
    @ParameterizedTest(name = "{index}. {displayName}, message={0}, {1}")
    @CsvSource({"10, '자바 스터디'", "20, 스프링"})
    @DisplayName("테스트 반복하기 4")
    void parameterizedTest4(Integer limit, String name){
        Study study = new Study(limit, name);
        log.debug(study.toString());
    }

    //5. ArgumentsAccessor 이용해 Study 생성
    @Tag("fast")
    @ParameterizedTest(name = "{index}. {displayName}, message={0}, {1}")
    @CsvSource({"30, 'HTTP'", "40, 스프링 부트"})
    @DisplayName("테스트 반복하기 5")
    void parameterizedTest5(ArgumentsAccessor argumentsAccessor){
        Study study = new Study(argumentsAccessor.getInteger(0), argumentsAccessor.getString(1));
        log.debug(study.toString());
    }

    //6. 커스텀 Accessor를 이용해 Study 생성
    @ParameterizedTest(name = "{index}. {displayName}, message={0}, {1}")
    @CsvSource({"40, 'JPA'", "50, JUnit"})
    @DisplayName("테스트 반복하기 6")
    void parameterizedTest6(@AggregateWith(StudyAggregator.class) Study study){
        log.debug(study.toString());
    }

    static class StudyAggregator implements ArgumentsAggregator {
        @Override
        public Object aggregateArguments(ArgumentsAccessor argumentsAccessor, ParameterContext parameterContext) throws ArgumentsAggregationException {
            return new Study(argumentsAccessor.getInteger(0), argumentsAccessor.getString(1));
        }
    }
}
