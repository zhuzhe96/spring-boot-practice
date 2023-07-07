package com.zhuzhe.accessingdatamongodb.service;

import com.zhuzhe.accessingdatamongodb.entity.User;
import jakarta.annotation.Resource;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@SuppressWarnings({"unused", "rawtypes"})
public class DocumentService {

  public static final String COLLECTION_NAME = "users";
  @Resource
  private MongoTemplate template;

  // 插入单个文档
  public User insert(User user) {
    // @MongoId有id相同的风险,这里选用uuid, 不然集合中存在相同_id的模板,则在save时会出错,save失败变为插入
    user.setId(UUID.randomUUID().toString().replace("-", ""));
    return template.insert(user, COLLECTION_NAME);
  }

  // 插入多个文档
  public Collection<User> insert(List<User> users) {
    users.forEach(user -> user.setId(UUID.randomUUID().toString().replace("-", "")));
    return template.insert(users, COLLECTION_NAME);
  }

  // 保存单个文档
  public User save(User user) {
    return template.save(user, COLLECTION_NAME);
  }

  // 查询全部文档
  public List<User> findAll() {
    return template.findAll(User.class, COLLECTION_NAME);
  }

  // 根据id查询
  public User findById(String id) {
    return template.findById(id, User.class, COLLECTION_NAME);
  }

  // 返回匹配条件的第一条数据
  public User findOne(int age) {
    var criteria = Criteria.where("age").is(age);
    return template.findOne(new Query(criteria), User.class, COLLECTION_NAME);
  }

  // 查询符合条件的所有数据
  public List<User> find(String sex) {
    var criteria = Criteria.where("sex").is(sex);
    return template.find(new Query(criteria), User.class, COLLECTION_NAME);
  }

