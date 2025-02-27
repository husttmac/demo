# demo 

## 功能设计

模拟银行交易管理。接口文档地址：http://127.0.0.1:8080/swagger-ui/index.html

## 使用说明
1. git clone https://github.com/husttmac/demo.git 
2. cd demo 然后 mvn clean  package 打包
3. 手动构建镜像   docker build -t demo:0.1 .
4. 启动镜像  docker run -p 8080:8080 -d demo:0.1

## 系统技术选型及相关依赖
1. springboot web 基础框架
2. lombok 简化代码
3. springdoc-openapi-starter-webmvc-uiswagger文档
4. spring-boot-starter-cache  缓存
5. com.github.ben-manes.caffeine 缓存
通过引入锁及cache，增强性能及保障并发安全

## 代码结构
1. controller：api
2. service：逻辑实现
3. repository：模拟数据操作
4. common：配置一及工具类

## 测试
单元测试 ：应对每个service的功能及相关状态流转做单元测试，因事件原因，仅做了接口的模拟测试。
性能测试： 另外用jemeter也做了一些测试，具体见jemeter文件demo-jemeter-test.jmx
