# spring boot 案例

* 项目使用多模块结构,思路来自：[SpringBoot多模块项目构建](https://cloud.tencent.com/developer/article/2141489)
* 项目是根据官网学习，官网地址：[Spring官网概要](https://spring.io/guides)
* 会在已有的基础模块上实现比较经典的案例
* 目前已构建模块
  * common: [公用的实体和工具模块](./common/HELP.md)
  * restful-web【5000】: [测试 RESTful Web 服务](./restful-web/HELP.md)
  * scheduling-tasks【5001】：[调度任务](./scheduling-tasks/HELP.md)
  * h2-service【5002】：[H2 Database 服务](./h2-service/HELP.md)
  * consuming-rest【5003】：[使用 RESTful Web 服务](./consuming-rest/HELP.md)
  * relational-data-access【5004】：[关系数据访问](./relational-data-access/HELP.md)
  * uploading-files【5005】：[文件上传](./uploading-files/HELP.md)
  * authenticating-ldap【5006】[LDAP验证](./authenticating-ldap/HELP.md)
  * messaging-redis【5007】[Redis消息](./messaging-redis/HELP.md)
  * messaging-rabbitmq【5008】[RabbitMQ消息](./messaging-rabbitmq/HELP.md)
  * accessing-data-neo4j【5009】[neo4j数据库](./accessing-data-neo4j/HELP.md)
  * validating-input【5010】[数据校验输入](./validating-input/HELP.md)
  * actuator-service【5011,5012】[系统监控](./actuator-service/HELP.md)
  * messaging-jms【5013】[Java消息服务](./messaging-jms/HELP.md)
  * batch-processing【5014】[批量操作](./batch-processing/HELP.md)
  * security-rbac【5015】[Security-RBAC权限控制](./security-rbac/HELP.md)
  * rest-hateoas【5016】[REST超媒体返回](./rest-hateoas/HELP.md)
  * accessing-data-geode【5017】[Geode数据库](./accessing-data-geode/HELP.md)
  * integration-mqtt【5018】[MQTT服务](./integration-mqtt/HELP.md)
  * accessing-data-mongodb【5019】[MongoDB数据库](./accessing-data-mongodb/HELP.md)
  * quartz-tasks【5020】[Quartz定时任务](./quartz-tasks/HELP.md)
  * jdk-new-feature【5021】[Jdk新特性练习](./jdk-new-feature/HELP.md)
---
### 项目信息
* spring boot version : 3.1.0
* java version : 17.0.6
* Maven 配置 ：
  ```xml
    <mirrors>
      <mirror>
        <id>maven-default-http-blocker</id>
        <mirrorOf>external:http:*</mirrorOf>
        <name>Pseudo repository to mirror external repositories initially using HTTP.</name>
        <url>http://0.0.0.0/</url>
        <blocked>true</blocked>
      </mirror>
      <mirror>
        <id>aliyunmaven</id>
        <mirrorOf>*</mirrorOf>
        <name>阿里云公共仓库</name>
        <url>https://maven.aliyun.com/repository/public</url>
      </mirror>
    </mirrors>
  ```