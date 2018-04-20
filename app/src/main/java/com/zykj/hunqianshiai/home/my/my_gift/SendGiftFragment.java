package com.zykj.hunqianshiai.home.my.my_gift;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.activities.information.InformationDetailsActivity;
import com.zykj.hunqianshiai.bases.BaseFragment;
import com.zykj.hunqianshiai.bases.BaseModel;
import com.zykj.hunqianshiai.bases.BaseModelImpl;
import com.zykj.hunqianshiai.bases.BasePresenterImpl;
import com.zykj.hunqianshiai.bases.BaseView;
import com.zykj.hunqianshiai.net.UrlContent;
import com.zykj.hunqianshiai.tools.JsonUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by xu on 2017/12/5.
 */

public class SendGiftFragment extends BaseFragment implements BaseView<String> {

    @Bind(R.id.recycler_information)
    RecyclerView mRecyclerView;

    public SendGiftFragment() {
    }

    public static SendGiftFragment getInstance() {
        return Instance.mSendGiftFragment;
    }


    private static class Instance {
        static SendGiftFragment mSendGiftFragment = new SendGiftFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_information;
    }

    @Override
    public void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        BasePresenterImpl presenter = new BasePresenterImpl(this, new BaseModelImpl());
        mParams.clear();
        mParams.put("userid", UrlContent.USER_ID);
        presenter.getData(UrlContent.SEND_GIFT_URL,mParams, BaseModel.DEFAULT_TYPE);
    }

    @Override
    public void success(String bean) {
        SendGiftBean sendGiftBean = JsonUtils.GsonToBean(bean, SendGiftBean.class);
        List<SendGiftBean.SendGiftData> data = sendGiftBean.data;


        SendGiftAdapter sendGiftAdapter = new SendGiftAdapter(data);
        mRecyclerView.setAdapter(sendGiftAdapter);

        sendGiftAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                openActivity(InformationDetailsActivity.class);
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
