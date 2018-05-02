package com.zykj.hunqianshiai.home.dynamic;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.activities.information.PopupWindowInformationReply;
import com.zykj.hunqianshiai.bases.BaseFragment;
import com.zykj.hunqianshiai.bases.BaseModel;
import com.zykj.hunqianshiai.bases.BaseModelImpl;
import com.zykj.hunqianshiai.bases.BasePopupWindow;
import com.zykj.hunqianshiai.bases.BasePresenterImpl;
import com.zykj.hunqianshiai.bases.BaseView;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.home.dynamic.dynamic_details.DynamicDetailsActivity;
import com.zykj.hunqianshiai.home.dynamic.my_dynamic.IssueDynamicActivity;
import com.zykj.hunqianshiai.home.dynamic.my_dynamic.MyDynamicActivity;
import com.zykj.hunqianshiai.net.UrlContent;
import com.zykj.hunqianshiai.tools.JsonUtils;
import com.zykj.hunqianshiai.user_home.UserPageActivity;
import com.zykj.hunqianshiai.user_home.dynamic.UserDynamicAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 动态
 * Created by xu on 2017/12/5.
 */

public class DynamicFragment extends BaseFragment implements BaseView<String>, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.recycler_dynamic)
    RecyclerView mRecyclerView;
    @Bind(R.id.tv_condition)
    TextView condition;
    @Bind(R.id.ll_layout)
    LinearLayout mLinearLayout;
    @Bind(R.id.iv_head)
    ImageView iv_head;
    @Bind(R.id.tv_title)
    TextView tv_title;

    private BasePresenterImpl mPresenter;
    private UserDynamicAdapter mUserDynamicAdapter;
    private int page = 1;
    private Bundle mBundle;

    public DynamicFragment() {
    }

    public static DynamicFragment getInstance() {
        return Instance.mDynamicFragment;
    }

    private static class Instance {
        static DynamicFragment mDynamicFragment = new DynamicFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_dynamic;
    }

    @Override
    public void initView() {
        //onRefresh();

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);



        Glide.with(mContext)
                .load(UrlContent.USER_PIC)
                .apply(BasesActivity.mCircleRequestOptions)
                .into(iv_head);
        tv_title.setText(UrlContent.USER_NAME);

        mBundle = new Bundle();

        mPresenter = new BasePresenterImpl(this, new BaseModelImpl());
        mParams.clear();
        mParams.put("userid", UrlContent.USER_ID);
//        mParams.put("city", 0);
//        mParams.put("other", 0);
//        mParams.put("me", 0);

        mParams.put("page", page);
        mParams.put("size", 15);
        mPresenter.getData(UrlContent.ALL_DYNAMIC_URL, mParams, BaseModel.DEFAULT_TYPE);

    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        mPresenter = new BasePresenterImpl(this, new BaseModelImpl());
