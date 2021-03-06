package com.zykj.hunqianshiai.home.dynamic.my_dynamic;

import android.app.Activity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BasePopupWindow;

/**
 * 举报弹窗
 * Created by xu on 2017/12/22.
 */

public class PopupWindowSelectLook extends BasePopupWindow {

    private RadioGroup mRgInform;

    protected PopupWindowSelectLook(Activity activity) {
        super(activity);
        setAnimationStyle(R.style.popwin_anim_style);
        TextView cancel = view.findViewById(R.id.tv_cancel);
        TextView affirm = view.findViewById(R.id.tv_affirm);
        mRgInform = view.findViewById(R.id.rg_inform);
        cancel.setOnClickListener(this);
        affirm.setOnClickListener(this);
        mRgInform.check(R.id.rb_1);
    }

    @Override
    public int getViewId() {
        return R.layout.popupwindow_select_look;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_affirm:
                RadioButton rb = view.findViewById(mRgInform.getCheckedRadioButtonId());
                mClickListener.onClickListener(rb.getText());
                dismiss();
                break;
        }
    }
}
