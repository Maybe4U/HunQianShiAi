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

public class PopupWindowHei extends BasePopupWindow {
    public PopupWindowHei(Activity activity) {
        super(activity);
        TextView tv_reset = view.findViewById(R.id.tv_reset);
        TextView tv_affirm = view.findViewById(R.id.tv_affirm);
        TextView tv_content = view.findViewById(R.id.tv_content);
        tv_reset.setOnClickListener(this);
        tv_affirm.setOnClickListener(this);
        tv_content.setText("确定要将TA加入黑名单吗？");
        tv_affirm.setText("确定");
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
                mClickListener.onClickListener("");
                dismiss();
                break;
        }
    }
}
