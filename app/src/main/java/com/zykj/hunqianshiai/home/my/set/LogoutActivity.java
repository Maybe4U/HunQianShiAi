package com.zykj.hunqianshiai.home.my.set;

import android.hardware.SensorEventListener;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BaseBean;
import com.zykj.hunqianshiai.bases.BaseModel;
import com.zykj.hunqianshiai.bases.BaseModelImpl;
import com.zykj.hunqianshiai.bases.BasePresenterImpl;
import com.zykj.hunqianshiai.bases.BaseView;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.intro.ChooseLoginActivity;
import com.zykj.hunqianshiai.net.SPKey;
import com.zykj.hunqianshiai.net.UrlContent;
import com.zykj.hunqianshiai.tools.JsonUtils;
import com.zykj.hunqianshiai.tools.SPUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 注销
 */
public class LogoutActivity extends BasesActivity implements BaseView<String> {

    @Bind(R.id.tv_right)
    TextView tv_right;
    @Bind(R.id.cb_1)
    CheckBox cb_1;
    @Bind(R.id.cb_2)
    CheckBox cb_2;
    @Bind(R.id.cb_3)
    CheckBox cb_3;
    @Bind(R.id.cb_4)
    CheckBox cb_4;
    private String reason = "";

    private BasePresenterImpl mPresenter;

    @Override
    protected int getContentViewX() {
        return R.layout.activity_logout;
    }

    @Override
    protected void initView() {
        appBar("注销资料");
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText("确认");

        mPresenter = new BasePresenterImpl(this, new BaseModelImpl());
    }

    @OnClick(R.id.tv_right)
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_right:
                if (cb_1.isChecked()) {
                    reason = "已经找到伴侣了";
                }
                if (cb_2.isChecked()) {
                    reason = reason + ",通过其他渠道已经找到伴侣了";
                }
                if (cb_3.isChecked()) {
                    reason = reason + ",被婚托、酒托骚扰";
                }
                if (cb_4.isChecked()) {
                    reason = reason + ",其他原因";
                }
                mParams.clear();
                mParams.put("userid", UrlContent.USER_ID);
                mParams.put("remark", reason);
                mPresenter.getData(UrlContent.WRITE_OFF_URL, mParams, BaseModel.DEFAULT_TYPE);

                break;
        }
    }

    @Override
    public void success(String bean) {
        BaseBean baseBean = JsonUtils.GsonToBean(bean, BaseBean.class);
        toastShow(baseBean.message);
        destroyApp();
        openActivity(ChooseLoginActivity.class);
        SPUtils.remove(this, SPKey.COMPLETE_LOGIN_KEY);
        SPUtils.remove(this, SPKey.USER_ID_KEY);
        finish();
    }

    @Override
    public void refresh(String bean) {

    }

    @Override
    public void loadMore(String bean) {

    }

    @Override
    public void failed(String content) {

    }
}
