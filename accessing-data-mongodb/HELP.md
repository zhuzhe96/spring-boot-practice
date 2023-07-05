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