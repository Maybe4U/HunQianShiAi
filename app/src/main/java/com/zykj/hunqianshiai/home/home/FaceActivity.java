package com.zykj.hunqianshiai.home.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.Toast;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BaseBean;
import com.zykj.hunqianshiai.bases.BaseModel;
import com.zykj.hunqianshiai.bases.BaseModelImpl;
import com.zykj.hunqianshiai.bases.BasePopupWindow;
import com.zykj.hunqianshiai.bases.BasePresenterImpl;
import com.zykj.hunqianshiai.bases.BaseView;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.home.PopupWindowChatVip;
import com.zykj.hunqianshiai.home.region.RegionBean;
import com.zykj.hunqianshiai.login_register.UploadBean;
import com.zykj.hunqianshiai.net.UrlContent;
import com.zykj.hunqianshiai.tools.JsonUtils;
import com.zykj.hunqianshiai.tools.L;
import com.zykj.hunqianshiai.user_home.GiftBean;
import com.zykj.hunqianshiai.user_home.GiftPayActivity;
import com.zykj.hunqianshiai.user_home.PopupWindowGift;
import com.zykj.hunqianshiai.user_home.UserPageActivity;

import java.util.List;

import butterknife.Bind;
import io.rong.imkit.RongIM;

/**
 * 脸谱
 */
public class FaceActivity extends BasesActivity implements BaseView<String> {
    @Bind(R.id.web_view)
    ObservableWebView mWebView;
    private float x1;
    private float y1;
    private float x2;
    private float y2;
    private int p = 1;
    private BasePresenterImpl mPresenter;
    private String mData;
    private Bundle mBundle;
    private String mPic;

    @Override
    protected int getContentViewX() {
        return R.layout.activity_face;
    }


    @Override
    protected void initView() {
        appBar("人脸匹配");
        mBundle = getIntent().getExtras();
        mPic = mBundle.getString("pic");
        WebSettings settings = mWebView.getSettings();
        //设置加载进来的页面自适应手机屏幕
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setJavaScriptEnabled(true);
        mWebView.loadUrl(UrlContent.FACE_ALIKE_URL + "&userid=" + UrlContent.USER_ID + "&url=" + mPic + "&p=" + p);

        mPresenter = new BasePresenterImpl(this, new BaseModelImpl());
        mParams.clear();
        mParams.put("userid", UrlContent.USER_ID);
        mPresenter.getData(UrlContent.GET_BALANCE_URL, mParams, BaseModel.DEFAULT_TYPE);

        mWebView.setDefaultHandler(new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {

                final RegionBean regionBean = JsonUtils.GsonToBean(data, RegionBean.class);
                switch (regionBean.flg) {
                    case "1":

                        mParams.clear();
                        mParams.put("userid", UrlContent.USER_ID);
                        mParams.put("other_id", regionBean.userid);
                        mParams.put("rdm", UrlContent.RDM);
                        mParams.put("sign", UrlContent.SIGN);

                        OkGo.<String>post(UrlContent.CHAT_COUNT_URL)
                                .params(mParams)
                                .execute(new StringCallback() {
                                    @Override
                                    public void onSuccess(Response<String> response) {
                                        BaseBean baseBean = JsonUtils.GsonToBean(response.body(), BaseBean.class);
                                        if (baseBean.code == 200) {
                                            RongIM.getInstance().startPrivateChat(FaceActivity.this, regionBean.userid, regionBean.username);
                                        } else {
                                            PopupWindowChatVip popupWindowChatVip = new PopupWindowChatVip(FaceActivity.this);
                                            popupWindowChatVip.showAtLocation(mWebView, Gravity.CENTER, 0, 0);
                                        }
                                    }
                                });
                        break;
                    case "8":
                        final PopupWindowGift popupWindowGift = new PopupWindowGift(FaceActivity.this, mData);
                        popupWindowGift.showAtLocation(mWebView, Gravity.BOTTOM, 0, 0);
                        popupWindowGift.setClickListener(new BasePopupWindow.ClickListener() {
                            @Override
                            public void onClickListener(Object object) {
                                List<GiftBean.GiftData> mData = (List<GiftBean.GiftData>) object;
                                for (int i = 0; i < mData.size(); i++) {
                                    if (mData.get(i).select == 1) {
                                        GiftBean.GiftData giftData = mData.get(i);
                                        String id = giftData.id;
                                        String price = giftData.price;
                                        mBundle.clear();
                                        mBundle.putString("price", price);
                                        mBundle.putString("other_id", id);
                                        mBundle.putString("other_userid", regionBean.userid);
                                        openActivity(GiftPayActivity.class, mBundle);
                                        popupWindowGift.dismiss();
                                    }
                                }

                            }
                        });
                        break;
                    case "20":
                        mBundle.clear();
                        mBundle.putString("userid", regionBean.userid);
                        openActivity(UserPageActivity.class, mBundle);
                        break;
                }
            }
        });


//        mWebView.setOnScrollChangedCallback(new ObservableWebView.OnScrollChangedCallback() {
//            @Override
//            public void onScroll(int sx, int sy, int ex, int ey) {
//                L.e(sx + "   " + sy + "   " + ex + "   " + ey);
//            }
//        });
        mWebView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //继承了Activity的onTouchEvent方法，直接监听点击事件
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    //当手指按下的时候
                    x1 = motionEvent.getX();
                    y1 = motionEvent.getY();
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    //当手指离开的时候
                    x2 = motionEvent.getX();
                    y2 = motionEvent.getY();
                    if (x1 - x2 > 100) {
//                        Toast.makeText(FaceActivity.this, "向左滑", Toast.LENGTH_SHORT).show();
//                        mWebView.loadUrl(UrlContent.FACE_ALIKE_URL + "&userid=" + UrlContent.USER_ID);
                        p++;
                        mWebView.loadUrl(UrlContent.FACE_ALIKE_URL + "&userid=" + UrlContent.USER_ID + "&url=" + mPic + "&p=" + p);
                        return true;
                    }
//                    if(y1 - y2 > 50) {
//                        Toast.makeText(FaceActivity.this, "向上滑", Toast.LENGTH_SHORT).show();
//                    } else if(y2 - y1 > 50) {
//                        Toast.makeText(FaceActivity.this, "向下滑", Toast.LENGTH_SHORT).show();
//                    } else if(x1 - x2 > 50) {
//                        Toast.makeText(FaceActivity.this, "向左滑", Toast.LENGTH_SHORT).show();
//                    } else if(x2 - x1 > 50) {
//                        Toast.makeText(FaceActivity.this, "向右滑", Toast.LENGTH_SHORT).show();
//                    }
                }
                return false;
            }
        });
    }

    @Override
    public void success(String bean) {
        BaseBean baseBean = JsonUtils.GsonToBean(bean, BaseBean.class);
        if (baseBean.code != 200) {
            return;
        }
        UploadBean uploadBean = JsonUtils.GsonToBean(bean, UploadBean.class);
        mData = uploadBean.data;
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
}
