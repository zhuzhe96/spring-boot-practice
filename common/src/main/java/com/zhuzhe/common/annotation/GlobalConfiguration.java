package com.zhuzhe.common.annotation;

import com.zhuzhe.common.advice.ControllerExceptionHandler;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({ControllerExceptionHandler.class})
public @interface GlobalConfiguration {}
