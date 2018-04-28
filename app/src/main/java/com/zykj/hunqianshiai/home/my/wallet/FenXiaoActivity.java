package com.zykj.hunqianshiai.home.my.wallet;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BaseModel;
import com.zykj.hunqianshiai.bases.BaseModelImpl;
import com.zykj.hunqianshiai.bases.BasePresenterImpl;
import com.zykj.hunqianshiai.bases.BaseView;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.home.my.set.ShareActivity;
import com.zykj.hunqianshiai.net.UrlContent;
import com.zykj.hunqianshiai.tools.JsonUtils;
import com.zykj.hunqianshiai.user_home.UserPageActivity;

import java.io.File;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class FenXiaoActivity extends BasesActivity implements BaseView<String> {
    @Bind(R.id.tv_right)
    TextView tv_right;
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private BasePresenterImpl mPresenter;
    private int page = 1;
    private Bundle mBundle;

    @Override
    protected int getContentViewX() {
        return R.layout.activity_fen_xiao;
    }

    @Override
    protected void initView() {
        appBar("我的代理");
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText("邀请好友");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mBundle = new Bundle();
        mPresenter = new BasePresenterImpl(this, new BaseModelImpl());
        mParams.clear();
        mParams.put("userid", "2202");
        mParams.put("page", page);
        mParams.put("size", 10);
        mPresenter.getData(UrlContent.FEN_XIAO_URL, mParams, BaseModel.DEFAULT_TYPE);
    }

    @OnClick({R.id.tv_right})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_right:
                mBundle.clear();
                mBundle.putString("title", "分享");
                mBundle.putString("url", "http://47.104.91.22/api.php?c=Fell&a=fenxiang&userid="+ UrlContent.USER_ID + "&comeuserid=" + UrlContent.USER_ID);
                openActivity(ShareActivity.class, mBundle);
                break;
        }
    }

    @Override
    public void success(String bean) {
        FenXiaoBean fenXiaoBean = JsonUtils.GsonToBean(bean, FenXiaoBean.class);
         List<FenXiaoBean.FenXiaoData> data = fenXiaoBean.data;

        //逆序排列
        //Collections.reverse(data);

        FenXiaoAdapter fenXiaoAdapter = new FenXiaoAdapter(data);
        mRecyclerView.setAdapter(fenXiaoAdapter);

        fenXiaoAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<FenXiaoBean.FenXiaoData> data1 = adapter.getData();
                FenXiaoBean.FenXiaoData fenXiaoData = data1.get(position);
                mBundle.clear();
                mBundle.putString("userid", fenXiaoData.userid);
                openActivity(UserPageActivity.class, mBundle);
            }
        });
    }

    @Override
    public void refresh(String bean) {

    }

    @Override
    public void loadMore(String bean) {

    }

    @Override
    public void failed(String content) {

    }
}
