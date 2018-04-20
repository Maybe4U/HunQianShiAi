package com.zykj.hunqianshiai.login_register.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.view.View;
import android.widget.EditText;

import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BaseBean;
import com.zykj.hunqianshiai.bases.BaseModel;
import com.zykj.hunqianshiai.bases.BaseModelImpl;
import com.zykj.hunqianshiai.bases.BasePresenterImpl;
import com.zykj.hunqianshiai.bases.BaseView;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.home.HomeMainActivity;
import com.zykj.hunqianshiai.login_register.CertificationActivity;
import com.zykj.hunqianshiai.login_register.FaceRecognitionActivity;
import com.zykj.hunqianshiai.login_register.ForgetPasswordActivity;
import com.zykj.hunqianshiai.login_register.NameAuthenticationActivity;
import com.zykj.hunqianshiai.login_register.RegisterActivity;
import com.zykj.hunqianshiai.login_register.SetInfoActivity;
import com.zykj.hunqianshiai.login_register.hobby_interest.HobbyInterestActivity;
import com.zykj.hunqianshiai.net.SPKey;
import com.zykj.hunqianshiai.net.UrlContent;
import com.zykj.hunqianshiai.tools.JsonUtils;
import com.zykj.hunqianshiai.tools.SPUtils;
import com.zykj.hunqianshiai.tools.TextUtil;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

public class LoginActivity extends BasesActivity implements BaseView<String>, PlatformActionListener {

    @Bind(R.id.et_phone_number)
    EditText phoneNumber;
    @Bind(R.id.et_password)
    EditText password;
    private BasePresenterImpl mPresenter;
    private Platform platform;
    private int type;
    private LocationManager mLocationManager;
    private String locationProvider;
    private Location mLocation;
    private double mLongitude =118.34014333333334;
    private double mLatitude = 35.106123333333336;

