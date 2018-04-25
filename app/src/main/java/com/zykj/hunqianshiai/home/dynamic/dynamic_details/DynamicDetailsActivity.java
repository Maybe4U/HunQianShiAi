package com.zykj.hunqianshiai.home.dynamic.dynamic_details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BaseModel;
import com.zykj.hunqianshiai.bases.BaseModelImpl;
import com.zykj.hunqianshiai.bases.BasePopupWindow;
import com.zykj.hunqianshiai.bases.BasePresenterImpl;
import com.zykj.hunqianshiai.bases.BaseView;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.home.dynamic.PopupWindowDelete;
import com.zykj.hunqianshiai.home.dynamic.PopupWindowMoreOption;
import com.zykj.hunqianshiai.look_pic_video.PicActivity;
import com.zykj.hunqianshiai.net.UrlContent;
import com.zykj.hunqianshiai.tools.JsonUtils;
import com.zykj.hunqianshiai.user_home.dynamic.UserDynamicAdapter;

import java.io.Serializable;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class DynamicDetailsActivity extends BasesActivity implements BaseView<String>, View.OnLayoutChangeListener {

    @Bind(R.id.recycler_comment)
    RecyclerView mRecyclerView;
    @Bind(R.id.et_content)
    EditText et_content;
    @Bind(R.id.rl_layout)
    RelativeLayout rl_layout;

    private ImageView mIv_headpic;
    private TextView mTv_username;
    private RecyclerView mRecycler_pic;
    private TextView mTv_arename;
    private RecyclerView mRecycler_like;
    private TextView mTv_addtime3;
    private View mHeadView;
    private ImageView mIv_like;
    private String mFriends_id;
    private BasePresenterImpl mPresenter;
    private DynamicDetailsAdapter mDynamicDetailsAdapter;
    //软件盘弹起后所占高度阀值
    private int keyHeight = 0;
    //屏幕高度
    private int screenHeight = 0;
    public boolean reply_dynamic = true;//判断回复动态  还是回复评论
    private String mOther_id = "";
    private List<String> mImg;


    @Override
    protected int getContentViewX() {
        return R.layout.activity_dynamic_details;
    }

    @Override
    protected void initView() {
        appBar("动态详情");
        Bundle bundle = getIntent().getExtras();
        mFriends_id = bundle.getString("friends_id");
        mHeadView = LayoutInflater.from(this).inflate(R.layout.dynamic_details_head, null);
        mIv_headpic = mHeadView.findViewById(R.id.iv_headpic);
        mTv_username = mHeadView.findViewById(R.id.tv_username);
        mRecycler_pic = mHeadView.findViewById(R.id.recycler_pic);
        mTv_arename = mHeadView.findViewById(R.id.tv_arename);
        mRecycler_like = mHeadView.findViewById(R.id.recycler_like);
        mTv_addtime3 = mHeadView.findViewById(R.id.tv_addtime3);
        mIv_like = mHeadView.findViewById(R.id.iv_like);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mPresenter = new BasePresenterImpl(this, new BaseModelImpl());
        mParams.clear();
        mParams.put("userid", UrlContent.USER_ID);
        mParams.put("friends_id", mFriends_id);
        mPresenter.getData(UrlContent.DYNAMIC_DETAILS_URL, mParams, BaseModel.DEFAULT_TYPE);

        mPresenter.getData(UrlContent.LIKE_LIST_URL, mParams, BaseModel.LOADING_MORE_TYPE);

        //监听软键盘
        rl_layout.addOnLayoutChangeListener(this);
        //获取屏幕高度
        screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight / 3;
    }

    @Override
    public void success(String bean) {
        DynamicDetailsBean dynamicDetailsBean = JsonUtils.GsonToBean(bean, DynamicDetailsBean.class);
        if (dynamicDetailsBean.code != 200) {
            return;
        }
        DynamicDetailsBean.DynamicDetailsData dynamicDetailsData = dynamicDetailsBean.data.get(0);
        glide(dynamicDetailsData.headpic, mIv_headpic, mCircleRequestOptions);
        mTv_username.setText(dynamicDetailsData.content);
        mTv_addtime3.setText(dynamicDetailsData.addtime3);
        mTv_arename.setText(dynamicDetailsData.arename);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        mRecycler_pic.setLayoutManager(gridLayoutManager);
        mImg = dynamicDetailsData.img;
        UserDynamicAdapter.PicAdapter picAdapter = new UserDynamicAdapter.PicAdapter(mImg);
        mRecycler_pic.setAdapter(picAdapter);

        picAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(DynamicDetailsActivity.this, PicActivity.class);
                intent.putExtra("images", (Serializable) mImg);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });

        final List<DynamicDetailsBean.Comment> comment = dynamicDetailsData.comment;
        mDynamicDetailsAdapter = new DynamicDetailsAdapter(comment);
        mRecyclerView.setAdapter(mDynamicDetailsAdapter);
        mDynamicDetailsAdapter.addHeaderView(mHeadView);

        mDynamicDetailsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<DynamicDetailsBean.Comment> data = adapter.getData();
                final DynamicDetailsBean.Comment comment1 = data.get(position);
                if (comment1.userid.equals(UrlContent.USER_ID)) {
                    PopupWindowDelete popupWindowDelete = new PopupWindowDelete(DynamicDetailsActivity.this);
                    popupWindowDelete.showAtLocation(mRecyclerView, Gravity.BOTTOM, 0, 0);
                    popupWindowDelete.setClickListener(new BasePopupWindow.ClickListener() {
                        @Override
                        public void onClickListener(Object object) {
                            String id = comment1.id;
                            toastShow("删除");
                            mParams.clear();
                            mParams.put("comment_id", id);
                            mParams.put("rdm", UrlContent.RDM);
                            mParams.put("sign", UrlContent.SIGN);
                            OkGo.<String>post(UrlContent.DELETE_COMMENT_URL)
                                    .params(mParams)
                                    .execute(new StringCallback() {
                                        @Override
                                        public void onSuccess(Response<String> response) {
                                            et_content.setText("");
                                            mParams.clear();
                                            mParams.put("userid", UrlContent.USER_ID);
                                            mParams.put("friends_id", mFriends_id);
                                            mPresenter.getData(UrlContent.DYNAMIC_DETAILS_URL, mParams, BaseModel.REFRESH_TYPE);
                                        }
                                    });
                        }
                    });

                } else {
                    reply_dynamic = false;
                    mOther_id = comment1.userid;
                    et_content.setHint("回复 " + comment1.username);
                    et_content.setFocusable(true);
                    et_content.setFocusableInTouchMode(true);
                    et_content.requestFocus();
                    InputMethodManager inputManager = (InputMethodManager) et_content.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.showSoftInput(et_content, 0);
                }
            }
        });
    }

    @Override
    public void refresh(String bean) {
        DynamicDetailsBean dynamicDetailsBean = JsonUtils.GsonToBean(bean, DynamicDetailsBean.class);
        if (dynamicDetailsBean.code != 200) {
            return;
        }
        List<DynamicDetailsBean.Comment> comment = dynamicDetailsBean.data.get(0).comment;
        mDynamicDetailsAdapter.setNewData(comment);
    }

    @Override
    public void loadMore(String bean) {
        LikeBean likeBean = JsonUtils.GsonToBean(bean, LikeBean.class);
        List<LikeBean.LikeData> data = likeBean.data;
        if (data.isEmpty()) {
            mIv_like.setVisibility(View.GONE);
            return;
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecycler_like.setLayoutManager(layoutManager);
        LikeAdapter likeAdapter = new LikeAdapter(data);
        mRecycler_like.setAdapter(likeAdapter);
    }

    @Override
    public void failed(String content) {

    }

    @OnClick({R.id.tv_send})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_send:
                String trim = et_content.getText().toString().trim();
                if (TextUtils.isEmpty(trim)) {
                    toastShow("内容不能为空");
                    return;
                }
                if (reply_dynamic) {
                    mParams.clear();
                    mParams.put("userid", UrlContent.USER_ID);
                    mParams.put("friend_id", mFriends_id);
                    mParams.put("content", trim);
                    mParams.put("rdm", UrlContent.RDM);
                    mParams.put("sign", UrlContent.SIGN);
                    OkGo.<String>post(UrlContent.COMMENT_DYNAMIC_URL)
                            .params(mParams)
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(Response<String> response) {
//                                                onRefresh();
                                    et_content.setText("");
                                    mParams.clear();
                                    mParams.put("userid", UrlContent.USER_ID);
                                    mParams.put("friends_id", mFriends_id);
                                    mPresenter.getData(UrlContent.DYNAMIC_DETAILS_URL, mParams, BaseModel.REFRESH_TYPE);
                                }
                            });
                } else {
                    mParams.clear();
                    mParams.put("userid", UrlContent.USER_ID);
                    mParams.put("friend_id", mFriends_id);
                    mParams.put("content", trim);
                    mParams.put("other_id", mOther_id);
                    mParams.put("rdm", UrlContent.RDM);
                    mParams.put("sign", UrlContent.SIGN);
                    OkGo.<String>post(UrlContent.REPLY_URL)
                            .params(mParams)
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(Response<String> response) {
//                                                onRefresh();
                                    et_content.setText("");
                                    mParams.clear();
                                    mParams.put("userid", UrlContent.USER_ID);
                                    mParams.put("friends_id", mFriends_id);
                                    mPresenter.getData(UrlContent.DYNAMIC_DETAILS_URL, mParams, BaseModel.REFRESH_TYPE);
                                }
                            });

                    mOther_id = "";
                }

                break;
        }
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        //现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起
        if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {

//            toastShow("监听到软键盘弹起");
        } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
            reply_dynamic = true;
//            mOther_id = "";
            et_content.setHint("评论");
//            toastShow("监听到软件盘关闭");
        }
    }

    public class LikeAdapter extends BaseQuickAdapter<LikeBean.LikeData, BaseViewHolder> {

        public LikeAdapter(@Nullable List<LikeBean.LikeData> data) {
            super(R.layout.item_like, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, LikeBean.LikeData item) {
            glide(item.username_src, (ImageView) helper.getView(R.id.iv_like), mCircleRequestOptions);
        }
    }
}
