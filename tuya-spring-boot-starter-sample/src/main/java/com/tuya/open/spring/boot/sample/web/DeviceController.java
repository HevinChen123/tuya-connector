package com.tuya.open.spring.boot.sample.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
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

    /**
     * 获取指令集（按品类）
     * 按品类来查询指令集，该指令集为涂鸦公版品类下最丰富的指令集，可供开发者参考使用。如果是平台类开发者，建议可按照此类进行开发对接。
     * category，字符串，类别名，例如：kg、cz、dj
     */
    @RequestMapping(value = "/functionCategory", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public String functionCategory(@RequestParam String category) {
        Object functionCategoryData;
        try {
            functionCategoryData = deviceService.functionCategory(category);
        } catch (Exception e) {
            JSONObject res = new JSONObject();
            res.put("code", "400");
            res.put("msg", e.getMessage());
            return res.toJSONString();
        }
        return JSON.toJSONString(functionCategoryData);
    }

    /**
     * 根据设备 ID 获取指令集（按设备）
     * 查询设备支持的功能，获取到的指令可用于下发控制。
     */
    @RequestMapping(value = "/functionDevice", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public String functionDevice(@RequestParam String deviceId) {
        Object functionDeviceData;
        try {
            functionDeviceData = deviceService.functionDevice(deviceId);
        } catch (Exception e) {
            JSONObject res = new JSONObject();
            res.put("code", "400");
            res.put("msg", e.getMessage());
            return res.toJSONString();
        }
        return JSON.toJSONString(functionDeviceData);
    }

    /**
     * 根据设备 ID 批量获取指令集（按设备）
     * 批量获取（多个）设备列表支持的指令集合，设备ID列表，多个ID逗号分隔，最多支持20个设备
     */
    @RequestMapping(value = "/functionDeviceBatch", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public String functionDeviceBatch(@RequestParam String deviceIds) {
        Object functionDeviceData;
        try {
            functionDeviceData = deviceService.functionDeviceBatch(deviceIds);
        } catch (Exception e) {
            JSONObject res = new JSONObject();
            res.put("code", "400");
            res.put("msg", e.getMessage());
            return res.toJSONString();
        }
        return JSON.toJSONString(functionDeviceData);
    }

    /**
     * 根据设备 ID 获取设备规格属性（包含指令集、状态集）
     */
    @RequestMapping(value = "/specificationsHome", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public String specifications(@RequestParam String deviceId) {
        Object specificationsData;
        try {
            specificationsData = deviceService.specifications(deviceId);
        } catch (Exception e) {
            JSONObject res = new JSONObject();
            res.put("code", "400");
            res.put("msg", e.getMessage());
            return res.toJSONString();
        }
        return JSON.toJSONString(specificationsData);
    }

    /**
     * 请求涂鸦接口，给设备（根据设备id）发送控制命令
     * 命令 commands 是json对象数组
     * 数组元素是，对象，结构
     * 根据获取到的指令集，可按一组或多组指令集进行下发，是否同时支持多指令同时执行依具体产品而定。
     * 可以参考上面官方提供的commandDevice方法
     */
    @RequestMapping(value = "/sendCommandControlDevice", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String sendCommandControlDevice(@RequestBody JSONObject param) {
        Object sendCommandData;
        try {
            String deviceId = param.getString("deviceId");
            JSONArray commands = param.getJSONArray("commands");
            JSONObject commandsObject = new JSONObject();
            commandsObject.put("commands", commands);
            sendCommandData = deviceService.sendCommandControlDevice(deviceId, commandsObject);
        } catch (Exception e) {
            JSONObject res = new JSONObject();
            res.put("code", "400");
            res.put("msg", e.getMessage());
            return res.toJSONString();
        }
        return JSON.toJSONString(sendCommandData);
    }

    /**
     * 根据设备 ID 来查询设备最新状态
     */
    @RequestMapping(value = "/deviceStatus", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public String deviceStatus(@RequestParam String deviceId) {
        Object statusData;
        try {
            statusData = deviceService.deviceStatus(deviceId);
        } catch (Exception e) {
            JSONObject res = new JSONObject();
            res.put("code", "400");
            res.put("msg", e.getMessage());
            return res.toJSONString();
        }
        return JSON.toJSONString(statusData);
    }
}
