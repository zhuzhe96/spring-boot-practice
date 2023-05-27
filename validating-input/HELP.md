# 数据校验

### 实现功能
* 实现了validation的常用注解
* 结合全局异常处理，做了返回体的封装，对指定的controller返回时封装一层结构
* 这个封装可以规范在正常返回和异常返回时统一结构
* 自定义了校验注解，校验对象中的所有字段（测试时是校验address是否北京市开头）

### 测试数据
* 请求地址 http://localhost:5010/ 
* 请求方式 POST
* 无效的输入
    ```json
    {
        "name": "陈",
        "age": 1,
        "height": 1721,
        "address": "京市本1111溪市合山市",
        "hobby": [],
        "phone": 111111111111111111,
        "cost": 112.113,
        "username": "只因你太"
    }
    ```
* 输出结果
    ```json
    {
        "code": 1009,
        "message": "phone无效的手机号, username用户名不符合字符+数字的组合, age年龄至少18岁以上, hobby至少输入一个, height不能高于200, form对象存在字段不符合规范, name名称不符合2-10个字符要求, cost无效的金额",
        "data": null
    }
    ```
* 有效的输入
    ```json
    {
        "name": "鸡大侠",
        "age": 19,
        "height": 171,
        "address": "北京市xxxxxx",
        "hobby": ["码字"],
        "phone": 13288888888,
        "cost": 112.50,
        "username": "ikun"
    }
    ```
* 有效输入的返回
    ```json
    {
        "code": 1000,
        "message": "操作成功",
        "data": {
            "name": "鸡大侠",
            "age": 19,
            "height": 171,
            "address": "北京市xxxxxx",
            "hobby": [
                "码字"
            ],
            "phone": 13288888888,
            "cost": 112.50,
            "username": "ikun"
        }
    }
    ```

### 各种注解的作用

* @NotNull - 检查对象是否为 null。
* @NotEmpty - 检查字符串、集合、Map 或数组是否不为空。
* @NotBlank - 检查字符串是否不为空或空白字符。
* @Size - 检查字符串、集合、Map 或数组的长度或大小是否在指定范围内。
* @Min - 检查数值是否大于或等于指定值。
* @Max - 检查数值是否小于或等于指定值。
* @DecimalMin - 检查 BigDecimal、BigInteger、byte、short、int、long 等数值是否大于或等于指定值。
* @DecimalMax - 检查 BigDecimal、BigInteger、byte、short、int、long 等数值是否小于或等于指定值。
* @Digits - 检查数字是否在允许的整数和小数部分位数内。
* @Pattern - 检查字符串是否匹配指定的正则表达式模式。

