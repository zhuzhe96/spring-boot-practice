package com.zhuzhe.accessingdatamongodb.example;

import static com.zhuzhe.accessingdatamongodb.util.DataUtil.getNum;
import static com.zhuzhe.accessingdatamongodb.util.DataUtil.randomMAC;
import static com.zhuzhe.accessingdatamongodb.util.DataUtil.randomName;
import static com.zhuzhe.accessingdatamongodb.util.DataUtil.randomSN;
import static com.zhuzhe.accessingdatamongodb.util.DataUtil.randomSexCode;

import com.zhuzhe.accessingdatamongodb.entity.Device;
import com.zhuzhe.accessingdatamongodb.entity.Status;
import com.zhuzhe.accessingdatamongodb.entity.ThirdPartDevice;
import com.zhuzhe.accessingdatamongodb.entity.User;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AddFieldsOperation;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators.Filter;
import org.springframework.data.mongodb.core.aggregation.ComparisonOperators.Eq;
import org.springframework.data.mongodb.core.aggregation.ConvertOperators.ToObjectId;
import org.springframework.data.mongodb.core.aggregation.ConvertOperators.ToString;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

@Slf4j
@SpringBootTest
class AccessingDataMongodbApplicationTests {
  private static final String type = "BW-TT2";
  private static final String swr = "1.3.13";
  private static final String hw = "0.0.8";

  @Autowired
  private MongoTemplate template;

  @Test
  void example01() {
    log.info("hello mongo! 开始测试");
  }

  @Test
  void example02() {
    log.info("=============插入设备数据==============");
    try {
      for (int i = 0; i < 1; i++) {
        var mac = randomMAC();
        Device device = Device.builder()
            .mac(mac)
            .sn(randomSN())
            .type(type)
            .name(type + "-" + mac.substring(mac.length() - 4))
            .swr(swr)
            .hw(hw)
            .role("test")
            .active(getNum(0, 1)==1)
            .build();
        template.insert(device);
      }
      log.info("pass");
    } catch (Exception e) {
      log.error("error");
    }
  }

  @Test
  void example03() {
    log.info("=============插入用户数据==============");
    try {
      for (int i = 0; i < 1000; i++) {
        var user = User.builder()
            .name(randomName())
            .sex(randomSexCode())
            .salary(getNum(5000, 12000))
            .age(getNum(15, 50))
            .status(Status.builder()
                .height(getNum(150, 180))
                .weight(getNum(90, 180))
                .build())
            .role("test")
            .build();
        template.save(user);
      }
      log.info("pass");
    } catch (Exception e) {
      log.error("error");
    }
  }

  @Test
  void example04() {
    log.info("=============乐观锁测试===============");
    var device = template.find(Query.query(Criteria.where("role").is("test")), Device.class)
        .stream().findAny().orElseThrow();
    log.info("ver={}", device.getVersion());
    try {
      var preDevice = template.findOne(Query.query(Criteria.where("id").is(device.getId())),
          Device.class);
      device.setHw("1.9.9");
      template.save(device);
      log.info("ver={}", device.getVersion());
      device.setHw("1.9.8");
      template.save(device);
      log.info("ver={}", device.getVersion());
      assert preDevice != null;
      template.save(preDevice);
    } catch (Exception e) {
      if (e instanceof OptimisticLockingFailureException) {
        log.info("pass");
      } else {
        log.error("error");
      }
    }
  }

  @Test
  void example05() {
    log.info("=============数据更新测试==============");
    try {
      for (var i = 1; i< 500; i++) {
        var user = template.findOne(Query.query(Criteria.where("role").is("test")).limit(1), User.class);
        assert user != null;
        template.updateMulti(Query.query(Criteria.where("role").is("test").and("userId").is(null)).limit(10),
            Update.update("userId", user.getId()), Device.class);
      }
      log.info("pass");
    } catch (Exception e) {
      log.error("error");
    }
  }

  @Test
  void example06() {
    try {
      log.info("=============关联查询测试(一对多)==============");
      // 添加字段操作
      var addFieldsOperation = AddFieldsOperation.addField("uid")
          .withValue(ToString.toString("$_id")).build();
      // 联表操作
      var lookupOperation = LookupOperation.newLookup()
          .from("device")// 关联表名
          .localField("uid")// 当前表字段
          .foreignField("userId")// 关联表字段
          .as("devices");// 关联后的集合名称
      // 过滤操作
      var matchOperation = Aggregation.match(Criteria.where("devices").ne(List.of()));
      // 合并聚合操作
      var aggregation = Aggregation.newAggregation(addFieldsOperation, lookupOperation,
          matchOperation);
      var users = template.aggregate(aggregation, "user", User.class).getMappedResults();
      log.info("pass users={}", users);
    } catch (Exception e) {
      log.error("error", e);
    }
  }

  @Test
  void example07() {
    try {
      log.info("=============关联查询测试(一对一)==============");
      var addFieldsOperation = AddFieldsOperation.addField("uid")
          .withValue(ToObjectId.toObjectId("$userId")).build();
      var lookupOperation = LookupOperation.newLookup()
          .from("user")
          .localField("uid")
          .foreignField("_id")
          .as("user");
      var matchOperation = Aggregation.match(Criteria.where("user").ne(List.of()));
      var unwindOperation = Aggregation.unwind("$user");
      var aggregation = Aggregation.newAggregation(addFieldsOperation, lookupOperation,
          matchOperation, unwindOperation);
      var devices = template.aggregate(aggregation, "device", Device.class).getMappedResults();
      log.info("pass devices={}", devices);
    } catch (Exception e) {
      log.error("error", e);
    }
  }

  @Test
  void example08() {
    log.info("=============关联查询测试(一对多 + 列表类型成员过滤)==============");
    var addFieldsOperation = AddFieldsOperation.addField("uid").withValue(ToString.toString("$_id")).build();
    var lookupOperation = LookupOperation.newLookup().from("device").localField("uid").foreignField("userId").as("devices");
    var matchOperation = Aggregation.match(Criteria.where("devices").ne(List.of()));
    var projectOperation = Aggregation.project().andInclude("_id", "_class")
        .and(Filter.filter("devices").as("list").by(
        Eq.valueOf("$$list.active").equalToValue(true))).as("devices");
    var aggregation = Aggregation.newAggregation(addFieldsOperation, lookupOperation,
        matchOperation, projectOperation);
    var users = template.aggregate(aggregation, "user", User.class).getMappedResults();
    log.info("pass users={}", users);
  }

  @Test
  void example09() {
    log.info("=============插入设备数据==============");
    try {
      for (int i = 0; i < 1; i++) {
        var mac = randomMAC();
        ThirdPartDevice thirdPartDevice = ThirdPartDevice.builder()
            .desc("测试数据aaaaaa")
            .mac(mac)
            .sn(type)
            .type(type)
            .name(type + "-" + mac.substring(mac.length() - 4))
            .swr(swr)
            .hw(hw)
            .role("test")
            .active(getNum(0, 1)==1)
            .build();
        template.insert(thirdPartDevice);
      }
      log.info("pass");
    } catch (Exception e) {
      log.error("error");
    }
  }

  @Test
  void example10() {
    log.info("=============测试数据清理==============");
    try {
      template.remove(Query.query(Criteria.where("role").is("test")), Device.class);
      template.remove(Query.query(Criteria.where("role").is("test")), User.class);
      log.info("pass");
    } catch (Exception e) {
      log.error("error");
    }
  }
}
