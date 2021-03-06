package com.zykj.hunqianshiai.user_home;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;
import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BaseBean;
import com.zykj.hunqianshiai.bases.BaseModel;
import com.zykj.hunqianshiai.bases.BaseModelImpl;
import com.zykj.hunqianshiai.bases.BasePopupWindow;
import com.zykj.hunqianshiai.bases.BasePresenterImpl;
import com.zykj.hunqianshiai.bases.BaseView;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.chat.PopupWindowVip;
import com.zykj.hunqianshiai.home.PopupWindowChatVip;
import com.zykj.hunqianshiai.home.my.InformUserActivity;
import com.zykj.hunqianshiai.home.my.info.HobbiesBean;
import com.zykj.hunqianshiai.home.my.info.PersonageInfoBean;
import com.zykj.hunqianshiai.home.my.info.SpouseStandardsBean;
import com.zykj.hunqianshiai.login_register.hobby_interest.HobbyInterestBean;
import com.zykj.hunqianshiai.look_pic_video.PicActivity;
import com.zykj.hunqianshiai.look_pic_video.VideoActivity;
import com.zykj.hunqianshiai.net.UrlContent;
import com.zykj.hunqianshiai.tools.JsonUtils;
import com.zykj.hunqianshiai.user_home.dynamic.UserDynamicActivity;
import com.zykj.hunqianshiai.user_home.personal_assistant.OpenCounselorActivity;
import com.zykj.hunqianshiai.user_home.personal_assistant.PersonalAssistantActivity;
import com.zykj.hunqianshiai.utils.ObservableScrollView;
import com.zykj.hunqianshiai.web.WebViewActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * 用户页
 */
public class UserPageActivity extends BasesActivity implements BaseView<String> {

    @Bind(R.id.sv_scroll)
    ObservableScrollView mScrollView;
    @Bind(R.id.iv_toolbar_color)
    TextView toolbarColor;
    @Bind(R.id.ll_layout)
    LinearLayout layout;
    @Bind(R.id.tv_family_information)
    TextView familyInformation;
    @Bind(R.id.tv_career_planning)
    TextView careerPlanning;
    @Bind(R.id.tv_affective_experience)
    TextView affectiveExperience;

    @Bind(R.id.iv_head)
    ImageView iv_head;
    @Bind(R.id.tv_like)
    TextView tv_like;
    @Bind(R.id.tv_name)
    TextView tv_name;
    @Bind(R.id.tv_age)
    TextView tv_age;
    @Bind(R.id.tv_lines)
    TextView tv_lines;
    @Bind(R.id.tv_id)
    TextView tv_id;
    @Bind(R.id.tv_marry)
    TextView tv_marry;
    @Bind(R.id.ll_remind)
    LinearLayout ll_remind;
    @Bind(R.id.tv_pic_number)
    TextView tv_pic_number;
    @Bind(R.id.recycler_pics)
    RecyclerView mRecyclerView;
    @Bind(R.id.tv_dynamic)
    TextView tv_dynamic;
    @Bind(R.id.ll_sfrz)
    LinearLayout ll_sfrz;
    @Bind(R.id.tv_sfrz)
    TextView tv_sfrz;
    @Bind(R.id.tv_rz_name)
    TextView tv_rz_name;
    @Bind(R.id.tv_rz_number)
    TextView tv_rz_number;
    @Bind(R.id.tv_rz_time)
    TextView tv_rz_time;
    @Bind(R.id.tv_xueli)
    TextView tv_xueli;
    @Bind(R.id.tv_xueli_auth)
    TextView tv_xueli_auth;
    @Bind(R.id.tv_car)
    TextView tv_car;
    @Bind(R.id.tv_car_auth)
    TextView tv_car_auth;
    @Bind(R.id.tv_house)
    TextView tv_house;
    @Bind(R.id.tv_huose_auth)
    TextView tv_huose_auth;
    @Bind(R.id.tv_meinfo)
    TextView tv_meinfo;
    @Bind(R.id.id_flowlayout)
    TagFlowLayout mTagFlowLayout;
    @Bind(R.id.id_flowlayout1)
    TagFlowLayout mTagFlowLayout1;
    @Bind(R.id.id_flowlayout2)
    TagFlowLayout mTagFlowLayout2;
    @Bind(R.id.recycler_tag)
    RecyclerView recycler_tag;
    @Bind(R.id.tv_online)
    TextView tv_online;
    @Bind(R.id.tv_heartveat)
    TextView tv_heartveat;
    @Bind(R.id.rl_video)
    RelativeLayout rl_video;
    @Bind(R.id.check_like)
    CheckBox check_like;
    @Bind(R.id.iv_play)
    ImageView mIvPlay;

