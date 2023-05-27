package com.zhuzhe.common.entity;

import com.zhuzhe.common.annotation.ValidForm;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ValidForm
public class Form{
  @NotBlank(message = "不能为空") @Size(min = 2, max = 10, message = "不符合2-10个字符要求") private String name;
  @NotNull(message = "不能为空") @Min(value = 18, message = "至少18岁以上") private Integer age;
  @NotNull(message = "不能为空") @Min(value = 160, message = "不能低于160") @Max(value = 200, message = "不能高于200") private Integer height;
  @NotBlank(message = "不能为空") private String address;
  @NotEmpty(message = "至少输入一个") private List<String> hobby;
  @DecimalMin(value = "10000000000", message = "无效") @DecimalMax(value = "20000000000", message = "无效") private BigInteger phone;
  @Digits(integer = 5, fraction = 2 ,message = "无效") private BigDecimal cost;
  @Pattern(regexp = "[a-zA-Z0-9]+", message = "不符合字符+数字的组合") private String username;
}
