package com.zhuzhe.accessingdatajpa;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.zhuzhe.accessingdatajpa.AccessingDataJpaApplicationTests.CustomOrder;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Month;
import java.util.Arrays;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.MethodDescriptor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.MethodOrdererContext;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 测试用例八大要素: 1、测试用例编号；2、测试项目；3、测试用例标题；4、重要级别；5、预置条件；6、测试输入；7、操作步骤；8、预期结果。
 */
@Slf4j
@SpringBootTest
@DisplayName("JUnit5测试用例练习")
// @TestMethodOrder(MethodOrderer.OrderAnnotation.class)// 根据@Order定义执行先后
// @TestMethodOrder(MethodOrderer.DisplayName.class)// 根据定义别名的字母数字顺序
// @TestMethodOrder(MethodOrderer.MethodName.class)// 根据原始方法明的字母数组顺序
@TestMethodOrder(CustomOrder.class)// 自定义顺序
class AccessingDataJpaApplicationTests {

  /**
   * 自定义用例执行顺序
   */
  static class CustomOrder implements MethodOrderer {
    @Override
    public void orderMethods(MethodOrdererContext context) {
      context.getMethodDescriptors().sort(
          (MethodDescriptor m1, MethodDescriptor m2)->
              m1.getMethod().getName().compareToIgnoreCase(m2.getMethod().getName()));
    }
  }

  /**
   * 测试数据类
   */
  static class TestData {

    /**
     * 执行这个方法将抛出除零异常
     */
    @SuppressWarnings("all")
    public static void method01() {
      var num = 1 / 0;
    }

    /**
     * 执行这个方法将返回固定字符串
     *
     * @return 执行结果
     */
    public static String method02() {
      return "zhuzhe";
    }

    /**
     * 执行这个方法将返回null
     *
     * @return 执行结果
     */
    public static String method03() {
      return null;
    }

    /**
     * 执行这个方法将耗时4s
     *
     * @return 执行结果
     */
    public static String method04() throws InterruptedException {
      Thread.sleep(2900);
      return "TimeTask";
    }
  }

  // 所有的用例执行先都会先执行
  @BeforeEach
  void before() {
    log.info("==============================Test Start==============================");
    assertFalse(System.getProperty("os.name").contains("Linux"), "该环境为Linux环境, 无法执行用例");
    log.info("预制条件: 非Linux环境检查通过");
  }

  @AfterEach
  void after() {
    log.info("==============================Test End===============================");
  }

  @RepeatedTest(5)
  @DisplayName("循环执行测试")
  void case01() {
    log.info("操作步骤: 循环输出");
    log.info("预期结果: 正常输出");
  }

  @Test
  @DisplayName("异常抛出测试")
  void case02() {
    log.info("操作步骤: 测试方法是否抛出指定异常");
    log.info("预期结果: 抛出除零异常");
    var exception = assertThrows(ArithmeticException.class, TestData::method01);
    assertEquals("/ by zero", exception.getMessage());
  }

  @Test
  @DisplayName("方法返回值测试")
  void case03() {
    log.info("操作步骤: 测试方法是否返回指定内容");
    log.info("预期结果: 返回非空");
    assertAll("用例执行出错", () -> assertEquals("zhuzhe", TestData.method02()),
        () -> assertNull(TestData.method03()));
  }

  @Test
  @DisplayName("方法执行超时测试")
  void case04() {
    log.info("操作步骤: 测试方法是否在3s内执行返回");
    log.info("预期结果: 执行时间控制3s内, 且返回结果正确");
    var result = assertTimeout(Duration.ofSeconds(3), TestData::method04);
    assertEquals("TimeTask", result);
  }

