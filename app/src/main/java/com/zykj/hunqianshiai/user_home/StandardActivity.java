package com.zykj.hunqianshiai.user_home;

import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BasesActivity;

/**
 * 择偶标准
 */
public class StandardActivity extends BasesActivity {

    @Override
    protected int getContentViewX() {
        return R.layout.activity_standard;
    }

    @Override
    protected void initView() {
        appBar("择偶标准");
    }
}
