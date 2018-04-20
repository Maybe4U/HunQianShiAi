package com.zykj.hunqianshiai.home.message.system_message;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BaseModel;
import com.zykj.hunqianshiai.bases.BaseModelImpl;
import com.zykj.hunqianshiai.bases.BasePresenterImpl;
import com.zykj.hunqianshiai.bases.BaseView;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.net.UrlContent;
import com.zykj.hunqianshiai.tools.JsonUtils;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 系统通知
 */
public class SystemMessageActivity extends BasesActivity implements BaseView<String> {

    @Bind(R.id.recycler_system)
    RecyclerView mRecyclerView;

    @Override
    protected int getContentViewX() {
        return R.layout.activity_system_message;
    }

    @Override
    protected void initView() {
        appBar("系统通知");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        Bundle bundle = getIntent().getExtras();
        String userid = bundle.getString("userid");
        BasePresenterImpl presenter = new BasePresenterImpl(this, new BaseModelImpl());
        mParams.clear();
        mParams.put("userid", userid);
        presenter.getData(UrlContent.SYSTEM_MESSAGE_URL, mParams, BaseModel.DEFAULT_TYPE);

    }

    @Override
    public void success(String bean) {
        SystemMessageBean systemMessageBean = JsonUtils.GsonToBean(bean, SystemMessageBean.class);
        List<SystemMessageBean.SystemMessageData> data = systemMessageBean.data;
        SystemMessageAdapter systemMessageAdapter = new SystemMessageAdapter(data);
        mRecyclerView.setAdapter(systemMessageAdapter);
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
