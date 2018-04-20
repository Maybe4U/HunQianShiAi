package com.zykj.hunqianshiai.home.authentication;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BasePopupWindow;

/**
 * Created by xu on 2018/1/28.
 */

public class PopupWindowRz extends BasePopupWindow {

    public PopupWindowRz(Activity activity, String content) {
        super(activity);
        TextView tv_content = view.findViewById(R.id.tv_content);
        tv_content.setText(content);
        TextView tv_reset = view.findViewById(R.id.tv_reset);
        TextView tv_affirm = view.findViewById(R.id.tv_affirm);
        tv_affirm.setOnClickListener(this);
        tv_reset.setOnClickListener(this);

    }

    @Override
    public int getViewId() {
        return R.layout.popupwindow_rz;
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
