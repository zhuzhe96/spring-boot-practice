package com.zhuzhe.jdknewfeature.jdk8.lambda;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.util.CollectionUtils;

public class ConvertUtil {

  /**
   * 转换Collection为Map
   *
   * @param collection 集合对象
   * @param keyMapper  map的键构建形式(取对象中哪一个成员作为键， 同时value是对象本身)
   * @return Map对象Map<field, Object>
   */
  public static <T, K> Map<K, T> toMap(Collection<T> collection,
      Function<? super T, ? extends K> keyMapper) {
    return toMap(collection, keyMapper, Function.identity());
  }

  /**
   * 转换Collection为Map
   *
   * @param collection    集合对象
   * @param keyFunction   键构建方式
   * @param valueFunction 值构建方式
   * @return Map对象 Map< field, field or Object>
   */
  public static <T, K, V> Map<K, V> toMap(Collection<T> collection,
      Function<? super T, ? extends K> keyFunction,
      Function<? super T, ? extends V> valueFunction) {
    return toMap(collection, keyFunction, valueFunction, next());
  }

  /**
   * 转化Collection为Map
   *
   * @param collection    集合类型
   * @param keyFunction   要提取为键的字段
   * @param valueFunction 要提取为值的字段
   * @param mergeFunction 当遇到一个键多个值时的选择
   * @return Map对象
   */
  public static <T, K, V> Map<K, V> toMap(Collection<T> collection,
      Function<? super T, ? extends K> keyFunction,
      Function<? super T, ? extends V> valueFunction,
      BinaryOperator<V> mergeFunction) {
    if (CollectionUtils.isEmpty(collection)) {
      return new HashMap<>(0);
    }
    return collection.stream().collect(Collectors.toMap(keyFunction, valueFunction, mergeFunction));
  }

  /**
   * 转换Map
   *
   * @param map           输入的map< key, Object>
   * @param valueFunction 新map的value
   * @return map<key, Object.field> 遇到相同key时选择后者
   */
  public static <K, V, C> Map<K, C> convertMapValue(Map<K, V> map,
      BiFunction<K, V, C> valueFunction) {
    return convertMapValue(map, valueFunction, next());
  }

  /**
   * 转换Map
   *
   * @param map           输入的map< key, Object>
   * @param valueFunction 新map的value
   * @param mergeFunction 新map在遇到多个value时的处理
   * @return map<key, Object.field>
   */
  public static <K, V, C> Map<K, C> convertMapValue(Map<K, V> map,
      BiFunction<K, V, C> valueFunction, BinaryOperator<C> mergeFunction) {
    if (CollectionUtils.isEmpty(map)) {
      return new HashMap<>();
    }
    return map.entrySet().stream().collect(Collectors.toMap(Entry::getKey,
        entry -> valueFunction.apply(entry.getKey(), entry.getValue()), mergeFunction));
  }

  /**
   * 将任意集合类型转为List
   *
   * @param collection 集合对象
   * @return List对象
   */
  public static <T> List<T> toList(Collection<T> collection) {
    if (CollectionUtils.isEmpty(collection)) {
      return new ArrayList<>();
    }
    if (collection instanceof List<T>) {
      return (List<T>) collection;
    }
    return new ArrayList<>(collection);
  }

  /**
   * 将任意集合类型转为Set
   *
   * @param collection 集合对象
   * @return Set对象
   */
  public static <T> Set<T> toSet(Collection<T> collection) {
    if (CollectionUtils.isEmpty(collection)) {
      return new HashSet<>();
    }
    if (collection instanceof Set<T>) {
      return (Set<T>) collection;
    }
    return new HashSet<>(collection);
  }

  /**
   * 提取list的成员构建新list
   *
   * @param list     list对象
   * @param function 提取方式
   * @return List<object.field>
   */
  public static <T, R> List<R> map(List<T> list, Function<T, R> function) {
    return list.stream().map(function).collect(Collectors.toList());
  }

  /**
   * 提取set的成员构建新set
   *
   * @param set      set对象
   * @param function 提取方式
   * @return Set<object.field>
   */
  public static <T, R> Set<R> map(Set<T> set, Function<T, R> function) {
    return set.stream().map(function).collect(Collectors.toSet());
  }

  /**
   * 提取任意collection对象的成员为list
   *
   * @param collection 集合对象
   * @param function   提取方式
   * @return List<object.field>
   */
  public static <T, R> List<R> mapToList(Collection<T> collection, Function<T, R> function) {
    return collection.stream().map(function).collect(Collectors.toList());
  }

  /**
   * 提取任意collection对象的成员为Set
   *
   * @param collection 集合对象
   * @param function   提取方式
   * @return Set<object.field>
   */
  public static <T, R> Set<R> mapToSet(Collection<T> collection, Function<T, R> function) {
    return collection.stream().map(function).collect(Collectors.toSet());
  }


  /**
   * 二元操作
   *
   * @return 只返回前一个值(选择用旧值)
   */
  public static <T> BinaryOperator<T> pre() {
    return (pre, next) -> pre;
  }

  /**
   * 二元操作
   *
   * @return 只返回后一个值(选择用新值)
   */
  public static <T> BinaryOperator<T> next() {
    return (pre, next) -> next;
  }

  @Data
  @Builder
  private static class TestData {
    private Long id;
    private String name;

    @Override
    public String toString() {
      return "TestData{" +
          "id=" + id +
          ", name='" + name + '\'' +
          '}';
    }
  }

  @Data
  private static class TwoValue<T extends List<?>, D extends List<?>> {

    private T addList;
    private D delList;

    public TwoValue(T addList, D delList) {
      this.addList = addList;
      this.delList = delList;
    }

    @Override
    public String toString() {
      return "TwoValue{" +
          "addList=" + addList +
          ", delList=" + delList +
          '}';
    }
  }

  public static <T> TwoValue<List<T>, List<T>> filterAdd0rDel(List<T> dtolist, List<T> joinlist,
      Function<T, String> function) {
    //需要新增的
    List<T> addList = new ArrayList<>();
    //需要删除的
    List<T> delList = new ArrayList<>();

    if (!CollectionUtils.isEmpty(joinlist)) {
      var dtoMap = dtolist.stream().collect(Collectors.toMap(function, Function.identity()));
      var joinMap = joinlist.stream().collect(Collectors.toMap(function, Function.identity()));
      for (T dto : dtolist) {
        // 新增的
        if (!joinMap.containsKey(function.apply(dto))) {
          addList.add(dto);
        }
      }
      for (T join : joinlist) {
        // 删除的
        if (!dtoMap.containsKey(function.apply(join))) {
          delList.add(join);
        }
      }
    } else {
      addList = dtolist;
    }
    return new TwoValue<>(addList, delList);
  }

  public static void main(String[] args) {
    //[1,[2,3],4,5]
    var dtoList = new ArrayList<TestData>(
        List.of(TestData.builder().id(1001L).name("zhuzhe01").build(),
            TestData.builder().id(1002L).name("zhuzhe02").build(),
            TestData.builder().id(1003L).name("zhuzhe03").build()));
    var joinList = new ArrayList<TestData>(
        List.of(TestData.builder().id(1002L).name("zhuzhe02").build(),
            TestData.builder().id(1003L).name("zhuzhe03").build(),
            TestData.builder().id(1004L).name("zhuzhe04").build(),
            TestData.builder().id(1005L).name("zhuzhe05").build()));
    var twoValue = filterAdd0rDel(dtoList, joinList, TestData::getName);
    System.out.println("twoValue = " + twoValue);
  }
}
