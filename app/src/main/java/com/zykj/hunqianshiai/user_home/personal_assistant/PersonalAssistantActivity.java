package com.zykj.hunqianshiai.user_home.personal_assistant;

import android.os.Bundle;
import android.view.View;

import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BasesActivity;

import butterknife.OnClick;

/**
 * 私人婚恋助理
 */
public class PersonalAssistantActivity extends BasesActivity {

    private Bundle mBundle;

    @Override
    protected int getContentViewX() {
        return R.layout.activity_personal_assistant;
    }

    @Override
    protected void initView() {
        appBar("私人婚恋助理");
        mBundle = new Bundle();
    }

    @OnClick({R.id.tv_open_counselor, R.id.tv_open_steward})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_open_counselor://开通助理

                mBundle.clear();
                mBundle.putString("title", "开通婚恋助理");
                mBundle.putString("type1", "2");
                mBundle.putString("type2", "1");
                mBundle.putString("type3", "5");
                openActivity(OpenCounselorActivity.class, mBundle);
//                mBundle.clear();
//                mBundle.putString("title", "开通婚恋管家");
//                mBundle.putString("type", "2");
//                openActivity(OpenStewardActivity.class, mBundle);
//                openActivity(OpenCounselorActivity.class);
                break;
            case R.id.tv_open_steward://开通管家
                mBundle.clear();
                mBundle.putString("title", "开通婚恋管家");
                mBundle.putString("type1", "3");
                mBundle.putString("type2", "2");
                mBundle.putString("type3", "6");
                openActivity(OpenCounselorActivity.class, mBundle);
//                mBundle.clear();
//                mBundle.putString("title", "开通婚恋管家");
//                mBundle.putString("type", "3");
//                openActivity(OpenStewardActivity.class, mBundle);
                break;
        }
    }
}
