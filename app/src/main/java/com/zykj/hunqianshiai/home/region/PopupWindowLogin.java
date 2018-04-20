package com.zykj.hunqianshiai.home.region;

import android.app.Activity;

import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BasePopupWindow;

/**
 * Created by xu on 2018/2/1.
 */

public class PopupWindowLogin extends BasePopupWindow {
    protected PopupWindowLogin(Activity activity) {
        super(activity);
    }

    @Override
    public int getViewId() {
        return R.layout.popupwindow_login;
    }
}