//        mParams.clear();
//        mParams.put("userid", UrlContent.USER_ID);
//        mParams.put("page", page);
//        mParams.put("size", 12);
//        mPresenter.getData(UrlContent.ALL_DYNAMIC_URL, mParams, BaseModel.DEFAULT_TYPE);
//    }

    @OnClick({R.id.iv_like, R.id.ll_my_dynamic, R.id.tv_condition, R.id.tv_all, R.id.tv_same_city, R.id.tv_my_like, R.id.tv_like_me, R.id.iv_add_dynamic})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.iv_like://点赞评论
                mBundle.clear();
                mBundle.putString("userid", UrlContent.USER_ID);
                openActivity(LikeCommentActivity.class, mBundle);
                break;
            case R.id.ll_my_dynamic://我的动态
                openActivity(MyDynamicActivity.class);
                break;
            case R.id.tv_condition://全部
                if (mLinearLayout.getVisibility() == View.VISIBLE) {
                    mLinearLayout.setVisibility(View.GONE);
                } else {
                    mLinearLayout.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.tv_all:
                mLinearLayout.setVisibility(View.GONE);
                condition.setText("全部");
                mParams.clear();
                mParams.put("userid", UrlContent.USER_ID);
                mParams.put("page", page);
                mParams.put("size", 12);
                mPresenter.getData(UrlContent.ALL_DYNAMIC_URL, mParams, BaseModel.REFRESH_TYPE);
                break;
            case R.id.tv_same_city:
                mLinearLayout.setVisibility(View.GONE);
                condition.setText("只看同城");
                mParams.clear();
                mParams.put("userid", UrlContent.USER_ID);
                mParams.put("city", 1);
                mParams.put("page", page);
                mParams.put("size", 12);
                mPresenter.getData(UrlContent.ALL_DYNAMIC_URL, mParams, BaseModel.REFRESH_TYPE);
                break;
            case R.id.tv_my_like:
                mLinearLayout.setVisibility(View.GONE);
                condition.setText("我心动的");
                mParams.clear();
                mParams.put("userid", UrlContent.USER_ID);
                mParams.put("me", 1);
                mParams.put("page", page);
                mParams.put("size", 12);
                mPresenter.getData(UrlContent.ALL_DYNAMIC_URL, mParams, BaseModel.REFRESH_TYPE);
                break;
            case R.id.tv_like_me:
                mLinearLayout.setVisibility(View.GONE);
                condition.setText("心动我的");
                mParams.clear();
                mParams.put("userid", UrlContent.USER_ID);
                mParams.put("other", 1);
                mParams.put("page", page);
                mParams.put("size", 12);
                mPresenter.getData(UrlContent.ALL_DYNAMIC_URL, mParams, BaseModel.REFRESH_TYPE);
                break;
            case R.id.iv_add_dynamic://添加动态
                openActivity(IssueDynamicActivity.class);
                break;
            default:
                break;
        }
    }

    @Override
    public void success(String bean) {
        DynamicBean dynamicBean = JsonUtils.GsonToBean(bean, DynamicBean.class);
        List<DynamicBean.DynamicData> data = dynamicBean.data;
        mUserDynamicAdapter = new UserDynamicAdapter(data);
        mRecyclerView.setAdapter(mUserDynamicAdapter);

        mUserDynamicAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<DynamicBean.DynamicData> data1 = adapter.getData();
                String id = data1.get(position).id;
                mBundle.clear();
                mBundle.putString("friends_id", id);
                openActivity(DynamicDetailsActivity.class, mBundle);
            }
        });

        mUserDynamicAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
                final List<DynamicBean.DynamicData> data1 = adapter.getData();
                final String id = data1.get(position).id;
                final String userid = data1.get(position).userid;

                switch (view.getId()) {
                    case R.id.iv_user_heard:
                        mBundle.clear();
                        mBundle.putString("userid",userid);
                        openActivity(UserPageActivity.class,mBundle);
                        break;
                    case R.id.iv_more_option://更多选项
                        if (userid.equals(UrlContent.USER_ID)) {
                            PopupWindowDelete popupWindowDelete = new PopupWindowDelete(getActivity());
                            popupWindowDelete.showAtLocation(mRecyclerView, Gravity.BOTTOM, 0, 0);
                            popupWindowDelete.setClickListener(new BasePopupWindow.ClickListener() {
                                @Override
                                public void onClickListener(Object object) {
                                    mParams.clear();
                                    mParams.put("userid", UrlContent.USER_ID);
                                    mParams.put("friend_id", id);
                                    mParams.put("rdm", UrlContent.RDM);
                                    mParams.put("sign", UrlContent.SIGN);
                                    OkGo.<String>post(UrlContent.DELETE_DYNAMIC_URL)
                                            .params(mParams)
                                            .execute(new StringCallback() {
                                                @Override
                                                public void onSuccess(Response<String> response) {
                                                    mUserDynamicAdapter.remove(position);
                                                }
                                            });
                                }
                            });
                        } else {
                            PopupWindowMoreOption popupWindowMoreOption = new PopupWindowMoreOption(mActivity, id, userid);
                            popupWindowMoreOption.showAtLocation(condition, Gravity.BOTTOM, 0, 0);
                            popupWindowMoreOption.setClickListener(new BasePopupWindow.ClickListener() {
                                @Override
                                public void onClickListener(Object object) {
                                    mParams.clear();

                                    mParams.put("rdm", UrlContent.RDM);
                                    mParams.put("sign", UrlContent.SIGN);
                                    mParams.put("type", 0);
                                    if (object.equals("0")) {
                                        mParams.put("userid", UrlContent.USER_ID);
                                        mParams.put("other_id", userid);

                                    } else {
                                        mParams.put("userid", userid);
                                        mParams.put("other_id", UrlContent.USER_ID);
                                    }
                                    OkGo.<String>post(UrlContent.SEE_URL)
                                            .params(mParams)
                                            .execute(new StringCallback() {
                                                @Override
                                                public void onSuccess(Response<String> response) {
                                                    onRefresh();
                                                }
                                            });
                                }
                            });
                        }

                        break;
                    case R.id.tv_private_comment://私密评论
                        PopupWindowInformationReply popupWindowInformationReply = new PopupWindowInformationReply(mActivity);
                        popupWindowInformationReply.showAtLocation(condition, Gravity.BOTTOM, 0, 0);
                        popupWindowInformationReply.setClickListener(new BasePopupWindow.ClickListener() {
                            @Override
                            public void onClickListener(Object object) {

                                mParams.clear();
                                mParams.put("userid", UrlContent.USER_ID);
                                mParams.put("friend_id", id);
                                mParams.put("content", object.toString());
                                mParams.put("rdm", UrlContent.RDM);
                                mParams.put("sign", UrlContent.SIGN);
                                OkGo.<String>post(UrlContent.COMMENT_DYNAMIC_URL)
                                        .params(mParams)
                                        .execute(new StringCallback() {
                                            @Override
                                            public void onSuccess(Response<String> response) {
//                                                onRefresh();
                                            }
                                        });
                            }
                        });
                        break;
                    default:
                        break;
                }
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mUserDynamicAdapter.setOnLoadMoreListener(this, mRecyclerView);

        if (data.size() < 6) {
            mUserDynamicAdapter.loadMoreEnd();
        }
    }

    @Override
    public void onRefresh() {
        page = 1;
        mParams.clear();
        mParams.put("userid", UrlContent.USER_ID);
        String trim = condition.getText().toString().trim();
        if (trim.equals("只看同城")) {
            mParams.put("city", 1);
        } else if (trim.equals("我心动的")) {
            mParams.put("me", 1);
        } else if (trim.equals("心动我的")) {
            mParams.put("other", 1);
        }
        mParams.put("page", page);
        mParams.put("size", 12);
        mPresenter.getData(UrlContent.ALL_DYNAMIC_URL, mParams, BaseModel.REFRESH_TYPE);
    }

    @Override
    public void onLoadMoreRequested() {
        page += 1;
        mParams.clear();
        mParams.put("userid", UrlContent.USER_ID);
        String trim = condition.getText().toString().trim();
        if (trim.equals("只看同城")) {
            mParams.put("city", 1);
        } else if (trim.equals("我心动的")) {
            mParams.put("me", 1);
        } else if (trim.equals("心动我的")) {
            mParams.put("other", 1);
        }
        mParams.put("page", page);
        mParams.put("size", 12);
        mPresenter.getData(UrlContent.ALL_DYNAMIC_URL, mParams, BaseModel.LOADING_MORE_TYPE);
    }

    @Override
    public void refresh(String bean) {
        DynamicBean dynamicBean = JsonUtils.GsonToBean(bean, DynamicBean.class);
        List<DynamicBean.DynamicData> data = dynamicBean.data;
        mUserDynamicAdapter.setNewData(data);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void loadMore(String bean) {
        DynamicBean dynamicBean = JsonUtils.GsonToBean(bean, DynamicBean.class);
        List<DynamicBean.DynamicData> data = dynamicBean.data;
        mUserDynamicAdapter.addData(data);
        mUserDynamicAdapter.loadMoreComplete();
        if (data.size() < 6) {
            mUserDynamicAdapter.loadMoreEnd();
        }
    }

    @Override
    public void failed(String content) {

    }
}
