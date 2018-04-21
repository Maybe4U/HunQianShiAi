package com.zykj.hunqianshiai.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.activities.activity.ActivitiesBean;
import com.zykj.hunqianshiai.bases.BaseBean;
import com.zykj.hunqianshiai.bases.BaseModel;
import com.zykj.hunqianshiai.bases.BaseModelImpl;
import com.zykj.hunqianshiai.bases.BasePopupWindow;
import com.zykj.hunqianshiai.bases.BasePresenterImpl;
import com.zykj.hunqianshiai.bases.BaseView;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.chat.MyExtensionModule;
import com.zykj.hunqianshiai.home.authentication.AuthenticationActivity;
import com.zykj.hunqianshiai.home.authentication.PopupWindowRz;
import com.zykj.hunqianshiai.home.authentication.RecognitionActivity;
import com.zykj.hunqianshiai.home.dynamic.DynamicFragment;
import com.zykj.hunqianshiai.home.dynamic.my_dynamic.IssueDynamicActivity;
import com.zykj.hunqianshiai.home.home.HomeFragment;
import com.zykj.hunqianshiai.home.message.MessageFragment;
import com.zykj.hunqianshiai.home.my.MyFragment;
import com.zykj.hunqianshiai.home.region.RegionsFragment;
import com.zykj.hunqianshiai.login_register.login.UserStateBean;
import com.zykj.hunqianshiai.net.UrlContent;
import com.zykj.hunqianshiai.tools.JsonUtils;
import com.zykj.hunqianshiai.tools.TabButton;
import com.zykj.hunqianshiai.user_home.UserPageActivity;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.IExtensionModule;
import io.rong.imkit.RongExtensionManager;
import io.rong.imkit.RongIM;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;

public class HomeMainActivity extends BasesActivity implements RadioGroup.OnCheckedChangeListener, BaseView<String> {

    @Bind(R.id.tb_home)
    TabButton home;
    @Bind(R.id.tb_region)
    TabButton region;
    @Bind(R.id.tb_dynamic)
    TabButton dynamic;
    @Bind(R.id.tb_message)
    TabButton message;
    @Bind(R.id.tb_my)
    TabButton tb_my;
    @Bind(R.id.rg_tab)
    RadioGroup tab;
    @Bind(R.id.rb_tab5)
    TabButton rb_tab5;
    @Bind(R.id.iv_message_dot)
    ImageView iv_message_dot;

    private int click = 0;
    private HomeFragment mHomeFragment;
    private RegionsFragment mRegionFragment;
    private DynamicFragment mDynamicFragment;
    private MessageFragment mMessageFragment;
    private MyFragment mMyFragment;
    private BasePresenterImpl mPresenter;
    private Subscription mSubscribe;
    private Bundle mBundle;

