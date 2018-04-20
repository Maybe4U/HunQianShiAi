package com.zykj.hunqianshiai.user_home.dynamic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.activities.information.PopupWindowInformationReply;
import com.zykj.hunqianshiai.bases.BaseModel;
import com.zykj.hunqianshiai.bases.BaseModelImpl;
import com.zykj.hunqianshiai.bases.BasePopupWindow;
import com.zykj.hunqianshiai.bases.BasePresenterImpl;
import com.zykj.hunqianshiai.bases.BaseView;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.home.dynamic.PopupWindowDelete;
import com.zykj.hunqianshiai.home.dynamic.PopupWindowMoreOption;
import com.zykj.hunqianshiai.home.dynamic.dynamic_details.DynamicDetailsActivity;
import com.zykj.hunqianshiai.home.dynamic.my_dynamic.IssueDynamicActivity;
import com.zykj.hunqianshiai.home.dynamic.my_dynamic.MyDynamicActivity;
import com.zykj.hunqianshiai.home.dynamic.my_dynamic.MyDynamicAdapter;
import com.zykj.hunqianshiai.home.dynamic.my_dynamic.MyDynamicBean;
import com.zykj.hunqianshiai.net.UrlContent;
import com.zykj.hunqianshiai.tools.JsonUtils;

import java.util.List;

import butterknife.Bind;

/**
 * TAd的动态
 */
public class UserDynamicActivity extends BasesActivity implements BaseView<String>, BaseQuickAdapter.RequestLoadMoreListener {

    //    @Bind(R.id.swipe_refresh)
//    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.recycler_dynamic)
    RecyclerView mRecyclerView;
    private BasePresenterImpl mPresenter;
    private int page = 1;
    private MyDynamicAdapter mMyDynamicAdapter;
    private String mUserid;
    private Bundle mBundle;

    @Override
    protected int getContentViewX() {
        return R.layout.activity_user_dynamic;
    }

    @Override
    protected void initView() {
        appBar("TA的动态");
        mBundle = getIntent().getExtras();
        mUserid = mBundle.getString("userid");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mPresenter = new BasePresenterImpl(this, new BaseModelImpl());
        mParams.clear();
        mParams.put("userid", mUserid);
        mParams.put("page", page);
        mParams.put("size", 6);
        mPresenter.getData(UrlContent.MY_DYNAMIC_URL, mParams, BaseModel.DEFAULT_TYPE);
        showDialog();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.iv_add_dynamic:
                Intent intent = new Intent(this, IssueDynamicActivity.class);
                startActivityForResult(intent, 103);

                break;
            default:
                break;
        }
    }

    @Override
    public void success(String bean) {
        hideDialog();
        MyDynamicBean myDynamicBean = JsonUtils.GsonToBean(bean, MyDynamicBean.class);
        if (myDynamicBean.code != 200) {
            toastShow(myDynamicBean.message);
            return;
        }

        final List<MyDynamicBean.MyDynamicData> data = myDynamicBean.data;
        mMyDynamicAdapter = new MyDynamicAdapter(data);
        mRecyclerView.setAdapter(mMyDynamicAdapter);

        mMyDynamicAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                List<MyDynamicBean.MyDynamicData> data1 = adapter.getData();
                final String id = data1.get(position).id;
                switch (view.getId()) {
                    case R.id.tv_private_comment:
                        PopupWindowInformationReply popupWindowInformationReply = new PopupWindowInformationReply(UserDynamicActivity.this);
                        popupWindowInformationReply.showAtLocation(mRecyclerView, Gravity.BOTTOM, 0, 0);
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

                                            }
                                        });
                            }
                        });
                        break;
                    case R.id.iv_more_option:
                        PopupWindowMoreOption popupWindowMoreOption = new PopupWindowMoreOption(UserDynamicActivity.this, id, mUserid);
                        popupWindowMoreOption.showAtLocation(mRecyclerView, Gravity.BOTTOM, 0, 0);
                        popupWindowMoreOption.setClickListener(new BasePopupWindow.ClickListener() {
                            @Override
                            public void onClickListener(Object object) {
                                mParams.clear();
                                mParams.put("userid", UrlContent.USER_ID);
                                mParams.put("friend_id", mUserid);
                                mParams.put("rdm", UrlContent.RDM);
                                mParams.put("sign", UrlContent.SIGN);
                                if (object.equals("0")) {
                                    mParams.put("type", 0);
                                } else {
                                    mParams.put("type", 1);
                                }
                                OkGo.<String>post(UrlContent.SEE_URL)
                                        .params(mParams)
                                        .execute(new StringCallback() {
                                            @Override
                                            public void onSuccess(Response<String> response) {
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

        mMyDynamicAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<MyDynamicBean.MyDynamicData> data1 = adapter.getData();
                String id = data1.get(position).id;
                mBundle.clear();
                mBundle.putString("friends_id", id);
                openActivity(DynamicDetailsActivity.class, mBundle);
            }
        });

        mMyDynamicAdapter.setOnLoadMoreListener(this, mRecyclerView);
    }

    @Override
    public void refresh(String bean) {
        MyDynamicBean myDynamicBean = JsonUtils.GsonToBean(bean, MyDynamicBean.class);
        if (myDynamicBean.code == 200) {
            List<MyDynamicBean.MyDynamicData> data = myDynamicBean.data;

            mMyDynamicAdapter.setNewData(data);
        }
    }

    @Override
    public void loadMore(String bean) {
        MyDynamicBean myDynamicBean = JsonUtils.GsonToBean(bean, MyDynamicBean.class);

        List<MyDynamicBean.MyDynamicData> data = myDynamicBean.data;

        mMyDynamicAdapter.addData(data);
        mMyDynamicAdapter.loadMoreComplete();
        if (data.size() < 6) {
            mMyDynamicAdapter.loadMoreEnd();
        }

    }

    @Override
    public void failed(String content) {

    }

    @Override
    public void onLoadMoreRequested() {
        page++;
        mParams.clear();
        mParams.put("userid", mUserid);
        mParams.put("page", page);
        mParams.put("size", 6);
        mPresenter.getData(UrlContent.MY_DYNAMIC_URL, mParams, BaseModel.LOADING_MORE_TYPE);
    }
}
