package com.zykj.hunqianshiai.activities.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BaseFragment;
import com.zykj.hunqianshiai.bases.BaseModel;
import com.zykj.hunqianshiai.bases.BaseModelImpl;
import com.zykj.hunqianshiai.bases.BasePresenterImpl;
import com.zykj.hunqianshiai.bases.BaseView;
import com.zykj.hunqianshiai.home.OnDoubleClickListener;
import com.zykj.hunqianshiai.net.UrlContent;
import com.zykj.hunqianshiai.tools.DoubleClick;
import com.zykj.hunqianshiai.tools.JsonUtils;
import com.zykj.hunqianshiai.web.WebViewActivity;

import java.util.List;

import butterknife.Bind;

/**
 * 活动列表
 * Created by xu on 2017/12/5.
 */

public class ActivitiesFragment extends BaseFragment implements BaseView<String>, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @Bind(R.id.recycler_activities)
    RecyclerView mRecyclerView;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private BasePresenterImpl mPresenter;
    private int page = 1;
    private ActivitiesAdapter mActivitiesAdapter;
    private Bundle mBundle;

    //RecyclerView滚动到的item位置
    private int firstPosition;

    public ActivitiesFragment() {
    }

    public static ActivitiesFragment getInstance() {
        return Instance.mRegionFragment;
    }

    private static class Instance {
        static ActivitiesFragment mRegionFragment = new ActivitiesFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_activities;
    }

    @Override
    public void initView() {

        mBundle = new Bundle();
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        showDialog();
        mPresenter = new BasePresenterImpl(this, new BaseModelImpl());
        mParams.clear();
        mParams.put("userid", UrlContent.USER_ID);
        mParams.put("page", page);
        mParams.put("size", 6);
        mPresenter.getData(UrlContent.ACTIVITIES_LIST_URL, mParams, BaseModel.DEFAULT_TYPE);
    }

    @Override
    public void success(String bean) {
        hideDialog();
        ActivitiesBean activitiesBean = JsonUtils.GsonToBean(bean, ActivitiesBean.class);
        if (activitiesBean.code != 200) {
            return;
        }

        List<ActivitiesBean.ActivitiesData> data = activitiesBean.data;
        mActivitiesAdapter = new ActivitiesAdapter(data);
        mRecyclerView.setAdapter(mActivitiesAdapter);

        mActivitiesAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<ActivitiesBean.ActivitiesData> data1 = adapter.getData();
                ActivitiesBean.ActivitiesData activitiesData = data1.get(position);
                String actid = activitiesData.actid;
                mBundle.clear();
                mBundle.putString("title", "活动详情");
                mBundle.putString("actid",actid);
                mBundle.putString("cost",activitiesData.cost);
                mBundle.putString("state",activitiesData.state);
                mBundle.putString("url", UrlContent.PIC_URL + "api.php?c=Fell&a=huodong&actid=" + actid + "&userid=" + UrlContent.USER_ID);
                openActivity(ActivitiesDetailActivity.class, mBundle);
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mActivitiesAdapter.setOnLoadMoreListener(this, mRecyclerView);
        if (data.size() < 6) {
            mActivitiesAdapter.loadMoreEnd();
        }
    }

    @Override
    public void refresh(String bean) {
        ActivitiesBean activitiesBean = JsonUtils.GsonToBean(bean, ActivitiesBean.class);

        List<ActivitiesBean.ActivitiesData> data = activitiesBean.data;
        mActivitiesAdapter.setNewData(data);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void loadMore(String bean) {
        ActivitiesBean activitiesBean = JsonUtils.GsonToBean(bean, ActivitiesBean.class);

        List<ActivitiesBean.ActivitiesData> data = activitiesBean.data;
        mActivitiesAdapter.addData(data);
        mActivitiesAdapter.loadMoreComplete();
        if (data.size() < 6) {
            mActivitiesAdapter.loadMoreEnd();
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
        mParams.put("size", 6);
        mPresenter.getData(UrlContent.ACTIVITIES_LIST_URL, mParams, BaseModel.REFRESH_TYPE);
    }

    @Override
    public void onLoadMoreRequested() {
        page++;
        mParams.clear();
        mParams.put("userid", UrlContent.USER_ID);
        mParams.put("page", page);
        mParams.put("size", 6);
        mPresenter.getData(UrlContent.ACTIVITIES_LIST_URL, mParams, BaseModel.LOADING_MORE_TYPE);
    }
}
