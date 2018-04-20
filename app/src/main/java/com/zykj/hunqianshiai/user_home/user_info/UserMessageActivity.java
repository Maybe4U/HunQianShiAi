package com.zykj.hunqianshiai.user_home.user_info;

import android.view.View;

import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BasesActivity;

import butterknife.OnClick;

public class UserMessageActivity extends BasesActivity {

    @Override
    protected int getContentViewX() {
        return R.layout.activity_user_message;
    }

    @Override
    protected void initView() {

    }

    @OnClick({R.id.iv_back})
    @Override
    public void onClick(View view) {
        super.onClick(view);
    }
}