    @Override
    protected int getContentViewX() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {

        mBundle = new Bundle();
        mPresenter = new BasePresenterImpl(this, new BaseModelImpl());

        /*==================修复附近等闪退=====================*/
        //对USER_ID进行判空，防止闪退
        if (!UrlContent.USER_ID.isEmpty()){
            JPushInterface.setAlias(this, Integer.valueOf(UrlContent.USER_ID), "alias_" + UrlContent.USER_ID);
        }

        mParams.clear();
        mParams.put("userid", UrlContent.USER_ID);
        mParams.put("flag", 1);
        mPresenter.getData(UrlContent.GET_TOKEN_URL, mParams, BaseModel.REFRESH_TYPE);

        mParams.clear();
        mParams.put("rdm", UrlContent.RDM);
        mParams.put("sign", UrlContent.SIGN);
        OkGo.<String>post(UrlContent.ACTIVITIES_POSTER_URL)
                .params(mParams)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        //                        toastShow(response.body());
                        ActivitiesBean activitiesBean = JsonUtils.GsonToBean(response.body(), ActivitiesBean.class);
                        List<ActivitiesBean.ActivitiesData> data1 = activitiesBean.data;
                        if (!data1.isEmpty()) {
                            PopupWindowActivities popupWindowActivities = new PopupWindowActivities(data1, HomeMainActivity.this);
                            popupWindowActivities.showAtLocation(tab, Gravity.CENTER, 0, 0);
                        }

                    }
                });
        //设置在线状态
        mSubscribe = Observable.timer(5, TimeUnit.SECONDS)
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        mParams.clear();
                        mParams.put("userid", UrlContent.USER_ID);
                        mParams.put("type", 1);
                        mPresenter.getData(UrlContent.SET_ONLINE_URL, mParams, BaseModel.DEFAULT_TYPE);

                        mParams.clear();
                        mParams.put("userid", UrlContent.USER_ID);
                        //                        mPresenter.getData(UrlContent.GET_INFO_URL, mParams, BaseModel.LOADING_MORE_TYPE);

                        mPresenter.getData(UrlContent.USER_STATE_URL, mParams, BaseModel.LOADING_MORE_TYPE);

                    }
                });
        tab.setOnCheckedChangeListener(this);
        home.performClick();


    }

    private void hideFragment(FragmentTransaction transaction) {
        if (null != mHomeFragment) {
            transaction.hide(mHomeFragment);
        }
        if (null != mRegionFragment) {
            transaction.hide(mRegionFragment);
        }
        if (null != mDynamicFragment) {
            transaction.hide(mDynamicFragment);
        }
        if (null != mMessageFragment) {
            transaction.hide(mMessageFragment);
        }
        if (null != mMyFragment) {
            transaction.hide(mMyFragment);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideFragment(transaction);
        click = 0;
        switch (i) {
            case R.id.tb_home://首页
                message.setChecked(false);
                if (null == mHomeFragment) {
                    mHomeFragment = HomeFragment.getInstance();
                    transaction.add(R.id.fl_layout, mHomeFragment);
                } else {
                    transaction.show(mHomeFragment);
                }

                break;
            case R.id.tb_region://活动
                message.setChecked(false);
                if (null == mRegionFragment) {
                    mRegionFragment = RegionsFragment.getInstance();
                    transaction.add(R.id.fl_layout, mRegionFragment);
                } else {
                    transaction.show(mRegionFragment);
                }
                break;
            case R.id.tb_dynamic://动态
                message.setChecked(false);
                if (null == mDynamicFragment) {
                    mDynamicFragment = DynamicFragment.getInstance();
                    transaction.add(R.id.fl_layout, mDynamicFragment);
                } else {
                    transaction.show(mDynamicFragment);
                }
                break;
            case R.id.rb_tab5:

                if (null == mMessageFragment) {
                    mMessageFragment = MessageFragment.getInstance();
                    transaction.add(R.id.fl_layout, mMessageFragment);
                } else {
                    transaction.show(mMessageFragment);
                }
                break;
            case R.id.tb_my:
                message.setChecked(false);
                if (null == mMyFragment) {
                    mMyFragment = MyFragment.getInstance();
                    transaction.add(R.id.fl_layout, mMyFragment);
                } else {
                    transaction.show(mMyFragment);
                }
                break;
        }
        transaction.commit();
    }

    @OnClick({R.id.tb_dynamic, R.id.tb_message})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tb_dynamic:
                click++;
                if (click > 1) {
                    //                    toastShow(dynamic.isChecked() + "");
                    openActivity(IssueDynamicActivity.class);
                }

                break;
            case R.id.tb_message://消息
                rb_tab5.performClick();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 创建退出对话框
            AlertDialog isExit = new AlertDialog.Builder(this).create();
            // 设置对话框标题
            isExit.setTitle("系统提示");
            // 设置对话框消息
            isExit.setMessage("确定要退出吗？");
            // 添加选择按钮并注册监听
            isExit.setButton("确定", listener);
            isExit.setButton2("取消", listener);
            // 显示对话框
            isExit.show();

        }

        return false;

    }

    /**
     * 监听对话框里面的button点击事件
     */
    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序/完善信息
                    finish();
                    break;
                case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void success(String bean) {

    }

    @Override
    public void refresh(String bean) {
        BaseBean baseBean = JsonUtils.GsonToBean(bean, BaseBean.class);
        //        toastShow(baseBean.message);
        if (200 == baseBean.code) {
            TokenBean tokenBean = JsonUtils.GsonToBean(bean, TokenBean.class);
            connect(tokenBean.data);
        }
    }

    @Override
    public void loadMore(String bean) {
        //        PersonageInfoBean personageInfoBean = JsonUtils.GsonToBean(bean, PersonageInfoBean.class);
        //        PersonageInfoBean.PersonageInfoData data = personageInfoBean.data;

        UserStateBean userStateBean = JsonUtils.GsonToBean(bean, UserStateBean.class);
        UserStateBean.UserStateData data = userStateBean.data;
        if (data.is_shenfeizheng.equals("0")) {
            PopupWindowRz popupWindowRz = new PopupWindowRz(this, "您还没有实名认证，是否去认证？");
            popupWindowRz.showAtLocation(tab, Gravity.CENTER, 0, 0);
            popupWindowRz.setClickListener(new BasePopupWindow.ClickListener() {
                @Override
                public void onClickListener(Object object) {
                    openActivity(AuthenticationActivity.class);
                }
            });

        } else {
            if (data.is_ren_lian.equals("0")) {
                PopupWindowRz popupWindowRz = new PopupWindowRz(this, "您还没有进行人脸识别，是否去识别？");
                popupWindowRz.showAtLocation(tab, Gravity.CENTER, 0, 0);
                popupWindowRz.setClickListener(new BasePopupWindow.ClickListener() {
                    @Override
                    public void onClickListener(Object object) {
                        openActivity(RecognitionActivity.class);
                    }
                });

            }
        }
    }

    @Override
    public void failed(String content) {

    }


    /**
     * <p>连接服务器，在整个应用程序全局，只需要调用一次，需在 {@link # init(Context)} 之后调用。</p>
     * <p>如果调用此接口遇到连接失败，SDK 会自动启动重连机制进行最多10次重连，分别是1, 2, 4, 8, 16, 32, 64, 128, 256, 512秒后。
     * 在这之后如果仍没有连接成功，还会在当检测到设备网络状态变化时再次进行重连。</p>
     *
     * @return RongIM  客户端核心类的实例。
     */
    private void connect(TokenBean.TokenData data) {
        String token = data.token;
        UrlContent.USER_NAME = data.username;
        UrlContent.USER_PIC = data.headpic;

        setConnect(token);


        RongIM.getInstance().setCurrentUserInfo(new UserInfo(data.userid, data.username, Uri.parse(data.headpic)));

        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {

            @Override
            public UserInfo getUserInfo(String userId) {

                return null;
            }

        }, true);

        RongIM.setOnReceiveMessageListener(new RongIMClient.OnReceiveMessageListener() {
            @Override
            public boolean onReceived(Message message, int i) {
                return false;
            }
        });
        setMyExtensionModule();

        /**
         * 设置会话界面操作的监听器。
         */
        RongIM.setConversationBehaviorListener(new RongIM.ConversationBehaviorListener() {
            @Override
            public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
                String userId = userInfo.getUserId();
                //                toastShow(userId);
                mBundle.clear();
                mBundle.putString("userid", userId);
                openActivity(UserPageActivity.class, mBundle);
                return false;
            }

            @Override
            public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
                return false;
            }

            @Override
            public boolean onMessageClick(Context context, View view, Message message) {
                return false;
            }

            @Override
            public boolean onMessageLinkClick(Context context, String s) {
                return false;
            }

            @Override
            public boolean onMessageLongClick(Context context, View view, Message message) {
                return false;
            }
        });
        RongIM.getInstance().addUnReadMessageCountChangedObserver(new IUnReadMessageObserver() {
            @Override
            public void onCountChanged(int i) {
                if (i == 0) {
                    iv_message_dot.setVisibility(View.GONE);
                } else {
                    iv_message_dot.setVisibility(View.VISIBLE);
                }
            }
        }, Conversation.ConversationType.PRIVATE);

    }

    private void setConnect(String token) {
        RongIM.connect(token, new RongIMClient.ConnectCallback() {

            /**
             * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
             *                  2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
             */
            @Override
            public void onTokenIncorrect() {
                //                toastShow("重新请求一个新的 Token");
                mParams.clear();
                mParams.put("userid", UrlContent.USER_ID);
                //                mPresenter.getData(UrlContent.REFRESH_TOKEN_URL, mParams, BaseModel.);
                mParams.put("rdm", UrlContent.RDM);
                mParams.put("sign", UrlContent.SIGN);
                OkGo.<String>post(UrlContent.REFRESH_TOKEN_URL)
                        .params(mParams)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                //                                toastShow(response.body());
                            }

                            @Override
                            public void onError(Response<String> response) {

                                super.onError(response);
                            }
                        });
            }

            /**
             * 连接融云成功
             * @param userid 当前 token 对应的用户 id
             */
            @Override
            public void onSuccess(String userid) {
                //toastShow("--onSuccess" + userid);
                //                    startActivity(new Intent(LoginActivity.this, HomeMainActivity.class));
                //                    finish();
                //                toastShow("连接融云成功");
            }

            /**
             * 连接融云失败
             * @param errorCode 错误码，可到官网 查看错误码对应的注释
             */
            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                toastShow("连接融云失败");
            }
        });
    }

    public void setMyExtensionModule() {
        List<IExtensionModule> moduleList = RongExtensionManager.getInstance().getExtensionModules();
        IExtensionModule defaultModule = null;
        if (moduleList != null) {
            for (IExtensionModule module : moduleList) {
                if (module instanceof DefaultExtensionModule) {
                    defaultModule = module;
                    break;
                }
            }
            if (defaultModule != null) {
                RongExtensionManager.getInstance().unregisterExtensionModule(defaultModule);
                RongExtensionManager.getInstance().registerExtensionModule(new MyExtensionModule());
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (null != mSubscribe && !mSubscribe.isUnsubscribed()) {
            mSubscribe.unsubscribe();
        }
        RongIM.getInstance().logout();
        super.onDestroy();
    }

}
