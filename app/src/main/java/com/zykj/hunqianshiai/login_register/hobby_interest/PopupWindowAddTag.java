package com.zykj.hunqianshiai.login_register.hobby_interest;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BasePopupWindow;

/**
 * Created by xu on 2017/12/20.
 */

public class PopupWindowAddTag extends BasePopupWindow {

    private EditText mMoreTag;
    private TextView tag;

    protected PopupWindowAddTag(Activity activity, TextView tag) {
        super(activity);
        this.tag = tag;
        dismissPop();
        mMoreTag = view.findViewById(R.id.et_more_tag);
        TextView affirm = view.findViewById(R.id.tv_affirm);
        affirm.setOnClickListener(this);
    }

    @Override
    public int getViewId() {
        return R.layout.popupwindow_add_tag;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_affirm:
                String trim = mMoreTag.getText().toString().trim();
                tag.setText(trim);
                dismiss();
                break;
        }
    }
}
