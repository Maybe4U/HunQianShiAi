package com.zykj.hunqianshiai.login_register;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.bases.BaseBean;
import com.zykj.hunqianshiai.bases.BaseModel;
import com.zykj.hunqianshiai.bases.BaseModelImpl;
import com.zykj.hunqianshiai.bases.BasePresenterImpl;
import com.zykj.hunqianshiai.bases.BaseView;
import com.zykj.hunqianshiai.net.UrlContent;
import com.zykj.hunqianshiai.pay.CertificationPayActivity;
import com.zykj.hunqianshiai.tools.JsonUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 提交认证申请
 */
public class CertificationActivity extends BasesActivity implements BaseView<String> {

    @Bind(R.id.tv_renz)
    TextView tv_renz;
    @Bind(R.id.tv_tuig)
    TextView tv_tuig;
    @Bind(R.id.tv_count)
    TextView tv_count;
    private String mTUI_price;
    private BasePresenterImpl mPresenter;
    private Bundle mBundle;

    @Override
    protected int getContentViewX() {
        return R.layout.activity_certification;
    }

    @Override
    protected void initView() {
        appBar("提交认证申请");
        mBundle = new Bundle();
        mPresenter = new BasePresenterImpl(this, new BaseModelImpl());

        mParams.clear();
        mParams.put("userid", UrlContent.USER_ID);
        mPresenter.getData(UrlContent.REN_ZHENG_URL, mParams, BaseModel.DEFAULT_TYPE);
    }

    @OnClick({R.id.tv_certification})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_certification:
                if (mTUI_price == null) {
                    return;
                }
                double tui = Double.valueOf(mTUI_price);

//                mBundle.clear();
//                mBundle.putString("type", "1");
//                mBundle.putString("price", mTUI_price);
//                mBundle.putString("beizhu", "认证费");
//                openActivity(CertificationPayActivity.class,mBundle);
                if (tui == 0) {
                    mParams.clear();
                    mParams.put("userid", UrlContent.USER_ID);
                    mPresenter.getData(UrlContent.SET_STATE_URL, mParams, BaseModel.REFRESH_TYPE);

                } else {
                    mBundle.clear();
                    mBundle.putString("paytype", "1");
                    mBundle.putString("price", mTUI_price);
                    mBundle.putString("beizhu", "认证费");
                    openActivity(CertificationPayActivity.class,mBundle);
                }

                break;
        }
    }

    @Override
    public void success(String bean) {
        BaseBean baseBean = JsonUtils.GsonToBean(bean, BaseBean.class);
        if (baseBean.code != 200) {
            return;
        }
        CertificationBean certificationBean = JsonUtils.GsonToBean(bean, CertificationBean.class);
        CertificationBean.Data data = certificationBean.data;
        tv_renz.setText("永久认证￥" + data.RZF_PRICE);
        mTUI_price = data.TUI_PRICE;

        tv_tuig.setText("推广期￥" + data.TUI_PRICE);
        tv_count.setText("已有" + data.count + "位优质单身用户已付费认证身份");
    }

    @Override
    public void refresh(String bean) {
        openActivity(NameAuthenticationActivity.class);
        finish();
    }

    @Override
    public void loadMore(String bean) {

    }

    @Override
    public void failed(String content) {

    }
}
