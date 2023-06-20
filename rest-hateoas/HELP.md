# REST风格——HATEOAS超媒体格式

## HATEOAS 概念

> HATEOAS (Hypermedia as the Engine of Application State) 被称为超媒体驱动是因为它的设计思想主张使用超媒体来驱动应用程序的状态转换。在 HATEOAS 中，每个资源都包含了可以让客户端进行状态转换所需的所有信息，包括可执行的操作和相关参数等。这些信息以超媒体格式（例如 HTML 或 JSON）呈现，因此客户端能够使用这些超媒体链接自动地发现和执行状态转换。

## REST 架构

### REST 风格
REST架构风格成为当今构建Web服务时，应该遵循的事实标准。

### REST 定义
> REST (representational stat transfer)





Spring Hateoas是一个用于支持实现超文本驱动的REST Web服务的开发库。

使用方式：
```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-hateoas</artifactId>
</dependency>
```

参考链接： https://www.cnblogs.com/kaixinyufeng/p/8283289.html

### 测试
发送请求: http://localhost:5016/greeting
返回结果: 
```
{"content":"Hello, zhuzhe!","_links":{"self":{"href":"http://localhost:5016/greeting?name=zhuzhe"}}}
```

