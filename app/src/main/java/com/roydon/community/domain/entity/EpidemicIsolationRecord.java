package com.roydon.community.domain.entity;

import java.util.Date;
import java.util.Map;

/**
 * 隔离记录对象 epidemic_isolation_record
 *
 * @author roydon
 * @date 2023-08-09
 */
public class EpidemicIsolationRecord extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long recordId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户
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
     * 隔离天数（单位天）
     */
    private Integer isolationTime;

    /**
     * 剩余隔离天数
     */
    private Integer remainingIsolationTime;

    /**
     * 隔离结束时间
     */
    private Date isolationFinishTime;

    /**
     * 0正常2已删除
     */
    private String delFlag;

    public EpidemicIsolationRecord() {
    }

    public EpidemicIsolationRecord(Long recordId, Long userId, String username, String realName, String telephone, Integer isolationTime, Integer remainingIsolationTime, Date isolationFinishTime, String delFlag) {
        this.recordId = recordId;
        this.userId = userId;
        this.username = username;
        this.realName = realName;
        this.telephone = telephone;
        this.isolationTime = isolationTime;
        this.remainingIsolationTime = remainingIsolationTime;
        this.isolationFinishTime = isolationFinishTime;
        this.delFlag = delFlag;
    }

    public EpidemicIsolationRecord(String searchValue, String createBy, Date createTime, String updateBy, Date updateTime, String remark, Map<String, Object> params, Long recordId, Long userId, String username, String realName, String telephone, Integer isolationTime, Integer remainingIsolationTime, Date isolationFinishTime, String delFlag) {
        super(searchValue, createBy, createTime, updateBy, updateTime, remark, params);
        this.recordId = recordId;
        this.userId = userId;
        this.username = username;
        this.realName = realName;
        this.telephone = telephone;
        this.isolationTime = isolationTime;
        this.remainingIsolationTime = remainingIsolationTime;
        this.isolationFinishTime = isolationFinishTime;
        this.delFlag = delFlag;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public Integer getIsolationTime() {
        return isolationTime;
    }

    public void setIsolationTime(Integer isolationTime) {
        this.isolationTime = isolationTime;
    }

    public Integer getRemainingIsolationTime() {
        return remainingIsolationTime;
    }

    public void setRemainingIsolationTime(Integer remainingIsolationTime) {
        this.remainingIsolationTime = remainingIsolationTime;
    }

    public Date getIsolationFinishTime() {
        return isolationFinishTime;
    }

    public void setIsolationFinishTime(Date isolationFinishTime) {
        this.isolationFinishTime = isolationFinishTime;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }
}
