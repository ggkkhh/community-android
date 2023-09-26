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
import com.roydon.community.adapter.TemperatureReportRecordAdapter;
import com.roydon.community.api.Api;
import com.roydon.community.api.ApiConfig;
import com.roydon.community.api.HttpCallback;
import com.roydon.community.constants.BundleConstants;
import com.roydon.community.domain.entity.EpidemicTemperatureReport;
import com.roydon.community.domain.response.BaseResponse;
import com.roydon.community.domain.response.EpidemicTemperatureReportHistoryRes;
import com.roydon.community.domain.vo.AppUser;
import com.roydon.community.listener.OnItemClickListener;
import com.roydon.community.ui.dialog.TemperatureDialog;
import com.roydon.community.utils.string.StringUtil;
import com.roydon.community.widget.HintLayout;
import com.roydon.library.BaseDialog;
import com.roydon.library.layout.SettingBar;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TemperatureReportActivity extends BaseActivity implements StatusAction {
    // handler
    private static final int HANDLER_WHAT_EMPTY = 0;
    private static final int HANDLER_WHAT_REPORT_LIST = 1;

    private String TOOL_TITLE = "体温上报";

    private EditText etRealName, etTelephone, etIdCard;
    private SettingBar sbTemperature;
    private Button btnConfirmReport;

    private AppUser appUser;

    private HintLayout mHintLayout;
    private RefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private int pageNum = 1;
    private TemperatureReportRecordAdapter reportAdapter;
    private List<EpidemicTemperatureReport> reportList = new ArrayList<>();

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
                case HANDLER_WHAT_REPORT_LIST:
                    reportAdapter.setData(reportList);
                    reportAdapter.notifyDataSetChanged();
                    showComplete();
                    break;
            }
        }
    };

    @Override
    protected int initLayout() {
        return R.layout.activity_temperature_report;
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
        sbTemperature = findViewById(R.id.sb_temperature);

        mHintLayout = findViewById(R.id.hintLayout);
        refreshLayout = findViewById(R.id.refreshLayout);
        recyclerView = findViewById(R.id.recyclerView);

        btnConfirmReport = findViewById(R.id.btn_confirm_report);

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
        // 设置适配器，展示上报记录
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        reportAdapter = new TemperatureReportRecordAdapter(this);
        recyclerView.setAdapter(reportAdapter);
        reportAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        });
        getTemperatureReportRecordList(true);
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            pageNum = 1;
            getTemperatureReportRecordList(true);
        });
        refreshLayout.setOnLoadMoreListener((refreshlayout) -> {
            pageNum++;
            getTemperatureReportRecordList(false);
        });
        sbTemperature.setOnClickListener(v -> {
            // 时间选择对话框
            new TemperatureDialog.Builder(this).setTitle(getString(R.string.temperature_title)).setConfirm(getString(R.string.common_confirm)).setCancel(getString(R.string.common_cancel)).setListener(new TemperatureDialog.OnListener() {
                @Override
                public void onSelected(BaseDialog dialog, String integer, String decimal) {
                    toast(integer + getString(R.string.common_point) + decimal + getString(R.string.common_c));
                    sbTemperature.setRightText(integer + getString(R.string.common_point) + decimal);
                }

                @Override
                public void onCancel(BaseDialog dialog) {
//                            toast("取消了");
                }
            }).show();
        });
        btnConfirmReport.setOnClickListener(v -> {
            temperatureReport(etRealName.getText().toString().trim(), etTelephone.getText().toString().trim(), etIdCard.getText().toString().trim(), sbTemperature.getRightText().toString().trim());
        });
    }

    /**
     * 填充表单
     *
     * @param appUser
     */
    private void showAppUserInForm(AppUser appUser) {
        etRealName.setText(appUser.getRealName());
        etTelephone.setText(appUser.getPhonenumber());
        etIdCard.setText(appUser.getIdCard());
    }

    /**
     * 体温上报
     */
    private void temperatureReport(String realName, String telephone, String idCard, String temperature) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("realName", realName);
        params.put("telephone", telephone);
        params.put("idCard", idCard);
        params.put("temperature", temperature);
        Api.build(ApiConfig.TEMPERATURE_REPORT, params).postRequestWithToken(this, new HttpCallback() {
            @Override
            public void onSuccess(final String res) {
                Log.e("temperatureReport", res);
                BaseResponse response = new Gson().fromJson(res, BaseResponse.class);
                if (response != null && response.getCode() == 200) {
                    finish();
                    showSyncShortToast("上报成功");
                } else {
                    showSyncShortToast("上报失败" + response.getMsg());
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    /**
     * 上报记录集合
     *
     * @param isRefresh
     */
    private void getTemperatureReportRecordList(final boolean isRefresh) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("pageNum", pageNum);
        params.put("pageSize", ApiConfig.PAGE_SIZE);
        Api.build(ApiConfig.TEMPERATURE_REPORT_HISTORY_MY, params).postRequestWithToken(this, new HttpCallback() {
            @Override
            public void onSuccess(final String res) {
                if (isRefresh) {
                    refreshLayout.finishRefresh(true);
                } else {
                    refreshLayout.finishLoadMore(true);
                }
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                EpidemicTemperatureReportHistoryRes response = gson.fromJson(res, EpidemicTemperatureReportHistoryRes.class);
                if (response != null && response.getCode() == 200) {
                    List<EpidemicTemperatureReport> list = response.getData();
                    if (list != null && list.size() > 0) {
                        if (isRefresh) {
                            reportList = list;
                        } else {
                            reportList.addAll(list);
                        }
                        mHandler.sendEmptyMessage(HANDLER_WHAT_REPORT_LIST);
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

}