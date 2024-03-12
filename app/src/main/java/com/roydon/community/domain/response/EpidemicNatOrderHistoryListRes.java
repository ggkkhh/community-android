package com.roydon.community.domain.response;

import com.roydon.community.domain.entity.EpidemicNatOrder;

import java.util.List;

/**
 * @author roydon
 * @date 2023/9/29 20:23
 * @description community-android
 */
public class EpidemicNatOrderHistoryListRes extends BaseResponse{
    private int total;
    private List<EpidemicNatOrder> data;

    public EpidemicNatOrderHistoryListRes() {
    }

    public EpidemicNatOrderHistoryListRes(String msg, int code) {
        super(msg, code);
    }

    public EpidemicNatOrderHistoryListRes(int total, List<EpidemicNatOrder> data) {
        this.total = total;
        this.data = data;
    }

    public EpidemicNatOrderHistoryListRes(String msg, int code, int total, List<EpidemicNatOrder> data) {
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

    public List<EpidemicNatOrder> getData() {
        return data;
    }

    public void setData(List<EpidemicNatOrder> data) {
        this.data = data;
    }
}
