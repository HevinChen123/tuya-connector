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

    @RequestMapping(value = "day", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public String dataDay(@RequestParam String deviceId,
                            @RequestParam String startDay,
                            @RequestParam String endDay) {
        String code = "add_ele";
        String statType = "sum";
        Object dayData;
        try {
            dayData = deviceService.dataDay(deviceId, code, startDay, endDay, statType);
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
}
