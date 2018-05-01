package com.zykj.hunqianshiai.bases;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.net.UrlContent;
import com.zykj.hunqianshiai.onekeyshare.OnekeyShare;
import com.zykj.hunqianshiai.tools.CustomDialog;
import com.zykj.hunqianshiai.tools.JsonUtils;
import com.zykj.hunqianshiai.tools.T;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by ${xu} on 2017/10/12.
 */

public abstract class BasesActivity extends AppCompatActivity implements View.OnClickListener {

    public static RequestOptions mOptions;
    public static RequestOptions mCircleRequestOptions;
    private CustomDialog mDialog;
    public static HttpParams mParams;
    public static List<Activity> mActivityList;
    public static RequestOptions mOptionsX;

    protected abstract int getContentViewX();

    protected abstract void initView();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initMapView();
        setContentView(getContentViewX());
        ButterKnife.bind(this);

        if (null == mOptions) {
            mOptions = new RequestOptions()
                    .centerCrop()
                    .error(R.drawable.placeholder);
        }
        if (null == mCircleRequestOptions) {
            mCircleRequestOptions = RequestOptions.circleCropTransform()
                    .error(R.mipmap.logom);
        }

        if (null == mOptionsX) {
            mOptionsX = new RequestOptions()
                    .error(R.drawable.placeholder);
        }

        if (null == mParams) {
            mParams = new HttpParams();
        }

        if (null == mActivityList) {
            mActivityList = new ArrayList<>();
        }
        mActivityList.add(this);
        initView();
    }
    //初始化地图引擎
    protected void initMapView() {

    }

    public void destroyApp() {
        for (int i = 0; i < mActivityList.size(); i = 1) {
            Activity activity = mActivityList.get(0);
            if (null != activity) {
                activity.finish();
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        mActivityList.remove(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    public void openActivity(Class clazz) {
        openActivity(clazz, null);
    }

    public void openActivity(Class clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    public void openActivity(Class clazz, Bundle bundle, int requestCode) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent,requestCode);
    }

    public void appBar(String content) {
        ImageView back = findViewById(R.id.iv_back);
        TextView title = findViewById(R.id.tv_title);
        title.setText(content);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    public void toastShow(String content) {
        T.showShort(this, content);
    }

    public void glide(String url, ImageView imageView, RequestOptions options) {
        Glide.with(this)
                .load(url)
                .apply(options)
                .into(imageView);
    }

    public void okGo(String url) {
        OkGo.<String>get(url)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        BaseBean bean = JsonUtils.GsonToBean(response.body(), BaseBean.class);
                        if (bean.code == 200) {
//                            toastShow("成功");
                            if (null != mDataListener) {
                                mDataListener.setOnDataListener(response.body());
                            }
                        } else {
                            toastShow("失败");
                        }
                    }
                });
    }

    public DataListener mDataListener;

    public void setDataListener(DataListener dataListener) {
        this.mDataListener = dataListener;
    }

    public interface DataListener {
        void setOnDataListener(String json);
    }


    public void showDialog() {
        if (null == mDialog) {
            mDialog = new CustomDialog(this, R.style.CustomDialog);
        }
        mDialog.show();
    }

    public void hideDialog() {
        if (null != mDialog) {
            mDialog.dismiss();
            mDialog = null;
        }
    }



    public void showShare(String url, String content) {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不     调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("没有爱，我过不好这一生");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://47.104.91.22/api.php?c=Fell&a=saoma2&comeuserid="+ UrlContent.USER_ID);
        // text是分享文本，所有平台都需要这个字段
        oks.setText("首家实名制认证严肃婚恋社交平台");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        oks.setImagePath(url);//确保SDcard下面存在此张图片
        oks.setImageUrl(url);
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://47.104.91.22/api.php?c=Fell&a=saoma2&comeuserid="+ UrlContent.USER_ID);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment(getString(R.string.app_name));
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://47.104.91.22/api.php?c=Fell&a=saoma2&comeuserid="+ UrlContent.USER_ID);

        // 启动分享GUI
        oks.show(this);
    }


    public void showShare(String title, String text, String url, String actid) {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不     调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(title);
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://47.104.91.22/api.php?c=Fell&a=huodong&actid="+ actid + "&userid=" +  UrlContent.USER_ID);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(text);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //        oks.setImagePath(url);//确保SDcard下面存在此张图片
        oks.setImageUrl(url);
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://47.104.91.22/api.php?c=Fell&a=huodong&actid="+ actid + "&userid=" +  UrlContent.USER_ID);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment(getString(R.string.app_name));
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://47.104.91.22/api.php?c=Fell&a=huodong&actid="+ actid + "&userid=" +  UrlContent.USER_ID);

        // 启动分享GUI
        oks.show(this);
    }


    /*==================键盘隐藏相关  begin=====================*/
    //判断点击当前位置是不是TextView 否则自动隐藏软键盘
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);

            WindowManager manager = this.getWindowManager();
            DisplayMetrics outMetrics = new DisplayMetrics();
            manager.getDefaultDisplay().getMetrics(outMetrics);
            int width = outMetrics.widthPixels;
            int height = outMetrics.heightPixels;

            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    //right = left + v.getWidth();
                    right = left + width;
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    /*==================键盘隐藏相关  end=====================*/
}
