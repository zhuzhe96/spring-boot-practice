# RabbitMQ消息

### 实现
* 消息传递对象和接收对象
* 消息支持失败重试
* 超过重试次数将进入死信队列

---
### RabbitMQ 环境
* 部署在Docker环境中
    ```
    docker run -d --name rabbitmq -p 15672:15672 -p 5672:5672 rabbitmq:3-management
    ```
* RabbitMQ的配置 [配置文件](./rabbit_5b0105e8d29d_2023-5-26.json)