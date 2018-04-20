package com.zykj.hunqianshiai.activities.information;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BasePopupWindow;
import com.zykj.hunqianshiai.tools.T;

/**
 * 发布评论
 * Created by xu on 2017/12/25.
 */

public class PopupWindowInformationReply extends BasePopupWindow {

    private  EditText et_content;

    @SuppressLint("WrongConstant")
    public PopupWindowInformationReply(Activity activity) {
        super(activity);
        setAnimationStyle(R.style.popwin_anim_style);
        TextView cancel = view.findViewById(R.id.tv_cancel);
        cancel.setOnClickListener(this);
        TextView affirm = view.findViewById(R.id.tv_affirm);
        affirm.setOnClickListener(this);
        et_content = view.findViewById(R.id.et_content);

        //防止PopupWindow被软件盘挡住
        setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    public int getViewId() {
        return R.layout.popupwindow_information_reply;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_affirm:
                String trim = et_content.getText().toString().trim();
                if (TextUtils.isEmpty(trim)) {
                    T.showShort(mActivity,"评论内容不能为空");
                    return;
                }

                mClickListener.onClickListener(trim);
                dismiss();
                break;
        }
    }
}
