# Getting Started

### 
---
### 依赖介绍
```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-artemis</artifactId>
</dependency>
```
是为 Apache ActiveMQ Artemis 消息中间件提供的 Spring Boot 快速启动器（Starter）。它简化了在 Spring Boot 应用程序中使用 ActiveMQ Artemis 的配置和集成。该 Starter 可以帮助开发人员快速构建消息驱动的应用程序，并利用 ActiveMQ Artemis 提供的高性能、可靠和可扩展的消息传递功能，使应用程序更加可靠和健壮。
### JMS介绍
JMS即Java Message Service，是JavaEE的消息服务接口。 两个进程之间，通过消息服务器传递消息：
```
┌────────┐    ┌──────────────┐    ┌────────┐
│Producer│───>│Message Server│───>│Consumer│
└────────┘    └──────────────┘    └────────┘
```
ActiveMQ Classic原来就叫ActiveMQ，是Apache开发的基于JMS 1.1的消息服务器，目前稳定版本号是5.x，而ActiveMQ Artemis是由RedHat捐赠的HornetQ服务器代码的基础上开发的，目前稳定版本号是2.x。和ActiveMQ Classic相比，Artemis版的代码与Classic完全不同，并且，它支持JMS 2.0，使用基于Netty的异步IO，大大提升了性能。此外，Artemis不仅提供了JMS接口，它还提供了AMQP接口，STOMP接口和物联网使用的MQTT接口。选择Artemis，相当于一鱼四吃。
### ActiveMQ Artemis 安装
官网下载包: https://www.apache.org/dyn/closer.cgi?filename=activemq/activemq-artemis/2.28.0/apache-artemis-2.28.0-bin.tar.gz&action=download

下载完成后, 放入到[activemq-artemis-docker-compose](./activemq-artemis-docker-compose)文件夹中, 将这个文件夹一并传递到服务器上.
这个需要打包成镜像,所以需要docker-compose这个环境, 按照下面的[官网安装](####官网安装)或[手动安装](####手动安装)进行安装docker compose环境.

在服务器上进入到文件夹activemq-artemis-docker-compose, 执行
```shell
docker-compose up
```
即可完成镜像编译和容器启动

#### 官网安装
官网选择版本 https://github.com/docker/compose/releases
* 以下命令手动修改版本号,例如1.24.1
    ```shell
    curl -L https://github.com/docker/compose/releases/download/1.24.1/docker-compose-`uname -s`-`uname -m` -o /usr/local/bin/docker-compose
    ```
* 添加执行权限
    ```shell
    chmod +x /usr/local/bin/docker-compose
    ```
* 检查docker compose版本
    ```shell
    docker-compose version
    ```
#### 手动安装
官网选择版本
https://github.com/docker/compose/releases

选择相应版本,下载docker-compose-Linux-x86_64到本地或者服务器中
更名为docker-compose,并移动到 /usr/local/bin 目录下

* 添加执行权限
  ```shell
  chmod +x /usr/local/bin/docker-compose
  ```
* 检查docker compose版本
  ```shell
  docker-compose version
  ```