package com.zykj.hunqianshiai.home.dynamic.my_dynamic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.zykj.hunqianshiai.home.dynamic.secret_dynamic.SecretDynamicActivity;
import com.zykj.hunqianshiai.home.dynamic.PopupWindowDelete;
import com.zykj.hunqianshiai.home.dynamic.dynamic_details.DynamicDetailsActivity;
import com.zykj.hunqianshiai.net.UrlContent;
import com.zykj.hunqianshiai.tools.JsonUtils;

import java.util.List;

import butterknife.Bind;

/**
 * 我的动态
 */
public class MyDynamicActivity extends BasesActivity implements BaseView<String>, BaseQuickAdapter.RequestLoadMoreListener {

    @Bind(R.id.recycler_dynamic)
    RecyclerView mRecyclerView;

    @Bind(R.id.tv_right)
    TextView mSecret;

    private View mHeadView;
    private BasePresenterImpl mPresenter;
    private MyDynamicAdapter mMyDynamicAdapter;
    private int page = 1;
    private Bundle mBundle;

    @Override
    protected int getContentViewX() {
        return R.layout.activity_my_dynamic;
    }

    @Override
    protected void initView() {
        appBar("我的动态");
        mSecret.setVisibility(View.VISIBLE);
        mSecret.setText("隐私");
        mHeadView = LayoutInflater.from(this).inflate(R.layout.my_dynamic_item_header, null);
        ImageView addDynamic = mHeadView.findViewById(R.id.iv_add_dynamic);
        addDynamic.setOnClickListener(this);

        mBundle = new Bundle();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mPresenter = new BasePresenterImpl(this, new BaseModelImpl());
        mParams.clear();
        mParams.put("userid", UrlContent.USER_ID);
        mParams.put("page", page);
        mParams.put("size", 6);
        mPresenter.getData(UrlContent.MY_DYNAMIC_URL, mParams, BaseModel.DEFAULT_TYPE);
        showDialog();

        //进入屏蔽页面
        mSecret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity(SecretDynamicActivity.class);
            }
        });
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

        List<MyDynamicBean.MyDynamicData> data = myDynamicBean.data;
        mMyDynamicAdapter = new MyDynamicAdapter(data);
        mRecyclerView.setAdapter(mMyDynamicAdapter);
        mMyDynamicAdapter.addHeaderView(mHeadView);

        mMyDynamicAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
                List<MyDynamicBean.MyDynamicData> data1 = adapter.getData();
                final String id = data1.get(position).id;
                switch (view.getId()) {
                    case R.id.tv_private_comment:
                        PopupWindowInformationReply popupWindowInformationReply = new PopupWindowInformationReply(MyDynamicActivity.this);
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
                        PopupWindowDelete popupWindowDelete = new PopupWindowDelete(MyDynamicActivity.this);
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
                                                mMyDynamicAdapter.remove(position);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 104) {
            page = 1;
            mParams.clear();
            mParams.put("userid", UrlContent.USER_ID);
            mParams.put("page", page);
            mParams.put("size", 6);
            mPresenter.getData(UrlContent.MY_DYNAMIC_URL, mParams, BaseModel.REFRESH_TYPE);
        }
    }

    @Override
    public void onLoadMoreRequested() {
        page++;
        mParams.clear();
        mParams.put("userid", UrlContent.USER_ID);
        mParams.put("page", page);
        mParams.put("size", 6);
        mPresenter.getData(UrlContent.MY_DYNAMIC_URL, mParams, BaseModel.LOADING_MORE_TYPE);
    }
}
