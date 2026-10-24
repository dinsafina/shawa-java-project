package com.shawarmashop.tests.ui.junit;

import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Маркерная аннотация для автоматического включения Selenide Proxy через Extension на уровне метода или поля. */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(SelenideProxyExtension.class)
public @interface UseSelenideProxy {
}