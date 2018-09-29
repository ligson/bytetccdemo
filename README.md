bytetccdemo
====================

### 结构
    
    sample-consumer:对外服务

    sample-provider:内部服务

    sample-eureka-server：注册中心
    
### TCC事务机制简介

关于TCC（Try-Confirm-Cancel）的概念，最早是由Pat Helland于2007年发表的一篇名为《Life beyond Distributed Transactions:an Apostate’s Opinion》的论文提出。在该论文中，TCC还是以Tentative-Confirmation-Cancellation作为名称；正式以Try-Confirm-Cancel作为名称的，可能是Atomikos（Gregor Hohpe所著书籍《Enterprise Integration Patterns》中收录了关于TCC的介绍，提到了Atomikos的Try-Confirm-Cancel，并认为二者是相似的概念）。国内最早关于TCC的报道，应该是InfoQ上对阿里程立博士的一篇采访。经过程博士的这一次传道之后，TCC在国内逐渐被大家广为了解并接受。相应的实现方案和开源框架也先后被发布出来，ByteTCC就是其中之一。TCC事务机制相对于传统事务机制（X/Open XA），其特征在于它不依赖资源管理器(RM)对XA的支持，而是通过对（由业务系统提供的）业务逻辑的调度来实现分布式事务。对于业务系统中一个特定的业务逻辑S，其对外提供服务时，必须接受一些不确定性，即对业务逻辑执行的一次调用仅是一个临时性操作，调用它的消费方服务M保留了后续的取消权。如果M认为全局事务应该rollback，它会要求取消之前的临时性操作，这就对应S的一个取消操作。而当M认为全局事务应该commit时，它会放弃之前临时性操作的取消权，这对应S的一个确认操作。 每一个初步操作，最终都会被确认或取消。因此，针对一个具体的业务服务，TCC事务机制需要业务系统提供三段业务逻辑：初步操作Try、确认操作Confirm、取消操作Cancel。

1. 初步操作（Try）

    TCC事务机制中的业务逻辑（Try），从执行阶段来看，与传统事务机制中业务逻辑相同。但从业务角度来看，是不一样的。TCC机制中的Try仅是一个初步操作，它和后续的次确认一起才能真正构成一个完整的业务逻辑。因此，可以认为[传统事务机制]的业务逻辑 = [TCC事务机制]的初步操作（Try） + [TCC事务机制]的确认逻辑（Confirm）。TCC机制将传统事务机制中的业务逻辑一分为二，拆分后保留的部分即为初步操作（Try）；而分离出的部分即为确认操作（Confirm），被延迟到事务提交阶段执行。

    TCC事务机制以初步操作（Try）为中心，确认操作（Confirm）和取消操作（Cancel）都是围绕初步操作（Try）而展开。因此，Try阶段中的操作，其保障性是最好的，即使失败，仍然有取消操作（Cancel）可以将其不良影响进行回撤。

2. 确认操作（Confirm）

    确认操作（Confirm）是对初步操作（Try）的一个补充。当TCC事务管理器认为全局事务可以正确提交时，就会逐个执行初步操作（Try）指定的确认操作（Confirm），将初步操作（Try）未完成的事项最终完成。

3. 取消操作（Cancel）

    取消操作（Cancel）是对初步操作（Try）的一个回撤。当TCC事务管理器认为全局事务不能正确提交时，就会逐个执行初步操作（Try）指定的取消操作（Cancel），将初步操作（Try）已完成的事项全部撤回。

    在传统事务机制中，业务逻辑的执行和事务的处理，是在不同的阶段由不同的部件来处理的：业务逻辑部分访问资源实现数据存储，其处理是由业务系统负责；事务处理部分通过协调资源管理器以实现事务管理，其处理由事务管理器来负责。二者没有太多交互的地方，所以，传统事务管理器的事务处理逻辑，仅需要着眼于事务完成（commit/rollback）阶段，而不必关注业务执行阶段。而在TCC事务机制中的业务逻辑和事务处理，其关系就错综复杂：业务逻辑（Try/Confirm/Cancel）阶段涉及所参与资源事务的commit/rollback；全局事务commit/rollback时又涉及到业务逻辑（Try/Confirm/Cancel）的执行。

### 使用流程
    
    1. 把dt.sql dt2.sql 导入数据库
    
    2. sample-consumer使用数据库 dt2(数据库连接方式配置参考ConsumerConfig)
    
    3. sample-provider使用数据dt (数据库连接方式配置参考ProviderConfig)

    4. 配置后依次启动 sample-eureka-server sample-provider sample-consumer
    
    5. post调用接口 http://localhost:7080/transfer 参数
     
       sourceAcctId : 1001   //用户1 id
       targetAcctId : 2001   //用户2 id
       amount : 10           //用户1001 向2001转账金额

### 业务流程（用户1001(provider服务)像用户2001(consumer服务)转账10元）
    
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

3. 由于代码比较繁琐，try时必须预留冻结状态字段，所以关键操作这么做，不然代码量会比较多

### bytetcc使用约束

1.共同约束
    
    仅支持使用@Transactional来配置事务，而不能通过xml来配置事务；
    针对一个特定的可补偿型服务接口，业务系统提供的Try、Confirm、Cancel三个实现类，其Try实现类必须定义@Compensable注解，而Confirm、Cancel实现类则不能定义Compensable注解；
    可补偿型服务的Try/Confirm/Cancel实现类/实现方法必须定义Transactional注解，且propagation必须是Required, RequiresNew, Mandatory中的一种（即业务代码必须参与事务，从0.3.0开始强制要求）；
    业务系统不可使用随机端口（本约束与采用的负载均衡分发粒度相关，当使用ByteTCC按请求粒度负载均衡时可忽略该约束）；
    用@Compensable注解的TCC型服务Controller必须实现一个业务接口（业务系统自行指定，但需要与Compensable.interfaceClass一致）；
    在每个参与tcc事务的数据库中创建bytejta表（ddl见bytetcc-supports.jar/bytetcc.sql）；
    JDK版本：7.0及以上版本；

1.使用Spring Cloud的约束
    
    服务提供方Controler必须添加@Compensable注解；
    不允许对Feign/Ribbon/RestTemplate等HTTP请求自行进行封装，但允许拦截；
    如果需要定制instanceId, 格式必须为${ip}:${自行指定}:${port}；
    


