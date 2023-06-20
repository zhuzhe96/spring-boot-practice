# REST风格——HATEOAS超媒体格式
> HATEOAS (Hypermedia as the Engine of Application State) 被称为超媒体驱动是因为它的设计思想主张使用超媒体来驱动应用程序的状态转换。在 HATEOAS 中，每个资源都包含了可以让客户端进行状态转换所需的所有信息，包括可执行的操作和相关参数等。这些信息以超媒体格式（例如 HTML 或 JSON）呈现，因此客户端能够使用这些超媒体链接自动地发现和执行状态转换。

### 测试
发送请求: http://localhost:5016/greeting
返回结果: 
```
{"content":"Hello, zhuzhe!","_links":{"self":{"href":"http://localhost:5016/greeting?name=zhuzhe"}}}
```

