package com.zykj.hunqianshiai.activities.information;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
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
import com.zykj.hunqianshiai.home.dynamic.dynamic_details.DynamicDetailsAdapter;
import com.zykj.hunqianshiai.home.dynamic.dynamic_details.DynamicDetailsBean;
import com.zykj.hunqianshiai.net.UrlContent;
import com.zykj.hunqianshiai.tools.JsonUtils;
import com.zykj.hunqianshiai.user_home.UserPageActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import io.rong.imkit.RongIM;

import static io.rong.imkit.utilities.RongUtils.screenHeight;

/**
 * 资讯详情页
 */
public class InformationDetailsActivity extends BasesActivity implements BaseView<String>, CompoundButton.OnCheckedChangeListener, View.OnLayoutChangeListener {

    @Bind(R.id.recycler_details)
    RecyclerView mRecyclerView;
    @Bind(R.id.iv_right_share)
    ImageView rightShare;
    @Bind(R.id.et_content)
    EditText et_content;
    @Bind(R.id.tv_send)
    TextView tv_send;
    @Bind(R.id.rl_layout)
    RelativeLayout rl_layout;

    private View mHeadView;
    private TextView mTv_num;
    private TextView mTv_see;
    private TextView mTv_content;
    private TextView mTv_time;
    private ImageView mIv_thumb;
    private TextView mTv_title;
    private CheckBox mCheck_like;
    private String mOther_id;
    private int keyHeight;
    private String mId;
    private BasePresenterImpl mPresenter;
    private String mId1;
    private InformationDetailsAdapter mInformationDetailsAdapter;
    private TextView mComment_num;
    private TextView mThumb_up;
    private ImageView mIv_details_comment;
    private int mLike_num;

    private String mUserid;
    private Bundle mBundle;

    private String mState;
    private String mTitle;
    private String mUrl;
    private String mActid;
    //点赞数量
    private int mThumbUpNum;
    private PopupWindowInformationReply mPopupWindowInformationReply;


    @Override
    protected int getContentViewX() {
        return R.layout.activity_information_details;
    }

    @Override
    protected void initView() {
        appBar("资讯详情页");
        //        rightShare.setVisibility(View.VISIBLE);
        Bundle bundle = getIntent().getExtras();

        mId1 = bundle.getString("id");

        rightShare.setVisibility(View.VISIBLE);

        mHeadView = LayoutInflater.from(this).inflate(R.layout.information_details_header, null);
        ImageView headViewComment = mHeadView.findViewById(R.id.iv_details_comment);
        mTv_title = mHeadView.findViewById(R.id.tv_title);
        mIv_thumb = mHeadView.findViewById(R.id.iv_thumb);
        mTv_time = mHeadView.findViewById(R.id.tv_time);
        mTv_content = mHeadView.findViewById(R.id.tv_content);
        mTv_see = mHeadView.findViewById(R.id.tv_see);
        mTv_num = mHeadView.findViewById(R.id.tv_num);
        mCheck_like = mHeadView.findViewById(R.id.check_like);

        //评论数量
        mComment_num = mHeadView.findViewById(R.id.comment_num);
        //点赞数量
        mThumb_up = mHeadView.findViewById(R.id.thumb_up_num);


        //评论
        mIv_details_comment = mHeadView.findViewById(R.id.iv_details_comment);

        headViewComment.setOnClickListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        //监听软键盘
        rl_layout.addOnLayoutChangeListener(this);
        //获取屏幕高度
        screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight / 3;
        //查询我是否点赞
        queryThumb();

        mPresenter = new BasePresenterImpl(this, new BaseModelImpl());
        mParams.clear();
        mParams.put("userid", UrlContent.USER_ID);
        mParams.put("id", mId1);

        mPresenter.getData(UrlContent.INFORMATION_PARTICULARS_URL, mParams, BaseModel.DEFAULT_TYPE);
        //mPresenter.getData(UrlContent.IS_LIKE_URL, mParams, BaseModel.DEFAULT_TYPE);

    }

