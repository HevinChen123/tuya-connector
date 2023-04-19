package com.tuya.open.spring.boot.sample;

import com.alibaba.fastjson.JSON;
import com.alibaba.ttl.threadpool.TtlExecutors;
import com.tuya.connector.api.config.Configuration;

import com.tuya.connector.open.messaging.autoconfig.MessageProperties;
import com.tuya.connector.spring.boot.autoconfigure.ConnectorProperties;
import com.tuya.open.spring.boot.sample.service.DeviceService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import java.util.Calendar;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@SpringBootTest(classes = {TuyaSpringBootStarterSampleApplication.class})
class TuyaSpringBootStarterSampleApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private Configuration configuration;

    @Autowired
    private Environment environment;

    //Test类这里使用了message，并且用的是启动类主类的class，因此需要使用这个测试类，就需要在项目启动类，需要开启注解@EnableMessaging
    @Autowired
    private MessageProperties messageProperties;

    @Autowired
    private DeviceService deviceService;

    @Test
    void contextLoads() {
        ConnectorProperties bean = applicationContext.getBean(ConnectorProperties.class);
        Boolean auto = environment.getRequiredProperty("connector.api.auto-set-header", Boolean.class);
        System.out.println(configuration.getApiDataSource().getBaseUrl());
        System.out.println(messageProperties.getUrl());
        assertNotNull(bean.getApi().getContextManager());
        assertTrue(auto);
    }

    public ExecutorService executor = Executors.newFixedThreadPool(1);

    @Test
    public void threadTest() throws InterruptedException {
        executor = TtlExecutors.getTtlExecutorService(executor);

        CountDownLatch countDownLatch = new CountDownLatch(2);
        Thread t1 = new Thread(() ->{
            configuration.getApiDataSource().setAk("1");
            configuration.getApiDataSource().setSk("1");
            log.info("t1:{} ak:{}, sk:{}", Thread.currentThread(), configuration.getApiDataSource().getAk(), configuration.getApiDataSource().getSk());
            try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            executor.execute(()->
                    log.info("t1 child:{} ak:{}, sk:{}", Thread.currentThread(), configuration.getApiDataSource().getAk(), configuration.getApiDataSource().getSk()));
            countDownLatch.countDown();
        });

        Thread t2 = new Thread(() ->{
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            configuration.getApiDataSource().setAk("2");
            configuration.getApiDataSource().setSk("2");
            log.info("t2:{} ak:{}, sk{}",Thread.currentThread(), configuration.getApiDataSource().getAk(), configuration.getApiDataSource().getSk());
            executor.execute(() ->
                    log.info("t2 child:{} ak:{}, sk:{}", Thread.currentThread(), configuration.getApiDataSource().getAk(), configuration.getApiDataSource().getSk()));
            countDownLatch.countDown();
        });

        t1.start();
        t2.start();
        countDownLatch.await();
    }

    @Test
    public void test1() {
        System.out.println("测试获取信息");
        String deviceId = "6c13084e0437d1aece3lhn";
        String deviceIds = "6c13084e0437d1aece3lhn,6cbe71eed388c4e50dgnf4,6c93af6a5489e900acqghk";
        String deviceIdsEU = "bf109a9c840211e278vyws,bffad4b5bb317c8c5bijii";
        //System.out.println(deviceService.getById("6c13084e0437d1aece3lhn"));
        String energyType = "electricity";
        String energyAction = "consume";
        String statisticsType = "month";
        String startTime = "202303";
        String endTime = "202304";
        String containChilds = "true";
        String uid = "ay1655202900103fXX5k";
        //Object electricityObj = deviceService.statisticsSum(energyType, energyAction, statisticsType, startTime, endTime, containChilds, deviceIds);
        //System.out.println(JSON.toJSONString(electricityObj));

        System.out.println(JSON.toJSONString(deviceService.devices(deviceIdsEU, 1, 20), true));

//        Calendar calendar = Calendar.getInstance();
//        Long startTimeStamp = calendar.getTimeInMillis();
//        calendar.roll(Calendar.DAY_OF_MONTH, 5);
//        Long endTimeStamp = calendar.getTimeInMillis();
//        Object logsResult = deviceService.deviceLog(deviceId, 3, startTimeStamp, endTimeStamp);
//        System.out.println("日志结果 - " + JSON.toJSONString(logsResult, true));

//        Object devicesOfUserRes = deviceService.devicesOfUser(uid, "home");
//        System.out.println("用户下属设备");
//        System.out.println(JSON.toJSONString(devicesOfUserRes, true));
          /*
            6c3a59a3b95775e0f8ligo,6c13084e0437d1aece3lhn,6c1754b46ea4e48cffizu8,6cb360b67b6d960b7a4bqs,6c93af6a5489e900acqghk
            可得知，能管的 uid和涂鸦上的 uid，是对应同一个用户，但是两处的id不同
           */
    }

    @Test
    public void test2() {
        // 获取设备当前支持的统计类型。
        String deviceId = "6c13084e0437d1aece3lhn";
        Object allStatisticType = deviceService.allStatisticType(deviceId);
        System.out.println("设备支持的数据类型");
        System.out.println(JSON.toJSONString(allStatisticType, true));
    }

    @Test
    public void test3() {
        // 获取历史累计值
        String deviceId = "6c13084e0437d1aece3lhn";
        String code = "add_ele";
        Object historyData = deviceService.historyStatistic(deviceId, code);
        System.out.println("历史累计值");
        System.out.println(JSON.toJSONString(historyData, true));
    }

    @Test
    public void test4() {
        // 按分钟统计
        String deviceId = "6c13084e0437d1aece3lhn";
        String code = "add_ele";
        String startMinute = "202304170130";
        String endMinute = "202304181600";
        Object minuteData = deviceService.dataMinute(deviceId, code, startMinute, endMinute);
        System.out.println("按分钟统计");
        System.out.println(JSON.toJSONString(minuteData, true));
    }

    @Test
    public void test5() {
        // 按小时统计
        String deviceId = "6c13084e0437d1aece3lhn";
        String code = "add_ele";
        String startHour = "2023041700";
        String endHour = "2023041723";
        String statType = "sum";
        Object hourData = deviceService.dataHour(deviceId, code, startHour, endHour, statType);
        System.out.println("按小时统计");
        System.out.println(JSON.toJSONString(hourData, true));
    }

    @Test
    public void test6() {
        // 按天统计
        String deviceId = "6c13084e0437d1aece3lhn";
        String code = "add_ele";
        String startDay = "20230101";
        String endDay = "20230418";
        String statType = "sum";
        Object dayData = deviceService.dataDay(deviceId, code, startDay, endDay, statType);
        System.out.println("按天统计");
        System.out.println(JSON.toJSONString(dayData, true));
    }

    @Test
    public void test7() {
        // 按星期统计
        String deviceId = "6c13084e0437d1aece3lhn";
        String code = "add_ele";
        String startWeek = "202317";
        String endWeek = "202318";
        Object weekData = deviceService.dataWeek(deviceId, code, startWeek, endWeek);
        System.out.println("按星期统计");
        System.out.println(JSON.toJSONString(weekData, true));
    }

    @Test
    public void test8() {
        // 按月统计
        String deviceId = "6c13084e0437d1aece3lhn";
        String code = "add_ele";
        String startMonth = "202301";
        String endMonth = "202304";
        Object monthData = deviceService.dataMonth(deviceId, code, startMonth, endMonth);
        System.out.println("按月统计");
        System.out.println(JSON.toJSONString(monthData, true));
    }

}
