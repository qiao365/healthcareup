package com.semioe.healthcareup.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.semioe.healthcareup.R;
import com.semioe.healthcareup.utils.LoginConfig;

public class SplashActivity extends Activity {

    private ImageView iv_loading;
    private LoginConfig mLoginConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mLoginConfig = new LoginConfig(this);
        initView();
        anim();
    }

    public void initView() {
        iv_loading = (ImageView) findViewById(R.id.iv_loading);

    }

    // 设置动画
    private void anim() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.main_loadingpage);

        anim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // checkVersion();
                if (!mLoginConfig.getAvailbleTime().equals("0")) {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else
                    goLogin();
            }

        });
        iv_loading.setAnimation(anim);
        anim.start();
    }

    // 进入登录页
    private void goLogin() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