  @Test
  @DisplayName("方法执行超时测试")
  void case05() {
    log.info("操作步骤: 测试方法是否在3s内执行返回");
    log.info("预期结果: 执行时间控制3s内, 且返回结果正确, 超时时直接中断用例");
    assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
      Thread.sleep(1900);
      log.info("执行耗时1.9s");
    });
  }

  static class MyClass {
    public int multiply(int i, int j) {
      return i * j;
    }
  }

  @Order(1)
  @TestFactory
  @DisplayName("动态测试: 一个用例类")
  Stream<DynamicTest> case06() {
    var tester = new MyClass();
    var data = new int[][]{{1, 2, 2}, {5, 3, 15}, {121, 4, 484}};
    return Arrays.stream(data).map(entry -> {
      log.info("操作步骤: 动态测试MyClass的multiply方法");
      log.info("预期结果: 提供一组数据, 返回指定结果");
      int m1 = entry[0];
      int m2 = entry[1];
      int expected = entry[2];
      return DynamicTest.dynamicTest(m1 + "*" + m2 + "=" + expected,
          () -> assertEquals(expected, tester.multiply(m1, m2)));
    });
  }

  public static int[][] data() {
    return new int[][]{{1, 2, 2}, {5, 3, 15}, {121, 4, 484}};
  }

  @Order(2)
  @ParameterizedTest
  @MethodSource("data")// 命名方法的结果作为参数
  @DisplayName("参数化测试, 多个用例")
  void case07(int[] data) {
    log.info("操作步骤: 参数化测试MyClass的multiply方法");
    log.info("预期结果: 提供一组数据, 返回指定结果");
    var tester = new MyClass();
    int m1 = data[0];
    int m2 = data[1];
    int expected = data[2];
    assertEquals(expected, tester.multiply(m1, m2));
  }

  @Order(3)
  @ParameterizedTest
  @DisplayName("参数化测试, 多个用例")
  @ValueSource(ints = {1, 2, 3, 4})// 定义测试值数组
  void case08(int num) {
    log.info("操作步骤: 参数化测试一组值是否都符合");
    log.info("预期结果: 这组数据都小于5");
    assertTrue(num < 5);
  }

  @Order(4)
  @ParameterizedTest
  @DisplayName("参数化测试, 多个用例")
  @EnumSource(value = Month.class, names = {"JULY", "SEPTEMBER"})// 枚举常量作为测试类传递
  void case09(Month month) {
    log.info("操作步骤: 参数化测试一组值是否都符合");
    log.info("预期结果: 这组数据都在6-10月范围内");
    int monthNumber = month.getValue();
    assertTrue(monthNumber >= 6 && monthNumber <= 10);
  }

  @Order(5)
  @ParameterizedTest
  @DisplayName("参数化测试, 多个用例")
  @CsvSource({"test,TEST", "tEst,TEST", "Java,JAVA"})
  void case10(String input, String expected) {
    log.info("操作步骤: 参数化测试一组值是否都符合");
    log.info("预期结果: 这组数据都符合大小写转换");
    String actualValue = input.toUpperCase();
    assertEquals(expected, actualValue);
  }

  static class BlankStringsArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
      return Stream.of(
          Arguments.of((String) null),
          Arguments.of(""),
          Arguments.of("   ")
      );
    }
  }

  @Order(6)
  @ParameterizedTest
  @DisplayName("参数化测试, 多个用例")
  @ArgumentsSource(BlankStringsArgumentsProvider.class)// 指定提供测试数据的类
  void case11(String input) {
    log.info("操作步骤: 参数化测试一组值是否都符合");
    log.info("预期结果: 这组字符串都为空");
    assertTrue(Strings.isBlank(input));
  }


  /**
   * 转换器:十进制转八进制
   */
  static class ToOctalStringArgumentConverter extends SimpleArgumentConverter {
    @Override
    protected Object convert(Object source, Class<?> targetType) throws ArgumentConversionException {
      assertEquals(Integer.class, source.getClass(), "不支持的类型");
      assertEquals(String.class, targetType, "不支持的类型");
      return Integer.toOctalString((Integer) source);
    }
  }

  @Order(7)
  @ParameterizedTest
  @DisplayName("参数化测试, 多个用例")
  @ValueSource(ints = {1, 2, 3, 4})
  void case12(@ConvertWith(ToOctalStringArgumentConverter.class)String argument) {
    log.info("操作步骤: 参数化测试一组值是否都符合");
    log.info("预期结果: 这组数值在转为八进制后不为空");
    assertNotNull(argument);
  }

  @Test
  @DisplayName("临时文件测试")
  void case13(@TempDir Path tempDir, @TempDir Path tempDir2) throws IOException {
    log.info("操作步骤: 临时创建文件");
    log.info("预期结果: 文件能正常创建并且可以写入数据");
    var file1 = tempDir.resolve("zhuzhe.txt");
    var input = Arrays.asList("t1", "t2", "t3");
    Files.write(file1, input);
    assertTrue(Files.exists(file1));
    // var file2 = tempDir2.resolve("ccc.img");
    // assertTrue(Files.exists(file2), "文件应该存在！");
  }

  @Nested
  @DisplayName("第一层")
  class NestedTest01 {
    @BeforeEach
    void before() {
      log.info("预制条件: 01");
    }
    @Nested
    @DisplayName("第二层")
    class NestedTest02 {
      @BeforeEach
      void before() {
        log.info("预制条件: 02");
      }
      @Test
      @DisplayName("嵌套用例")
      void case14() {
        log.info("操作步骤: 测试输出");
        log.info("预期结果: 输出");
        System.out.println("NestedTest01.NestedTest02.case01()");
      }
    }
  }
}
