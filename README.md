# sprbot-freedom

## 依赖版本清单
| 序号	| 依赖名称	| 版本号|
|--- |--- |--- |
|1 | JDK | 17
|2 | Spring Boot | 3.1.9
|3 | Mybatis-Plus | 3.5.5
|4 | Druid | 1.1.23
|5 | jjwt | 0.9.1
|6 | Mysql-driver | 8.0.23
|7 | fastdfs | 1.26.3
|8 | Mysql | 5.7
|9 | Redis | 5.0
|10 | Mongodb | 7.0.0
|11 | Netty | 5.0.0.Alpha2
|12 | Undertow | 2.3.12.Final
|13 | Nginx | 1.15.3

## 目录结构
![image](https://github.com/univspeed/sprbot-freedom/assets/23521638/84b25c8f-c1e3-4ecc-96d5-7b90efe92fc1)

## 权限校验
权限校验分为数据权限校验以及功能权限校验，权限通过前端进行配置，内容涵盖了接口、按钮、tab页、菜单权限，并与角色进行绑定，用户进行多角色关联，登陆后即可通过pipeline中的interfacePermFilterHandler和ResourcePermFilterHandler进行校验，完整流程如下所示：

![image](https://github.com/univspeed/sprbot-freedom/assets/23521638/9b62f5ad-c9da-4c41-8af7-b0c4b771f62a)

## 业务代码使用案例

## 部署
程序中提供了docker-compose.yml，用于构建scs项目容器的构建和管理。Dockerfile用于将项目构建成为Docker镜像。
按照如下的结构放置文件，如图：
 ![image](https://github.com/univspeed/sprbot-freedom/assets/23521638/19a5cd46-29e8-4894-a4a5-47741105ba94)
 
```
docker-compose build 构建镜像
docker-compose up -d 启动容器
```


