package com.zykj.hunqianshiai.home.my.online;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BaseBean;
import com.zykj.hunqianshiai.bases.BaseModel;
import com.zykj.hunqianshiai.bases.BaseModelImpl;
import com.zykj.hunqianshiai.bases.BasePresenterImpl;
import com.zykj.hunqianshiai.bases.BaseView;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.net.UrlContent;
import com.zykj.hunqianshiai.tools.JsonUtils;
import com.zykj.hunqianshiai.user_home.personal_assistant.OnlineTimeBean;
import com.zykj.hunqianshiai.user_home.personal_assistant.OpenCounselorActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 上线提醒
 */
public class OnlineActivity extends BasesActivity implements BaseView<String> {

    @Bind(R.id.tv_time)
    TextView tv_time;
    @Bind(R.id.recycler_online)
    RecyclerView mRecyclerView;
    private BasePresenterImpl mPresenter;
    private Bundle mBundle;

    @Override
    protected int getContentViewX() {
        return R.layout.activity_online;
    }

    @Override
    protected void initView() {
        appBar("上线提醒");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mBundle = new Bundle();

        mPresenter = new BasePresenterImpl(this, new BaseModelImpl());
        mParams.clear();
        mParams.put("userid", UrlContent.USER_ID);
        mParams.put("other_id", UrlContent.USER_ID);
        mPresenter.getData(UrlContent.ONLINE_LIST_URL, mParams, BaseModel.DEFAULT_TYPE);

        mParams.clear();
        mParams.put("userid", UrlContent.USER_ID);
        mParams.put("type", 3);
        mPresenter.getData(UrlContent.GET_STATE_URL, mParams, BaseModel.REFRESH_TYPE);
    }

    @Override
    public void success(String bean) {
        OnlineBean onlineBean = JsonUtils.GsonToBean(bean, OnlineBean.class);
        List<OnlineBean.OnlineData> data = onlineBean.data;
        final OnlineAdapter onlineAdapter = new OnlineAdapter(data);
        mRecyclerView.setAdapter(onlineAdapter);

        onlineAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                List<OnlineBean.OnlineData> data1 = adapter.getData();
                OnlineBean.OnlineData onlineData = data1.get(position);
                String userid = onlineData.userid;
                switch (view.getId()) {
                    case R.id.tv_remove:
                        onlineAdapter.remove(position);
                        mParams.clear();
                        mParams.put("userid", UrlContent.USER_ID);
                        mParams.put("other_id", userid);
                        mPresenter.getData(UrlContent.REMOVE_REMIND_URL, mParams, BaseModel.LOADING_MORE_TYPE);
                        break;
                }
            }
        });
    }

    @Override
    public void refresh(String bean) {
        BaseBean baseBean = JsonUtils.GsonToBean(bean, BaseBean.class);
        if (baseBean.code != 200) {

            return;
        }
        OnlineTimeBean onlineTimeBean = JsonUtils.GsonToBean(bean, OnlineTimeBean.class);
        if (null != onlineTimeBean.data) {
            String endtime = onlineTimeBean.data.endtime;
            if (null != endtime) {
                tv_time.setText(endtime);
            }
        }
    }

    @Override
    public void loadMore(String bean) {

    }

    @Override
    public void failed(String content) {

    }

    @OnClick({R.id.tv_renew})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_renew:
                mBundle.clear();
                mBundle.putString("title", "上线提醒");
                mBundle.putString("type1", "4");
                mBundle.putString("type2", "3");
                mBundle.putString("type3", "8");
                openActivity(OpenCounselorActivity.class, mBundle);
                break;
        }
    }
}
