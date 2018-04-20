package com.zykj.hunqianshiai.home.home;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BaseBean;
import com.zykj.hunqianshiai.bases.BaseFragment;
import com.zykj.hunqianshiai.bases.BaseModel;
import com.zykj.hunqianshiai.bases.BaseModelImpl;
import com.zykj.hunqianshiai.bases.BasePresenterImpl;
import com.zykj.hunqianshiai.bases.BaseView;
import com.zykj.hunqianshiai.home.OnDoubleClickListener;
import com.zykj.hunqianshiai.home.PopupWindowChatVip;
import com.zykj.hunqianshiai.home.home.face.FacialActivity;
import com.zykj.hunqianshiai.login_register.login.LoginActivity;
import com.zykj.hunqianshiai.net.UrlContent;
import com.zykj.hunqianshiai.seek.SeekActivity;
import com.zykj.hunqianshiai.tools.DoubleClick;
import com.zykj.hunqianshiai.tools.JsonUtils;
import com.zykj.hunqianshiai.user_home.UserPageActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import io.rong.imkit.RongIM;

/**
 * 首页
 * Created by xu on 2017/12/5.
 */

public class HomeFragment extends BaseFragment implements BaseView<String>, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener,OnDoubleClickListener{

    @Bind(R.id.iv_home_message)
    ImageView homeMessage;
    @Bind(R.id.iv_home_seek)
    ImageView homeSeek;
    @Bind(R.id.tv_home_text)
    TextView homeText;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.rv_home_recycler)
    RecyclerView mRecyclerView;

    @Bind(R.id.topLayout)
    RelativeLayout topLayout;

    private BasePresenterImpl mPresenter;
    private int page = 1;
    private HomeAdapter mHomeAdapter;
    private Bundle mBundle;
    private String url;
    private int type = 1;

    //RecyclerView滚动到的item位置
    private int firstPosition;

    public HomeFragment() {
    }

    public static HomeFragment getInstance() {
        return Instance.mHomeFragment;
    }

    private static class Instance {
        static HomeFragment mHomeFragment = new HomeFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void initView() {
        showDialog();
        mBundle = new Bundle();
        mPresenter = new BasePresenterImpl(this, new BaseModelImpl());
        page = 1;
        if (UrlContent.GOAGO) {
            mParams.clear();
            mParams.put("sex", UrlContent.SEX);
            mParams.put("page", page);
            mParams.put("size", 6);
            mPresenter.getData(UrlContent.HOME_PAGE_URL, mParams, BaseModel.DEFAULT_TYPE);
        } else {
            mParams.clear();
            mParams.put("userid", UrlContent.USER_ID);
            mParams.put("page", page);
            mParams.put("size", 6);
            url = UrlContent.HOME_COUNTRY_URL;
            mPresenter.getData(url, mParams, BaseModel.DEFAULT_TYPE);

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
                                UrlContent.IS_MEMBER_KEY = true;
                            } else {
                                UrlContent.IS_MEMBER_KEY = false;
                            }
                        }
                    });
        }

    }

    @OnClick({R.id.iv_home_message, R.id.iv_home_seek, R.id.tv_home_text,R.id.tv_text})
    @Override
    public void onClick(View view) {
        if (UrlContent.GOAGO) {
            openActivity(LoginActivity.class);
            return;
        }
        switch (view.getId()) {
            case R.id.tv_text:
            case R.id.iv_home_message://活动
//                openActivity(FaceActivity.class);
                // 进入相册 以下是例子：用不到的api可以不写
//                openActivity(ActivitiesActivity.class);
                openActivity(FacialActivity.class);
                break;
            case R.id.iv_home_seek://搜索
                openActivity(SeekActivity.class);
                break;
            case R.id.tv_home_text:
//                toastShow("tv_home_text");
                String trim = homeText.getText().toString().trim();
                page = 1;
                if (trim.equals("同城")) {
                    homeText.setText("全国");
                    mParams.put("userid", UrlContent.USER_ID);
                    mParams.put("page", page);
                    mParams.put("size", 6);
                    url = UrlContent.HOME_COUNTRY_URL;
                    mPresenter.getData(UrlContent.HOME_COUNTRY_URL, mParams, BaseModel.REFRESH_TYPE);
                } else {

                    homeText.setText("同城");
                    mParams.put("userid", UrlContent.USER_ID);
                    mParams.put("page", page);
                    mParams.put("size", 6);
                    url = UrlContent.HOME_CITY_URL;
                    mPresenter.getData(UrlContent.HOME_CITY_URL, mParams, BaseModel.DEFAULT_TYPE);
                }
                break;
        }
    }

    @Override
    public void success(String bean) {
        hideDialog();
        final HomeBean homeBean = JsonUtils.GsonToBean(bean, HomeBean.class);
        List<HomeBean.HomeData> data = homeBean.data;


        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mHomeAdapter = new HomeAdapter(data);
        mRecyclerView.setAdapter(mHomeAdapter);

        /*==================添加双击标题栏回到顶部，优化动画效果=====================*/

        //给tvText注册双击监听器
        DoubleClick.registerDoubleClickListener(topLayout,this);
        //监听RecyclerView滚动位置firstPosition，并将firstPosition值传递给GoTopTask任务
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                RecyclerView.LayoutManager lm = recyclerView.getLayoutManager();
                if(lm instanceof LinearLayoutManager){
                    LinearLayoutManager linearManager = (LinearLayoutManager) lm;
                    //获取第一个可见view的位置
                    int firstItemPosition = linearManager.findFirstVisibleItemPosition();
                    firstPosition = firstItemPosition;
                }
            }
        });

        mHomeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //firstposition = position;
                if (UrlContent.GOAGO) {
                    openActivity(LoginActivity.class);
                    return;
                }
                List<HomeBean.HomeData> data1 = adapter.getData();
                mBundle.clear();
                mBundle.putString("userid", data1.get(position).userid);
                openActivity(UserPageActivity.class, mBundle);
            }
        });

        mHomeAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (UrlContent.GOAGO) {
                    openActivity(LoginActivity.class);
                    return;
                }
                List<HomeBean.HomeData> data1 = adapter.getData();
                HomeBean.HomeData homeData = data1.get(position);
                final String userid = homeData.userid;
                final String username = homeData.username;
                switch (view.getId()) {
                    case R.id.ll_chat:
//                        RongIM.getInstance().startPrivateChat(mContext, "2202","xu");
//                        RongIM.getInstance().startPrivateChat(mContext, "2203", "sfghg");

                        mParams.clear();
                        mParams.put("userid", UrlContent.USER_ID);
                        mParams.put("other_id", userid);
                        mParams.put("rdm", UrlContent.RDM);
                        mParams.put("sign", UrlContent.SIGN);

                        OkGo.<String>post(UrlContent.CHAT_COUNT_URL)
                                .params(mParams)
                                .execute(new StringCallback() {
                                    @Override
                                    public void onSuccess(Response<String> response) {
                                        BaseBean baseBean = JsonUtils.GsonToBean(response.body(), BaseBean.class);
                                        if (baseBean.code == 200) {
                                            RongIM.getInstance().startPrivateChat(mContext, userid, username);
                                        } else {
                                            PopupWindowChatVip popupWindowChatVip = new PopupWindowChatVip(mActivity);
                                            popupWindowChatVip.showAtLocation(homeText, Gravity.CENTER, 0, 0);
                                        }
                                    }
                                });

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

        if (UrlContent.GOAGO) {
            mParams.clear();
            mParams.put("sex", UrlContent.SEX);
            mParams.put("page", page);
            mParams.put("size", 6);
            mPresenter.getData(UrlContent.HOME_PAGE_URL, mParams, BaseModel.REFRESH_TYPE);
        } else {
            mParams.clear();
            mParams.put("userid", UrlContent.USER_ID);
            mParams.put("page", page);
            mParams.put("size", 6);
            if (type == 1) {
                type = 2;
                url = UrlContent.HOME_COUNTRY_URL;
            } else if (type == 2) {
                type = 1;
                url = UrlContent.HOME_CITY_URL;
            }
            mPresenter.getData(url, mParams, BaseModel.REFRESH_TYPE);
        }

    }

    @Override
    public void onLoadMoreRequested() {
        page++;
        if (UrlContent.GOAGO) {
            mParams.clear();
            mParams.put("sex", UrlContent.SEX);
            mParams.put("page", page);
            mParams.put("size", 6);
            mPresenter.getData(UrlContent.HOME_PAGE_URL, mParams, BaseModel.LOADING_MORE_TYPE);
        } else {
            mParams.clear();
            mParams.put("userid", UrlContent.USER_ID);
            mParams.put("page", page);
            mParams.put("size", 6);
            mPresenter.getData(url, mParams, BaseModel.LOADING_MORE_TYPE);
        }
    }

    /*===================监听点击事件====================*/
    //监听单击
    @Override
    public void OnSingleClick(View v) {

    }
    //监听双击
    @Override
    public void OnDoubleClick(View v) {
        GoTopTask task=new GoTopTask(mRecyclerView);
        task.execute(firstPosition);
    }

}