    @Bind(R.id.tv_black)
    TextView tv_black;

    @Bind(R.id.rl_chat)
    RelativeLayout rl_chat;
    @Bind(R.id.iv_sfrz)
    ImageView mIvSfrz;
    @Bind(R.id.cb_xueli)
    CheckBox mCbXueli;
    @Bind(R.id.cb_car)
    CheckBox mCbCar;
    @Bind(R.id.cb_house)
    CheckBox mCbHouse;
    String[] hobbies = {"电影", "音乐", "书籍", "美食", "运动"};
    int[] images = {R.mipmap.dianying, R.mipmap.yinyue, R.mipmap.shuji, R.mipmap.meishi, R.mipmap.yundong};

    private Bundle mBundle;
    private HobbyInterestBean mHobbyInterestBean;
    private List<HobbyInterestBean.Label> mLabel;
    private UserHobbyInterestAdapter mHobbyInterestAdapter;
    private String mUserid;
    private BasePresenterImpl mPresenter;
    private String mId;
    private String mUsername;
    private String mBalance;
    private String mVideo1;
    private String mHeadpic;
    private int mLike_num;

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
    private View mView;
    private ImageView mIv_play;
    private AlertDialog.Builder mDialog;
    private String mString;
    private ImageView mIv_video;

    @Override
    protected int getContentViewX() {
        return R.layout.activity_user_page;
    }

