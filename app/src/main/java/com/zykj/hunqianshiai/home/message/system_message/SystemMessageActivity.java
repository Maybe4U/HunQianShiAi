package com.zykj.hunqianshiai.home.message.system_message;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhihu.matisse.internal.utils.UIUtils;
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
    private SystemMessageAdapter mSystemMessageAdapter;

    private List<SystemMessageBean.SystemMessageData> mData;
    private TextView mContent;
    private String mContentText;

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

        View view = View.inflate(this,R.layout.system_message_imte,null);
        mContent = (TextView)view.findViewById(R.id.tv_content);

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
        mData = systemMessageBean.data;

        mSystemMessageAdapter = new SystemMessageAdapter(mData);
        mRecyclerView.setAdapter(mSystemMessageAdapter);

        Bundle bundle = new Bundle();


        mSystemMessageAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                bundle.clear();
                bundle.putString("content",mData.get(position).content);
                bundle.putString("time",mData.get(position).time);
                bundle.putString("title",mData.get(position).title);
                openActivity(SysMsgDetailsActivity.class,bundle);
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
