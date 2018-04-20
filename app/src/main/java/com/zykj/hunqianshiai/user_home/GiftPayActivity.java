package com.zykj.hunqianshiai.user_home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
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

public class GiftPayActivity extends BasesActivity implements CompoundButton.OnCheckedChangeListener, BaseView<String> {

    @Bind(R.id.cb_zfb)
    CheckBox zfb;
    @Bind(R.id.cb_wx)
    CheckBox wx;
    @Bind(R.id.cb_remaining)
    CheckBox remaining;
    @Bind(R.id.tv_price)
    TextView tv_price;

    private int paytype = 0;
    private Subscription mSubscribe;
    private BasePresenterImpl mPresenter;
    private String mOther_id;
    private String mOther_userid;
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;
    private String mPrice;

    @Override
    protected int getContentViewX() {
        return R.layout.activity_gift_pay;
    }

    @Override
    protected void initView() {
        appBar("支付");

        Bundle bundle = getIntent().getExtras();
        mPrice = bundle.getString("price");
        mOther_id = bundle.getString("other_id");
        mOther_userid = bundle.getString("other_userid");

        mPresenter = new BasePresenterImpl(this, new BaseModelImpl());

        tv_price.setText(mPrice + "元");
        zfb.setOnCheckedChangeListener(this);
        wx.setOnCheckedChangeListener(this);
        remaining.setOnCheckedChangeListener(this);

        mSubscribe = RxBus.getInstance().toObserverable(WXBean.class)
                .subscribe(new Action1<WXBean>() {
                    @Override
                    public void call(WXBean wxBean) {
                        finish();
                    }
                });
    }

    @OnClick({R.id.tv_cancel, R.id.tv_affirm})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.tv_affirm:
                if (paytype == 0) {
                    toastShow("请选择支付方式");
                    return;
                }
                mParams.clear();
                mParams.put("userid", UrlContent.USER_ID);
//                mParams.put("price", mPrice);
                mParams.put("price", mPrice);
                mParams.put("type", 3);
                mParams.put("beizhu", "购买礼物");
                mParams.put("paytype", paytype);
                mParams.put("num", 1);
                mParams.put("other_id", mOther_id);
                mParams.put("other_userid", mOther_userid);
                mPresenter.getData(UrlContent.PAY_URL, mParams, BaseModel.DEFAULT_TYPE);
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.cb_zfb:
                if (b) {
                    wx.setChecked(false);
                    remaining.setChecked(false);
                } else {
                    if (!wx.isChecked() && !remaining.isChecked()) {
                        zfb.setChecked(true);
                    }
                }
                paytype = 1;
                break;
            case R.id.cb_wx:
                if (b) {
                    zfb.setChecked(false);
                    remaining.setChecked(false);
                } else {
                    if (!zfb.isChecked() && !remaining.isChecked()) {
                        wx.setChecked(true);
                    }
                }
                paytype = 2;
                break;
            case R.id.cb_remaining:
                if (b) {
                    wx.setChecked(false);
                    zfb.setChecked(false);
                } else {
                    if (!wx.isChecked() && !zfb.isChecked()) {
                        remaining.setChecked(true);
                    }
                }
                paytype = 3;
                break;
            default:
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
                    PayTask alipay = new PayTask(GiftPayActivity.this);
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
        } else if (paytype == 3) {
            BaseBean baseBean = JsonUtils.GsonToBean(bean, BaseBean.class);
            toastShow(baseBean.message);
            if (baseBean.message.equals("支付成功")) {
                finish();
            }
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
                        Toast.makeText(GiftPayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
//                        openActivity(NameAuthenticationActivity.class);
                        finish();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(GiftPayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(GiftPayActivity.this, "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        // 其他状态值则为授权失败
                        Toast.makeText(GiftPayActivity.this, "授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();

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
