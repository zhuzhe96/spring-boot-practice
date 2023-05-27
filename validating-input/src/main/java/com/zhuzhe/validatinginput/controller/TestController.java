package com.zhuzhe.validatinginput.controller;

import com.zhuzhe.common.entity.Form;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class TestController {
  @PostMapping
  public Form test(@RequestBody @Validated Form form){
    return form;
  }
}
