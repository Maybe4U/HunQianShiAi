package com.zykj.hunqianshiai.home.my;

import android.os.Bundle;
import android.view.View;

import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.user_home.personal_assistant.OpenCounselorActivity;

import butterknife.OnClick;

public class GuanjiaActivity extends BasesActivity {

    private Bundle mBundle;

    @Override
    protected int getContentViewX() {
        return R.layout.activity_guanjia;
    }

    @Override
    protected void initView() {
        appBar("开通婚恋管家");
        mBundle = new Bundle();
    }

    @OnClick({R.id.tv_open_steward})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {

            case R.id.tv_open_steward://开通管家
                mBundle.clear();
                mBundle.putString("title", "开通婚恋管家");
                mBundle.putString("type1", "3");
                mBundle.putString("type2", "2");
                mBundle.putString("type3", "6");
                openActivity(OpenCounselorActivity.class, mBundle);
                break;
        }
    }
}
