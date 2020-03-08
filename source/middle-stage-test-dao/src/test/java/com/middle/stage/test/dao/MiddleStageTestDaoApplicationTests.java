package com.middle.stage.test.dao;

import com.middle.stage.test.dao.mapper.UserMapper;
import com.middle.stage.test.dao.data.UserDO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

@SpringBootTest
@Slf4j
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

    @DisplayName("测试添加用户")
    @Test
    @Transactional
    void insertTest() {

        //验证测试用例是否创建
        Assertions.assertNotNull(userDO, "userDO is null");

        userMapper.insertSelective(userDO);
        UserDO userEntity = userMapper.selectByMobile(mobile);

        //验证是否添加成功
        Assertions.assertNotNull(userEntity, "insert error");
        Assertions.assertEquals(password, userEntity.getPassword(), "password not equals");
        //Assertions.assertEquals(userName, userEntity.getUserName(), "userName not equals");
        log.info("测试通过");
    }

}
