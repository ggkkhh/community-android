package com.roydon.community;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.roydon.community.activity.HomeActivity;
import com.roydon.community.activity.LoginActivity;
import com.roydon.community.activity.RegisterActivity;
import com.roydon.community.constants.Constants;
import com.roydon.community.utils.string.StringUtil;

/**
 * @author roydon
 */
public class MainActivity extends BaseActivity {

    private Button buttonLogin, buttonRegister;

    @Override
    protected int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        buttonLogin = findViewById(R.id.button_login);
        buttonRegister = findViewById(R.id.button_register);
    }

    @Override
    protected void initData() {
        // 判断token缓存是否为空
        if (StringUtil.isEmpty(findByKey(Constants.TOKEN))) {
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
        } else {
            // 非空跳转主页面
            navigateToWithFlag(HomeActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            finish();
        }
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateTo(LoginActivity.class);
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateTo(RegisterActivity.class);
            }
        });
    }
}