    @Override
    protected void initView() {
        mBundle = getIntent().getExtras();
        mUserid = mBundle.getString("userid");
        mPresenter = new BasePresenterImpl(this, new BaseModelImpl());
        mParams.clear();
        mParams.put("userid", mUserid);
        mParams.put("user_id_self", UrlContent.USER_ID);
        mParams.put("comerid", UrlContent.USER_ID);
        mPresenter.getData(UrlContent.USERINFO_URL, mParams, BaseModel.DEFAULT_TYPE);
        mPresenter.getData(UrlContent.ADD_LOOK_URL, mParams, BaseModel.LOADING_MORE_TYPE);

        mScrollView.setOnScrollChangedListener(new ObservableScrollView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
                if (y >= 0 && y <= 500) {
                    float i = (float) (y * 1.0 / 500);
                    toolbarColor.setAlpha(i);
                } else if (y > 500) {
                    toolbarColor.setAlpha(1);
                }
                layout.setVisibility(View.GONE);
            }
        });


        // 获得decorView
        decorView = getWindow().getDecorView();

        // 获得手机屏幕的宽度和高度，单位像素
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;

        ObservableScrollView scrollView = new ObservableScrollView(UserPageActivity.this, screenWidth);
    }

    @OnClick({R.id.ll_user_dynamic, R.id.iv_back, R.id.tv_assistant, R.id.rl_user_info, R.id.iv_more, R.id.tv_share, R.id.tv_inform,
            R.id.tv_family_information, R.id.tv_career_planning, R.id.tv_affective_experience, R.id.iv_gift, R.id.rl_standard, R.id.ll_remind,
            R.id.rl_like, R.id.rl_chat, R.id.rl_video, R.id.tv_black})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_black://拉黑
                //showDialog();

                mString = tv_black.getText().toString();
                if (mString.equals("拉黑")) {
                    showConfirmDialog();
                } else {
                    showCancelDialog();
                }

                break;
            case R.id.rl_video:
                if (null != mVideo1 && !TextUtils.isEmpty(mVideo1)) {
                    mBundle.clear();
                    mBundle.putString("video", mVideo1);
                    openActivity(VideoActivity.class, mBundle);
                }
                break;
            case R.id.rl_chat:
                mParams.clear();//
                mParams.put("userid", UrlContent.USER_ID);
                mParams.put("other_id", mUserid);
                mParams.put("rdm", UrlContent.RDM);
                mParams.put("sign", UrlContent.SIGN);

                OkGo.<String>post(UrlContent.CHAT_COUNT_URL)
                        .params(mParams)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                BaseBean baseBean = JsonUtils.GsonToBean(response.body(), BaseBean.class);
                                if (baseBean.code == 200) {
                                    RongIM.getInstance().startPrivateChat(UserPageActivity.this, mUserid, mUsername);
                                } else {
                                    PopupWindowChatVip popupWindowChatVip = new PopupWindowChatVip(UserPageActivity.this);
                                    popupWindowChatVip.showAtLocation(tv_heartveat, Gravity.CENTER, 0, 0);
                                }
                            }
                        });

                break;
            case R.id.rl_like:
                mParams.clear();
                mParams.put("userid", UrlContent.USER_ID);
                mParams.put("uid", mUserid);
                mParams.put("rdm", UrlContent.RDM);
                mParams.put("sign", UrlContent.SIGN);

                int position = mBundle.getInt("position");
                if (tv_heartveat.getText().equals("心动")) {

                    OkGo.<String>post(UrlContent.ADD_LIKE_URL)
                            .params(mParams)
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(Response<String> response) {
                                    check_like.setChecked(true);
                                    mLike_num++;
                                    tv_like.setText(mLike_num + "");
                                    tv_heartveat.setText("已心动");
                                    toastShow("心动成功");
                                    heartBeatBroadcast(position);
                                }
                            });
                } else {
                    OkGo.<String>post(UrlContent.DELETE_LIKE_URL)
                            .params(mParams)
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(Response<String> response) {
                                    mLike_num--;
                                    tv_like.setText(mLike_num + "");
                                    check_like.setChecked(false);
                                    tv_heartveat.setText("心动");
                                    toastShow("取消心动");
                                    heartBeatBroadcast(position);
                                }
                            });
                }


                break;
            case R.id.ll_user_dynamic://Ta的动态
                mBundle.clear();
                mBundle.putString("userid", mId);
                openActivity(UserDynamicActivity.class, mBundle);
                break;
            case R.id.tv_assistant://婚恋助理

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
                                    mParams.put("other_id", UrlContent.TARGET_ID);
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
                                    openActivity(PersonalAssistantActivity.class);
                                }
                            }
                        });

                break;
            case R.id.rl_user_info://基本资料
                mBundle.clear();
                mBundle.putString("title", "基本资料");
                mBundle.putString("url", "http://47.104.91.22/api.php?c=Fell&a=jibenziliao&actid=13&userid=" + mUserid);
                openActivity(WebViewActivity.class, mBundle);
                //                openActivity(UserInfoActivity.class);
                break;
            case R.id.iv_more://显示更多
                if (layout.getVisibility() == View.GONE) {
                    layout.setVisibility(View.VISIBLE);
                } else {
                    layout.setVisibility(View.GONE);
                }
                break;
            case R.id.tv_share://分享
                layout.setVisibility(View.GONE);
                showShare(mHeadpic, mUsername);
                break;
            case R.id.tv_inform://举报
                layout.setVisibility(View.GONE);
                mBundle.clear();
                mBundle.putString("userid", mUserid);

                openActivity(InformUserActivity.class, mBundle);
                break;
            case R.id.tv_family_information://家庭情况
                if (familyInformation.getText().toString().equals("查看")) {
                    mBundle.clear();
                    mBundle.putString("title", "家庭情况");
                    mBundle.putString("url", "http://47.104.91.22/api.php?c=Fell&a=xiangqing_1&userid=" + mUserid + "&type=" + "1");
                    openActivity(WebViewActivity.class, mBundle);
                }
                break;
            case R.id.tv_career_planning://职业规划
                if (careerPlanning.getText().toString().equals("查看")) {
                    mBundle.clear();
                    mBundle.putString("title", "职业规划");
                    mBundle.putString("url", "http://47.104.91.22/api.php?c=Fell&a=xiangqing_1&userid=" + mUserid + "&type=" + "2");
                    openActivity(WebViewActivity.class, mBundle);
                }
                break;
            case R.id.tv_affective_experience://感情经历
                if (affectiveExperience.getText().toString().equals("查看")) {
                    mBundle.clear();
                    mBundle.putString("title", "感情经历");
                    mBundle.putString("url", "http://47.104.91.22/api.php?c=Fell&a=xiangqing_1&userid=" + mUserid + "&type=" + "3");
                    openActivity(WebViewActivity.class, mBundle);
                    //                    openActivity(UserMessageActivity.class);
                }
                break;
            case R.id.iv_gift://礼物
                final PopupWindowGift popupWindowGift = new PopupWindowGift(this, mBalance);
                backgroundAlpha(1f);
                popupWindowGift.showAtLocation(affectiveExperience, Gravity.BOTTOM, 0, 0);
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
                                mBundle.putString("other_userid", mUserid);
                                openActivity(GiftPayActivity.class, mBundle);
                                popupWindowGift.dismiss();
                            }
                        }

                    }
                });
                break;

            case R.id.rl_standard: //择偶标准
                mBundle.clear();
                mBundle.putString("title", "择偶标准");
                mBundle.putString("url", "http://47.104.91.22/api.php?c=Fell&a=zeoubiaozhun&actid=13&userid=" + mUserid);
                openActivity(WebViewActivity.class, mBundle);
                //                openActivity(StandardActivity.class);
                break;
            case R.id.ll_remind://开通/添加 上线提醒
                String s = tv_online.getText().toString();
                if (s.equals("开通上线提醒")) {
                    mBundle.clear();
                    mBundle.putString("title", "上线提醒");
                    mBundle.putString("type1", "4");
                    mBundle.putString("type2", "3");
                    mBundle.putString("type3", "8");
                    openActivity(OpenCounselorActivity.class, mBundle);
                } else {
                    mParams.clear();
                    mParams.put("userid", UrlContent.USER_ID);
                    mParams.put("other_id", mUserid);
                    mPresenter.getData(UrlContent.ONLINE_REMIND_URL, mParams, BaseModel.REFRESH_TYPE);
                }
                break;
            default:
                break;
        }
    }

    private void showConfirmDialog() {
        mDialog = new AlertDialog.Builder(UserPageActivity.this);
        mDialog.setTitle("提示");
        mDialog.setMessage("确定要将TA加入黑名单吗？");
        mDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        intoBlacklist();
                    }
                });
        mDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        // 显示
        mDialog.show();
    }

    private void showCancelDialog() {
        mDialog = new AlertDialog.Builder(UserPageActivity.this);
        mDialog.setTitle("提示");
        mDialog.setMessage("确定要将TA解除黑名单吗？");
        mDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        intoBlacklist();
                    }
                });
        mDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        // 显示
        mDialog.show();
    }

    private void intoBlacklist() {

        if (mString.equals("拉黑")) {
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
                                mParams.clear();
                                mParams.put("userid", UrlContent.USER_ID);
                                mParams.put("ids", mUserid);
                                mParams.put("rdm", UrlContent.RDM);
                                mParams.put("sign", UrlContent.SIGN);
                                OkGo.<String>post(UrlContent.ADD_BLACKLIST_URL)
                                        .params(mParams)
                                        .execute(new StringCallback() {
                                            @Override
                                            public void onSuccess(Response<String> response) {
                                                BaseBean baseBean = JsonUtils.GsonToBean(response.body(), BaseBean.class);
                                                //                                                        toastShow(baseBean.message);
                                                if (baseBean.code == 200) {
                                                    RongIM.getInstance().addToBlacklist(mUserid, new RongIMClient.OperationCallback() {
                                                        @Override
                                                        public void onSuccess() {
                                                            //hideDialog();
                                                            runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    tv_black.setText("已拉黑");
                                                                    toastShow("加入黑名单成功");
                                                                }
                                                            });

                                                        }

                                                        @Override
                                                        public void onError(RongIMClient.ErrorCode errorCode) {
                                                            hideDialog();
                                                        }
                                                    });
                                                }

                                                //                                            mPresenter.getData(UrlContent.ADD_BLACKLIST_URL, mParams, BaseModel.LOADING_MORE_TYPE);

                                            }
                                        });
                            } else {
                                //hideDialog();
                                PopupWindowVip popupWindowVip = new PopupWindowVip(UserPageActivity.this);
                                backgroundAlpha(1f);
                                popupWindowVip.showAtLocation(toolbarColor, Gravity.CENTER, 0, 0);
                            }
                        }
                    });
        } else {
            //hideDialog();
            RongIM.getInstance().removeFromBlacklist(mUserid, new RongIMClient.OperationCallback() {
                @Override
                public void onSuccess() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_black.setText("拉黑");
                            toastShow("解除黑名单成功");
                        }
                    });
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                }
            });
            mParams.clear();
            mParams.put("userid", UrlContent.USER_ID);
            mParams.put("ids", mUserid);
            mParams.put("rdm", UrlContent.RDM);
            mParams.put("sign", UrlContent.SIGN);
            OkGo.<String>post(UrlContent.REMOVE_BLACKLIST_URL)
                    .params(mParams)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {

                        }
                    });

        }
    }

    @Override
    public void success(String bean) {
        BaseBean baseBean = JsonUtils.GsonToBean(bean, BaseBean.class);
        if (200 != baseBean.code) {
            toastShow(baseBean.message);
            return;
        }

        UserPageBean userPageBean = JsonUtils.GsonToBean(bean, UserPageBean.class);
        final UserPageBean.UserPageData data = userPageBean.data;

        if (null != data.is_blacklist) {
            if (data.is_blacklist.equals("0")) {
                tv_black.setText("拉黑");
            } else {
                tv_black.setText("已拉黑");
            }
        }
        mVideo1 = data.video;
        /*====================优化===================*/
        //限制自己和自己聊天
        if (UrlContent.USER_ID.equals(mUserid)) {
            rl_chat.setVisibility(View.GONE);
        }
        mView = View.inflate(UserPageActivity.this, R.layout.video_pic, null);
        mIv_play = mView.findViewById(R.id.iv_play);
        if (!TextUtils.isEmpty(mVideo1)) {
            ImageView video = mView.findViewById(R.id.iv_video);
            Glide.with(UserPageActivity.this)
                    .load(UrlContent.PIC_URL + data.video)
                    .apply(BasesActivity.mOptions)
                    .into(iv_head);
            mIvPlay.setVisibility(View.VISIBLE);
        } else {
            mView = null;
            mHeadpic = data.headpic;
            glide(mHeadpic, iv_head, mOptions);
            mIvPlay.setVisibility(View.GONE);
        }

        iv_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBundle.clear();
                mBundle.putString("video", mVideo1);
                openActivity(VideoActivity.class, mBundle);
            }
        });

        mLike_num = data.like_num;
        tv_like.setText(mLike_num + "");
        mUsername = data.username;
        tv_name.setText(mUsername);
        if (!TextUtils.isEmpty(data.age)) {
            tv_age.setText(data.age + "岁");
        }
        if (data.isonline.equals("0")) {
            tv_lines.setText("离线");
        } else {
            tv_lines.setText("在线");
        }

        mBalance = data.balance;


        if (null != mVideo1 && !TextUtils.isEmpty(mVideo1)) {
            rl_video.setVisibility(View.VISIBLE);
        } else {
            rl_video.setVisibility(View.GONE);
        }

        mId = data.userid;
        tv_id.setText("ID: " + mId);
        if (null != data.marrytime && !TextUtils.isEmpty(data.marrytime)) {
            tv_marry.setText(data.areaname + " | 期望" + data.marrytime + "结婚");
        } else {
            tv_marry.setText(data.areaname);
        }

        tv_pic_number.setText("(" + data.pic_num + ")");
        if (data.is_like.equals("1")) {
            check_like.setChecked(true);
            tv_heartveat.setText("已心动");
        } else {
            check_like.setChecked(false);
            tv_heartveat.setText("心动");
        }

        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(layoutManager);
        List<String> pic = data.pic;
        if (null != pic) {
            if (pic.size() > 6) {
                pic = pic.subList(0, 6);
            }
            PicAdapter picAdapter = new PicAdapter(pic);
            mRecyclerView.setAdapter(picAdapter);
            picAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    mBundle.clear();
                    mBundle.putSerializable("images", (Serializable) data.pic);
                    mBundle.putInt("position", position);
                    openActivity(PicActivity.class, mBundle);
                }
            });
        }


        tv_dynamic.setText(data.comment_num);

        PersonageInfoBean.RenZheng renzheng = data.renzheng;

        tv_sfrz.setText(renzheng.shenfen_auth);
        if (renzheng.shenfen_auth2.equals("1")) {
            mIvSfrz.setImageResource(R.mipmap.ge_shenfenrenzheng);
            tv_sfrz.setTextColor(ContextCompat.getColor(this, R.color.default_color));
        } else {
            tv_sfrz.setTextColor(ContextCompat.getColor(this, R.color.black));
        }
        String realname = renzheng.realname;
        if (!TextUtils.isEmpty(realname)) {
            String substring = realname.substring(0, 1);
            substring = substring + "**";
            tv_rz_name.setText(substring);
        }

        String card_number = renzheng.card_number;
        if (!TextUtils.isEmpty(card_number)) {
            String substring = card_number.substring(0, 10);
            substring = substring + "*****";
            tv_rz_number.setText(substring);
        }

        if (!TextUtils.isEmpty(renzheng.verifytime)) {
            tv_rz_time.setText("认证通过时间：" + renzheng.verifytime);
        }

        tv_xueli.setText(renzheng.xueli);
        tv_xueli_auth.setText(renzheng.xueli_auth);
        if (renzheng.xueli_auth2.equals("1")) {
            mCbXueli.setChecked(true);
            tv_xueli.setTextColor(ContextCompat.getColor(this, R.color.default_color));
            tv_xueli_auth.setTextColor(ContextCompat.getColor(this, R.color.default_color));
        } else {
            mCbXueli.setChecked(false);
            tv_xueli.setTextColor(ContextCompat.getColor(this, R.color.black));
            tv_xueli_auth.setTextColor(ContextCompat.getColor(this, R.color.black));
        }

        tv_car.setText(renzheng.car);
        tv_car_auth.setText(renzheng.car_auth);
        if (renzheng.car_auth2.equals("1")) {
            mCbCar.setChecked(true);
            tv_car.setTextColor(ContextCompat.getColor(this, R.color.default_color));
            tv_car_auth.setTextColor(ContextCompat.getColor(this, R.color.default_color));
        } else {
            mCbCar.setChecked(false);
            tv_car.setTextColor(ContextCompat.getColor(this, R.color.black));
            tv_car_auth.setTextColor(ContextCompat.getColor(this, R.color.black));
        }

        tv_house.setText(renzheng.house);
        tv_huose_auth.setText(renzheng.huose_auth);
        if (renzheng.huose_auth2.equals("1")) {
            mCbHouse.setChecked(true);
            tv_house.setTextColor(ContextCompat.getColor(this, R.color.default_color));
            tv_huose_auth.setTextColor(ContextCompat.getColor(this, R.color.default_color));
        } else {
            mCbHouse.setChecked(false);
            tv_house.setTextColor(ContextCompat.getColor(this, R.color.black));
            tv_huose_auth.setTextColor(ContextCompat.getColor(this, R.color.black));
        }
        tv_meinfo.setText("自我介绍： " + data.meinfo);


        final LayoutInflater mInflater = LayoutInflater.from(this);
        ArrayList<String> strings = new ArrayList<>();
        if (!TextUtils.isEmpty(data.age)) {
            strings.add(data.age + "岁");
        }
        if (!TextUtils.isEmpty(data.height)) {
            strings.add(data.height + "cm");
        }
        if (!TextUtils.isEmpty(data.household)) {
            strings.add("户籍（老家）：" + data.household);
        }
        if (!TextUtils.isEmpty(data.yearmoney)) {
            strings.add(data.yearmoney);
        }
        if (!TextUtils.isEmpty(data.marrytime)) {
            strings.add(data.marrytime);
        }
        if (!TextUtils.isEmpty(data.areaname)) {
            strings.add(data.areaname);
        }
        if (!TextUtils.isEmpty(data.marryinfo)) {
            strings.add(data.marryinfo);
        }

        UserPageBean.Auth auth = data.auth;
        if (auth.code == 1) {
            if (data.self_shangxiantixing_state.equals("1")) {
                ll_remind.setVisibility(View.GONE);

            } else {
                tv_online.setText("加入上线提醒");
            }
        }

        mTagFlowLayout.setAdapter(new TagAdapter<String>(strings) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.tv, mTagFlowLayout, false);
                tv.setText(s);
                return tv;
            }
        });

        SpouseStandardsBean.SpouseStandardsData like_biaozhun = data.like_biaozhun;
        ArrayList<String> strings1 = new ArrayList<>();
        if (!TextUtils.isEmpty(like_biaozhun.age)) {
            strings1.add(like_biaozhun.age + "岁");
        }
        if (!TextUtils.isEmpty(like_biaozhun.height)) {
            strings1.add(like_biaozhun.height + "cm");
        }
        if (!TextUtils.isEmpty(data.household)) {
            strings.add("户籍（老家）：" + data.household);
        }
        if (!TextUtils.isEmpty(like_biaozhun.yearmoney)) {
            strings1.add(like_biaozhun.yearmoney);
        }
        //        if (!TextUtils.isEmpty(like_biaozhun.marrytime)) {
        //            strings.add(like_biaozhun.marrytime);
        //        }
        if (!TextUtils.isEmpty(like_biaozhun.areaname)) {
            strings1.add(like_biaozhun.areaname);
        }
        if (!TextUtils.isEmpty(like_biaozhun.marryinfo)) {
            strings1.add(like_biaozhun.marryinfo);
        }
        mTagFlowLayout1.setAdapter(new TagAdapter<String>(strings1) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.tv, mTagFlowLayout1, false);
                tv.setText("户籍： " + s);
                return tv;
            }
        });

        if (TextUtils.isEmpty(data.familyinfo)) {
            familyInformation.setText("未填写");
            familyInformation.setTextColor(getResources().getColor(R.color.gray));
        } else {
            familyInformation.setText("查看");
            familyInformation.setTextColor(getResources().getColor(R.color.default_color));
        }

        if (TextUtils.isEmpty(data.workplan)) {
            careerPlanning.setText("未填写");
            careerPlanning.setTextColor(getResources().getColor(R.color.gray));
        } else {
            careerPlanning.setText("查看");
            careerPlanning.setTextColor(getResources().getColor(R.color.default_color));
        }

        if (TextUtils.isEmpty(data.feeling)) {
            affectiveExperience.setText("未填写");
            affectiveExperience.setTextColor(getResources().getColor(R.color.gray));
        } else {
            affectiveExperience.setText("查看");
            affectiveExperience.setTextColor(getResources().getColor(R.color.default_color));
        }
        HobbiesBean.HobbiesData aihao = data.aihao;
        List<HobbiesBean.Biaoqian> biaoqian = aihao.biaoqian;
        if (!biaoqian.isEmpty()) {
            ArrayList<String> strings2 = new ArrayList<>();
            for (int i = 0; i < biaoqian.size(); i++) {
                if (!TextUtils.isEmpty(biaoqian.get(i).interestclassname)) {
                    strings2.add(biaoqian.get(i).interestclassname);
                }

            }

            mTagFlowLayout2.setAdapter(new TagAdapter<String>(strings2) {
                @Override
                public View getView(FlowLayout parent, int position, String s) {
                    TextView tv = (TextView) mInflater.inflate(R.layout.tv, mTagFlowLayout2, false);
                    tv.setText(s);
                    return tv;
                }
            });
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_tag.setLayoutManager(linearLayoutManager);

        mHobbyInterestBean = new HobbyInterestBean();
        mHobbyInterestBean.label = new ArrayList<HobbyInterestBean.Label>();
        for (int i = 0; i < images.length; i++) {
            HobbyInterestBean.Label label = new HobbyInterestBean.Label();
            label.image = images[i];
            label.title = hobbies[i];
            label.tag = new ArrayList<String>();
            mHobbyInterestBean.label.add(label);
        }
        mLabel = mHobbyInterestBean.label;

        List<HobbiesBean.Biaoqian> video = aihao.video;
        if (null != video && !video.isEmpty()) {
            List<String> tag2 = mLabel.get(0).tag;
            for (int i = 0; i < video.size(); i++) {
                if (!TextUtils.isEmpty(video.get(i).interestclassname)) {

                    tag2.add(video.get(i).interestclassname);
                }

            }
        }

        List<HobbiesBean.Biaoqian> music = aihao.music;
        if (null != music && !music.isEmpty()) {
            List<String> tag3 = mLabel.get(1).tag;
            for (int i = 0; i < music.size(); i++) {

                if (!TextUtils.isEmpty(music.get(i).interestclassname)) {
                    tag3.add(music.get(i).interestclassname);
                }
            }
        }

        List<HobbiesBean.Biaoqian> book = aihao.book;
        if (null != book && !book.isEmpty()) {
            List<String> tag4 = mLabel.get(2).tag;
            for (int i = 0; i < book.size(); i++) {
                if (!TextUtils.isEmpty(book.get(i).interestclassname)) {
                    tag4.add(book.get(i).interestclassname);
                }
            }
        }
        List<HobbiesBean.Biaoqian> food = aihao.food;
        if (null != food && !food.isEmpty()) {
            List<String> tag5 = mLabel.get(3).tag;
            for (int i = 0; i < food.size(); i++) {
                if (!TextUtils.isEmpty(food.get(i).interestclassname)) {
                    tag5.add(food.get(i).interestclassname);
                }
            }
        }

        List<HobbiesBean.Biaoqian> yundong = aihao.yundong;
        if (null != yundong && !yundong.isEmpty()) {
            List<String> tag6 = mLabel.get(4).tag;
            for (int i = 0; i < yundong.size(); i++) {
                if (!TextUtils.isEmpty(yundong.get(i).interestclassname)) {
                    tag6.add(yundong.get(i).interestclassname);
                }
            }
        }

        mHobbyInterestAdapter = new UserHobbyInterestAdapter(mLabel);
        recycler_tag.setAdapter(mHobbyInterestAdapter);

    }

    @Override
    public void refresh(String bean) {
        ll_remind.setVisibility(View.GONE);
    }

    @Override
    public void loadMore(String bean) {
        //        toastShow(bean);

    }

    @Override
    public void failed(String content) {

    }


    //发送心动广播  主要目的是在用户详情界面点击心动后，首页同步心动
    private void heartBeatBroadcast(int position) {
        Intent intent = new Intent("com.zykj.hunqianshiai.HEART_BEAT");
        intent.putExtra("position", position);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    class PicAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
        public PicAdapter(@Nullable List<String> data) {
            super(R.layout.page_pic, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            glide(UrlContent.PIC_URL + item, (ImageView) helper.getView(R.id.iv_pic), mOptions);
            int adapterPosition = helper.getAdapterPosition();

            if (adapterPosition == 5) {
                helper.setVisible(R.id.tv_all, true);
            } else {
                helper.setVisible(R.id.tv_all, false);
            }
        }
    }


    float width6 = screenWidth / 6;
    float width4 = screenWidth / 4;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {// 当按下时
            // 获得按下时的X坐标

            downX = event.getX();
            downY = event.getY();


        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {// 当手指滑动时
            // 获得滑过的距离
            float moveDistanceX = event.getX() - downX;
            float moveDistanceY = Math.abs(event.getY() - downY);
            float flag = moveDistanceX - moveDistanceY;
            if (moveDistanceX > 0 && downX > width6 && downX < width4) {// 如果是向右滑动
                decorView.setX(moveDistanceX); // 设置界面的X到滑动到的位置
            } else if (moveDistanceX <= 0) {
                // 恢复初始状态
                rebackToLeft(0);
            }


        } else if (event.getAction() == MotionEvent.ACTION_UP) {// 当抬起手指时
            // 获得滑过的距离
            float moveDistanceX = event.getX() - downX;
            float moveDistanceY = Math.abs(event.getY() - downY);
            if (moveDistanceX > screenWidth / 3) {
                // 如果滑动的距离超过了手机屏幕的一半, 结束当前Activity
                continueMove(moveDistanceX);

            } else if (moveDistanceX > 0) { // 如果滑动距离没有超过一半
                // 恢复初始状态
                rebackToLeft(moveDistanceX);
            } else {
                decorView.setX(0);
            }
        }
        //return super.onTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }

    /*==================右滑返回上一层=====================*/

    /**
     * 通过重写该方法，对触摸事件进行处理
     */
    //    @Override
    //    public boolean onTouchEvent(MotionEvent event) {
    //        if (event.getAction() == MotionEvent.ACTION_DOWN) {// 当按下时
    //            // 获得按下时的X坐标
    //
    //            downX = event.getX();
    //            downY = event.getY();
    //
    //        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {// 当手指滑动时
    //            // 获得滑过的距离
    //            float moveDistanceX = event.getX() - downX;
    //            float moveDistanceY = Math.abs(event.getY() - downY);
    //            if(moveDistanceX > moveDistanceY){
    //                if (moveDistanceX > 0) {// 如果是向右滑动
    //                    decorView.setX(moveDistanceX); // 设置界面的X到滑动到的位置
    //                } else {
    //                    decorView.setX(event.getX()); //
    //                }
    //            }
    //
    //
    //        } else if (event.getAction() == MotionEvent.ACTION_UP) {// 当抬起手指时
    //            // 获得滑过的距离
    //            float moveDistanceX = event.getX() - downX;
    //            float moveDistanceY = Math.abs(event.getY() - downY);
    //            if(moveDistanceX > moveDistanceY){
    //                if (moveDistanceX > screenWidth / 2) {
    //                    // 如果滑动的距离超过了手机屏幕的一半, 结束当前Activity
    //                    continueMove(moveDistanceX);
    //
    //                } else{ // 如果滑动距离没有超过一半
    //                    // 恢复初始状态
    //                    rebackToLeft(moveDistanceX);
    //                }
    //            }
    //        }
    //        return super.onTouchEvent(event);
    //    }

    /**
     * 从当前位置一直往右滑动到消失。
     * 这里使用了属性动画。
     */
    private void continueMove(float moveDistanceX) {
        // 从当前位置移动到右侧。
        ValueAnimator anim = ValueAnimator.ofFloat(moveDistanceX, screenWidth);
        anim.setDuration(300); // 一秒的时间结束, 为了简单这里固定为1秒
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
    private void rebackToLeft(float moveDistanceX) {
        ObjectAnimator.ofFloat(decorView, "X", moveDistanceX, 0).setDuration(300).start();
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }
}
