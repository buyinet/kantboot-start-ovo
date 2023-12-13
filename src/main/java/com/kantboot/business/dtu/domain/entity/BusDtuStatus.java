package com.kantboot.business.dtu.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "bus_dtu")
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
public class BusDtuStatus {

    /**
     * 主键
     * IMEI
     */
    @Id
    @Column(name = "imei",length = 64)
    private String imei;

    /**
     * 电流01
     */
    @Column(name = "electric_current_01")
    private Float electricCurrent01;

    /**
     * 电流02
     */
    @Column(name = "electric_current_02")
    private Float electricCurrent02;

    /**
     * 电压01
     */
    @Column(name = "voltage_01")
    private Float voltage01;

    /**
     * 频率01
     */
    @Column(name = "frequency_01")
    private Float frequency01;

    /**
     * 电流03
     */
    @Column(name = "electric_current_03")
    private Float electricCurrent03;

    /**
     * 电流04
     */
    @Column(name = "electric_current_04")
    private Float electricCurrent04;

    /**
     * 电压02
     */
    @Column(name = "voltage_02")
    private Float voltage02;

    /**
     * 频率02
     */
    @Column(name = "frequency_02")
    private Float frequency02;

    /**
     * 过电压值
     */
    @Column(name = "over_voltage_value")
    private Float overVoltageValue;

    /**
     * 过电压时限
     */
    @Column(name = "over_voltage_time_limit")
    private Long overVoltageTimeLimit;

    /**
     * 过电压次数
     */
    @Column(name = "over_voltage_count")
    private Long overVoltageCount;

    /**
     * 低电压值
     */
    @Column(name = "low_voltage_value")
    private Float lowVoltageValue;

    /**
     * 低电压时限
     */
    @Column(name = "low_voltage_time_limit")
    private Long lowVoltageTimeLimit;

    /**
     * 低电压次数
     */
    @Column(name = "low_voltage_count")
    private Long lowVoltageCount;

    /**
     * 漏电流值
     */
    @Column(name = "leakage_current_value")
    private Float leakageCurrentValue;

    /**
     * 漏电流时限
     */
    @Column(name = "leakage_current_time_limit")
    private Long leakageCurrentTimeLimit;

    /**
     * 漏电流次数
     */
    @Column(name = "leakage_current_count")
    private Long leakageCurrentCount;

    /**
     * 过载保护
     */
    @Column(name = "overload_protection")
    private Float overloadProtection;

    /**
     * 过载时限
     */
    @Column(name = "overload_time_limit")
    private Long overloadTimeLimit;

    /**
     * 过载次数
     */
    @Column(name = "overload_count")
    private Long overloadCount;

    /**
     * 短路次数
     */
    @Column(name = "short_circuit_count")
    private Long shortCircuitCount;

    /**
     * 雷击次数
     */
    @Column(name = "lightning_strike_count")
    private Long lightningStrikeCount;

    /**
     * 重合闸次数
     */
    @Column(name = "closing_operation_count")
    private Long closingOperationCount;

    /**
     * 保存
     */
    @Column(name = "save_count")
    private Long saveCount;

    /**
     * 过压合闸电压
     */
    @Column(name = "over_voltage_closing_voltage")
    private Float overVoltageClosingVoltage;

    /**
     * 欠压合闸电压
     */
    @Column(name = "under_voltage_closing_voltage")
    private Float underVoltageClosingVoltage;

    /**
     * 过压状态
     */
    @Column(name = "over_voltage_status")
    private Long overVoltageStatus;

    /**
     * 欠压状态
     */
    @Column(name = "under_voltage_status")
    private Long underVoltageStatus;

    /**
     * 漏电状态
     */
    @Column(name = "leakage_status")
    private Long leakageStatus;

    /**
     * 过载状态
     */
    @Column(name = "overload_status")
    private Long overloadStatus;

    /**
     * 短路状态
     */
    @Column(name = "short_circuit_status")
    private Long shortCircuitStatus;


    /**
     * 重合闸动作次数
     */
    @Column(name = "number_of_reclosing_actions")
    private Long numberOfreclosingActions;

    /**
     * 重合闸剩余时间
     */
    @Column(name = "closing_operation_remaining_time")
    private Float closingOperationRemainingTime;

