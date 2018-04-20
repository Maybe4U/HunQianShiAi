package com.zykj.hunqianshiai.user_home.user_info;

import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BasesActivity;

public class UserInfoActivity extends BasesActivity {

    @Override
    protected int getContentViewX() {
        return R.layout.activity_user_info;
    }

    @Override
    protected void initView() {
        appBar("基本资料");
    }
}
