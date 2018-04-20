package com.zykj.hunqianshiai.bases;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.zykj.hunqianshiai.R;


/**
 * Created by ${xu} on 2017/10/11.
 */

public abstract class BasePopupWindow extends PopupWindow implements View.OnClickListener{

    public abstract int getViewId();

    public Activity mActivity;
    public View view;

    protected BasePopupWindow(Activity activity) {
        this(activity, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    protected BasePopupWindow(Activity activity, int width, int height) {

        view = LayoutInflater.from(activity).inflate(getViewId(), null);
        setWidth(width);
        setHeight(height);
        setContentView(view);
        setBackgroundDrawable(new ColorDrawable(0x00000000));
        setOutsideTouchable(false);
        setFocusable(true);

        this.mActivity = activity;

        backgroundAlpha(0.5f);
    }

    @Override
    public void dismiss() {
        backgroundAlpha(1);
        super.dismiss();
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    private void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        mActivity.getWindow().setAttributes(lp);
    }

    public void dismissPop() {
        ImageView dismiss = (ImageView) view.findViewById(R.id.iv_dismiss);
        dismiss.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_dismiss:
                dismiss();
                break;
        }
    }

    public ClickListener mClickListener;
    public interface ClickListener {
        void onClickListener(Object object);
    }

    public void setClickListener(ClickListener clickListener) {
        this.mClickListener = clickListener;
    }
}
