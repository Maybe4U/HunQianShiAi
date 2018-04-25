package com.zykj.hunqianshiai.home.message.system_message;

/**
 * Created by Maybe on 2018/4/25.
 */


import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BaseModel;
import com.zykj.hunqianshiai.bases.BaseModelImpl;
import com.zykj.hunqianshiai.bases.BasePresenterImpl;
import com.zykj.hunqianshiai.bases.BaseView;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.net.UrlContent;
import com.zykj.hunqianshiai.tools.JsonUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 系统通知详情
 */
public class SysMsgDetailsActivity extends BasesActivity implements BaseView<String> {

    @Bind(R.id.iv_back)
    ImageView mIvBack;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.iv_right_share)
    ImageView mIvRightShare;
    @Bind(R.id.tv_right)
    TextView mTvRight;
    @Bind(R.id.app_bar_layout)
    RelativeLayout mAppBarLayout;
    @Bind(R.id.tv_time)
    TextView mTvTime;
    @Bind(R.id.tv_content)
    TextView mTvContent;


//    private SystemMessageAdapter mSystemMessageAdapter;
//
//    private List<SystemMessageBean.SystemMessageData> mData;
//    private TextView mContent;
//    private String mContentText;

    @Override
    protected int getContentViewX() {
        return R.layout.activity_sysmsg_detail;
    }

    @Override
    protected void initView() {
        appBar("通知详情");

        View view = View.inflate(this, R.layout.system_message_imte, null);
        //mContent = (TextView) view.findViewById(R.id.tv_content);

        Bundle bundle = getIntent().getExtras();
        String content = bundle.getString("content");
        String time = bundle.getString("time");
        String title = bundle.getString("title");

        mTvTime.setText(time);
        mTvTitle.setText(title);
        mTvContent.setText(content);

//        BasePresenterImpl presenter = new BasePresenterImpl(this, new BaseModelImpl());
//        mParams.clear();
//        mParams.put("userid", userid);
//        presenter.getData(UrlContent.SYSTEM_MESSAGE_URL, mParams, BaseModel.DEFAULT_TYPE);
    }


    @Override
    public void success(String bean) {
//        SystemMessageBean systemMessageBean = JsonUtils.GsonToBean(bean, SystemMessageBean.class);
//        mData = systemMessageBean.data;
//
//        mSystemMessageAdapter = new SystemMessageAdapter(mData);
//        mRecyclerView.setAdapter(mSystemMessageAdapter);
//
//        Bundle bundle = new Bundle();
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

