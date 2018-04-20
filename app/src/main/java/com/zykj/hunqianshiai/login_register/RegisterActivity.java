package com.zykj.hunqianshiai.login_register;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BaseBean;
import com.zykj.hunqianshiai.bases.BaseModel;
import com.zykj.hunqianshiai.bases.BaseModelImpl;
import com.zykj.hunqianshiai.bases.BasePresenterImpl;
import com.zykj.hunqianshiai.bases.BaseView;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.net.SPKey;
import com.zykj.hunqianshiai.net.UrlContent;
import com.zykj.hunqianshiai.tools.JsonUtils;
import com.zykj.hunqianshiai.tools.SPUtils;
import com.zykj.hunqianshiai.tools.TextUtil;
import com.zykj.hunqianshiai.web.WebViewActivity;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

/**
 * 注册
 */
public class RegisterActivity extends BasesActivity implements BaseView<String> {

    @Bind(R.id.et_phone_number)
    EditText phoneNumber;
    @Bind(R.id.et_verification_code)
    EditText verificationCode;
    @Bind(R.id.et_password)
    EditText password;
    @Bind(R.id.et_again_password)
    EditText againPassword;
    @Bind(R.id.et_invitation_code)
    EditText invitationCode;
    @Bind(R.id.tv_get_code)
    TextView getCode;
    @Bind(R.id.tv_login)
    TextView register;

    //《注册协议》文本
    @Bind(R.id.tv_text)
    TextView protocol;

    private Subscription mSubscribe;
    private LocationManager mLocationManager;
    private String locationProvider;

    private Location mLocation;
    private BasePresenterImpl mPresenter;
    private double mLongitude = 118.34014333333334;
    private double mLatitude = 35.106123333333336;
    private String mPhone;
    private EventHandler mEh;
    private String mPasswords;

    @Override
    protected int getContentViewX() {

        return R.layout.activity_register;
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void initView() {
        appBar("注册");

        mPresenter = new BasePresenterImpl(this, new BaseModelImpl());

        //获取地理位置管理器
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //获取所有可用的位置提供器
        List<String> providers = mLocationManager.getProviders(true);

        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS
            locationProvider = LocationManager.GPS_PROVIDER;
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是Network
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else {
//            toastShow("没有可用的位置提供器");

        }

        if (null != locationProvider) {
            //获取Location
            mLocation = mLocationManager.getLastKnownLocation(locationProvider);
            if (null != mLocation) {
                mLongitude = mLocation.getLongitude();
                mLatitude = mLocation.getLatitude();
            }
        }

        smsListener();

        //《婚前试爱注册协议》监听
        protocolListener();
    }

    /**
     * method desc.  : 《婚前试爱注册协议》监听
     * params        :
     * return        :
     */
    private void protocolListener() {
        //获取文本信息
        String protocolContent = protocol.getText().toString().trim();
        SpannableStringBuilder spannable = new SpannableStringBuilder(protocolContent);
        //对特定TextView文本设置颜色  8和18分别为“《” “》” 一般不改动
        spannable.setSpan(new ForegroundColorSpan(Color.RED),8,18
                , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //对特定TextView文本设置点击事件
        protocol.setMovementMethod(LinkMovementMethod.getInstance());
        spannable.setSpan(new TextClick(),8,18
                , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置
        protocol.setText(spannable);

    }
    /**
     * method desc.  : 《婚前试爱注册协议》监听 逻辑
     *                  根据客户要求修改
     * params        :
     * return        :
     */
    class TextClick extends ClickableSpan {
        @Override
        public void onClick(View view) {
            Bundle bundle = new Bundle();
            bundle.putString("url","http://47.104.91.22/api.php?c=Fell&a=xieyi&id=1");
            bundle.putString("title","婚前试爱注册协议");
            openActivity(WebViewActivity.class,bundle);
            //去除  添加ClickableSpan后点击选中文字背景变色的问题
            protocol.setHighlightColor(getResources().getColor(android.R.color.transparent));
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(ds.linkColor);
            ds.setUnderlineText(false);
        }
    }

    private void smsListener() {
        // 如果希望在读取通信录的时候提示用户，可以添加下面的代码，并且必须在其他代码调用之前，否则不起作用；如果没这个需求，可以不加这行代码
        SMSSDK.setAskPermisionOnReadContact(true);
        mEh = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                if (data instanceof Throwable) {
                    Throwable throwable = (Throwable) data;
                    final String msg = throwable.getMessage();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            toastShow(msg);
                            MsgBean msgBean = JsonUtils.GsonToBean(msg, MsgBean.class);
                            if (msgBean.status == 477) {
                                toastShow("亲，每天只能发送5条验证码，请明天再试！");
                            } else if (msgBean.status == 462){
                                toastShow("亲，每5分钟只能发送3条验证码，请5分钟后再试！");
                            }
                        }
                    });
                } else {
                    if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {

                    } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        // 这里是验证成功的回调，可以处理验证成功后您自己的逻辑，需要注意的是这里不是主线程
                        String code = invitationCode.getText().toString().trim();

                        mParams.clear();
                        mParams.put("phone", mPhone);
                        mParams.put("pass", mPasswords);
                        mParams.put("ma", code);
                        mParams.put("lng", mLongitude);
                        mParams.put("lat", mLatitude);
                        mPresenter.getData(UrlContent.REGISTER_PHONE_URL, mParams, BaseModel.DEFAULT_TYPE);

                    }
                }
            }
        };

        SMSSDK.registerEventHandler(mEh); //注册短信回调
    }

    @OnClick({R.id.tv_login, R.id.tv_get_code})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
