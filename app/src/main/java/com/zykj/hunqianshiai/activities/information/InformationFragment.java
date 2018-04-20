package com.zykj.hunqianshiai.activities.information;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.activities.activity.ActivitiesBean;
import com.zykj.hunqianshiai.bases.BaseFragment;
import com.zykj.hunqianshiai.bases.BaseModel;
import com.zykj.hunqianshiai.bases.BaseModelImpl;
import com.zykj.hunqianshiai.bases.BasePresenterImpl;
import com.zykj.hunqianshiai.bases.BaseView;
import com.zykj.hunqianshiai.net.UrlContent;
import com.zykj.hunqianshiai.tools.JsonUtils;

import java.util.List;

import butterknife.Bind;

/**
 * 资讯列表
 * Created by xu on 2017/12/5.
 */

public class InformationFragment extends BaseFragment implements BaseView<String>, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @Bind(R.id.recycler_activities)
    RecyclerView mRecyclerView;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private BasePresenterImpl mPresenter;
    private int page = 1;
    private InformationAdapter mInformationAdapter;
    private Bundle mBundle;

    public InformationFragment() {
    }

    public static InformationFragment getInstance() {
        return Instance.mRegionFragment;
    }

    private static class Instance {
        static InformationFragment mRegionFragment = new InformationFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_activities;
    }

    @Override
    public void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        showDialog();

        mBundle = new Bundle();
        mPresenter = new BasePresenterImpl(this, new BaseModelImpl());
        mParams.clear();
        mParams.put("userid", UrlContent.USER_ID);
        mParams.put("page", page);
        mParams.put("size", 6);
        mPresenter.getData(UrlContent.INFORMATION_LIST_URL, mParams, BaseModel.DEFAULT_TYPE);
    }

    @Override
    public void success(String bean) {
        hideDialog();
        ActivitiesBean activitiesBean = JsonUtils.GsonToBean(bean, ActivitiesBean.class);
        if (activitiesBean.code != 200) {
            return;
        }

        List<ActivitiesBean.ActivitiesData> data = activitiesBean.data;
        mInformationAdapter = new InformationAdapter(data);
        mRecyclerView.setAdapter(mInformationAdapter);

        mInformationAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<ActivitiesBean.ActivitiesData> data1 = adapter.getData();
                String actid = data1.get(position).actid;
                mBundle.clear();
                mBundle.putString("id", actid);
                openActivity(InformationDetailsActivity.class, mBundle);
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mInformationAdapter.setOnLoadMoreListener(this, mRecyclerView);
    }

    @Override
    public void refresh(String bean) {
        ActivitiesBean activitiesBean = JsonUtils.GsonToBean(bean, ActivitiesBean.class);
        if (activitiesBean.code != 200) {
            return;
        }
        List<ActivitiesBean.ActivitiesData> data = activitiesBean.data;
        mInformationAdapter.setNewData(data);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void loadMore(String bean) {
        ActivitiesBean activitiesBean = JsonUtils.GsonToBean(bean, ActivitiesBean.class);
        if (activitiesBean.code != 200) {
            return;
        }
        List<ActivitiesBean.ActivitiesData> data = activitiesBean.data;
        mInformationAdapter.addData(data);
        mInformationAdapter.loadMoreComplete();
        if (data.size() < 6) {
            mInformationAdapter.loadMoreEnd();
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
        mPresenter.getData(UrlContent.INFORMATION_LIST_URL, mParams, BaseModel.REFRESH_TYPE);
    }

    @Override
    public void onLoadMoreRequested() {
        page++;
        mParams.clear();
        mParams.put("userid", UrlContent.USER_ID);
        mParams.put("page", page);
        mParams.put("size", 6);
        mPresenter.getData(UrlContent.INFORMATION_LIST_URL, mParams, BaseModel.LOADING_MORE_TYPE);
    }
}
