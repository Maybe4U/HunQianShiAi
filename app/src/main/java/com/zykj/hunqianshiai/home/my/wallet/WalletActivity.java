package com.zykj.hunqianshiai.home.my.wallet;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BaseBean;
import com.zykj.hunqianshiai.bases.BaseModel;
import com.zykj.hunqianshiai.bases.BaseModelImpl;
import com.zykj.hunqianshiai.bases.BasePresenterImpl;
import com.zykj.hunqianshiai.bases.BaseView;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.home.my.my_gift.GiftActivity;
import com.zykj.hunqianshiai.login_register.UploadBean;
import com.zykj.hunqianshiai.net.UrlContent;
import com.zykj.hunqianshiai.tools.JsonUtils;

import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 钱包
 */
public class WalletActivity extends BasesActivity implements BaseView<String> {

    @Bind(R.id.tv_right)
    TextView tv_right;
    @Bind(R.id.tv_yue)
    TextView tv_yue;
    @Bind(R.id.recycler_record)
    RecyclerView mRecyclerView;

    @Override
    protected int getContentViewX() {
        return R.layout.activity_wallet;
    }

    @Override
    protected void initView() {
        appBar("我的钱包");
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText("我的代理");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        BasePresenterImpl presenter = new BasePresenterImpl(this, new BaseModelImpl());

        mParams.clear();
        mParams.put("userid", UrlContent.USER_ID);
        presenter.getData(UrlContent.GET_BALANCE_URL, mParams, BaseModel.DEFAULT_TYPE);
        presenter.getData(UrlContent.WALLET_RECORD_URL, mParams, BaseModel.REFRESH_TYPE);
    }

    @Override
    public void success(String bean) {
        BaseBean baseBean = JsonUtils.GsonToBean(bean, BaseBean.class);
        if (baseBean.code != 200) {
            return;
        }
        UploadBean uploadBean = JsonUtils.GsonToBean(bean, UploadBean.class);
        tv_yue.setText(uploadBean.data + "元（余额+礼物）");
    }

    @Override
    public void refresh(String bean) {
        WalletBean walletBean = JsonUtils.GsonToBean(bean, WalletBean.class);
        List<WalletBean.WalletData> data = walletBean.data;

        //逆序排列
        //Collections.reverse(data);

        WalletAdapter walletAdapter = new WalletAdapter(data);
        mRecyclerView.setAdapter(walletAdapter);
    }

    @Override
    public void loadMore(String bean) {

    }

    @Override
    public void failed(String content) {

    }

    @OnClick({R.id.tv_chongzhi, R.id.tv_tixian, R.id.tv_right})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_chongzhi:
                openActivity(RechargeActivity.class);
                break;
            case R.id.tv_tixian:
                openActivity(WithdrawDepositActivity.class);
                break;
            case R.id.tv_right:
//                openActivity(GiftActivity.class);
                openActivity(FenXiaoActivity.class);
                break;
        }
    }
}
