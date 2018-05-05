package com.zykj.hunqianshiai.home.message.look;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BaseFragment;
import com.zykj.hunqianshiai.bases.BaseModel;
import com.zykj.hunqianshiai.bases.BaseModelImpl;
import com.zykj.hunqianshiai.bases.BasePresenterImpl;
import com.zykj.hunqianshiai.bases.BaseView;
import com.zykj.hunqianshiai.home.message.MyLikeBean;
import com.zykj.hunqianshiai.net.UrlContent;
import com.zykj.hunqianshiai.tools.JsonUtils;
import com.zykj.hunqianshiai.user_home.UserPageActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;

/**
 * Created by xu on 2017/12/27.
 */

public class MyLookFragment extends BaseFragment implements BaseView<String> {
    @Bind(R.id.recycler_like_me)
    RecyclerView mRecyclerView;
    private Bundle mBundle;

    public static MyLookFragment getInstance() {
        return MyLookFragment.Instance.mMyLookFragment;
    }

    private static class Instance {
        static MyLookFragment mMyLookFragment = new MyLookFragment();
    }

    public MyLookFragment() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_like_me;
    }

    @Override
    public void initView() {

        mBundle = new Bundle();

        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        BasePresenterImpl presenter = new BasePresenterImpl(this, new BaseModelImpl());
        mParams.clear();
        mParams.put("userid", UrlContent.USER_ID);
        mParams.put("type", 2);
        presenter.getData(UrlContent.GET_COMER_URL, mParams, BaseModel.DEFAULT_TYPE);

//        List<String> pics = new ArrayList<>();
//        for (int i = 0; i < 6; i++) {
//            pics.add("1");
//        }
//
//        MyLookAdapter myLookAdapter = new MyLookAdapter(pics);
//        mRecyclerView.setAdapter(myLookAdapter);
    }

    @Override
    public void success(String bean) {
        MyLikeBean myLikeBean = JsonUtils.GsonToBean(bean, MyLikeBean.class);
        final List<MyLikeBean.MyLikeData> data = myLikeBean.data;
        if (data.isEmpty()) {
            toastShow("没有记录");
            return;
        }
        //过滤自己
        List<MyLikeBean.MyLikeData> filterData = new ArrayList<>();
        for(int i=0;i<data.size();i++){
            String comeid = data.get(i).comeuserid;
            if(!comeid.equals(UrlContent.USER_ID)){
                filterData.add(data.get(i));
            }
        }
        //逆序排列
        //Collections.reverse(filterData);

        MyLookAdapter myLookAdapter = new MyLookAdapter(filterData);
        mRecyclerView.setAdapter(myLookAdapter);
        myLookAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<MyLikeBean.MyLikeData> data1 = adapter.getData();
                String userid = data1.get(position).info.userid;
                mBundle.clear();
                mBundle.putString("userid", userid);
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
