# 医院预约和挂号系统

基于 Java + HTTP/1.1 实现的全栈服务项目

## 项目结构

```
HospitalBookingSystem/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/hospital/
│   │   │       ├── server/          # HTTP服务器
│   │   │       ├── model/           # 数据模型
│   │   │       ├── handler/         # HTTP处理器
│   │   │       └── util/            # 工具类
│   │   └── resources/
│   │       └── web/                 # 前端静态文件
│   └── test/
└── pom.xml
```

## 功能特性

- 患者管理（注册、查询）
- 医生管理（信息查询）
- 预约管理（创建、查询、取消预约）
- 挂号管理（挂号、查询挂号记录）
- RESTful API接口
- 前端Web界面

## 技术栈

- Java 11+
- HTTP/1.1（Java原生HttpServer）
- JSON（Gson）
- HTML/CSS/JavaScript（前端）

## 运行方式

### 编译项目
```bash
mvn clean compile
```

### 运行服务器
```bash
mvn exec:java
```

服务器将在 `http://localhost:8080` 启动

### 访问前端
在浏览器中打开 `http://localhost:8080/index.html`

## API接口

### 患者相关
- `GET /api/patients/{id}` - 查询患者信息
- `POST /api/patients` - 注册新患者

### 医生相关
- `GET /api/doctors` - 获取所有医生列表
- `GET /api/doctors/{id}` - 查询医生信息

### 预约相关
- `GET /api/appointments` - 获取所有预约
- `GET /api/appointments/{id}` - 查询预约信息
- `POST /api/appointments` - 创建预约
- `DELETE /api/appointments/{id}` - 取消预约

### 挂号相关
- `GET /api/registrations` - 获取所有挂号记录
- `GET /api/registrations/{id}` - 查询挂号信息
- `POST /api/registrations` - 创建挂号

## 开发说明

本项目使用Java原生的`com.sun.net.httpserver.HttpServer`实现HTTP/1.1服务器，不依赖任何Web框架，完全基于HTTP标准协议。


