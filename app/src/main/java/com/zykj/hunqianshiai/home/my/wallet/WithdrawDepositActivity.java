package com.zykj.hunqianshiai.home.my.wallet;

import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BaseBean;
import com.zykj.hunqianshiai.bases.BaseModel;
import com.zykj.hunqianshiai.bases.BaseModelImpl;
import com.zykj.hunqianshiai.bases.BasePresenterImpl;
import com.zykj.hunqianshiai.bases.BaseView;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.home.my.member.MemberActivity;
import com.zykj.hunqianshiai.home.my.member.VipBean;
import com.zykj.hunqianshiai.login_register.UploadBean;
import com.zykj.hunqianshiai.net.UrlContent;
import com.zykj.hunqianshiai.tools.JsonUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 提现
 */
public class WithdrawDepositActivity extends BasesActivity implements BaseView<String> {

    @Bind(R.id.tv_text1)
    TextView tv_text1;
    @Bind(R.id.tv_text2)
    TextView tv_text2;
    @Bind(R.id.et_account)
    TextView et_account;
    @Bind(R.id.et_money)
    TextView et_money;
    @Bind(R.id.tv_yue)
    TextView tv_yue;
    @Bind(R.id.ll_layout)
    LinearLayout ll_layout;

    private BasePresenterImpl mPresenter;

    @Override
    protected int getContentViewX() {
        return R.layout.activity_withdraw_deposit;
    }

    @Override
    protected void initView() {
        appBar("提现");
        String str = "2、如需要提现必须成为<font color='#edbd5a'>最高级别会员</font>，方可提现，普通会员只可看到提现的金额，不能提现。";
        tv_text1.setText(Html.fromHtml(str));
        String string = "3、<font color='#edbd5a'>礼物提现有效期为一年</font>，过期不能提现";
        tv_text2.setText(Html.fromHtml(string));

        mPresenter = new BasePresenterImpl(this, new BaseModelImpl());
        mParams.clear();
        mParams.put("userid", UrlContent.USER_ID);
        mPresenter.getData(UrlContent.GET_BALANCE_URL, mParams, BaseModel.REFRESH_TYPE);
        mPresenter.getData(UrlContent.IS_VIP_URL,mParams,BaseModel.LOADING_MORE_TYPE);
    }

    @OnClick({R.id.tv_upgrade, R.id.tv_affirm})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_upgrade:
                openActivity(MemberActivity.class);
                break;
            case R.id.tv_affirm:
                if (ll_layout.getVisibility() == View.VISIBLE) {
                    toastShow("您现在会员等级不足无法提现");
                    return;
                }
                String trim = et_account.getText().toString().trim();
                String trim1 = et_money.getText().toString().trim();

                if (TextUtils.isEmpty(trim) || TextUtils.isEmpty(trim1)) {
                    toastShow("数据不能为空");
                    return;
                }
                mParams.clear();
                mParams.put("userid", UrlContent.USER_ID);
                mParams.put("account", trim);
                mParams.put("price", trim1);
                mPresenter.getData(UrlContent.WITHDRAW_DEPOSIT_URL, mParams, BaseModel.DEFAULT_TYPE);
                break;
        }
    }

    @Override
    public void success(String bean) {
        BaseBean baseBean = JsonUtils.GsonToBean(bean, BaseBean.class);
        toastShow(baseBean.message);
    }

    @Override
    public void refresh(String bean) {
        BaseBean baseBean = JsonUtils.GsonToBean(bean, BaseBean.class);
        if (baseBean.code != 200) {
            return;
        }
        UploadBean uploadBean = JsonUtils.GsonToBean(bean, UploadBean.class);
        tv_yue.setText("可提现金额" + uploadBean.data + "元");
    }

    @Override
    public void loadMore(String bean) {
        BaseBean baseBean = JsonUtils.GsonToBean(bean, BaseBean.class);
        if (baseBean.code != 200) {
            return;
        }
        VipBean vipBean = JsonUtils.GsonToBean(bean, VipBean.class);

        String state = vipBean.data.state;
        if (state.equals("2")) {
            ll_layout.setVisibility(View.GONE);
        } else {
            ll_layout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void failed(String content) {

    }
}
