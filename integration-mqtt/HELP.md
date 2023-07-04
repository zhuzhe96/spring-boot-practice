# spring integration mqtt

## 基于spring-integration-mqtt实现的MQTT服务
1. 实现基本的通过代理服务器的云端消息发送和回复, 也实现了对设备端的调用和回复
2. 代码设计上尽量兼容多主题监听和消息统一处理, 也设计了一些统一管理的类方便全局调用
3. 在云端调用设备端时，设计的是基于函数式接口的请求异步回调
4. 在设备端调用云端时，设计的是基于注解的扫描反射调用

## MQTT的基本配置
### 服务器配置
官网： https://www.emqx.io/

安装方式：Docker

```bash
docker run -d --name emqx -p 1883:1883 -p 8083:8083 -p 8084:8084 -p 8883:8883 -p 18083:18083 emqx/emqx
```

暴露端口： 1883（连接使用），18083（web平台使用）

默认账号密码： admin public
### 项目配置
```properties
# 应用端口
server.port=5018
# mqtt broker（代理服务器）地址
mqtt.url=tcp://localhost:1883
# 代理服务器账号
mqtt.username=admin
# 代理服务器密码
mqtt.password=public
# 连接客户端（SpringBoot应用）的名称
mqtt.client_id=mqtt-cloud
# 连接超时时间
mqtt.timeout=60
# 心跳检测间隔时间
mqtt.keepalive=20
# 主题定义 /test/device/产品分组/SN/MAC
# 发送主题
mqtt.send_topic=/test/device/{group}/+/+
# 订阅主题
mqtt.handle_topic=/test/cloud/{group}/+/+
# 产品分组（用于拼接主题）
mqtt.group=watch,camera,battery
```

## 代理平台查看和调试工具 MQTTX
官网：https://mqttx.app/zh

连接操作:https://www.emqx.io/docs/zh/v5/getting-started/getting-started.html

启动后可以模拟从设备端发云端消息， 或是模拟云端发给设备端消息

![cloud-send-device](img/cloud-send-device.png)
## 主题订阅
在应用连接上代理平台后，在代码中就可以指定订阅主题。这里我的设计是假设有多种产品，而统一在一个代理服务器上收发，则可以借助主题的url拼接路径来实现不同产品的主题效果。
## 项目测试接口
### 手表产品
这里使用测试参数
* sn: 2022000000000001785
* mac: 036915CC0CBF

* 获取手表的网络信息
  ```http request
    GET http://localhost:5018/watch/network/{sn}/{mac}/{wifiName}
  ```
  返回结果：
  ```json
    {
      "wifiName": "zhuzhe-wifi",
      "wifiPassword": "123456",
      "cellularType": "feature",
      "accessPointName": "xxxxxxxx",
      "vpnConfig": "test-config"
    }
  ```
  实际上发送的给设备端的Mqtt消息：

  Topic: /test/device/watch/2022000000000001785/036915CC0CBF QoS: 0
  ```json
    {
      "id": "036915CC0CBF",
      "type": "GET",
      "url": "/zhuzhe/prod/network",
      "timestamp": 1688465883000,
      "status": 200,
      "message": null,
      "token": "98fbc4fbb25d4c3fb764d75a8fdc8646",
      "wifiName": "zhuzhe-wifi"
    }
  ```
  打开MQTTX连接代理服务器，在看到云端发送的消息后模拟设备端回复

  Topic: /test/cloud/watch/2022000000000001785/036915CC0CBF QoS: 0
  ```json
    {
      "id": "036915CC0CBF",
      "type": "ACK",
      "url": "/zhuzhe/prod/network",
      "timestamp": 1688465333910,
      "status": 200,
      "message": null,
      "token": "98fbc4fbb25d4c3fb764d75a8fdc8646",
      "data": {
        "wifiName": "zhuzhe-wifi",
        "wifiPassword": "123456",
        "cellularType": "feature",
        "accessPointName": "xxxxxxxx",
        "vpnConfig": "test-config"
      }
    }
  ```
