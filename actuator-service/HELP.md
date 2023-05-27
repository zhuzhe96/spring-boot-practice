# 监控功能
### springboot支持的系统监控功能
* 访问监控接口: http://localhost:5012/monitor
* 访问系统配置信息: http://localhost:5012/monitor/info
* 访问系统健康状态: http://localhost:5012/monitor/health
### 原生端点
#### 应用配置类
* 自动化健康报告: http://localhost:5012/monitor/conditions
* 系统上下文创建的beans: http://localhost:5012/monitor/beans
* 应用中配置的属性信息报告: http://localhost:5012/monitor/configprops
* 所有SpringMVC的控制器映射关系报告: http://localhost:5012/monitor/mappings
#### 度量指标类
* 应用所有可用的环境属性报告(环境变量、JVM属性、应用的配置配置、命令行中的参数): http://localhost:5012/monitor/env
* 返回当前应用的各类重要度量指标: http://localhost:5012/monitor/metrics
* 当前线程活动的快照: http://localhost:5012/monitor/threaddump
* HTTP跟踪信息: http://localhost:5012/monitor/trace
#### 操作控制类
* 关闭系统[POST]: http://localhost:5012/monitor/shutdown


### 监控配置
```properties
# 代表开启全部监控，也可仅配置需要开启的监控，如： management.endpoints.web.exposure.include=beans,trace
management.endpoints.web.exposure.include='*'
# health endpoint开启显示全部细节。默认情况下/actuator/health是公开的，但不显示细节
management.endpoint.health.show-details=always
# 启用指定的url地址访问根路径，默认路径为/actuator/*，开启则访问路径变为/monitor/*
management.endpoints.web.base-path=/monitor
# 启用接口关闭SpringBoot
management.endpoint.shutdown.enabled=true
# 支持其他域名的访问，规则：任意域名
management.endpoints.web.cors.allowed-origins=*
# 支持其他域名的方式，规则：任意请求方式
management.endpoints.web.cors.allowed-methods=*
# 开启展示info信息
management.info.env.enabled=true
# 下面info都是自定义信息,信息可以手动输入,也可以引用maven中的项目配置
info.app.name=actuator-service
info.app.description=Test Spring Boot Actuator
info.build.artifact=@project.artifactId@
info.build.version=@project.version@
info.build.name=@project.name@
info.build.description=@project.description@
```
### REST接口
| HTTP方法 | 路径            | 描述                                                         |
| -------- | --------------- | ------------------------------------------------------------ |
| GET      | /auditevents    | 显示当前应用程序的审计事件信息                               |
| GET      | /beans          | 显示一个应用中所有Spring Beans的完整列表                     |
| GET      | /conditions     | 显示配置类和自动配置类(configuration and auto-configuration classes)的状态及它们被应用或未被应用的原因。 |
| GET      | /configprops    | 显示一个所有@ConfigurationProperties的集合列表               |
| GET      | /env            | 显示来自Spring的ConfigurableEnvironment的属性。              |
| GET      | /flyway         | 显示数据库迁移路径，如果有的话。                             |
| GET      | /health         | 显示应用的健康信息（当使用一个未认证连接访问时显示一个简单的’status’，使用认证连接访问则显示全部信息详情） |
| GET      | /info           | 显示任意的应用信息                                           |
| GET      | /liquibase      | 展示任何Liquibase数据库迁移路径，如果有的话                  |
| GET      | /metrics        | 展示当前应用的metrics信息                                    |
| GET      | /mappings       | 显示一个所有@RequestMapping路径的集合列表                    |
| GET      | /scheduledtasks | 显示应用程序中的计划任务                                     |
| GET      | /sessions       | 允许从Spring会话支持的会话存储中检索和删除(retrieval and deletion)用户会话。使用Spring Session对反应性Web应用程序的支持时不可用。 |
| POST     | /shutdown       | 允许应用以优雅的方式关闭（默认情况下不启用）                 |
| GET      | /threaddump     | 执行一个线程dump                                             |
| GET      | /heapdump       | 返回一个GZip压缩的hprof堆dump文件                            |
| GET      | /jolokia        | 通过HTTP暴露JMX beans（当Jolokia在类路径上时，WebFlux不可用） |
| GET      | /logfile        | 返回日志文件内容（如果设置了logging.file或logging.path属性的话），支持使用HTTP Range头接收日志文件内容的部分信息 |
| GET      | /prometheus     | 以可以被Prometheus服务器抓取的格式显示metrics信息            |

### 健康状态
| 名称                         | 描述                              |
| ---------------------------- | --------------------------------- |
| CassandraHealthIndicator     | 检查Cassandra数据库是否已启动。   |
| CouchbaseHealthIndicator     | 检查Couchbase群集是否已启动。     |
| DiskSpaceHealthIndicator     | 检查磁盘空间不足。                |
| DataSourceHealthIndicator    | 检查是否可以建立连接DataSource。  |
| ElasticsearchHealthIndicator | 检查Elasticsearch集群是否已启动。 |
| InfluxDbHealthIndicator      | 检查InfluxDB服务器是否已启动。    |
| JmsHealthIndicator           | 检查JMS代理是否启动。             |
| MailHealthIndicator          | 检查邮件服务器是否已启动。        |
| MongoHealthIndicator         | 检查Mongo数据库是否已启动。       |
| Neo4jHealthIndicator         | 检查Neo4j服务器是否已启动。       |
| RabbitHealthIndicator        | 检查Rabbit服务器是否已启动。      |
| RedisHealthIndicator         | 检查Redis服务器是否启动。         |
| SolrHealthIndicator          | 检查Solr服务器是否已启动。        |
