package com.cybercloud.sprbotfreedom;

import com.cybercloud.sprbotfreedom.platform.StartApplication;
import com.cybercloud.sprbotfreedom.platform.datasource.DataSourceConfig;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author liuyutang
 * @date 2023/7/11
 */
@SpringBootTest(classes = StartApplication.class,webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = BaseTest.class)
@ConditionalOnClass(value = DataSourceConfig.class)
public class BaseTest {

}
