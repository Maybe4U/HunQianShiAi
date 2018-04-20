package com.zykj.hunqianshiai.home.my.info;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BasePopupWindow;

/**
 * Created by xu on 2017/12/20.
 */

public class PopupWindowAddInfo extends BasePopupWindow {

    private EditText mMoreTag;
    private TextView tag;
    private String cm;

    public PopupWindowAddInfo(Activity activity, TextView tag, String cm) {
        super(activity);
        this.tag = tag;
        this.cm = cm;
        dismissPop();
        mMoreTag = view.findViewById(R.id.et_more_tag);
        TextView affirm = view.findViewById(R.id.tv_affirm);
        affirm.setOnClickListener(this);
    }

    @Override
    public int getViewId() {
        return R.layout.popupwindow_add_info;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_affirm:
                String trim = mMoreTag.getText().toString().trim();
                tag.setText(trim + cm);
                mClickListener.onClickListener(trim);
                dismiss();
                break;
        }
    }
}
