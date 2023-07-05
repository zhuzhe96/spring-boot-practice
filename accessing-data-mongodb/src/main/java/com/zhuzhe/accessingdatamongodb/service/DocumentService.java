package com.zhuzhe.accessingdatamongodb.service;

import com.zhuzhe.accessingdatamongodb.entity.User;
import java.util.Collection;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DocumentService {

  public static final String COLLECTION_NAME = "users";
  @Autowired
  private MongoTemplate template;

  // 插入单个文档
  public User insert(User user) {
//    var user = new User().setId("10").setAge(22).setSex("男").setRemake("无").setSalary(15000)
//        .setName("zhuzhe").setBirthday(new Date())
//        .setStatus(new Status().setHeight(180).setWeight(120));
    return template.insert(user, COLLECTION_NAME);
  }

  // 插入多个文档
  public Collection<User> insert(List<User> users) {
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
  public List<User> findSortAndSkip(String sex, String sort, int skip){
    var criteria = Criteria.where("sex").is(sex);
    return template.find(new Query(criteria).with(Sort.by(sort)).skip(skip), User.class, COLLECTION_NAME);
  }

  // 查询存在指定字段的文档(前面是查有这个字段和对应的值)
  public List<User> findByExistsField(String field){
    var criteria = Criteria.where(field).exists(true);
    return template.find(new Query(criteria), User.class, COLLECTION_NAME);
  }

  // 多条件查询
  public List<User> findByCondition(String age, String sex){
    var age1 = Criteria.where("age").is(age);
    var age2 = Criteria.where("sex").is(sex);
    var criteria = new Criteria().andOperator(age1, age2);
    return template.find(new Query(criteria), User.class, COLLECTION_NAME);
  }


}
