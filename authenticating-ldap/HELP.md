# 使用 LDAP 验证用户

### Spring Security + LDAP

项目使用了Spring Security 和开源 LDAP 服务器 UnboundId:

* 在项目启动之后,在访问 http://localhost:5006 会拉起Security的登陆框
* 这里表单验证是接入了内嵌LDAP服务器,服务器通过test-server.ldif的配置,实现验证
* 输入 ben \ benspassword 即可实现登陆
---
```properties
# 指定了用于初始化嵌入式 LDAP 服务器的 LDIF 文件的位置
spring.ldap.embedded.ldif=classpath:test-server.ldif
# 指定了嵌入式 LDAP 服务器的根节点 DN (Distinguished Name)。
spring.ldap.embedded.base-dn=dc=springframework,dc=org
# 指定了嵌入式 LDAP 服务器的监听端口。
spring.ldap.embedded.port=8389
```

相关的概念:
* **LDAP** : 是的，LDAP是轻量目录访问协议（Lightweight Directory Access Protocol）的缩写。它是一种用于访问和维护分布式目录信息服务的开放标准协议。LDAP通常用于在企业或组织中管理用户、计算机、应用程序等资源的身份验证和授权。通过LDAP，用户可以使用单个用户名和密码来访问多个计算机系统和应用程序。此外，LDAP还提供了高效的目录数据存储和检索功能，支持各种安全和加密协议。
* **目录数据库** : 是一种特殊的数据库，用于存储和管理目录信息。
* **目录服务器** : LDAP服务器，是一个后台进程（daemon），通过使用LDAP协议提供目录数据库服务。（OpenLDAP、Microsoft Active Directory），
* **LDIF** : （LDAP Data Interchange Format）是一种用于导入和导出 LDAP 数据的格式。
* **AD域** : AD域指的是Active Directory域，它是一种由微软开发的网络服务，常用于组织中的用户和计算机管理。它允许管理员在一个中央位置创建、管理和认证所有的用户、计算机和其他网络资源，并为这些对象分配安全性和访问控制权限。 AD域不仅可以提高网络安全性，还可以简化 IT 管理工作，使 IT 管理员更容易地管理大型网络。AD域是建立在LDAP协议之上。