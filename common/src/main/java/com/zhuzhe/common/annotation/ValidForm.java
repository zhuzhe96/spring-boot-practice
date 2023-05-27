package com.zhuzhe.common.annotation;

import com.zhuzhe.common.validator.FormValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*对一个类中字段的自定义规则校验*/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = FormValidator.class)
public @interface ValidForm {
  String message() default "对象存在字段不符合规范";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
