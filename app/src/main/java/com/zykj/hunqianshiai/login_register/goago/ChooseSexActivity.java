package com.zykj.hunqianshiai.login_register.goago;

import android.os.Bundle;
import android.view.View;

import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.home.HomeMainActivity;
import com.zykj.hunqianshiai.net.UrlContent;

import butterknife.OnClick;

public class ChooseSexActivity extends BasesActivity {

    private Bundle mBundle;

    @Override
    protected int getContentViewX() {
        return R.layout.activity_choose_sex;
    }

    @Override
    protected void initView() {
        appBar("请选择性别");
        mBundle = new Bundle();
    }

    @OnClick({R.id.iv_women, R.id.iv_man})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        mBundle.clear();
        switch (v.getId()) {
            case R.id.iv_man:
                mBundle.putString("sex", "男");
                UrlContent.GOAGO = true;
                UrlContent.SEX = "男";
                openActivity(HomeGoagoActivity.class);
//                openActivity(GoagoActivity.class, mBundle);
                break;
            case R.id.iv_women:

                mBundle.putString("sex", "女");
                UrlContent.GOAGO = true;
                UrlContent.SEX = "女";
                openActivity(HomeGoagoActivity.class);
//                openActivity(GoagoActivity.class, mBundle);
                break;
        }
        finish();
    }
}
