package com.zykj.hunqianshiai.chat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
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
import com.zykj.hunqianshiai.home.my.info.PersonageInfoBean;
import com.zykj.hunqianshiai.net.UrlContent;
import com.zykj.hunqianshiai.tools.JsonUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.FileMessage;



public class ConversationActivity extends BasesActivity implements BaseView<String>{

    @Bind(R.id.iv_right_share)
    ImageView iv_right_share;
    private BasePresenterImpl mPresenter;

    /**
     * 整个Activity视图的根视图
     */
    View decorView;

    /**
     * 手指按下时的坐标
     */
    float downX, downY;

    /**
     * 手机屏幕的宽度和高度
     */
    float screenWidth, screenHeight;


    @Override
    protected int getContentViewX() {
        return R.layout.conversation;
    }

    @Override
    protected void initView() {

        // 获得decorView
        decorView = getWindow().getDecorView();

        // 获得手机屏幕的宽度和高度，单位像素
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;

        //ChatLinearLayout linearLayout = new ChatLinearLayout(ConversationActivity.this,screenWidth);

        String targetId = getIntent().getData().getQueryParameter("targetId");
        String titel = getIntent().getData().getQueryParameter("title");
        appBar(titel);

        mPresenter = new BasePresenterImpl(this, new BaseModelImpl());
        UrlContent.TARGET_ID = targetId;
        mParams.clear();
        mParams.put("userid", targetId);
        mPresenter.getData(UrlContent.GET_INFO_URL, mParams, BaseModel.DEFAULT_TYPE);
    }

    @Override
    public void success(String bean) {
        PersonageInfoBean personageInfoBean = JsonUtils.GsonToBean(bean, PersonageInfoBean.class);
        PersonageInfoBean.PersonageInfoData data = personageInfoBean.data;
        if (null==data.username) {
            toastShow("此用户已经注销");
            finish();
            return;
        }
        RongIM.getInstance().refreshUserInfoCache(new UserInfo(data.userid, data.username, Uri.parse(data.headpic)));
    }

    @Override
    public void refresh(String bean) {
        hideDialog();
        PersonageInfoBean personageInfoBean = JsonUtils.GsonToBean(bean, PersonageInfoBean.class);
        PersonageInfoBean.PersonageInfoData data = personageInfoBean.data;
        final String wx = data.wx;
        final PopupWindowWXNumber popupWindowWXNumber = new PopupWindowWXNumber(this, wx);
        backgroundAlpha(1f);
        popupWindowWXNumber.showAtLocation(iv_right_share, Gravity.CENTER, 0, 0);
        popupWindowWXNumber.setClickListener(new BasePopupWindow.ClickListener() {
            @Override
            public void onClickListener(Object object) {
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", wx);
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                // 将文本内容放到系统剪贴板里。
//                    cm.setText(fanKuiBean.url);
                toastShow("复制成功");
            }
        });
    }

    @Override
    public void loadMore(String bean) {
        BaseBean baseBean = JsonUtils.GsonToBean(bean, BaseBean.class);
        toastShow(baseBean.message);
        if (baseBean.code == 200) {
            RongIM.getInstance().addToBlacklist(UrlContent.TARGET_ID, new RongIMClient.OperationCallback() {
                @Override
                public void onSuccess() {
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                }
            });
            finish();
        }
    }

    @Override
    public void failed(String content) {

    }

