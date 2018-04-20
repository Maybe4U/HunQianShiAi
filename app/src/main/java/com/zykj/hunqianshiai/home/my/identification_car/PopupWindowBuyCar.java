package com.zykj.hunqianshiai.home.my.identification_car;

import android.app.Activity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BasePopupWindow;

/**
 * Created by xu on 2017/12/27.
 */

public class PopupWindowBuyCar extends BasePopupWindow {

    private RadioGroup mRgInform;

    protected PopupWindowBuyCar(Activity activity) {
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
        return R.layout.popupwindow_buy_car;
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
                if (null != mClickListener) {
                    mClickListener.onClickListener(rb.getText());
                }

                dismiss();
                break;
        }
    }
}