    /**
     * 重合闸故障复归时间
     */
    @Column(name = "closing_operation_fault_recovery_time")
    private Float closingOperationFaultRecoveryTime;

    /**
     * 输出状态指示
     */
    @Column(name = "output_status_indication")
    private Long outputStatusIndication;

    /**
     * 过流设定重合闸次数
     */
    @Column(name = "overcurrent_setting_closing_count")
    private Long overcurrentSettingClosingCount;

    /**
     * 过载重合闸动作次数
     */
    @Column(name = "overload_closing_operation_count")
    private Long overloadClosingOperationCount;

    /**
     * 过载动作合闸倒计时
     */
    @Column(name = "overload_action_closing_countdown")
    private Float overloadActionClosingCountdown;

    /**
     * 过载重合闸计次复归时间
     */
    @Column(name = "overload_closing_count_reset_time")
    private Float overloadClosingCountResetTime;

    /**
     * 总复归
     */
    @Column(name = "total_return")
    private Long totalReturn;

    /**
     * 雷击电压设定
     */
    @Column(name = "lightning_strike_voltage_setting")
    private Float lightningStrikeVoltageSetting;

    /**
     * 雷击单次屏蔽时间
     */
    @Column(name = "lightning_strike_single_time_shield_time")
    private Long lightningStrikeSingleTimeShieldTime;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "gmt_create")
    private Date gmtCreate;

    /**
     * 修改时间
     */
    @LastModifiedDate
    @Column(name = "gmt_modified")
    private Date gmtModified;


    @Override
    public String toString() {
        return "DeviceData{" +
                "IMEI=" + imei +
                ",电流01=" + electricCurrent01 +
                ", 电流02=" + electricCurrent02 +
                ", 电压01=" + voltage01 +
                ", 频率01=" + frequency01 +
                ", 电流03=" + electricCurrent03 +
                ", 电流04=" + electricCurrent04 +
                ", 电压02=" + voltage02 +
                ", 频率02=" + frequency02 +
                ", 过电压值=" + overVoltageValue +
                ", 过电压时限=" + overVoltageTimeLimit +
                ", 过电压次数=" + overVoltageCount +
                ", 低电压值=" + lowVoltageValue +
                ", 低电压时限=" + lowVoltageTimeLimit +
                ", 低电压次数=" + lowVoltageCount +
                ", 漏电流值=" + leakageCurrentValue +
                ", 漏电流时限=" + leakageCurrentTimeLimit +
                ", 漏电流次数=" + leakageCurrentCount +
                ", 过载保护=" + overloadProtection +
                ", 过载时限=" + overloadTimeLimit +
                ", 过载次数=" + overloadCount +
                ", 短路次数=" + shortCircuitCount +
                ", 雷击次数=" + lightningStrikeCount +
                ", 重合闸次数=" + closingOperationCount +
                ", 保存=" + saveCount +
                ", 过压合闸电压=" + overVoltageClosingVoltage +
                ", 欠压合闸电压=" + underVoltageClosingVoltage +
                ", 过压状态=" + overVoltageStatus +
                ", 欠压状态=" + underVoltageStatus +
                ", 漏电状态=" + leakageStatus +
                ", 过载状态=" + overloadStatus +
                ", 短路状态=" + shortCircuitStatus +
                ", 重合闸动作次数=" + numberOfreclosingActions +
                ", 重合闸剩余时间=" + closingOperationRemainingTime +
                ", 重合闸故障复归时间=" + closingOperationFaultRecoveryTime +
                ", 输出状态指示=" + outputStatusIndication +
                ", 过流设定重合闸次数=" + overcurrentSettingClosingCount +
                ", 过载重合闸动作次数=" + overloadClosingOperationCount +
                ", 过载动作合闸倒计时=" + overloadActionClosingCountdown +
                ", 过载重合闸计次复归时间=" + overloadClosingCountResetTime +
                ", 总复归=" + totalReturn +
                ", 雷击电压设定=" + lightningStrikeVoltageSetting +
                ", 雷击单次屏蔽时间=" + lightningStrikeSingleTimeShieldTime +
                '}';
    }
}
