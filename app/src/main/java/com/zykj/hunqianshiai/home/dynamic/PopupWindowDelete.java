package com.zykj.hunqianshiai.home.dynamic;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BasePopupWindow;

/**
 * Created by xu on 2018/1/24.
 */

public class PopupWindowDelete extends BasePopupWindow {
    public PopupWindowDelete(Activity activity) {
        super(activity);
        setAnimationStyle(R.style.popwin_anim_style);
        TextView tv_inform = view.findViewById(R.id.tv_inform);
        TextView tv_cancel = view.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(this);
        tv_inform.setOnClickListener(this);
    }

    @Override
    public int getViewId() {
        return R.layout.popupwindow_delete;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_inform:
                mClickListener.onClickListener("");
                dismiss();
                break;
            case R.id.tv_cancel:
                dismiss();
                break;

        }
    }
}
