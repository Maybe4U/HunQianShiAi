package com.zykj.hunqianshiai.home.dynamic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BasePopupWindow;
import com.zykj.hunqianshiai.inform.InformActivity;

/**
 * 更多选项弹窗
 * Created by xu on 2017/12/25.
 */

public class PopupWindowMoreOption extends BasePopupWindow {

    private String id;
    private String userid;
    private Bundle mBundle;

    public PopupWindowMoreOption(Activity activity, String id, String userid) {
        super(activity);
        setAnimationStyle(R.style.popwin_anim_style);
        TextView inform = view.findViewById(R.id.tv_inform);
        TextView cancel = view.findViewById(R.id.tv_cancel);
        TextView tv_my_look = view.findViewById(R.id.tv_my_look);
        TextView tv_look_me = view.findViewById(R.id.tv_look_me);
        cancel.setOnClickListener(this);
        inform.setOnClickListener(this);
        tv_my_look.setOnClickListener(this);
        tv_look_me.setOnClickListener(this);
        this.id = id;
        this.userid = userid;
        mBundle = new Bundle();
    }

    @Override
    public int getViewId() {
        return R.layout.popupwindow_more_option;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_inform:
                Intent intent = new Intent(mActivity, InformActivity.class);
                mBundle.putString("id", id);
                mBundle.putString("userid", userid);
                intent.putExtras(mBundle);
                mActivity.startActivity(intent);

                break;
            case R.id.tv_cancel:

                break;
            case R.id.tv_my_look:
                mClickListener.onClickListener("0");

                break;
            case R.id.tv_look_me:
                mClickListener.onClickListener("1");
                break;
        }
        dismiss();
    }
}
