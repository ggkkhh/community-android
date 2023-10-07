package com.roydon.community.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.google.gson.Gson;
import com.roydon.community.BaseActivity;
import com.roydon.community.R;
import com.roydon.community.api.Api;
import com.roydon.community.api.ApiConfig;
import com.roydon.community.api.HttpCallback;
import com.roydon.community.constants.BundleConstants;
import com.roydon.community.domain.response.BaseResponse;
import com.roydon.community.utils.string.StringUtil;
import com.roydon.community.widget.RoundImageView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class LoginConfirmActivity extends BaseActivity {
    // handler
    private static final int HANDLER_WHAT_EMPTY = 0;
    private static final int HANDLER_WHAT_CONFIRM = 1;

    private String TOOL_TITLE = "确认登录";

    private RoundImageView ivAvatar;
    private Button btnConfirm;

    private String avatar;
    private String uuid;

    @Override
    protected int initLayout() {
        return R.layout.activity_login_confirm;
    }

    @Override
    protected void initView() {
        initToolBar(TOOL_TITLE);
        ivAvatar = findViewById(R.id.iv_avatar);
        btnConfirm = findViewById(R.id.btn_confirm);
    }

    @Override
    protected void initData() {
        // 获取页面传来 appUser
        Bundle extras = getIntent().getExtras();
        if (StringUtil.isNotNull(extras)) {
            avatar = (String) extras.getSerializable(BundleConstants.AVATAR);
            uuid = (String) extras.getSerializable(BundleConstants.UUID);
            showUserAvatar(avatar);
        }
        btnConfirm.setOnClickListener(v -> {
            Log.e("uuid", uuid);
            confirmLogin(uuid);
        });

    }

    private void showUserAvatar(String avatar) {
        Picasso.with(this).load(avatar)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .into(ivAvatar);
    }

    /**
     * 确认登录
     */
    private void confirmLogin(String uuid) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("uuid", uuid);
        Api.build(ApiConfig.CONFIRM, params).getRequestWithToken(this, new HttpCallback() {
            @Override
            public void onSuccess(final String res) {
                BaseResponse response = new Gson().fromJson(res, BaseResponse.class);
                if (response != null && response.getCode() == 200) {
                    Log.e("confirmLogin", "ok");
                    toast("登录成功");
                    finish();
                } else {

                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }
}