//            case R.id.tv_text:
//                Bundle bundle = new Bundle();
//                bundle.putString("url","http://47.104.91.22/api.php?c=Fell&a=xieyi&id=1");
//                bundle.putString("title","婚前试爱注册协议");
//                openActivity(WebViewActivity.class,bundle);
//                break;
            case R.id.tv_login://注册
//                openActivity(SetInfoActivity.class);
                mPhone = phoneNumber.getText().toString().trim();
                if (!TextUtil.isMobile(mPhone)) {
                    toastShow("手机号不正确");
                    return;
                }

                mPasswords = password.getText().toString().trim();
                String againPasswords = againPassword.getText().toString().trim();
                if (!TextUtil.isPasswordLengthLegal(mPasswords) && !TextUtil.isPasswordLengthLegal(againPasswords)) {
                    toastShow("请按正确格式输入密码");
                    return;
                }

                if (!mPasswords.equals(againPasswords)) {
                    toastShow("2次密码不一样");
                    return;
                }
                register.setClickable(false);
                String verification = verificationCode.getText().toString().trim();
                SMSSDK.submitVerificationCode("86", mPhone, verification);
                break;
            case R.id.tv_get_code://获取验证码
                mPhone = phoneNumber.getText().toString().trim();
                if (!TextUtil.isMobile(mPhone)) {
                    toastShow("手机号不正确");
                    return;
                }


                final int count = 60;
                String trim = getCode.getText().toString().trim();
                if (trim.equals("获取验证码")) {
                    SMSSDK.getVerificationCode("86", mPhone);
                    //计时次数
                    mSubscribe = Observable.interval(0, 1, TimeUnit.SECONDS)
                            .observeOn(AndroidSchedulers.mainThread())
                            .take(count + 1)//计时次数
                            .map(new Func1<Long, Long>() {
                                @Override
                                public Long call(Long aLong) {
                                    return count - aLong;
                                }
                            })
                            .subscribe(new Subscriber<Long>() {
                                @Override
                                public void onCompleted() {
                                    getCode.setText("获取验证码");
                                }

                                @Override
                                public void onError(Throwable e) {
                                    e.printStackTrace();
                                }

                                @Override
                                public void onNext(Long integer) {
                                    getCode.setText(integer + "s");
                                }
                            });
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void success(String bean) {
        register.setClickable(true);
        BaseBean baseBean = JsonUtils.GsonToBean(bean, BaseBean.class);
        int code = baseBean.code;
        if (code == 200) {
            RegisterBean registerBean = JsonUtils.GsonToBean(bean, RegisterBean.class);
            SPUtils.put(this, SPKey.USER_ID_KEY, registerBean.data);
            UrlContent.USER_ID = registerBean.data;
            toastShow("注册成功");
            openActivity(SetInfoActivity.class);
            finish();
        } else if (code == 201) {
            toastShow("手机号已经被注册过了");
        } else {
            toastShow("注册失败");
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

    @Override
    protected void onDestroy() {
        if (null != mSubscribe && !mSubscribe.isUnsubscribed()) {
            mSubscribe.unsubscribe();
        }
        SMSSDK.unregisterEventHandler(mEh);
        super.onDestroy();
    }
}
