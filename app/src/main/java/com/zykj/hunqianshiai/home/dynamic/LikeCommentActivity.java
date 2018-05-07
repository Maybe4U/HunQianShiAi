package com.zykj.hunqianshiai.home.dynamic;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BaseModel;
import com.zykj.hunqianshiai.bases.BaseModelImpl;
import com.zykj.hunqianshiai.bases.BasePresenterImpl;
import com.zykj.hunqianshiai.bases.BaseView;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.home.dynamic.dynamic_details.DynamicDetailsActivity;
import com.zykj.hunqianshiai.home.message.like_me.LikeMeActivity;
import com.zykj.hunqianshiai.net.UrlContent;
import com.zykj.hunqianshiai.tools.JsonUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 动态的信息
 */
public class LikeCommentActivity extends BasesActivity implements BaseView<String>, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @Bind(R.id.recycler_like)
    RecyclerView mRecyclerView;
    @Bind(R.id.tv_right)
    TextView right;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private int page = 1;
    private BasePresenterImpl mPresenter;
    private LikeCommentAdapter mLikeCommentAdapter;
    private Bundle mBundle;

    @Override
    protected int getContentViewX() {
        return R.layout.activity_like_comment;
    }

    @Override
    protected void initView() {
        appBar("消息列表");
        right.setVisibility(View.VISIBLE);
        right.setText("清空");

        mBundle = getIntent().getExtras();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        String userid = mBundle.getString("userid");

        mPresenter = new BasePresenterImpl(this, new BaseModelImpl());
        mParams.clear();
        mParams.put("userid", userid);
        mParams.put("page", page);
        mParams.put("size", 10);
        mPresenter.getData(UrlContent.PUSH_MESSAGE_URL, mParams, BaseModel.DEFAULT_TYPE);

    }

    @Override
    public void success(String bean) {
        LikeCommentBean likeCommentBean = JsonUtils.GsonToBean(bean, LikeCommentBean.class);
        List<LikeCommentBean.LikeCommentData> data = likeCommentBean.data;
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mLikeCommentAdapter = new LikeCommentAdapter(data);
        mRecyclerView.setAdapter(mLikeCommentAdapter);

        mLikeCommentAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<LikeCommentBean.LikeCommentData> data1 = adapter.getData();
                LikeCommentBean.Other_table other_table = data1.get(position).other_table;
                String catogry = data1.get(position).catogry;
                if (null != other_table) {
                    String id = other_table.friend_id;
                    mBundle.clear();
                    mBundle.putString("friends_id", id);
                    openActivity(DynamicDetailsActivity.class, mBundle);
                }
                if(catogry.equals("5")){
//                    String id = other_table.friend_id;
//                    mBundle.clear();
//                    mBundle.putString("friends_id", id);
                    openActivity(LikeMeActivity.class);
                }
            }
        });

        mLikeCommentAdapter.setOnLoadMoreListener(this, mRecyclerView);
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void refresh(String bean) {
        LikeCommentBean likeCommentBean = JsonUtils.GsonToBean(bean, LikeCommentBean.class);
        List<LikeCommentBean.LikeCommentData> data = likeCommentBean.data;
        mLikeCommentAdapter.setNewData(data);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void loadMore(String bean) {
        LikeCommentBean likeCommentBean = JsonUtils.GsonToBean(bean, LikeCommentBean.class);
        List<LikeCommentBean.LikeCommentData> data = likeCommentBean.data;
        mLikeCommentAdapter.addData(data);
        mLikeCommentAdapter.loadMoreComplete();
        if (data.size()<10) {
            mLikeCommentAdapter.loadMoreEnd();
        }
    }

    @Override
    public void failed(String content) {

    }

    @Override
    public void onRefresh() {
        page = 1;
        mParams.clear();
        mParams.put("userid", UrlContent.USER_ID);
        mParams.put("page", page);
        mParams.put("size", 10);
        mPresenter.getData(UrlContent.PUSH_MESSAGE_URL, mParams, BaseModel.REFRESH_TYPE);
    }

    @Override
    public void onLoadMoreRequested() {
        page++;
        mParams.clear();
        mParams.put("userid", UrlContent.USER_ID);
        mParams.put("page", page);
        mParams.put("size", 10);
        mPresenter.getData(UrlContent.PUSH_MESSAGE_URL, mParams, BaseModel.LOADING_MORE_TYPE);
    }

    @OnClick({R.id.tv_right})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_right:
                mParams.clear();
                mParams.put("userid", UrlContent.USER_ID);
                mPresenter.getData(UrlContent.DELETE_MESSAGE_URL, mParams, BaseModel.REFRESH_TYPE);
                mLikeCommentAdapter.setNewData(null);
                break;
        }
    }
}