  // 多条件查询（注意排序的字段不要有重复数据）
  public Page<User> find(User user, Pageable pageable) {
    var criteria = new Criteria();
    Arrays.stream(user.getClass().getDeclaredFields()).forEach(field -> {
      try {
        field.setAccessible(true);
        Optional.ofNullable(field.get(user)).ifPresent(v -> criteria.and(field.getName()).is(v));
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    });
    var query = Query.query(criteria).with(pageable);
    return PageableExecutionUtils.getPage(template.find(query, User.class, COLLECTION_NAME),
        pageable, () -> template.count(query.limit(-1).skip(-1), User.class, COLLECTION_NAME));
  }

  // 查询+排序
  public List<User> findSort(String sex, String sort) {
    var criteria = Criteria.where("sex").is(sex);
    return template.find(new Query(criteria).with(Sort.by(sort)), User.class, COLLECTION_NAME);
  }

  // 查询+排序+限制条数
  public List<User> findSortAndLimit(String sex, String sort, int limit) {
    var criteria = Criteria.where("sex").is(sex);
    return template.find(new Query(criteria).with(Sort.by(sort)).limit(limit), User.class,
        COLLECTION_NAME);
  }

  // 查询+排序+跳过条数
  public List<User> findSortAndSkip(String sex, String sort, int skip) {
    var criteria = Criteria.where("sex").is(sex);
    return template.find(new Query(criteria).with(Sort.by(sort)).skip(skip), User.class,
        COLLECTION_NAME);
  }

  // 查询存在指定字段的文档(前面是查有这个字段和对应的值)
  public List<User> findByExistsField(String field) {
    var criteria = Criteria.where(field).exists(true);
    return template.find(new Query(criteria), User.class, COLLECTION_NAME);
  }

  // and 关联多条件查询
  public List<User> findByAndCondition(String age, String sex) {
    var age1 = Criteria.where("age").is(age);
    var age2 = Criteria.where("sex").is(sex);
    var criteria = new Criteria().andOperator(age1, age2);
    return template.find(new Query(criteria), User.class, COLLECTION_NAME);
  }

  public User update(User user) {
    return template.save(user, COLLECTION_NAME);
  }

  // 根据条件更新第一条文档,没有的情况下创建
  public long update(int age) {
    var criteria = Criteria.where("age").is(age);
    var query = new Query(criteria);
    var update = new Update().set("age", 33).set("name", "zhuzhe");
    var result = template.upsert(query, update, User.class, COLLECTION_NAME);
    return result.getMatchedCount();
  }

  // 根据条件更新第一条文档
  public long updateFirst(String name, int age) {
    var criteria = Criteria.where("name").is(name);
    var query = new Query(criteria).with(Sort.by("age").ascending());
    var update = new Update().set("age", age).set("name", name);
    var result = template.updateFirst(query, update, User.class, COLLECTION_NAME);
    log.info("共匹配到{}条数据, 更新了{}条数据.", result.getMatchedCount(),
        result.getModifiedCount());
    return result.getModifiedCount();
  }

  // 批量更新
  public long batchUpdate(int age, String salary) {
    var criteria = Criteria.where("age").gt(age);
    var query = new Query(criteria);
    var update = new Update().set("age", age).set("salary", salary);
    var result = template.updateMulti(query, update, User.class, COLLECTION_NAME);
    return result.getModifiedCount();
  }

  // 批量删除
  public long remove(int age, String sex) {
    var criteria = Criteria.where("age").is(age).and("sex").is(sex);
    var query = new Query(criteria);
    var result = template.remove(query, COLLECTION_NAME);
    return result.getDeletedCount();
  }

  // 删除单个
  public User findAndRemove(String id) {
    var criteria = Criteria.where("_id").is(id);
    return template.findAndRemove(new Query(criteria), User.class, COLLECTION_NAME);
  }

  // 聚合操作: 分组->统计每个分组的数量
  public List<Map> groupCount() {
    var operation = Aggregation.group("age").count().as("numCount");
    var aggregation = Aggregation.newAggregation(operation);
    var results = template.aggregate(aggregation, COLLECTION_NAME, Map.class);
    return results.getMappedResults();
  }

  // 聚合操作: 分组->统计每个分组的最大值
  public List<Map> groupMax() {
    var operation = Aggregation.group("sex").max("salary").as("salaryMax");
    var aggregation = Aggregation.newAggregation(operation);
    var results = template.aggregate(aggregation, COLLECTION_NAME, Map.class);
    return results.getMappedResults();
  }

  // 聚合操作: 分组->统计每个分组的最小值
  public List<Map> groupMin() {
    var operation = Aggregation.group("sex").min("salary").as("salaryMin");
    var aggregation = Aggregation.newAggregation(operation);
    var results = template.aggregate(aggregation, COLLECTION_NAME, Map.class);
    return results.getMappedResults();
  }

  // 聚合操作: 分组->统计每个分组的总和
  public List<Map> groupSum() {
    var operation = Aggregation.group("sex").sum("salary").as("salarySum");
    var aggregation = Aggregation.newAggregation(operation);
    var results = template.aggregate(aggregation, COLLECTION_NAME, Map.class);
    return results.getMappedResults();
  }

  // 聚合操作: 分组->统计每个分组的平均值
  public List<Map> groupAvg() {
    var operation = Aggregation.group("sex").avg("salary").as("salaryAvg");
    var aggregation = Aggregation.newAggregation(operation);
    var results = template.aggregate(aggregation, COLLECTION_NAME, Map.class);
    return results.getMappedResults();
  }

  // 聚合操作: 排序->分组->统计每组第一个值
  public List<Map> groupFirst() {
    var operation1 = Aggregation.sort(Sort.by("salary").ascending());
    var operation2 = Aggregation.group("sex").first("salary").as("salaryFirst");
    var aggregation = Aggregation.newAggregation(operation1, operation2);
    var results = template.aggregate(aggregation, COLLECTION_NAME, Map.class);
    return results.getMappedResults();
  }

  // 聚合操作: 排序->分组->统计每组最后一个值
  public List<Map> groupLast() {
    var operation1 = Aggregation.sort(Sort.by("salary").ascending());
    var operation2 = Aggregation.group("sex").last("salary").as("salaryLast");
    var aggregation = Aggregation.newAggregation(operation1, operation2);
    var results = template.aggregate(aggregation, COLLECTION_NAME, Map.class);
    return results.getMappedResults();
  }

  // 聚合操作: 分组->每个提取salary合成数组
  public List<Map> groupPush() {
    var operation = Aggregation.group("sex").push("salary").as("salaryArray");
    var aggregation = Aggregation.newAggregation(operation);
    var results = template.aggregate(aggregation, COLLECTION_NAME, Map.class);
    return results.getMappedResults();
  }

  // 聚合操作: 查询->分组->统计每分组的最大值
  public List<Map> groupMatch(int age) {
    var match = Aggregation.match(Criteria.where("age").lt(age));
    var group = Aggregation.group("sex").max("salary").as("sexSalary");
    var aggregation = Aggregation.newAggregation(match, group);
    var results = template.aggregate(aggregation, COLLECTION_NAME, Map.class);
    return results.getMappedResults();
  }

  // 聚合操作: 分组->统计每组最大值和数量->分组按照最大值排序
  public List<Map> groupSort() {
    var group = Aggregation.group("age").max("salary").as("ageSalary").count().as("ageCount");
    var sort = Aggregation.sort(Sort.by("ageSalary").ascending());
    var aggregation = Aggregation.newAggregation(group, sort);
    var results = template.aggregate(aggregation, COLLECTION_NAME, Map.class);
    return results.getMappedResults();
  }

  // 聚合操作: 分组->各种统计操作->限制展示前limitCount条
  public List<Map> groupLimit(int limitCount) {
    var group = Aggregation.group("age").sum("salary").as("sumSalary").max("salary").as("maxSalary")
        .min("salary").as("minSalary").avg("salary").as("avgSalary");
    var limit = Aggregation.limit(limitCount);
    var aggregation = Aggregation.newAggregation(group, limit);
    var results = template.aggregate(aggregation, COLLECTION_NAME, Map.class);
    return results.getMappedResults();
  }

  // 聚合操作: 分组->各种统计操作->跳过前limitCount条
  public List<Map> groupSkip(int skipCount) {
    var group = Aggregation.group("age").sum("salary").as("sumSalary").max("salary").as("maxSalary")
        .min("salary").as("minSalary").avg("salary").as("avgSalary");
    var skip = Aggregation.skip(skipCount);
    var aggregation = Aggregation.newAggregation(group, skip);
    var results = template.aggregate(aggregation, COLLECTION_NAME, Map.class);
    return results.getMappedResults();
  }

  // 聚合操作: 分组->各种统计操作->限制展示的字段
  // showField={sumSalary,maxSalary,minSalary,avgSalary}
  public List<Map> groupProject(String showField) {
    var group = Aggregation.group("age").sum("salary").as("sumSalary").max("salary").as("maxSalary")
        .min("salary").as("minSalary").avg("salary").as("avgSalary");
    var project = Aggregation.project(showField);
    var aggregation = Aggregation.newAggregation(group, project);
    var results = template.aggregate(aggregation, COLLECTION_NAME, Map.class);
    return results.getMappedResults();
  }

  // 聚合操作: 投影指定字段->拆分title文档

  /**
   * { "name": "John", "age": 30, "hobbies": ["reading", "painting", "gardening"] } 拆分后 [ { "name":
   * "John", "age": 30, "hobbies": "reading" }, { "name": "John", "age": 30, "hobbies": "painting"
   * }, { "name": "John", "age": 30, "hobbies": "gardening" } ]
   */
  public List<Map> projectUnwind() {
    var project = Aggregation.project("name", "age", "title");
    var unwind = Aggregation.unwind("title");
    var aggregation = Aggregation.newAggregation(project, unwind);
    var results = template.aggregate(aggregation, COLLECTION_NAME, Map.class);
    return results.getMappedResults();
  }
}
