package com.tuya.connector.open.ability.device.connector;

import com.tuya.connector.api.annotations.*;
import com.tuya.connector.open.ability.common.AbilityPage;
import com.tuya.connector.open.ability.device.model.request.DeviceCommandRequest;
import com.tuya.connector.open.ability.device.model.request.DeviceModifyRequest;
import com.tuya.connector.open.ability.device.model.response.DeviceSpecification;
import com.tuya.connector.open.ability.device.model.response.DeviceStatusLogResultRsp;
import com.tuya.connector.open.ability.device.model.response.DeviceStatuses;
import com.tuya.connector.open.ability.device.model.response.Devices;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * <p> TODO
 *
 * @author @author qiufeng.yu@tuya.com
 * @since 2021/4/1 10:10 下午
 */
public interface DeviceConnector {

    /**
     * delete device
     *
     * @param deviceId
     * @return
     */
    @DELETE("/v1.0/iot-03/devices/{device_id}")
    Boolean deleteDevice(@Path(("device_id")) String deviceId);

    /**
     * delete devices
     *
     * @param deviceIds
     * @return
     */
    @DELETE("/v1.0/iot-03/devices")
    Boolean deleteDevices(@Query("device_ids") String deviceIds);

    /**
     * modify device
     *
     * @param deviceId
     * @param request
     * @return
     */
    @PUT("/v1.0/iot-03/devices/{device_id}")
    Boolean modifyDevice(@Path("device_id") String deviceId, @Body DeviceModifyRequest request);

    /**
     * select device by deviceId
     *
     * @param deviceId
     * @return
     */
    //@GET("/v1.0/iot-03/devices/{device_id}")
    @GET("/v1.0/devices/{device_id}")
    Devices.Device selectDevice(@Path("device_id") String deviceId);

    /**
     * select device by deviceIds
     *
     * @param deviceIds
     * @return
     */
    @GET("/v1.0/iot-03/devices")
    Devices selectDevices(@Query("device_ids") String deviceIds);

    /**
     * select device status by deviceId
     *
     * @param deviceId
     * @return
     */
    @GET("/v1.0/iot-03/devices/{device_id}/status")
    List<DeviceStatuses.DeviceStatus> selectDeviceStatus(@Path("device_id") String deviceId);

    /**
     * select device status by deviceIds
     *
     * @param deviceIds
     * @return
     */
    @GET("/v1.0/iot-03/devices/status")
    List<DeviceStatuses> selectDeviceStatuses(@Query("device_ids") String deviceIds);

    /**
     * select device function
     *
     * @param deviceId
     * @return
     */
    @GET("/v1.0/iot-03/devices/{device_id}/functions")
    DeviceSpecification selectDeviceFunctions(@Path("device_id") String deviceId);

    /**
     * select device specification
     *
     * @param deviceId
     * @return
     */
    @GET("/v1.0/iot-03/devices/{device_id}/specification")
    DeviceSpecification selectDeviceSpecification(@Path("device_id") String deviceId);

    /**
     * select device commands
     *
     * @param deviceId
     * @param request
     * @return
     */
    @POST("/v1.0/iot-03/devices/{device_id}/commands")
    Boolean commandDevice(@Path("device_id") String deviceId, @Body DeviceCommandRequest request);

    /**
     * select device logs
     *
     * @param deviceId
     * @param startTime
     * @param endTime
     * @param lastRowKey
     * @param eventTypes
     * @param pageSize
     * @return
     */
    @GET("/v1.0/iot-03/devices/{device_id}/logs")
    AbilityPage<DeviceStatusLogResultRsp> deviceEventLog(@Path("device_id") String deviceId,
                                                         @Query("start_time") Long startTime,
                                                         @Query("end_time") Long endTime,
                                                         @Query("last_row_key") String lastRowKey,
                                                         @Query("event_types") String eventTypes,
                                                         @Query("size") Integer pageSize);

    /**
     * select device report-logs
     *
     * @param deviceId
     * @param startTime
     * @param endTime
     * @param lastRowKey
     * @param codes
     * @param pageSize
     * @return
     */
    @GET("/v1.0/iot-03/devices/{device_id}/report-logs")
    AbilityPage<DeviceStatusLogResultRsp> deviceStatusLog(@Path("device_id") String deviceId,
                                                          @Query("start_time") Long startTime,
                                                          @Query("end_time") Long endTime,
                                                          @Query("last_row_key") String lastRowKey,
                                                          @Query("codes") String codes,
                                                          @Query("size") Integer pageSize);


    /* 返回错误信息 https://support.tuya.com/zh/help/_detail/Kc6worzfbkldf

     */

    /**
     * 查询一定时间范围内，多设备下能源统计数据，包括水、电、气的使用、生成或转储统计数据
     * https://developer.tuya.com/cn/docs/cloud/a1a5f34cf5?id=Kb984gfjmbbc4
     */
    @GET("/v1.0/iot-03/energy/{energy_type}/device/nodes/statistics-sum")
    Float statisticsSum(@Path("energy_type") String energyType,
                         @Query("energy_action") String energyAction,
                         @Query("statistics_type") String statisticsType,
                         @Query("start_time") String startTime,
                         @Query("end_time") String endTime,
                         @Query("contain_childs") String containChilds,
                         @Query("device_ids") String deviceIds);

