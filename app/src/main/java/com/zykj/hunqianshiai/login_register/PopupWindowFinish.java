package com.zykj.hunqianshiai.login_register;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BasePopupWindow;

/**
 * Created by xu on 2018/1/15.
 */

public class PopupWindowFinish extends BasePopupWindow {
    public PopupWindowFinish(Activity activity) {
        super(activity);
        TextView tv_cancel = view.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(this);
        TextView tv_affirm = view.findViewById(R.id.tv_affirm);
        tv_affirm.setOnClickListener(this);

    }

    @Override
    public int getViewId() {
        return R.layout.popupwindow_finish;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_affirm:
                dismiss();
                break;
            case R.id.tv_cancel:
                dismiss();
                mActivity.finish();
        }
    }
}
