package me.whiteship.inflearnthejavatest.study;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Tag;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Tag("fast")
@RepeatedTest(value = 5, name = "{displayName} : {currentRepetition}/{totalRepetitions}")
public @interface RepeatFastTest {
}
