package com.zykj.hunqianshiai.home.my;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BasePopupWindow;
import com.zykj.hunqianshiai.user_home.personal_assistant.OpenCounselorActivity;
import com.zykj.hunqianshiai.user_home.personal_assistant.OpenStewardActivity;

/**
 * 上线提醒弹窗
 * Created by xu on 2018/1/10.
 */

public class PopupWindowRemind extends BasePopupWindow {
    protected PopupWindowRemind(Activity activity) {
        super(activity);
        TextView tv_open = view.findViewById(R.id.tv_open);
        tv_open.setOnClickListener(this);
        dismissPop();
    }

    @Override
    public int getViewId() {
        return R.layout.popupwindow_remind;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_open:

                Intent intent = new Intent(mActivity, OpenCounselorActivity.class);
                intent.putExtra("title", "上线提醒");
                intent.putExtra("type1", "4");
                intent.putExtra("type2", "3");
                intent.putExtra("type3", "8");
                mActivity.startActivity(intent);
                dismiss();
                break;
        }
    }
}
