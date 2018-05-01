package com.zykj.hunqianshiai.home.my.set;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;

import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;
import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BasePopupWindow;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.intro.ChooseLoginActivity;
import com.zykj.hunqianshiai.net.SPKey;
import com.zykj.hunqianshiai.net.UrlContent;
import com.zykj.hunqianshiai.tools.SPUtils;

import butterknife.Bind;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

public class SetActivity extends BasesActivity implements CompoundButton.OnCheckedChangeListener {

    @Bind(R.id.switch_message)
    SwitchCompat mSwitchCompat;
    private Bundle mBundle;
    private String mMa;

    @Override
    protected int getContentViewX() {
        return R.layout.activity_set;
    }

    @Override
    protected void initView() {
        appBar("设置");

        mBundle = getIntent().getExtras();
        mMa = mBundle.getString("ma");
        boolean push_message = (boolean) SPUtils.get(this, SPKey.PUSH_MESSAGE_KEY, true);
        mSwitchCompat.setChecked(push_message);
        mSwitchCompat.setOnCheckedChangeListener(this);
    }

    @OnClick({R.id.ll_help, R.id.ll_blacklist, R.id.ll_update, R.id.ll_logout, R.id.ll_cache, R.id.ll_criterion, R.id.ll_share, R.id.ll_info, R.id.tv_quit})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.ll_help://帮助
                mBundle.clear();
                mBundle.putString("title", "帮助与反馈");
                mBundle.putString("url", "http://47.104.91.22/api.php?c=Fell&a=bangzhufankui");
                openActivity(FanKuiActivity.class, mBundle);
                break;
            case R.id.ll_blacklist://黑名单
                openActivity(BlacklistActivity.class);
                break;
            case R.id.ll_update://检查更新

                PgyUpdateManager.register(this, new UpdateManagerListener() {

                    @Override
                    public void onUpdateAvailable(final String result) {

                        // 将新版本信息封装到AppBean中
                        final AppBean appBean = getAppBeanFromString(result);
                        new AlertDialog.Builder(SetActivity.this)
                                .setTitle("更新")
                                .setMessage("发现新版本，请更新后继续使用！")
                                .setNegativeButton("确定", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(
                                            DialogInterface dialog,
                                            int which) {
                                        startDownloadTask(SetActivity.this, appBean.getDownloadURL());
                                    }
                                }).show();
                    }

                    @Override
                    public void onNoUpdateAvailable() {
                        toastShow("已经是最新版本");
                    }
                });
                break;
            case R.id.ll_logout://注销资料
                openActivity(LogoutActivity.class);
                break;
            case R.id.ll_cache://清除缓存
                PopupWindowCache popupWindowCache = new PopupWindowCache(this);
                popupWindowCache.showAtLocation(mSwitchCompat, Gravity.CENTER, 0, 0);
                break;
            case R.id.ll_criterion://规范
                mBundle.clear();
                mBundle.putString("url", "http://47.104.91.22/api.php?m=Api&c=Fell&a=bangzhu2&bangzhuid=2");
                mBundle.putString("title","友善度规范");
                openActivity(WebsActivity.class, mBundle);
                break;
            case R.id.ll_share://分享
                mBundle.clear();
                mBundle.putString("title", "分享");
                mBundle.putString("url", "http://47.104.91.22/api.php?c=Fell&a=fenxiang&comeuserid=" + mMa + "&userid=" + UrlContent.USER_ID);
                openActivity(ShareActivity.class, mBundle);
                break;
            case R.id.ll_info://关于我们
                mBundle.clear();
                mBundle.putString("url", "http://47.104.91.22/api.php?m=Api&c=Fell&a=bangzhu2&bangzhuid=1");
                mBundle.putString("title","关于我们");
                openActivity(WebsActivity.class, mBundle);
                break;
            case R.id.tv_quit://退出
                PopupWindowQuit popupWindowQuit = new PopupWindowQuit(this, "确定退出吗？");
                popupWindowQuit.showAtLocation(mSwitchCompat, Gravity.CENTER, 0, 0);
                popupWindowQuit.setClickListener(new BasePopupWindow.ClickListener() {
                    @Override
                    public void onClickListener(Object object) {
                        //destroyApp();
                        openActivity(ChooseLoginActivity.class);
                        SPUtils.remove(SetActivity.this, SPKey.COMPLETE_LOGIN_KEY);
                        SPUtils.remove(SetActivity.this, SPKey.USER_ID_KEY);
                        finish();
                    }
                });

                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.switch_message:
                SPUtils.put(this, SPKey.PUSH_MESSAGE_KEY, b);
                if (b) {
                    JPushInterface.resumePush(this);
                } else {
                    JPushInterface.stopPush(this);
                }
                break;
        }
    }
}
