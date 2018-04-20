package com.zykj.hunqianshiai.login_register;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.bases.BaseBean;
import com.zykj.hunqianshiai.bases.BaseModel;
import com.zykj.hunqianshiai.bases.BaseModelImpl;
import com.zykj.hunqianshiai.bases.BasePresenterImpl;
import com.zykj.hunqianshiai.bases.BaseView;
import com.zykj.hunqianshiai.net.UrlContent;
import com.zykj.hunqianshiai.tools.JsonUtils;
import com.zykj.hunqianshiai.tools.TextUtil;

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

public class ForgetPasswordActivity extends BasesActivity implements BaseView<String> {
    @Bind(R.id.et_phone_number)
    EditText phoneNumber;
    @Bind(R.id.et_verification_code)
    EditText verificationCode;
    @Bind(R.id.et_password)
    EditText password;
    @Bind(R.id.et_again_password)
    EditText againPassword;
    @Bind(R.id.tv_get_code)
    TextView getCode;
    @Bind(R.id.tv_login)
    TextView register;

    private Subscription mSubscribe;

    private BasePresenterImpl mPresenter;
    private String mPhone;
    private EventHandler mEh;
    private String mPasswords;

    @Override
    protected int getContentViewX() {

        return R.layout.activity_forget_password;
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void initView() {
        appBar("忘记密码");

        mPresenter = new BasePresenterImpl(this, new BaseModelImpl());
        smsListener();
    }

    private void smsListener() {
        mEh = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {

                if (result == SMSSDK.RESULT_COMPLETE) {//回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功

                        mParams.clear();
                        mParams.put("phone", mPhone);
                        mParams.put("newpass", mPasswords);
                        mPresenter.getData(UrlContent.FORGET_PASSWORD_URL, mParams, BaseModel.DEFAULT_TYPE);
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {//获取验证码成功

                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {//返回支持发送验证码的国家列表
                    }
                } else {
                    ((Throwable) data).printStackTrace();
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
            case R.id.tv_login://注册

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

            toastShow("重置成功");
            finish();
        } else {
            toastShow("重置失败");
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
        register.setClickable(true);
    }

    @Override
    protected void onDestroy() {
        if (null != mSubscribe && !mSubscribe.isUnsubscribed()) {
            mSubscribe.unsubscribe();
        }
        super.onDestroy();
    }
}

