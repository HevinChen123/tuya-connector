package com.tuya.open.spring.boot.sample.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tuya.open.spring.boot.sample.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/electricity/")
public class ElectricityDataController {

    @Autowired
    private DeviceService deviceService;

    /**
     * 按月统计累计值
     * 以月为单位，返回设备的统计数据
     * 时间 精确到月，yyyyMM，201901
     * 开始月份和结束月份，不支持跨年
     */
    @RequestMapping(value = "month", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public String dataMonth(@RequestParam String deviceId,
                            @RequestParam String startMonth,
                            @RequestParam String endMonth) {
        String code = "add_ele";
        String statType = "sum";
        Object monthData;
        try {
            monthData = deviceService.dataMonth(deviceId, code, startMonth, endMonth);
        } catch (Exception e) {
            // 如果出错，则根据tuya结果，构造空对象
            JSONObject res = new JSONObject();
            res.put("months", new JSONObject());
            res.put("code", "400");
            res.put("msg", e.getMessage());
            return res.toJSONString();
        }
        return JSON.toJSONString(monthData);
    }

    /**
     * 按天统计累计值
     * 以天为单位，返回设备的统计数据
     * 时间，精确到日，格式：yyyyMMdd，结束大于开始
     * stat_type 统计类型默认为 sum，可选填 count 和 avg
     */
    @RequestMapping(value = "day", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public String dataDay(@RequestParam String deviceId,
                            @RequestParam String startDay,
                            @RequestParam String endDay) {
        String code = "add_ele";
        String statType = "sum";
        Object dayData;
        try {
            dayData = deviceService.dataDay(deviceId, code, startDay, endDay, statType);
//            System.out.println("请求了日数据接口");
//            System.out.println(JSON.toJSONString(dayData));
        } catch (Exception e) {
            // 如果出错，则根据tuya结果，构造空对象
            JSONObject res = new JSONObject();
            res.put("days", new JSONObject());
            res.put("code", "400");
            res.put("msg", e.getMessage());
            return res.toJSONString();
        }
        return JSON.toJSONString(dayData);
    }

    // 测试
    @RequestMapping(value = "test", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public String test() {
        return "hello-world";
    }

    /**
     * 按小时统计
     * 以小时为单位，返回设备近 7 天的统计数据。
     * 时间，精确到小时，格式：yyyyMMddHH，结束大于开始
     * stat_type 统计类型默认为 sum，可选填 count 和 avg
     * 注意，开始小时和结束小时不允许跨天，只能一天，并且开始必须是00，结束必须是23，否则报错 1109 参数非法
     * 一次只能拿到七天内的，某一天的24小时的，电量数据。如果今天还没有结束，那么后续的小时，电量都是填充0
     */
    @RequestMapping(value = "hour", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public String dataHour(@RequestParam String deviceId,
                          @RequestParam String startHour,
                          @RequestParam String endHour) {
        String code = "add_ele";
        String statType = "sum";
        Object hourData;
        try {
            hourData = deviceService.dataHour(deviceId, code, startHour, endHour, statType);
        } catch (Exception e) {
            // 如果出错，则根据tuya结果，构造空对象
            JSONObject res = new JSONObject();
            res.put("hours", new JSONObject());
            res.put("code", "400");
            res.put("msg", e.getMessage());
            return res.toJSONString();
        }
        return JSON.toJSONString(hourData);
    }


}
