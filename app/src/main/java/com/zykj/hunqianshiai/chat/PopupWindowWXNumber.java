package com.zykj.hunqianshiai.chat;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BasePopupWindow;

/**
 * Created by xu on 2018/1/19.
 */

public class PopupWindowWXNumber extends BasePopupWindow {

    private String number;
    protected PopupWindowWXNumber(Activity activity, String number) {
        super(activity);
        this.number = number;
        TextView tv_wx = view.findViewById(R.id.tv_wx);
        if (!TextUtils.isEmpty(number)) {
            tv_wx.setText("TA的微信号：" + number);
        } else {
            tv_wx.setText("TA的还没有设置微信号");
        }

        TextView tv_dismiss = view.findViewById(R.id.tv_dismiss);
        tv_dismiss.setOnClickListener(this);
        TextView tv_dism = view.findViewById(R.id.tv_dism);
        tv_dism.setOnClickListener(this);
    }

    @Override
    public int getViewId() {
        return R.layout.popupwindow_wx_number;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_dismiss:
                if (!TextUtils.isEmpty(number)) {
                    mClickListener.onClickListener("");
                }
                dismiss();
                break;
            case R.id.tv_dism:
                dismiss();
                break;
        }
    }
}
