package com.roydon.community.domain.response;

import com.roydon.community.domain.entity.EpidemicTemperatureReport;

import java.util.List;

/**
 * @author roydon
 * @date 2023/9/25 18:00
 * @description community-android
 */
public class EpidemicTemperatureReportHistoryRes extends BaseResponse{

    private int total;
    private List<EpidemicTemperatureReport> data;

    public EpidemicTemperatureReportHistoryRes() {
    }

    public EpidemicTemperatureReportHistoryRes(int total, List<EpidemicTemperatureReport> data) {
        this.total = total;
        this.data = data;
    }

    public EpidemicTemperatureReportHistoryRes(String msg, int code, int total, List<EpidemicTemperatureReport> data) {
        super(msg, code);
        this.total = total;
        this.data = data;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<EpidemicTemperatureReport> getData() {
        return data;
    }

    public void setData(List<EpidemicTemperatureReport> data) {
        this.data = data;
    }
}

