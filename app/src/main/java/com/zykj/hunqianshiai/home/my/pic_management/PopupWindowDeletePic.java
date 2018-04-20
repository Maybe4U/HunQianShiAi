package com.zykj.hunqianshiai.home.my.pic_management;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BasePopupWindow;

/**
 * Created by xu on 2018/1/10.
 */

public class PopupWindowDeletePic extends BasePopupWindow {

    protected PopupWindowDeletePic(Activity activity) {
        super(activity);
        TextView tv_add = view.findViewById(R.id.tv_add);
        TextView tv_delete = view.findViewById(R.id.tv_delete);
        tv_add.setOnClickListener(this);
        tv_delete.setOnClickListener(this);
    }

    @Override
    public int getViewId() {
        return R.layout.popupwindow_delete_pic;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_add:
                mClickListener.onClickListener("add");
                break;
            case R.id.tv_delete:
                mClickListener.onClickListener("delete");
                break;
        }
        dismiss();
    }
}
