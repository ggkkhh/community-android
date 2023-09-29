package com.roydon.community.activity;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.roydon.community.BaseActivity;
import com.roydon.community.R;
import com.roydon.community.api.Api;
import com.roydon.community.api.ApiConfig;
import com.roydon.community.api.HttpCallback;
import com.roydon.community.constants.Constants;
import com.roydon.community.domain.vo.LoginResponse;
import com.roydon.community.ui.dialog.WaitDialog;
import com.roydon.library.BaseDialog;

import java.util.HashMap;

/**
 * @author roydon
 */
public class LoginActivity extends BaseActivity {

    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvToSmsLogin, tvToRegister;

    @Override
    protected int initLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        tvToSmsLogin = findViewById(R.id.tv_to_sms_login);
        tvToRegister = findViewById(R.id.tv_to_register);
    }

    @Override
    protected void initData() {
        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            // 等待对话框
            final BaseDialog waitDialog = new WaitDialog.Builder(this)
                    // 消息文本可以不用填写
                    .setMessage(getString(R.string.login_loading))
                    .showDialog();
            login(username, password);
        });
        tvToSmsLogin.setOnClickListener(v -> {
            navigateToWithFlag(SmsLoginActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        });
        tvToRegister.setOnClickListener(v -> {
            navigateToWithFlag(RegisterActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        });
    }

    /**
     * 账号密码登录
     *
     * @param username
     * @param password
     */
    private void login(String username, String password) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        Api.build(ApiConfig.LOGIN, params).postRequest(this, new HttpCallback() {
            @Override
            public void onSuccess(final String res) {
                Gson gson = new Gson();
                LoginResponse loginResponse = gson.fromJson(res, LoginResponse.class);
                if (loginResponse.getCode() == 200) {
                    String token = loginResponse.getToken();
                    insertVal(Constants.TOKEN, token);
                    navigateToWithFlag(HomeActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    runOnUiThread(() -> {
                        showLongToast("登录成功");
                    });
                } else {
                    runOnUiThread(() -> {
                        showShortToast("登录失败");
                    });
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}