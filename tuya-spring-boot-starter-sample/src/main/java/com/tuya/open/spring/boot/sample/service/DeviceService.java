package com.tuya.open.spring.boot.sample.service;

import com.tuya.connector.api.annotations.Path;
import com.tuya.connector.api.annotations.Query;
import com.tuya.connector.open.ability.device.connector.DeviceConnector;
import com.tuya.connector.open.ability.device.model.request.DeviceCommandRequest;
import com.tuya.connector.open.ability.device.model.request.DeviceModifyRequest;
import com.tuya.connector.open.ability.device.model.response.Devices;
import com.tuya.open.spring.boot.sample.ability.model.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/4/1 10:13 下午
 */
@Service
public class DeviceService {
    @Autowired
    private DeviceConnector connector;

    public Devices.Device getById(String deviceId) {
        return connector.selectDevice(deviceId);
    }

    public Boolean updateName(String deviceId, DeviceModifyRequest request) {
        return connector.modifyDevice(deviceId, request);
    }

    public Boolean commands(String deviceId, DeviceCommandRequest request) {
        return connector.commandDevice(deviceId, request);
    }

    public Float statisticsSum(String energyType,
                               String energyAction,
                               String statisticsType,
                               String startTime,
                               String endTime,
                               String containChilds,
                               String deviceIds) {
        return connector.statisticsSum(energyType, energyAction, statisticsType, startTime, endTime, containChilds, deviceIds);
    }

    public Object statisticsSumTwo(
                               String energyAction,
                               String statisticsType,
                               String startTime,
                               String endTime,
                               String containChilds,
                               String deviceIds) {
        return connector.statisticsSumTwo(energyAction, statisticsType, startTime, endTime, containChilds, deviceIds);
    }

    public Object devices(String deviceIds, int pageNo, int pageSize) {
        return connector.devices(deviceIds, pageNo, pageSize);
    }

    public Object deviceLog(String deviceId, int type, Long startTime, Long endTime) {
        return connector.deviceLog(deviceId, type, startTime, endTime);
    }

    public Object devicesOfUser(String uid, String from) {
        return connector.devicesOfUser(uid, from);
    }

    public Object historyStatistic(String deviceId, String code) {
        return connector.historyStatistic(deviceId, code);
    }

    public Object allStatisticType(String devId) {
        return connector.allStatisticType(devId);
    }

    public Object dataMinute(String devId, String code, String startMinute, String endMinute) {
        return connector.dataMinute(devId, code, startMinute, endMinute);
    }

    public Object dataHour(String devId, String code, String startHour, String endHour, String statType) {
        return connector.dataHour(devId, code, startHour, endHour, statType);
    }

    public Object dataDay(String deviceId, String code, String startDay, String endDay, String statType) {
        return connector.dataDay(deviceId, code, startDay, endDay, statType);
    }

    public Object dataWeek(String deviceId, String code, String startWeek, String endWeek) {
        return connector.dataWeek(deviceId, code, startWeek, endWeek);
    }

    public Object dataMonth(String deviceId, String code, String startMonth, String endMonth) {
        return connector.dataMonth(deviceId, code, startMonth, endMonth);
    }
}
