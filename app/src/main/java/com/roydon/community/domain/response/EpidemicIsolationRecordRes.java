package com.roydon.community.domain.response;

import com.roydon.community.domain.entity.EpidemicIsolationRecord;

/**
 * @author roydon
 * @date 2023/10/3 17:42
 * @description community-android
 */
public class EpidemicIsolationRecordRes extends BaseResponse{

    private EpidemicIsolationRecord data;

    public EpidemicIsolationRecordRes() {
    }

    public EpidemicIsolationRecordRes(EpidemicIsolationRecord data) {
        this.data = data;
    }

    public EpidemicIsolationRecordRes(String msg, int code, EpidemicIsolationRecord data) {
        super(msg, code);
        this.data = data;
    }

    public EpidemicIsolationRecord getData() {
        return data;
    }

    public void setData(EpidemicIsolationRecord data) {
        this.data = data;
    }
}
