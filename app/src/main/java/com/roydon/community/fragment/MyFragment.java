package com.roydon.community.fragment;

import static com.roydon.community.constants.BundleConstants.APPUSER;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.roydon.community.R;
import com.roydon.community.activity.AccessRecordActivity;
import com.roydon.community.activity.BDAddressSelectActivity;
import com.roydon.community.activity.FeedbackActivity;
import com.roydon.community.activity.HealthCodeActivity;
import com.roydon.community.activity.HotlineActivity;
import com.roydon.community.activity.InoculationHistoryActivity;
import com.roydon.community.activity.MessageActivity;
import com.roydon.community.activity.NatOrderActivity;
import com.roydon.community.activity.SettingActivity;
import com.roydon.community.activity.TemperatureReportActivity;
import com.roydon.community.activity.UserAddressActivity;
import com.roydon.community.activity.UserInfoActivity;
import com.roydon.community.activity.UserOrderActivity;
import com.roydon.community.activity.WebviewActivity;
import com.roydon.community.api.Api;
import com.roydon.community.api.ApiConfig;
import com.roydon.community.api.HttpCallback;
import com.roydon.community.domain.entity.EpidemicIsolationRecord;
import com.roydon.community.domain.response.EpidemicIsolationRecordRes;
import com.roydon.community.domain.response.UserInfoRes;
import com.roydon.community.domain.vo.AppUser;
import com.roydon.community.enums.ThemeEnum;
import com.roydon.community.listener.OnShareDialogClickListener;
import com.roydon.community.ui.dialog.AddressDialog;
import com.roydon.community.utils.android.SystemUtils;
import com.roydon.community.utils.cache.SPUtils;
import com.roydon.community.utils.string.StringUtil;
import com.roydon.community.utils.string.TelephoneUtils;
import com.roydon.community.view.CircleTransform;
import com.roydon.community.view.DialogX;
import com.roydon.library.BaseDialog;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class MyFragment extends BaseFragment {
    // handler
    private static final int HANDLER_WHAT_EMPTY = 0;
    private static final int HANDLER_GET_USERINFO = 1;
    private static final int HANDLER_MY_ISOLATION_RECORD = 2; //myIsolationRecord

    //是否第一次加载
    private boolean isFirstLoading = true;

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private String[] mTitles;
    private ViewPager viewPager;
    private SlidingTabLayout slidingTabLayout;
    private LinearLayout mLinearLayout;
    private ImageView userBg, userAvatar;
    private TextView userNickName, userDept;
    // top-bar
    private ImageView ivSetting, ivShare, ivTheme;
    // 订单栏功能
    private LinearLayout llUserOrder, llAccessReport, llEpidemicHealthCode, llNatOrder, llEpidemicHotline, llEpidemicTemperature;
    private RelativeLayout rlUserAddress, rlInoculationHistoryReport, rlAddressSelecter, rlWebview, rlFeedback;

    private LinearLayout llIsolationState;

    // 测试功能区
    private RelativeLayout rlBDAddress, rlMessage;

    private AppUser appUser;
    private EpidemicIsolationRecord isolationRecord;

    private TextView tvIsolationRealName;
    private TextView tvIsolationTelephone;
    private TextView tvIsolationTime;
    private TextView tvIsolationPeriod;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_WHAT_EMPTY:
                    llIsolationState.setVisibility(View.GONE);
                    break;
                case HANDLER_GET_USERINFO:
                    showUserInfo(appUser);
                    break;
                case HANDLER_MY_ISOLATION_RECORD:
                    showMyIsolationRecordLayout(isolationRecord);
                    break;
                default:
                    break;
            }
        }
    };


    public MyFragment() {
    }

    public static MyFragment newInstance() {
        return new MyFragment();
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_my;
    }

    @Override
    protected void initView() {
        // 顶部功能icon
        ivShare = mRootView.findViewById(R.id.iv_share);
        ivSetting = mRootView.findViewById(R.id.iv_setting);
        ivTheme = mRootView.findViewById(R.id.iv_theme);

        mLinearLayout = mRootView.findViewById(R.id.layout_my_detail);
        rlUserAddress = mRootView.findViewById(R.id.rl_user_address);
        rlInoculationHistoryReport = mRootView.findViewById(R.id.rl_inoculation_history_report);
        rlAddressSelecter = mRootView.findViewById(R.id.rl_address_selecter);

        // 用户info栏
//        userBg = mRootView.findViewById(R.id.iv_user_bg);
        userAvatar = mRootView.findViewById(R.id.img_header);
        userNickName = mRootView.findViewById(R.id.user_nick_name);
        userDept = mRootView.findViewById(R.id.user_dept);
        // 订单栏功能
        llUserOrder = mRootView.findViewById(R.id.ll_user_order);

        // 疫情防控
        llAccessReport = mRootView.findViewById(R.id.ll_access_report);
        llEpidemicHealthCode = mRootView.findViewById(R.id.ll_epidemic_health_code);
        llNatOrder = mRootView.findViewById(R.id.ll_nat_order);
        llEpidemicHotline = mRootView.findViewById(R.id.ll_epidemic_hotline);
        llEpidemicTemperature = mRootView.findViewById(R.id.ll_epidemic_temperature);

        // 隔离状态
        llIsolationState = mRootView.findViewById(R.id.ll_isolation_state);
        llIsolationState.setVisibility(View.GONE);
        tvIsolationRealName = mRootView.findViewById(R.id.tv_isolation_real_name);
        tvIsolationTelephone = mRootView.findViewById(R.id.tv_isolation_telephone);
        tvIsolationTime = mRootView.findViewById(R.id.tv_isolation_time);
        tvIsolationPeriod = mRootView.findViewById(R.id.tv_isolation_period);
        //功能反馈
        rlFeedback = mRootView.findViewById(R.id.rl_feedback);

        // 功能测试区
        rlBDAddress = mRootView.findViewById(R.id.rl_bd_address);
        rlMessage = mRootView.findViewById(R.id.rl_message);
        rlWebview = mRootView.findViewById(R.id.rl_webview);

        // 设置
        ivSetting.setOnClickListener(v -> {
            navigateTo(SettingActivity.class);
        });
        // 分享
        ivShare.setOnClickListener(v -> {
            DialogX.showShareDialog(getContext(), new OnShareDialogClickListener() {
                @Override
                public void onShareToWechat() {
                    SystemUtils.copyToClipboard(getContext(), ApiConfig.BASE_URl);
                    showShortToast("Copied");
                }

                @Override
                public void onShareToPengyouquan() {
                    showShortToast("onShareToPengyouquan");
                }

                @Override
                public void onShareToQQ() {
                    showShortToast("onShareToQQ");
                }

                @Override
                public void onShareToWeibo() {
                    showShortToast("onShareToWeibo");
                }

                @Override
                public void onCancelShare() {

                }
            });
        });
        // 昼夜切换
        ivTheme.setOnClickListener(v -> {
            int current = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
            if (current == Configuration.UI_MODE_NIGHT_YES) {
                ((AppCompatActivity) getActivity()).getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                SPUtils.setTheme("theme", ThemeEnum.SUN.getCode(), getContext());
            } else {
                ((AppCompatActivity) getActivity()).getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                SPUtils.setTheme("theme", ThemeEnum.MOON.getCode(), getContext());
            }
        });
        mLinearLayout.setOnClickListener(v -> {
            navigateTo(UserInfoActivity.class);
        });
        rlFeedback.setOnClickListener(v -> {
            navigateTo(FeedbackActivity.class);
        });
        rlUserAddress.setOnClickListener(v -> {
            navigateTo(UserAddressActivity.class);
        });

        rlAddressSelecter.setOnClickListener(v -> {
            new AddressDialog.Builder(getContext()).setTitle(getString(R.string.address_selecter_title))
                    // 设置默认省份
                    .setProvince("河南省")
                    // 设置默认城市（必须要先设置默认省份）
                    .setCity("郑州市")
                    // 不选择县级区域
                    //.setIgnoreArea()
                    .setListener(new AddressDialog.OnListener() {

                        @Override
                        public void onSelected(BaseDialog dialog, String province, String city, String area) {
                            showShortToast(province + city + area);
                        }

                        @Override
                        public void onCancel(BaseDialog dialog) {
                            showShortToast("取消了");
                        }
                    }).show();

        });
        rlBDAddress.setOnClickListener(v -> {
            navigateTo(BDAddressSelectActivity.class);
        });
        // 用户订单
        llUserOrder.setOnClickListener(v -> {
            navigateTo(UserOrderActivity.class);
        });
        // 出入社区报备
        llAccessReport.setOnClickListener(v -> {
//            navigateTo(AccessRecordActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(APPUSER, appUser);
            navigateToWithBundle(AccessRecordActivity.class, bundle);
        });
        // 健康码
        llEpidemicHealthCode.setOnClickListener(v -> {
            navigateTo(HealthCodeActivity.class);
        });
        // 疫苗接种记录
        rlInoculationHistoryReport.setOnClickListener(v -> {
            navigateTo(InoculationHistoryActivity.class);
        });
        // 核酸预约
        llNatOrder.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable(APPUSER, appUser);
            navigateToWithBundle(NatOrderActivity.class, bundle);
        });
        // 紧急热线
        llEpidemicHotline.setOnClickListener(v -> {
            navigateTo(HotlineActivity.class);
        });
        //体温上报
        llEpidemicTemperature.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable(APPUSER, appUser);
            navigateToWithBundle(TemperatureReportActivity.class, bundle);
        });

        rlMessage.setOnClickListener(v -> {
            navigateTo(MessageActivity.class);
        });
        rlWebview.setOnClickListener(v -> {
            WebviewActivity.start(getActivity(), "http://106.14.105.101:88");
        });
    }

    @Override
    protected void initData() {
        getUserInfo();
        myIsolationRecord();
    }



    /**
     * 在fragment可见的时候，刷新数据
     */
    @Override
    public void onResume() {
        super.onResume();
        if (!isFirstLoading) {
            //如果不是第一次加载，刷新数据
            myIsolationRecord();
        }
        isFirstLoading = false;
    }

    /**
     * 用户信息
     */
    private void getUserInfo() {
        HashMap<String, Object> params = new HashMap<>();
        Api.build(ApiConfig.USER_INFO, params).getRequestWithToken(getActivity(), new HttpCallback() {
            @Override
            public void onSuccess(final String res) {
                Log.e("getUserInfo", res);
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                UserInfoRes response = gson.fromJson(res, UserInfoRes.class);
                if (response != null && response.getCode() == 200) {
                    AppUser user = response.getData();
                    if (StringUtil.isNotNull(user)) {
                        Log.e("getUserInfo", user.toString());
                        appUser = user;
                        mHandler.sendEmptyMessage(HANDLER_GET_USERINFO);
                    } else {
                        showShortToastSync("请重新登陆");
                    }
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    private void showUserInfo(AppUser appUser) {
        if (appUser.getAvatar() != null && !appUser.getAvatar().equals("")) {
            Picasso.with(getContext()).load(appUser.getAvatar()).transform(new CircleTransform()).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE)//限制Picasso从内存中加载图片  不然头像更换 不及时
                    .into(userAvatar);
        }
        userNickName.setText(appUser.getNickName());
        if (appUser.getDeptId() != 1) {
            // 已设置归属
            userNickName.setTextColor(Color.YELLOW);
        }
        userDept.setText(appUser.getDept().getDeptName());
    }

    /**
     * 隔离记录
     */
    private void myIsolationRecord() {
        HashMap<String, Object> params = new HashMap<>();
        Api.build(ApiConfig.EPIDEMIC_ISOLATION_UNFINISHED_RECORD, params).getRequestWithToken(getActivity(), new HttpCallback() {
            @Override
            public void onSuccess(final String res) {
                Log.e("myIsolationRecord", res);
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                EpidemicIsolationRecordRes response = gson.fromJson(res, EpidemicIsolationRecordRes.class);
                if (response != null && response.getCode() == 200) {
                    EpidemicIsolationRecord record = response.getData();
                    if (StringUtil.isNotNull(record)) {
                        Log.e("myIsolationRecord", record.toString());
                        isolationRecord = record;
                        mHandler.sendEmptyMessage(HANDLER_MY_ISOLATION_RECORD);
                    } else {
                        mHandler.sendEmptyMessage(HANDLER_WHAT_EMPTY);
                    }
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    @SuppressLint({"SetTextI18n", "SimpleDateFormat"})
    private void showMyIsolationRecordLayout(EpidemicIsolationRecord isolationRecord) {
        llIsolationState.setVisibility(View.VISIBLE);
        tvIsolationRealName.setText(isolationRecord.getRealName());
        tvIsolationTelephone.setText(TelephoneUtils.replaceSomeCharByAsterisk(isolationRecord.getTelephone()));
        tvIsolationTime.setText(isolationRecord.getIsolationTime() + "/" + isolationRecord.getRemainingIsolationTime());
        String beginTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(isolationRecord.getCreateTime());
        String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(isolationRecord.getIsolationFinishTime());
        tvIsolationPeriod.setText(beginTime + " — " + endTime);

    }

}