---
order: 2
group:
  title: 单元测试
  order: 3
---

# 数据访问测试

## 介绍

数据访问测试，就是项目中的 DAO 层测试。DAO 的测试相对简单，这一层不需要隔离依赖，您只需要遵守单元测试的几点[原则](https://github.com/alibaba/p3c/blob/master/p3c-gitbook/%E5%8D%95%E5%85%83%E6%B5%8B%E8%AF%95.md#L17)：

- 全自动&非交互式
- 设定自动回滚
- 遵守 AIR 原则

## 演示 [Demo](https://github.com/xiyun-international/java-unit-docs/tree/master/source/middle-stage-test-dao)

### Mapper 代码

```java
/**
 * 添加不为空字段
 * @param userDO
 * @return
 */
int insertSelective(UserDO userDO);

/**
 * 通过手机号查询用户
 * @param mobile
 * @return
 */
UserDO selectByMobile(String mobile);
```

### mapper.xml

mapper 中的代码过长就不予展示，可以[在线查看](https://github.com/xiyun-international/java-unit-docs/blob/master/source/middle-stage-test-dao/src/main/resources/mapper/UserMapper.xml)。

### 测试代码

- 通过 @BeforeAll 注解，在单元测试执行前准备测试数据。
- 通过 @Transactional 注解，保证单元测试运行后，事物自动回滚。
- 测试方法使用断言先判断测试数据是否初始化，然后将测试数据执行添加后，再执行查询，判断是否添加成功，及字段值是否符合预期值。

```java
@Slf4j
@SpringBootTest
class MiddleStageTestDaoApplicationTests {

    @Autowired
    private UserMapper userMapper;
    static UserDO userDO;
    static String mobile = "17612345678";
    static String password = "123456";
    String userName = "zyq";

    @BeforeAll
    static void beforInsertTest() {
        userDO = new UserDO();
        userDO.setId(UUID.randomUUID().toString());
        userDO.setMobile(mobile);
        userDO.setPassword(password);
        userDO.setCrateTime(new Date());
        userDO.setUpdateTime(new Date());
    }

    @Test
    @Transactional
    @DisplayName("测试添加用户")
    void insertTest() {

        //验证测试用例是否创建
        Assertions.assertNotNull(userDO, "userDO is null");

        userMapper.insertSelective(userDO);
        UserDO userResult = userMapper.selectByMobile(mobile);

        //验证是否添加成功
        Assertions.assertNotNull(userResult, "insert error");
        Assertions.assertEquals(password, userResult.getPassword(), "password not equals");

        //由于没有添加用户名，运行这断代码会抛出异常
        //Assertions.assertEquals(userName, userResult.getUserName(), "userName not equals");
        log.info("[测试通过]");
    }

}
```

## 运行结果

```java
2020-03-08 19:20:13.854  INFO 18264 --- [main] s.t.d.MiddleStageTestDaoApplicationTests : [测试通过]
2020-03-08 19:20:13.883  INFO 18264 --- [main] o.s.t.c.transaction.TransactionContext   : Rolled back transaction for test
```