* 设置手表的网络信息
  ```http request
    POST http://localhost:5018/watch/network/{sn}/{mac}
  ```
  ```json
    {
      "wifiName":"zhuzhe-wifi",
      "wifiPassword":"123456",
      "cellularType":"feature",
      "accessPointName":"xxxxxxxx",
      "vpnConfig":"test-config"
    }
  ```
  返回结果：
  ```json
    {
      "code": 200,
      "message": "success"
    }
  ```
  实际的Mqtt对话：

  Topic: /test/device/watch/2022000000000001785/036915CC0CBF QoS: 0
  ```json
    {
      "id": "036915CC0CBF",
      "type": "POST",
      "url": "/zhuzhe/prod/network",
      "timestamp": 1688466237017,
      "status": 200,
      "message": null,
      "token": "e9b839760e3a422888bfbc5c59a24ddc",
      "data": {
        "wifiName": "zhuzhe-wifi",
        "wifiPassword": "123456",
        "cellularType": "feature",
        "accessPointName": "xxxxxxxx",
        "vpnConfig": "test-config"
      }
    }
  ```
  Topic: /test/cloud/watch/2022000000000001785/036915CC0CBF QoS: 0
  ```json
    {
      "id": "036915CC0CBF",
      "type": "ACK",
      "url": "/zhuzhe/prod/network",
      "timestamp": 1688465333910,
      "status": 200,
      "message": null,
      "token": "e9b839760e3a422888bfbc5c59a24ddc"
    }
  ```
* 获取相机的基础数据
  ```http request
    GET http://localhost:5018/camera/{sn}/{mac}
  ```
  返回结果：
  ```json
    {
      "id": "123",
      "manufacturer": "zhuzhe",
      "model": "nighttime"
    }
  ```
  实际的Mqtt对话：

  Topic: /test/device/camera/2022000000000001785/036915CC0CBF QoS: 0
  ```json
    {
      "id": "036915CC0CBF",
      "type": "GET",
      "url": "/zhuzhe/prod/base_info",
      "timestamp": 1688466527526,
      "status": 200,
      "message": null,
      "token": "b5ff8e1079db4e458ef8612824315b52"
    }
  ```
  Topic: /test/cloud/watch/2022000000000001785/036915CC0CBF QoS: 0
  ```json
    {
      "id": "036915CC0CBF",
      "type": "ACK",
      "url": "/zhuzhe/prod/base_info",
      "timestamp": 1688464130503,
      "status": 200,
      "message": null,
      "token": "b5ff8e1079db4e458ef8612824315b52",
      "data": {
        "id": "123",
        "manufacturer": "zhuzhe",
        "model": "nighttime"
      }
    }
  ```
* 设备上线（设备端请求让手表设备上线，这里不是前端调接口）
  Mqtt对话：

  Topic: /test/cloud/watch/2022000000000001785/036915CC0CBF QoS: 0
  ```json
    {
      "id": "036915CC0CBF",
      "type": "GET",
      "url": "/zhuzhe/prod/online",
      "timestamp": 1688464130503,
      "status": 200,
      "message": null,
      "token": "b5ff8e1079db4e458ef8612824315b52",
      "data": {
        "key": "123123"
      }
    }
  ```
  Topic: /test/device/watch/2022000000000001785/036915CC0CBF QoS: 0
  ```json
    {
      "id": "036915CC0CBF",
      "type": "ACK",
      "url": "/zhuzhe/prod/online",
      "timestamp": 1688466967954,
      "status": 200,
      "message": null,
      "token": "b5ff8e1079db4e458ef8612824315b52",
      "data": {
        "result": "成功上线!"
      }
    }
  ```

## 核心类
* 配置类：负责初始化整套mqtt调用流程相关bean，以及连接mqtt代理服务器（EMQX Broker）
  * [MqttConfig.java](src/main/java/com/zhuzhe/integrationmqtt/mqtt/config/MqttConfig.java)
* 主题订阅类：负责根据配置文件，订阅主题
  * [MqttTopicSubscriber.java](src/main/java/com/zhuzhe/integrationmqtt/mqtt/MqttTopicSubscriber.java)
* 主题重连类：做的一层抽象，为了在断开连接时自动重试连接
  * [MqttDispatchHandler.java](src/main/java/com/zhuzhe/integrationmqtt/mqtt/MqttDispatchHandler.java)
* 消息调度者：整套设计的核心，负责对异步回调方法的缓存和执行，对注解方法扫描和反射调用
  * [AsyncCallbackDispatchHandler.java](src/main/java/com/zhuzhe/integrationmqtt/mqtt/handler/AsyncCallbackDispatchHandler.java)
* 手表产品的前端调用业务逻辑处理服务: 实现了通过函数式接口的异步回调
  * [WatchService.java](src/main/java/com/zhuzhe/integrationmqtt/service/watch/WatchService.java)
* 手表产品的后端监听设备端调用回复的服务：在设备端调用云端（后端）时，响应处理并回复给设备端结果
  * [WatchReplyService.java](src/main/java/com/zhuzhe/integrationmqtt/service/watch/WatchReplyService.java)