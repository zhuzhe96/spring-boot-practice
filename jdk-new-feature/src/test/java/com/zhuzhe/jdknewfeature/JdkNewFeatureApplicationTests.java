package com.zhuzhe.jdknewfeature;

import com.zhuzhe.jdknewfeature.jdk8.lambda.ConvertUtil;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Builder;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JdkNewFeatureApplicationTests {
  @Data
  @Builder
  private static class TestData {
    private Long id;
    private String name;
    private LocalDateTime create = LocalDateTime.now();
    private LocalDateTime update;
  }

  private static final Set<TestData> set = Set.<TestData>of(TestData.builder().id(1001L).name("A").build(),
      TestData.builder().id(1002L).name("B").build(),
      TestData.builder().id(1003L).name("C").build());

  @Test
  void example01() {
    var map = ConvertUtil.toMap(set, TestData::getId);
    System.out.println("map = " + map);
  }

  @Test
  void example02() {
    var map = ConvertUtil.toMap(set, TestData::getId);
    var newMap = ConvertUtil.convertMapValue(map, (id, testData) -> id + testData.getName());
    System.out.println("newMap = " + newMap);
  }

  @Test
  void example03() {
    var list = ConvertUtil.toList(set);
    list.add(TestData.builder().id(1001L).name("A").build());
    list.add(TestData.builder().id(1004L).name("D").build());
    list.add(TestData.builder().id(1005L).name("E").build());
    System.out.println("list = " + list);
    var newSet = ConvertUtil.toSet(list);
    System.out.println("newSet = " + newSet);
  }

  @Test
  void example04() {
    var list = ConvertUtil.toList(set);

    var nameSet = ConvertUtil.map(set, TestData::getName);
    var idList = ConvertUtil.map(list, TestData::getId);
    System.out.println("nameSet = " + nameSet);
    System.out.println("idList = " + idList);

    var nameList = ConvertUtil.mapToList(set, TestData::getName);
    var idSet = ConvertUtil.mapToSet(set, TestData::getId);
    System.out.println("nameList = " + nameList);
    System.out.println("idSet = " + idSet);
  }
}
