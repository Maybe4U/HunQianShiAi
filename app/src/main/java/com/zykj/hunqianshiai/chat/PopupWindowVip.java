package com.zykj.hunqianshiai.chat;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BasePopupWindow;
import com.zykj.hunqianshiai.home.my.member.MemberActivity;

/**
 * Created by xu on 2018/1/19.
 */

public class PopupWindowVip extends BasePopupWindow {
    public PopupWindowVip(Activity activity) {
        super(activity);
        TextView tv_reset = view.findViewById(R.id.tv_reset);
        TextView tv_affirm = view.findViewById(R.id.tv_affirm);
        tv_reset.setOnClickListener(this);
        tv_affirm.setOnClickListener(this);
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
                Intent intent = new Intent(mActivity, MemberActivity.class);
                mActivity.startActivity(intent);
                dismiss();
                break;
        }
    }
}