    @GET("/v1.0/energy/electricity/device/nodes/statistics-sum")
    Object statisticsSumTwo(
                        @Query("energy_action") String energyAction,
                        @Query("statistics_type") String statisticsType,
                        @Query("start_time") String startTime,
                        @Query("end_time") String endTime,
                        @Query("contain_childs") String containChilds,
                        @Query("device_ids") String deviceIds);

    /**
     * 批量获取设备信息（根据id）
     * 可以拿到设备主人用户在 tuya 中的uid
     */
    @GET("/v1.0/devices")
    Object devices(@Query("device_ids") String deviceIds,
                                 @Query("page_no") int pageNo,
                                 @Query("page_size") int pageSize);

    /**
     * 根据查询条件查询设备操作历史记录
     * https://developer.tuya.com/cn/docs/cloud/device-management?id=K9g6rfntdz78a#sjlx1
     * 时间，时间戳，Long 13位数
     */
    @GET("/v1.0/devices/{device_id}/logs")
    Object deviceLog(@Path("device_id") String deviceId,
                     @Query("type") int type,
                     @Query("start_time") Long startTime,
                     @Query("end_time") Long endTime);

    /**
     * 可查询用户下可操作的设备列表，包括设备属性、设备最新状态
     */
    @GET("/v1.0/users/{uid}/devices")
    Object devicesOfUser(@Path("uid") String uid,
                     @Query("from") String from);


    // ------以下为全屋体系的设备获取电量信息（单位，千瓦时）
    // https://developer.tuya.com/cn/docs/cloud/device-data-statistic?id=Ka7g7nvnad1rm


    /**
     * 获取历史累计值
     * 功能点 Code，根据产品而定（必填）。
     */
    @GET("/v1.0/devices/{device_id}/statistics/total")
    Object historyStatistic(@Path("device_id") String deviceId,
                            @Query("code") String code);

    /**
     * 获取设备当前支持的统计类型。
     * 对应历史累计值的，功能点 Code
     * 默认获取全部支持的功能点
     */
    @GET("/v1.0/devices/{dev_id}/all-statistic-type")
    Object allStatisticType(@Path("dev_id") String devId);

    /**
     * 按分钟统计
     * 以 15 分钟为间隔展示设备近 7 天的统计数据。
     * 时间 精确到分钟，格式：yyyyMMddHHmm
     */
    @GET("/v1.0/devices/{dev_id}/statistics/quarters")
    Object dataMinute(@Path("dev_id") String devId,
                      @Query("code") String code,
                      @Query("start_minute") String startMinute,
                      @Query("end_minute") String endMinute);


    /**
     * 按小时统计
     * 以小时为单位，返回设备近 7 天的统计数据。
     * 时间，精确到小时，格式：yyyyMMddHH，结束大于开始
     * stat_type 统计类型默认为 sum，可选填 count 和 avg
     * 注意，开始小时和结束小时不允许跨天，只能一天，并且开始必须是00，结束必须是23，否则报错 1109 参数非法
     * 一次只能拿到七天内的，某一天的24小时的，电量数据。如果今天还没有结束，那么后续的小时，电量都是填充0
     */
    @GET("/v1.0/devices/{device_id}/statistics/hours")
    Object dataHour(@Path("device_id") String devId,
                      @Query("code") String code,
                      @Query("start_hour") String startHour,
                      @Query("end_hour") String endHour,
                      @Query("stat_type") String statType);

    /**
     * 按天统计累计值
     * 以天为单位，返回设备的统计数据
     * 时间，精确到日，格式：yyyyMMdd，结束大于开始
     * stat_type 统计类型默认为 sum，可选填 count 和 avg
     */
    @GET("/v1.0/devices/{device_id}/statistics/days")
    Object dataDay(@Path("device_id") String deviceId,
                    @Query("code") String code,
                    @Query("start_day") String startDay,
                    @Query("end_day") String endDay,
                    @Query("stat_type") String statType);

    /**
     * 按星期统计累计值
     * 以周为单位，返回设备的统计数据
     * 开始时间的周数，示例：201805，表示 2018 年第 5 周。
     */
    @GET("/v1.0/devices/{device_id}/statistics/weeks")
    Object dataWeek(@Path("device_id") String deviceId,
                    @Query("code") String code,
                    @Query("start_week") String startWeek,
                    @Query("end_week") String endWeek);

    /**
     * 按月统计累计值
     * 以月为单位，返回设备的统计数据
     * 时间 精确到月，yyyyMM，201901
     */
    @GET("/v1.0/devices/{device_id}/statistics/months")
    Object dataMonth(@Path("device_id") String deviceId,
                    @Query("code") String code,
                    @Query("start_month") String startMonth,
                    @Query("end_month") String endMonth);
}
