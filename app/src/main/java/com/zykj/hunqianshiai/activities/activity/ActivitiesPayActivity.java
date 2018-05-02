package com.zykj.hunqianshiai.activities.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BaseBean;
import com.zykj.hunqianshiai.bases.BaseModel;
import com.zykj.hunqianshiai.bases.BaseModelImpl;
import com.zykj.hunqianshiai.bases.BasePresenterImpl;
import com.zykj.hunqianshiai.bases.BaseView;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.login_register.NameAuthenticationActivity;
import com.zykj.hunqianshiai.login_register.PopupWindowFinish;
import com.zykj.hunqianshiai.net.UrlContent;
import com.zykj.hunqianshiai.pay.AuthResult;
import com.zykj.hunqianshiai.pay.PayResult;
import com.zykj.hunqianshiai.pay.WXBean;
import com.zykj.hunqianshiai.pay.ZFBBean;
import com.zykj.hunqianshiai.tools.JsonUtils;
import com.zykj.hunqianshiai.tools.RxBus;

import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Subscription;
import rx.functions.Action1;

import static com.zykj.hunqianshiai.bases.MyApplication.api;

/**
 * 活动详情
 */
public class ActivitiesPayActivity extends BasesActivity implements CompoundButton.OnCheckedChangeListener, BaseView<String> {

    @Bind(R.id.check_zfb)
    CheckBox mCheckZFB;
    @Bind(R.id.check_wx)
    CheckBox mCheckWX;
    @Bind(R.id.tv_money)
    TextView tv_money;
    private BasePresenterImpl mPresenter;
    private int paytype;
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;
    private Subscription mSubscribe;
    private String mCost;

    @Override
    protected int getContentViewX() {
        return R.layout.activity_activities_pay;
    }

    @Override
    protected void initView() {
        appBar("活动详情");

        Bundle bundle = getIntent().getExtras();
        mCost = bundle.getString("cost");
        tv_money.setText(mCost + "元");
        mPresenter = new BasePresenterImpl(this, new BaseModelImpl());
        mCheckWX.setOnCheckedChangeListener(this);
        mCheckZFB.setOnCheckedChangeListener(this);
        mCheckZFB.performClick();

        mSubscribe = RxBus.getInstance().toObserverable(WXBean.class)
                .subscribe(new Action1<WXBean>() {
                    @Override
                    public void call(WXBean wxBean) {
//                        openActivity(NameAuthenticationActivity.class);
                        finish();
                    }
                });
    }

    @OnClick({R.id.tv_affirm, R.id.tv_cancel})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_affirm:

                mParams.clear();
                mParams.put("userid", UrlContent.USER_ID);
                mParams.put("price", mCost);
//                mParams.put("price", 0.01);
                mParams.put("type", 7);
                mParams.put("beizhu", "活动报名");
                mParams.put("paytype", paytype);
                mPresenter.getData(UrlContent.PAY_URL, mParams, BaseModel.DEFAULT_TYPE);
                break;
            case R.id.tv_cancel:
                finish();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.check_zfb:
                if (b) {
                    mCheckWX.setChecked(false);
                } else {
                    if (!mCheckWX.isChecked()) {
                        mCheckZFB.setChecked(true);
                    }
                }
                paytype = 1;
                break;
            case R.id.check_wx:
                if (b) {
                    mCheckZFB.setChecked(false);
                } else {
                    if (!mCheckZFB.isChecked()) {
                        mCheckWX.setChecked(true);
                    }
                }
                paytype = 2;
                break;

        }
    }

    @Override
    public void success(String bean) {
        if (paytype == 1) {//支付宝
            ZFBBean zfbBean = JsonUtils.GsonToBean(bean, ZFBBean.class);
            final String data = zfbBean.data;

            Runnable payRunnable = new Runnable() {

                @Override
                public void run() {
                    PayTask alipay = new PayTask(ActivitiesPayActivity.this);
                    Map<String, String> result = alipay.payV2(data, true);

                    Message msg = new Message();
                    msg.what = SDK_PAY_FLAG;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                }
            };
            // 必须异步调用
            Thread payThread = new Thread(payRunnable);
            payThread.start();

        } else if (paytype == 2) {//微信
            WXBean wxBean = JsonUtils.GsonToBean(bean, WXBean.class);
            WXBean.WXData data = wxBean.data;
            PayReq request = new PayReq();
            request.appId = data.appid;
            request.partnerId = data.partnerid;
            request.prepayId = data.prepayid;
            request.packageValue = "Sign=WXPay";
            request.nonceStr = data.noncestr;
            request.timeStamp = data.timestamp + "";
            request.sign = data.sign;
            api.sendReq(request);
        }

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

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(ActivitiesPayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
//                        openActivity(NameAuthenticationActivity.class);
                        finish();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(ActivitiesPayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        Toast.makeText(ActivitiesPayActivity.this, "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        // 其他状态值则为授权失败
                        Toast.makeText(ActivitiesPayActivity.this, "授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();

                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        if (null != mSubscribe && !mSubscribe.isUnsubscribed()) {
            mSubscribe.unsubscribe();
        }
        super.onDestroy();
    }
}
