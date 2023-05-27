package com.zhuzhe.common.validator;

import com.zhuzhe.common.annotation.ValidForm;
import com.zhuzhe.common.entity.Form;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

public class FormValidator implements ConstraintValidator<ValidForm, Form> {
  @Override
  public void initialize(ValidForm constraintAnnotation) {}
  @Override
  public boolean isValid(Form form, ConstraintValidatorContext constraintValidatorContext) {
    if (form==null) return true;
    // 模拟规则, 地址只能是北京市开头
    return StringUtils.startsWithIgnoreCase(form.getAddress(), "北京市");
  }
}
