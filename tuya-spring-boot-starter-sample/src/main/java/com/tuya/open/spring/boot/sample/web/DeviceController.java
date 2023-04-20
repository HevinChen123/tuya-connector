package com.tuya.open.spring.boot.sample.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tuya.connector.open.ability.device.model.request.DeviceCommandRequest;
import com.tuya.connector.open.ability.device.model.request.DeviceModifyRequest;
import com.tuya.connector.open.ability.device.model.response.Devices;
import com.tuya.open.spring.boot.sample.ability.model.Device;
import com.tuya.open.spring.boot.sample.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/4/1 10:14 下午
 */
@RestController
@RequestMapping("/devices")
public class DeviceController {
    @Autowired
    private DeviceService deviceService;


    @GetMapping("/{device_id}")
    public Devices.Device getById(@PathVariable("device_id") String deviceId) {
        return deviceService.getById(deviceId);
    }

    @PostMapping("/{device_id}")
    public Boolean updateName(@PathVariable("device_id") String deviceId, @RequestBody DeviceModifyRequest request) {
        return deviceService.updateName(deviceId, request);
    }

    @PostMapping("/{device_id}/commands")
    public Boolean commands(@PathVariable("device_id") String deviceId, @RequestBody List<DeviceCommandRequest.Command> commands) {
        DeviceCommandRequest request = new DeviceCommandRequest();
        request.setCommands(commands);
        return deviceService.commands(deviceId, request);
    }
    /**
     * 批量获取设备信息（根据id）
     * 可以拿到设备主人用户在 tuya 中的uid
     */
    @RequestMapping(value = "/batchGetDevicesByIds", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public String batchGetDevicesByIds(@RequestParam String deviceIds, @RequestParam int pageNo, @RequestParam int pageSize) {
        Object devicesData;
        if (pageNo <= 0) {
            pageNo = 1;
        }
        if (pageSize <= 0) {
            pageSize = 20;
        }
        try {
            devicesData = deviceService.devices(deviceIds, pageNo, pageSize);
        } catch (Exception e) {
            // 如果出错，则根据tuya结果，构造空对象
            JSONObject res = new JSONObject();
            res.put("code", "400");
            res.put("msg", e.getMessage());
            return res.toJSONString();
        }
        return JSON.toJSONString(devicesData);
    }

    /**
     * 可查询用户下可操作的设备列表，包括设备属性、设备最新状态
     * from 为 home 或 sharing，可空
     */
    @RequestMapping(value = "/devicesOfUser", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public String devicesOfUser(@RequestParam String uid, @RequestParam String from) {
        Object devicesData;
        try {
            devicesData = deviceService.devicesOfUser(uid, from);
        } catch (Exception e) {
            // 如果出错，则根据tuya结果，构造空对象
            JSONObject res = new JSONObject();
            res.put("code", "400");
            res.put("msg", e.getMessage());
            return res.toJSONString();
        }
        return JSON.toJSONString(devicesData);
    }
}
