bytetccdemo
====================

### 结构
    
    sample-consumer:对外服务

    sample-provider:内部服务

    sample-eureka-server：注册中心

### 使用流程
    
    1. 把dt.sql dt2.sql 导入数据库
    
    2. sample-consumer使用数据库 dt2(数据库连接方式配置参考ConsumerConfig)
    
    3. sample-provider使用数据dt (数据库连接方式配置参考ProviderConfig)

    4. 配置后依次启动 sample-eureka-server sample-provider sample-consumer
    
    5. post调用接口 http://localhost:7080/transfer 参数
     
       sourceAcctId : 1001   //用户1 id
       targetAcctId : 2001   //用户2 id
       amount : 10           //用户1001 向2001转账金额

### 业务流程
    
    1. sample-consumer调用sample-provider的接口decreaseAmount先扣除用户1001余额10 并且把10 存入用户1001的冻结字段
    
    2. 如果第一步正确，sample-consumer 中用户2001增加冻结金额10
    
    3. 如果前两部（try）正确则bytetcc会自动调用 sample-provider中配置的确认操作（Confirm）即扣除步骤1中用户2001冻结的余额10，然后在确认sample-consumer的确认操作
    
    4. 如果错误会自动调用对应的取消操作
    
### bytetcc使用说明

1.pom.xml 增加依赖
    
```xml
 <dependency>
            <groupId>org.bytesoft</groupId>
            <artifactId>bytetcc-supports-springcloud</artifactId>
            <version>0.4.17</version>
            <exclusions>
                <exclusion>
                    <groupId>asm</groupId>
                    <artifactId>asm</artifactId>
                </exclusion>
            </exclusions>
 </dependency>
```

2.在接口的controller 增加注解

```java
@Compensable(interfaceClass = IAccountService.class, confirmableKey = "accountServiceConfirm", cancellableKey = "accountServiceCancel")
```
2.1. 注解说明

*interfaceClass*: 定义接口，*controller*,确认，取消都要实现他的方法，并且方法上必须增加 *@Transactional* 注解
*confirmableKey*: 确认操作接口实现
*cancellableKey*: 取消操作接口实现

### bytetcc使用工作量分析

1. 每部操作代码实现时必须分两步(Try/(Confirm、Cancel))

2. 对应每个服务的方法必须实现确认和取消操作
    
    




