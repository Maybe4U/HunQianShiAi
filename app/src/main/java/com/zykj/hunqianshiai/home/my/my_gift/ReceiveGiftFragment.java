package com.zykj.hunqianshiai.home.my.my_gift;

import android.os.Bundle;
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
import com.zykj.hunqianshiai.home.my.wallet.FenXiaoBean;
import com.zykj.hunqianshiai.net.UrlContent;
import com.zykj.hunqianshiai.tools.JsonUtils;
import com.zykj.hunqianshiai.user_home.UserPageActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 收到礼物
 * Created by xu on 2017/12/5.
 */

public class ReceiveGiftFragment extends BaseFragment implements BaseView<String> {

    @Bind(R.id.recycler_information)
    RecyclerView mRecyclerView;
    private BasePresenterImpl mPresenter;

    public ReceiveGiftFragment() {
    }

    public static ReceiveGiftFragment getInstance() {
        return Instance.mReceiveGiftFragment;
    }


    private static class Instance {
        static ReceiveGiftFragment mReceiveGiftFragment = new ReceiveGiftFragment();
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

        mPresenter = new BasePresenterImpl(this, new BaseModelImpl());
        mParams.clear();
        mParams.put("userid",UrlContent.USER_ID);
        mPresenter.getData(UrlContent.RECEIVE_GIFT_URL, mParams, BaseModel.DEFAULT_TYPE);

    }

    @Override
    public void success(String bean) {
        SendGiftBean sendGiftBean = JsonUtils.GsonToBean(bean, SendGiftBean.class);
        List<SendGiftBean.SendGiftData> data = sendGiftBean.data;
        ReceiveGiftAdapter receiveGiftAdapter = new ReceiveGiftAdapter(data);
        mRecyclerView.setAdapter(receiveGiftAdapter);

        receiveGiftAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                openActivity(InformationDetailsActivity.class);
            }
        });

        receiveGiftAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()){
                    case R.id.iv_headpic:
                        String userid = data.get(position).userinfo.userid;
                        Bundle mBundle = new Bundle();
                        mBundle.clear();
                        mBundle.putString("userid", userid);
                        openActivity(UserPageActivity.class, mBundle);
                        break;
                    default:
                        break;
                }
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