    @Override
    protected int getContentViewX() {
        return R.layout.activity_login;
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void initView() {
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

        mPresenter = new BasePresenterImpl(this, new BaseModelImpl());
    }

    @OnClick({R.id.tv_forget_password, R.id.tv_login, R.id.tv_register, R.id.iv_weixin, R.id.iv_qq, R.id.iv_weibo})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_login://登录

                String phone = phoneNumber.getText().toString().trim();
                if (!TextUtil.isMobile(phone)) {
                    toastShow("手机号码不正确");
                    return;
                }
                String trim = password.getText().toString().trim();
                if (!TextUtil.isPasswordLengthLegal(trim)) {
                    toastShow("密码不正确");
                    return;
                }
                showDialog();
                mParams.clear();
                mParams.put("phone", phone);
                mParams.put("pass", trim);
                mPresenter.getData(UrlContent.LOGIN_PHONE_URL, mParams, BaseModel.DEFAULT_TYPE);

                break;
            case R.id.tv_register:
                openActivity(RegisterActivity.class);
                break;
            case R.id.tv_forget_password:
                openActivity(ForgetPasswordActivity.class);
                break;
            case R.id.iv_weixin:
                type = 2;
                platform = ShareSDK.getPlatform(Wechat.NAME);
                platform.SSOSetting(false);  //设置false表示使用SSO授权方式
                platform.setPlatformActionListener(this); // 设置分享事件回调
                platform.authorize();//单独授权
                break;
            case R.id.iv_qq:
                type = 1;
                platform = ShareSDK.getPlatform(QQ.NAME);
                platform.SSOSetting(false);  //设置false表示使用SSO授权方式
                platform.setPlatformActionListener(this); // 设置分享事件回调
                platform.authorize();//单独授权
                break;
            case R.id.iv_weibo:
                type = 3;
                platform = ShareSDK.getPlatform(SinaWeibo.NAME);
                platform.SSOSetting(false);  //设置false表示使用SSO授权方式
                platform.setPlatformActionListener(this); // 设置分享事件回调
                platform.authorize();//单独授权
                break;
        }
    }

    @Override
    public void success(String bean) {
        hideDialog();
        BaseBean baseBean = JsonUtils.GsonToBean(bean, BaseBean.class);
        if (baseBean.code == 200) {
            LoginBean loginBean = JsonUtils.GsonToBean(bean, LoginBean.class);
            UrlContent.USER_ID = loginBean.data;
            SPUtils.put(this, SPKey.USER_ID_KEY, loginBean.data);
            mParams.clear();
            mParams.put("userid", UrlContent.USER_ID);
            mPresenter.getData(UrlContent.USER_STATE_URL, mParams, BaseModel.REFRESH_TYPE);
        } else {
            toastShow(baseBean.message);
//            LoginBean loginBean = JsonUtils.GsonToBean(bean, LoginBean.class);
//            if (loginBean.data.equals("0")) {
//                toastShow(baseBean.message);
//            } else {
//                UrlContent.USER_ID = loginBean.data;
//                SPUtils.put(this, SPKey.USER_ID_KEY, loginBean.data);
//                mParams.clear();
//                mParams.put("userid", UrlContent.USER_ID);
//                mPresenter.getData(UrlContent.USER_STATE_URL, mParams, BaseModel.REFRESH_TYPE);
//                UrlContent.USER_ID = loginBean.data;
//                if (baseBean.message.equals("没交认证费!")) {
//                    UrlContent.USER_ID = loginBean.data;
//                    mParams.clear();
//                    mParams.put("userid", UrlContent.USER_ID);
//                    mPresenter.getData(UrlContent.USER_STATE_URL, mParams, BaseModel.REFRESH_TYPE);
//                } else if (baseBean.message.equals("登录成功")) {
//                    UrlContent.USER_ID = loginBean.data;
//                    SPUtils.put(this, SPKey.USER_ID_KEY, loginBean.data);
//                    mParams.clear();
//                    mParams.put("userid", UrlContent.USER_ID);
//                    mPresenter.getData(UrlContent.USER_STATE_URL, mParams, BaseModel.REFRESH_TYPE);
//
//                } else {
//                    toastShow(baseBean.message);
//                }
//            }

        }
    }

    @Override
    public void refresh(String bean) {

        UserStateBean userStateBean = JsonUtils.GsonToBean(bean, UserStateBean.class);
        UserStateBean.UserStateData data = userStateBean.data;
        if (data.isable.equals("1")) {
            toastShow("此号已经被封");
            return;
        }
        destroyApp();
        if (data.iscomplete.equals("0")) {
            openActivity(SetInfoActivity.class);
        } else {
            if (data.isauth.equals("3")) {
                openActivity(CertificationActivity.class);
            } else {
                if (data.is_shenfeizheng.equals("0")) {
                    openActivity(NameAuthenticationActivity.class);
                } else {
                    if (data.is_ren_lian.equals("0")) {
                        openActivity(FaceRecognitionActivity.class);
                    } else {
                        if (data.is_xingqu.equals("0")) {
                            openActivity(HobbyInterestActivity.class);
                        } else {
                            UrlContent.GOAGO = false;
                            openActivity(HomeMainActivity.class);
                            SPUtils.put(this, SPKey.COMPLETE_LOGIN_KEY, true);
                        }
                    }
                }
            }
        }
        finish();
    }

    @Override
    public void loadMore(String bean) {

    }

    @Override
    public void failed(String content) {

    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        if (platform.isAuthValid()) {
            String userId = platform.getDb().getUserId();
            String userGender = platform.getDb().getUserGender();
            String userName = platform.getDb().getUserName();
            String userIcon = platform.getDb().getUserIcon();
            String sexs;
            if (userGender.equals("m")) {
                sexs = "男";
            } else {
                sexs = "女";
            }
            thirdLogin(userName, userIcon, userId, sexs);
        } else {
            platform.setPlatformActionListener(this);
            platform.SSOSetting(false);
            platform.showUser(null);
        }
    }

    private void thirdLogin(String userName, String userIcon, String userId, String sexs) {
        mParams.clear();
        mParams.put("type", type);
        mParams.put("opend_id", userId);
        mParams.put("username", userName);
        mParams.put("headpic", userIcon);
        mParams.put("lng", mLongitude);
        mParams.put("lat", mLatitude);
        mPresenter.getData(UrlContent.THREE_LOGIN_URL, mParams, BaseModel.DEFAULT_TYPE);
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {

    }

    @Override
    public void onCancel(Platform platform, int i) {

    }
}
