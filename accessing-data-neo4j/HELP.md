# neo4j数据访问

### 实现基本的操作neo4j数据库的功能
* 项目启动后会自动插入测试数据,之后可以通过接口访问
* 查询所有Movie http://localhost:5009/movie
* 根据title查询Movie http://localhost:5009/movie/findByTitle?title=1
* 数据库中节点的数量 http://localhost:5009/movie/allNodes

---
### 环境搭建
* Docker
  ```
  docker run \
    --name myneo4j \
    -p 7474:7474 -p 7687:7687 \
    -v /path/to/data:/data \
    -v /path/to/logs:/logs \
    -d neo4j
  ```
* 部署后的默认账号密码 neo4j neo4j
* 登录时不能启动翻译，不然会登录失败
### 使用语法
```
@Node ：定义节点
@Relationship：定义关系
@Propert：定义节点或关系属性
@RelationshipProperties：定义关系属性集合
@Id：定义SDN的主键
@TargetNode：定义关系的另一端节点对象
```
### 数据介绍
* Movie类：表示电影对象，包含电影的标题、描述、演员和导演等属性。
* Person类：表示人物对象，包含人物的姓名和出生年份等属性。
* Role类：表示演员在电影中扮演的角色对象，包括角色的ID、角色名称以及演员的信息。
