# 集成MongoDB数据库

## 部署MongoDB服务

教程：https://www.cnblogs.com/Can-daydayup/p/16838976.html

运行容器，并指定MongoDB开启权限验证

```shell
docker run -itd --name mongo -p 27017:27017 mongo --auth
```

进入MongoDB服务，连接到admin数据库。

```shell
docker exec -it mongo mongosh admin
```

创建root用户，并设定密码和权限

```
db.createUser(
    {
        user:"root",
        pwd:"123456",
        roles:[{role:"root",db:"admin"}]
    }
);
```

登录root

```
db.auth('root','123456')
```
显示当前的数据库
```
show dbs
```
服务器开放端口后就可以使用RoBo 3T连接后操作

## 测试接口
* 新增集合
  ```
    PUT http://localhost:5019/collection
  ```
  ```json
    {
      "name": "test-collection",
      "size": 1000,
      "count": 8
    }
  ```
* 删除集合
  ```
    DELETE http://localhost:5019/collection/{collectionName}
  ```
* 新增文档
  ```
    PUT http://localhost:5019/document
  ```
  ```json
    {
      "name": "zhuzhe",
      "sex": "man",
      "salary": "15000",
      "age": "13",
      "birthday": "2023-07-06",
      "address": "shantou",
      "status": {
          "weight": 140,
          "height": 171
      }
    }
  ```
* 批量插入
  ```
    PUT http://localhost:5019/document/batch
  ```
  ```json
    [
      {
          "name": "asdasda",
          "sex": "man",
          "salary": "11333",
          "age": "32",
          "birthday": "2011-11-18",
          "address": "asdasdqeqweqw",
          "status": {
              "weight": 63,
              "height": 43
          }
      },
      {
          "name": "zxczxc",
          "sex": "woman",
          "salary": "23113",
          "age": "11",
          "birthday": "1975-10-07",
          "address": "dfghdfghdg",
          "status": {
              "weight": 91,
              "height": 24
          }
      }
    ]
  ```
* 删除文档
  ```
    DELETE http://localhost:5019/document/{userId}
  ```
* 更新文档
  ```
    PATCH http://localhost:5019/document
  ```
  ```json
    {
      "id": "649746baa55e4410a4d729f377f767f3",
      "name": "zhuzhe-123123",
      "sex": "woman",
      "salary": 111111,
      "age": 11,
      "birthday": "1975-10-07",
      "address": "123123123",
      "status": {
          "weight": 91,
          "height": 24
      }
    }
  ```
* 查询文档
  ```
    GET http://localhost:5019/document?{param}
  ```
  ```
    http://localhost:5019/document?name=zhuzhe&age=13&address=shantou&page=1&size=2&sort=id,dasc
  ```