    @OnClick({R.id.tv_jiaohuan, R.id.tv_yueta, R.id.tv_jujue})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_jiaohuan:
                showDialog();
                mParams.clear();
                mParams.put("userid", UrlContent.TARGET_ID);
                mPresenter.getData(UrlContent.GET_INFO_URL, mParams, BaseModel.REFRESH_TYPE);

//                mParams.clear();
//                mParams.put("rdm", UrlContent.RDM);
//                mParams.put("sign", UrlContent.SIGN);
//                mParams.put("userid", UrlContent.USER_ID);
//                OkGo.<String>post(UrlContent.IS_VIP_URL)
//                        .params(mParams)
//                        .execute(new StringCallback() {
//                            @Override
//                            public void onSuccess(Response<String> response) {
//                                BaseBean bean = JsonUtils.GsonToBean(response.body(), BaseBean.class);
//                                if (bean.code == 200) {
//                                    mParams.clear();
//                                    mParams.put("userid", UrlContent.USER_ID);
//                                    mPresenter.getData(UrlContent.GET_INFO_URL, mParams, BaseModel.REFRESH_TYPE);
//                                } else {
//                                    hideDialog();
//                                    PopupWindowVip popupWindowVip = new PopupWindowVip(ConversationActivity.this);
//                                    popupWindowVip.showAtLocation(iv_right_share, Gravity.CENTER, 0, 0);
//                                }
//                            }
//                        });
                break;
            case R.id.tv_yueta:
                showDialog();
                mParams.clear();
                mParams.put("rdm", UrlContent.RDM);
                mParams.put("sign", UrlContent.SIGN);
                mParams.put("userid", UrlContent.USER_ID);
                mParams.put("type", 1);
                OkGo.<String>post(UrlContent.GET_STATE_URL)
                        .params(mParams)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                BaseBean bean = JsonUtils.GsonToBean(response.body(), BaseBean.class);
                                if (bean.code == 200) {
                                    mParams.clear();
                                    mParams.put("userid", UrlContent.USER_ID);
                                    mParams.put("other_id",UrlContent.TARGET_ID);
                                    mParams.put("rdm", UrlContent.RDM);
                                    mParams.put("sign", UrlContent.SIGN);
                                    OkGo.<String>post(UrlContent.HELP_ME_URL)
                                            .params(mParams)
                                            .execute(new StringCallback() {
                                                @Override
                                                public void onSuccess(Response<String> response) {
                                                    hideDialog();
                                                    BaseBean baseBean = JsonUtils.GsonToBean(response.body(), BaseBean.class);
                                                    toastShow(baseBean.message);
                                                }
                                            });
//                                    mPresenter.getData(UrlContent.HELP_ME_URL, mParams, BaseModel.REFRESH_TYPE);
                                } else {
                                    hideDialog();
                                    PopupWindowAssistant popupWindowAssistant = new PopupWindowAssistant(ConversationActivity.this);
                                    backgroundAlpha(1f);
                                    popupWindowAssistant.showAtLocation(iv_right_share, Gravity.CENTER, 0, 0);
                                }
                            }
                        });

                break;
            case R.id.tv_jujue://黑名单
                mParams.clear();
                mParams.put("rdm", UrlContent.RDM);
                mParams.put("sign", UrlContent.SIGN);
                mParams.put("userid", UrlContent.USER_ID);
                OkGo.<String>post(UrlContent.IS_VIP_URL)
                        .params(mParams)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                BaseBean bean = JsonUtils.GsonToBean(response.body(), BaseBean.class);
                                if (bean.code == 200) {
                                    PopupWindowHei popupWindowVip = new PopupWindowHei(ConversationActivity.this);
                                    backgroundAlpha(1f);
                                    popupWindowVip.showAtLocation(iv_right_share, Gravity.CENTER, 0, 0);
                                    popupWindowVip.setClickListener(new BasePopupWindow.ClickListener() {
                                        @Override
                                        public void onClickListener(Object object) {
                                            mParams.clear();
                                            mParams.put("userid", UrlContent.USER_ID);
                                            mParams.put("ids", UrlContent.TARGET_ID);
                                            mPresenter.getData(UrlContent.ADD_BLACKLIST_URL, mParams, BaseModel.LOADING_MORE_TYPE);

                                        }
                                    });
                                } else {
                                    PopupWindowVip popupWindowVip = new PopupWindowVip(ConversationActivity.this);
                                    backgroundAlpha(1f);
                                    popupWindowVip.showAtLocation(iv_right_share, Gravity.CENTER, 0, 0);
                                }
                            }
                        });
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    List<LocalMedia> localMedia = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    String compressPath = localMedia.get(0).getPath();
//                    ImageMessage obtain = ImageMessage.obtain(Uri.parse(compressPath), Uri.parse(compressPath));
                    FileMessage fileMessage = FileMessage.obtain(Uri.parse("file://" + compressPath));
//                    fileMessage.setFileUrl(Uri.parse(compressPath));
                    Message message = Message.obtain(UrlContent.TARGET_ID, Conversation.ConversationType.PRIVATE, fileMessage);
                    RongIM.getInstance().sendMediaMessage(message, "[视频]", "", new IRongCallback.ISendMediaMessageCallback() {
                        @Override
                        public void onProgress(Message message, int i) {

                        }

                        @Override
                        public void onCanceled(Message message) {

                        }

                        @Override
                        public void onAttached(Message message) {

                        }

                        @Override
                        public void onSuccess(Message message) {

                        }

                        @Override
                        public void onError(Message message, RongIMClient.ErrorCode errorCode) {

                        }
                    });
                    break;
            }
        }
    }



    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){// 当按下时
            // 获得按下时的X坐标
            downX = event.getX();

        }else if(event.getAction() == MotionEvent.ACTION_MOVE){// 当手指滑动时
            // 获得滑过的距离
            float moveDistanceX = event.getX() - downX;
            if(moveDistanceX > 0){// 如果是向右滑动
                decorView.setX(moveDistanceX); // 设置界面的X到滑动到的位置
            }else {
                return false;
            }

        }else if(event.getAction() == MotionEvent.ACTION_UP){// 当抬起手指时
            // 获得滑过的距离
            float moveDistanceX =  event.getX() - downX;
            if(moveDistanceX > screenWidth / 2){
                // 如果滑动的距离超过了手机屏幕的一半, 结束当前Activity
                continueMove(moveDistanceX);
            }else if(moveDistanceX > 0){ // 如果滑动距离没有超过一半
                // 恢复初始状态
                rebackToLeft(moveDistanceX);
            }
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * 从当前位置一直往右滑动到消失。
     * 这里使用了属性动画。
     */
    private void continueMove(float moveDistanceX){
        // 从当前位置移动到右侧。
        ValueAnimator anim = ValueAnimator.ofFloat(moveDistanceX, screenWidth);
        anim.setDuration(500); // 一秒的时间结束, 为了简单这里固定为1秒
        anim.start();

        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 位移
                float x = (float) (animation.getAnimatedValue());
                decorView.setX(x);
            }
        });

        // 动画结束时结束当前Activity
        anim.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                finish();
            }

        });
    }

    /**
     * Activity被滑动到中途时，滑回去~
     */
    private void rebackToLeft(float moveDistanceX){
        ObjectAnimator.ofFloat(decorView, "X", moveDistanceX, 0).setDuration(300).start();
    }

    /**
     * 设置添加屏幕的背景透明度
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

}
