# 使用spring integration系列的MQTT

### 基于spring-integration-mqtt实现的MQTT服务

## 使用的MQTT Broker: EMQX
官网： https://www.emqx.io/

安装方式：Docker

```bash
docker run -d --name emqx -p 1883:1883 -p 8083:8083 -p 8084:8084 -p 8883:8883 -p 18083:18083 emqx/emqx
```

暴露端口： 1883（连接使用），18083（web平台使用）

默认账号密码： admin public


### 连接测试

MQTTX官网：https://mqttx.app/zh
连接操作:https://www.emqx.io/docs/zh/v5/getting-started/getting-started.html

### 主题订阅

在使用MQTTX连接上代理服务器后,就可以在MQTTX上自己订阅主题.自己接收和回复.

### 实现方式
方式一: 请求异步回调
方式二: 请求发布订阅