    //发送
    @OnClick({R.id.tv_send, R.id.iv_right_share})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_send:
                String trim = et_content.getText().toString().trim();
                if (TextUtils.isEmpty(trim)) {
                    toastShow("请输入评论内容");
                    return;
                }
                if (TextUtils.isEmpty(mOther_id)) {
                    mParams.clear();
                    mParams.put("userid", UrlContent.USER_ID);
                    mParams.put("act_id", mActid);
                    mParams.put("content", trim);
                    mParams.put("rdm", UrlContent.RDM);
                    mParams.put("sign", UrlContent.SIGN);
                    OkGo.<String>post(UrlContent.COMMENT_INFORMATION_URL)
                            .params(mParams)
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(Response<String> response) {
                                    mParams.clear();
                                    mParams.put("userid", UrlContent.USER_ID);
                                    mParams.put("id", mId1);
                                    mPresenter.getData(UrlContent.INFORMATION_PARTICULARS_URL, mParams, BaseModel.REFRESH_TYPE);
                                }
                            });
                } else {
                    mParams.clear();
                    mParams.put("userid", UrlContent.USER_ID);
                    mParams.put("act_id", mActid);
                    mParams.put("content", trim);

                    mParams.put("other_id", mOther_id);
                    mParams.put("rdm", UrlContent.RDM);
                    mParams.put("sign", UrlContent.SIGN);
                    OkGo.<String>post(UrlContent.REPLY_COMMENT_INFORMATION_URL)
                            .params(mParams)
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(Response<String> response) {
                                    mParams.clear();
                                    mParams.put("userid", UrlContent.USER_ID);
                                    mParams.put("id", mId1);
                                    mPresenter.getData(UrlContent.INFORMATION_PARTICULARS_URL, mParams, BaseModel.REFRESH_TYPE);
                                }
                            });
                    mOther_id = "";
                }
                break;
            case R.id.iv_details_comment:
                mPopupWindowInformationReply = new PopupWindowInformationReply(InformationDetailsActivity.this);
                mPopupWindowInformationReply.showAtLocation(rightShare, Gravity.BOTTOM, 0, 0);
                mPopupWindowInformationReply.setClickListener(new BasePopupWindow.ClickListener() {
                    @Override
                    public void onClickListener(Object object) {
                        mParams.clear();
                        mParams.put("userid", UrlContent.USER_ID);
                        mParams.put("act_id", mActid);
                        mParams.put("content", object.toString());
                        mParams.put("rdm", UrlContent.RDM);
                        mParams.put("sign", UrlContent.SIGN);
                        OkGo.<String>post(UrlContent.COMMENT_INFORMATION_URL)
                                .params(mParams)
                                .execute(new StringCallback() {
                                    @Override
                                    public void onSuccess(Response<String> response) {
                                        mParams.clear();
                                        mParams.put("userid", UrlContent.USER_ID);
                                        mParams.put("id", mId1);
                                        mPresenter.getData(UrlContent.INFORMATION_PARTICULARS_URL, mParams, BaseModel.REFRESH_TYPE);
                                    }
                                });
                    }
                });
                break;
            case R.id.iv_right_share:
                //Log.e("分享","点击");
                showShare(mUrl, mActid);
                break;
            default:
                break;
        }
    }


    @Override
    public void success(String bean) {
        BaseBean baseBean = JsonUtils.GsonToBean(bean, BaseBean.class);
        if (baseBean.code != 200) {
            return;
        }
        InformationDetailsBean informationDetailsBean = JsonUtils.GsonToBean(bean, InformationDetailsBean.class);
        InformationDetailsBean.InformationDetailsData data = informationDetailsBean.data;



        //Log.e("information", data.toString());
        mActid = data.actid;
        mTv_title.setText(data.title);
        glide(UrlContent.PIC_URL + data.thumb, mIv_thumb, mCircleRequestOptions);
        mTv_time.setText(data.addtime);
        mTv_content.setText(data.info);
        mTv_see.setText(data.see);

        //点赞数量
        mThumbUpNum = data.zan_num;
        mTv_num.setText("评论（" + data.comment_num + "）");
        mComment_num.setText(data.comment_num + "");
        //设置点赞数量显示
        mThumb_up.setText(mThumbUpNum + "");

        mCheck_like.setOnCheckedChangeListener(this);

        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            strings.add("1");
        }

        List<DynamicDetailsBean.Comment> comment = data.comment;
        //        mInformationDetailsAdapter = new DynamicDetailsAdapter(comment);
        mInformationDetailsAdapter = new InformationDetailsAdapter(comment);
        mInformationDetailsAdapter.addHeaderView(mHeadView);
        mRecyclerView.setAdapter(mInformationDetailsAdapter);

        mInformationDetailsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<DynamicDetailsBean.Comment> data = adapter.getData();
                final DynamicDetailsBean.Comment comment1 = data.get(position);
                mOther_id = comment1.userid;
                mId = comment1.id;
                et_content.setHint("回复 " + comment1.username);
                et_content.setFocusable(true);
                et_content.setFocusableInTouchMode(true);
                et_content.requestFocus();
                InputMethodManager inputManager = (InputMethodManager) et_content.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(et_content, 0);
            }
        });
    }

    private void queryThumb() {
        mParams.clear();
        mParams.put("userid", UrlContent.USER_ID);
        mParams.put("type", 1);
        mParams.put("other_id", mId1);
        mParams.put("rdm", UrlContent.RDM);
        mParams.put("sign", UrlContent.SIGN);
        OkGo.<String>post(UrlContent.ISMY_LIKE_URL)
                .params(mParams)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        BaseBean baseBean = JsonUtils.GsonToBean(response.body(), BaseBean.class);
                        if (baseBean.code == 200) {
                            if (baseBean.message.equals("有点赞")) {
                                mCheck_like.setChecked(true);
                            } else {
                                mCheck_like.setChecked(false);
                            }
                        }

                    }
                });
    }

    @Override
    public void refresh(String bean) {
        BaseBean baseBean = JsonUtils.GsonToBean(bean, BaseBean.class);
        if (baseBean.code != 200) {
            return;
        }
        et_content.setText("");
        InformationDetailsBean informationDetailsBean = JsonUtils.GsonToBean(bean, InformationDetailsBean.class);
        InformationDetailsBean.InformationDetailsData data = informationDetailsBean.data;
        mTv_num.setText("评论（" + data.comment_num + "）");
        mComment_num.setText(data.comment_num + "");
        List<DynamicDetailsBean.Comment> comment = data.comment;
        mInformationDetailsAdapter.setNewData(comment);
        View v = getCurrentFocus();
        IBinder windowToken = v.getWindowToken();
        hideKeyboard(windowToken);//评论点击发送后隐藏软键盘
    }

    @Override
    public void loadMore(String bean) {

    }

    @Override
    public void failed(String content) {

    }

    //点赞
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        mParams.clear();
        mParams.put("userid", UrlContent.USER_ID);
        mParams.put("type", 1);
        mParams.put("other_id", mActid);

        mParams.put("rdm", UrlContent.RDM);
        mParams.put("sign", UrlContent.SIGN);
        if (b) {
            mParams.put("type_c", 2);
            OkGo.<String>post(UrlContent.IS_LIKE_URL)
                    .params(mParams)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            BaseBean baseBean = JsonUtils.GsonToBean(response.body(), BaseBean.class);
                            if (baseBean.code == 200) {
                                toastShow(baseBean.message);
                            }
                            mCheck_like.setChecked(true);
                            mThumbUpNum++;
                            mThumb_up.setText(mThumbUpNum + "");
                        }
                    });
        } else {
            mParams.put("type_c", 1);
            OkGo.<String>post(UrlContent.IS_LIKE_URL)
                    .params(mParams)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            BaseBean baseBean = JsonUtils.GsonToBean(response.body(), BaseBean.class);
                            if (baseBean.code == 200) {
                                toastShow(baseBean.message);
                            }
                            mCheck_like.setChecked(false);
                            mThumbUpNum--;
                            mThumb_up.setText(mThumbUpNum + "");
                        }
                    });
        }

    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        return false;
//    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        //现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起
        if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {

            //            toastShow("监听到软键盘弹起");
        } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {

            et_content.setHint("评论");
            //            toastShow("监听到软件盘关闭");
        }
    }

}
