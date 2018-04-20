package com.zykj.hunqianshiai.chat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BasePopupWindow;
import com.zykj.hunqianshiai.home.my.member.MemberActivity;
import com.zykj.hunqianshiai.user_home.personal_assistant.OpenCounselorActivity;

/**
 * Created by xu on 2018/1/19.
 */

public class PopupWindowAssistant extends BasePopupWindow {

    private final Bundle mBundle;

    protected PopupWindowAssistant(Activity activity) {
        super(activity);
        TextView tv_reset = view.findViewById(R.id.tv_reset);
        TextView tv_affirm = view.findViewById(R.id.tv_affirm);
        TextView tv_content = view.findViewById(R.id.tv_content);
        tv_reset.setOnClickListener(this);
        tv_affirm.setOnClickListener(this);
        tv_content.setText("此功能需要开通私人助理才可使用");
        mBundle = new Bundle();
    }

    @Override
    public int getViewId() {
        return R.layout.popupwindow_vip;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_reset:
                dismiss();
                break;
            case R.id.tv_affirm:
                mBundle.clear();
                mBundle.putString("title","开通婚恋助理");
                mBundle.putString("type1","2");
                mBundle.putString("type2","1");
                mBundle.putString("type3","5");
                Intent intent = new Intent(mActivity, OpenCounselorActivity.class);
                intent.putExtras(mBundle);
                mActivity.startActivity(intent);
                dismiss();
                break;
        }
    }
}
