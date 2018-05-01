package com.zykj.hunqianshiai.bases;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.mob.MobSDK;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zykj.hunqianshiai.net.UrlContent;

import cn.jpush.android.api.JPushInterface;
import io.rong.callkit.RongCloudEvent;
import io.rong.imkit.RongIM;

/**
 * Created by xu on 2017/12/28.
 */

public class MyApplication extends MultiDexApplication {

    public static IWXAPI api;

    @Override
    public void onCreate() {
        super.onCreate();

        //初始化极光推送
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        //初始化融云  和融云消息的监听器
        RongIM.init(this);
        RongCloudEvent.init(this);

        // MOB  通过代码注册你的AppKey和AppSecret
        //MobSDK.init(this, "226a52e6f74e0", "eb2a1d829bbb5a20061f5aa54e04d814");
        MobSDK.init(this, "226a52e6f74e0", "eb2a1d829bbb5a20061f5aa54e04d814");
        api = WXAPIFactory.createWXAPI(this, UrlContent.WX_APP_ID, true);
        api.registerApp(UrlContent.WX_APP_ID);

//        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
//            @Override
//            public void uncaughtException(Thread thread, Throwable throwable) {
//                throwable.printStackTrace();
//            }
//        });
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


}
