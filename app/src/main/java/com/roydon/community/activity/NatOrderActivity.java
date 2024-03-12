package com.roydon.community.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.roydon.community.BaseActivity;
import com.roydon.community.R;
import com.roydon.community.action.StatusAction;
import com.roydon.community.adapter.NatOrderRecordAdapter;
import com.roydon.community.api.Api;
import com.roydon.community.api.ApiConfig;
import com.roydon.community.api.HttpCallback;
import com.roydon.community.constants.BundleConstants;
import com.roydon.community.domain.entity.EpidemicNatOrder;
import com.roydon.community.domain.response.BaseResponse;
import com.roydon.community.domain.response.EpidemicNatOrderHistoryListRes;
import com.roydon.community.domain.vo.AppUser;
import com.roydon.community.listener.OnItemClickListener;
import com.roydon.community.utils.string.StringUtil;
import com.roydon.community.widget.HintLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NatOrderActivity extends BaseActivity implements StatusAction {
    // handler
    private static final int HANDLER_WHAT_EMPTY = 0;
    private static final int HANDLER_WHAT_ORDER_LIST = 1;

    private String TOOL_TITLE = "核酸预约";

    private EditText etRealName, etTelephone, etIdCard;
    private Button btnNatOrder;

    private AppUser appUser;

    private HintLayout mHintLayout;
    private RefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private int pageNum = 1;
    private NatOrderRecordAdapter recordAdapter;
    private List<EpidemicNatOrder> recordList = new ArrayList<>();

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_WHAT_EMPTY:
                    showEmpty();
                    break;
                case HANDLER_WHAT_ORDER_LIST:
                    recordAdapter.setData(recordList);
                    recordAdapter.notifyDataSetChanged();
                    showComplete();
                    break;
            }
        }
    };

    @Override
    protected int initLayout() {
        return R.layout.activity_nat_order;
    }

    @Override
    public HintLayout getHintLayout() {
        return mHintLayout;
    }

    @Override
    protected void initView() {
        etRealName = findViewById(R.id.et_real_name);
        etTelephone = findViewById(R.id.et_telephone);
        etIdCard = findViewById(R.id.et_id_card);

        mHintLayout = findViewById(R.id.hintLayout);
        refreshLayout = findViewById(R.id.refreshLayout);
        recyclerView = findViewById(R.id.recyclerView);

        btnNatOrder = findViewById(R.id.btn_nat_order);

        showLoading();
    }

    @Override
    protected void initData() {
        initToolBar(TOOL_TITLE);
        // 获取页面传来 appUser
        Bundle extras = getIntent().getExtras();
        if (StringUtil.isNotNull(extras)) {
            appUser = (AppUser) extras.getSerializable(BundleConstants.APPUSER);
            showAppUserInForm(appUser);
        }
        // 设置适配器，展示预约记录
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recordAdapter = new NatOrderRecordAdapter(this);
        recyclerView.setAdapter(recordAdapter);
        recordAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        });
        getNatOrderRecordList(true);
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            pageNum = 1;
            getNatOrderRecordList(true);
        });
        refreshLayout.setOnLoadMoreListener((refreshlayout) -> {
            pageNum++;
            getNatOrderRecordList(false);
        });
        btnNatOrder.setOnClickListener(v -> {
            natOrderQuick();
        });

    }

    private void showAppUserInForm(AppUser appUser) {
        etRealName.setText(appUser.getRealName());
        etTelephone.setText(appUser.getPhonenumber());
        etIdCard.setText(appUser.getIdCard());
    }

    /**
     * 预约记录
     *
     * @param isRefresh
     */
    private void getNatOrderRecordList(final boolean isRefresh) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("pageNum", pageNum);
        params.put("pageSize", ApiConfig.PAGE_SIZE);
        Api.build(ApiConfig.NAT_ORDER_MINE_HISTORY, params).postRequestWithToken(this, new HttpCallback() {
            @Override
            public void onSuccess(final String res) {
                if (isRefresh) {
                    refreshLayout.finishRefresh(true);
                } else {
                    refreshLayout.finishLoadMore(true);
                }
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                EpidemicNatOrderHistoryListRes response = gson.fromJson(res, EpidemicNatOrderHistoryListRes.class);
                if (response != null && response.getCode() == 200) {
                    List<EpidemicNatOrder> list = response.getData();
                    if (list != null && list.size() > 0) {
                        if (isRefresh) {
                            recordList = list;
                        } else {
                            recordList.addAll(list);
                        }
                        mHandler.sendEmptyMessage(HANDLER_WHAT_ORDER_LIST);
                    } else {
                        if (isRefresh) {
                            Log.e("getTemperatureReportRecordList", "暂时无数据");
                            mHandler.sendEmptyMessage(HANDLER_WHAT_EMPTY);
                        } else {
                            Log.e("getTemperatureReportRecordList", "没有更多数据");
                        }
                    }
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    /**
     * 一键预约
     */
    private void natOrderQuick() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("realName", etRealName.getText().toString().trim());
        params.put("telephone", etTelephone.getText().toString().trim());
        params.put("idCard", etIdCard.getText().toString().trim());
        Api.build(ApiConfig.NAT_ORDER_QUICK, params).postRequestWithToken(this, new HttpCallback() {
            @Override
            public void onSuccess(final String res) {
                Log.e("natOrderQuick", res);
                BaseResponse response = new Gson().fromJson(res, BaseResponse.class);
                if (response != null && response.getCode() == 200) {
                    finish();
                    showSyncShortToast("预约成功");
                } else {
                    showSyncShortToast("预约失败" + response.getMsg());
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }
}