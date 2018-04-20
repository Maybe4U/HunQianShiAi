package com.zykj.hunqianshiai.intro;

import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.home.HomeMainActivity;
import com.zykj.hunqianshiai.net.SPKey;
import com.zykj.hunqianshiai.net.UrlContent;
import com.zykj.hunqianshiai.tools.JsonUtils;
import com.zykj.hunqianshiai.tools.Md5Utils;
import com.zykj.hunqianshiai.tools.SPUtils;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;

/**
 * 闪屏页
 */
public class SplashActivity extends BasesActivity {

    private Subscription mSubscription;

    @Override
    protected int getContentViewX() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {

        setDataListener(new DataListener() {
            @Override
            public void setOnDataListener(String json) {
                GetIpBean getIpBean = JsonUtils.GsonToBean(json, GetIpBean.class);
                if (200 == getIpBean.code) {
                    UrlContent.RDM = Md5Utils.md5(getIpBean.data);
                    UrlContent.SIGN = Md5Utils.md5(UrlContent.KEY, 1);

                    mSubscription = Observable.timer(2000, TimeUnit.MILLISECONDS).subscribe(new Action1<Long>() {
                        @Override
                        public void call(Long aLong) {
                            boolean first_login = (boolean) SPUtils.get(SplashActivity.this, SPKey.FIRST_LOGIN_KEY, true);
                            if (first_login) {//第一次进入闪屏页
                                openActivity(IntroActivity.class);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);//这部分代码是切换Activity时的动画，看起来就不会很生硬
                            } else {
                                boolean complete_login = (boolean) SPUtils.get(SplashActivity.this, SPKey.COMPLETE_LOGIN_KEY, false);
                                if (complete_login) {//是否已经完成注册
                                    UrlContent.USER_ID = (String) SPUtils.get(SplashActivity.this, SPKey.USER_ID_KEY, "");
                                    openActivity(HomeMainActivity.class);

                                } else {
                                    openActivity(ChooseLoginActivity.class);
                                }

                            }
                            finish();
                        }
                    });
                }
            }
        });
        okGo(UrlContent.FIRST_URL);
    }

    @Override
    protected void onDestroy() {
        if (null != mSubscription && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
        super.onDestroy();
    }
}
