# campusdemo-backend

## 项目简介
`campusdemo-backend` 是一个基于 Java 和 Spring Boot 的校园服务后端项目，使用 Maven 进行依赖管理。项目实现了校园活动、通知、聊天等基础功能，适用于校园信息化场景。

## 环境要求
- JDK 17 及以上
- Maven 3.6 及以上
- MySQL 或其他兼容数据库
- Git

## 快速开始

1. **克隆项目**
   ```bash
   git clone https://github.com/muxi3302159484/campusdemo-backend.git
   cd campusdemo-backend
配置数据库  
在 src/main/resources/application.properties 中配置数据库连接（建议使用环境变量或 .env 文件管理敏感信息）。
安装依赖并构建  
mvn clean install
运行项目  
mvn spring-boot:run
访问接口
默认服务端口为 8080，可通过 http://localhost:8080 访问。
