package com.zykj.hunqianshiai.home.my;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.user_home.personal_assistant.OpenCounselorActivity;

import butterknife.Bind;
import butterknife.OnClick;

public class ZhuLiActivity extends BasesActivity {


    private Bundle mBundle;

    @Override
    protected int getContentViewX() {
        return R.layout.activity_zhu_li;
    }

    @Override
    protected void initView() {
        appBar("开通私人助理");
        mBundle = new Bundle();
    }

    @OnClick({R.id.tv_open_counselor})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_open_counselor:
                mBundle.clear();
                mBundle.putString("title", "开通婚恋助理");
                mBundle.putString("type1", "2");
                mBundle.putString("type2", "1");
                mBundle.putString("type3", "5");
                openActivity(OpenCounselorActivity.class, mBundle);
                break;
        }
    }
}
