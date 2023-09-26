package com.roydon.community.domain.entity;

import java.util.Date;
import java.util.Map;

/**
 * 体温上报对象 epidemic_temperature_report
 *
 * @author roydon
 * @date 2023-09-25
 */
public class EpidemicTemperatureReport extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 联系电话
     */
    private String telephone;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 体温
     */
    private String temperature;

    public EpidemicTemperatureReport() {
    }

    public EpidemicTemperatureReport(String searchValue, String createBy, Date createTime, String updateBy, Date updateTime, String remark, Map<String, Object> params) {
        super(searchValue, createBy, createTime, updateBy, updateTime, remark, params);
    }

    public EpidemicTemperatureReport(Long id, String username, String realName, String telephone, String idCard, String temperature) {
        this.id = id;
        this.username = username;
        this.realName = realName;
        this.telephone = telephone;
        this.idCard = idCard;
        this.temperature = temperature;
    }

    public EpidemicTemperatureReport(String searchValue, String createBy, Date createTime, String updateBy, Date updateTime, String remark, Map<String, Object> params, Long id, String username, String realName, String telephone, String idCard, String temperature) {
        super(searchValue, createBy, createTime, updateBy, updateTime, remark, params);
        this.id = id;
        this.username = username;
        this.realName = realName;
        this.telephone = telephone;
        this.idCard = idCard;
        this.temperature = temperature;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }
}
