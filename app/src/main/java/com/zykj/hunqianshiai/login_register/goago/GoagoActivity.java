package com.zykj.hunqianshiai.login_register.goago;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BaseModel;
import com.zykj.hunqianshiai.bases.BaseModelImpl;
import com.zykj.hunqianshiai.bases.BasePresenterImpl;
import com.zykj.hunqianshiai.bases.BaseView;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.home.home.HomeAdapter;
import com.zykj.hunqianshiai.home.home.HomeBean;
import com.zykj.hunqianshiai.net.UrlContent;
import com.zykj.hunqianshiai.tools.JsonUtils;
import com.zykj.hunqianshiai.user_home.UserPageActivity;

import java.util.List;

import butterknife.Bind;
import io.rong.imkit.RongIM;

public class GoagoActivity extends BasesActivity implements BaseView<String>, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.rv_home_recycler)
    RecyclerView mRecyclerView;

    private BasePresenterImpl mPresenter;
    private int page = 1;
    private HomeAdapter mHomeAdapter;

    @Override
    protected int getContentViewX() {
        return R.layout.activity_goago;
    }

    @Override
    protected void initView() {
        appBar("逛一逛");
        Bundle bundle = getIntent().getExtras();
        String sex = bundle.getString("sex");
        mPresenter = new BasePresenterImpl(this, new BaseModelImpl());
        mParams.clear();
        mParams.put("sex", sex);
        mParams.put("page", page);
        mParams.put("size", 6);
        mPresenter.getData(UrlContent.HOME_PAGE_URL, mParams, BaseModel.DEFAULT_TYPE);
    }

    @Override
    public void success(String bean) {
        hideDialog();
        final HomeBean homeBean = JsonUtils.GsonToBean(bean, HomeBean.class);
        List<HomeBean.HomeData> data = homeBean.data;


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mHomeAdapter = new HomeAdapter(data);
        mRecyclerView.setAdapter(mHomeAdapter);

        mHomeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<HomeBean.HomeData> data1 = adapter.getData();

//                mBundle.clear();
//                mBundle.putString("userid", data1.get(position).userid);
//                openActivity(UserPageActivity.class, mBundle);
            }
        });

        mHomeAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                List<HomeBean.HomeData> data1 = adapter.getData();
                HomeBean.HomeData homeData = data1.get(position);
                String userid = homeData.userid;
                String username = homeData.username;
                switch (view.getId()) {
                    case R.id.ll_chat:
//                        RongIM.getInstance().startPrivateChat(mContext, "2202","xu");
//                        RongIM.getInstance().startPrivateChat(mContext, "2203", "sfghg");
//                        RongIM.getInstance().startPrivateChat(mContext, userid, username);
                        break;
                }
            }
        });

        mHomeAdapter.setOnLoadMoreListener(this, mRecyclerView);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        if (data.size() < 6) {
            mHomeAdapter.loadMoreEnd();
        }
    }

    @Override
    public void refresh(String bean) {
        HomeBean homeBean = JsonUtils.GsonToBean(bean, HomeBean.class);
        List<HomeBean.HomeData> data = homeBean.data;
        mHomeAdapter.setNewData(data);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void loadMore(String bean) {
        HomeBean homeBean = JsonUtils.GsonToBean(bean, HomeBean.class);
        List<HomeBean.HomeData> data = homeBean.data;
        mHomeAdapter.addData(data);
        mHomeAdapter.loadMoreComplete();
        if (data.size() < 6) {
            mHomeAdapter.loadMoreEnd();
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
        mPresenter.getData(UrlContent.HOME_PAGE_URL, mParams, BaseModel.REFRESH_TYPE);
    }

    @Override
    public void onLoadMoreRequested() {
        page++;
        mParams.clear();
        mParams.put("userid", UrlContent.USER_ID);
        mParams.put("page", page);
        mParams.put("size", 6);
        mPresenter.getData(UrlContent.HOME_PAGE_URL, mParams, BaseModel.LOADING_MORE_TYPE);
    }
}
