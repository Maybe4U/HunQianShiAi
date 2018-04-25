package com.zykj.hunqianshiai.seek;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BaseBean;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.bases.BaseModel;
import com.zykj.hunqianshiai.bases.BaseModelImpl;
import com.zykj.hunqianshiai.bases.BasePresenterImpl;
import com.zykj.hunqianshiai.bases.BaseView;
import com.zykj.hunqianshiai.home.PopupWindowChatVip;
import com.zykj.hunqianshiai.home.home.HomeAdapter;
import com.zykj.hunqianshiai.home.home.HomeBean;
import com.zykj.hunqianshiai.net.UrlContent;
import com.zykj.hunqianshiai.tools.JsonUtils;
import com.zykj.hunqianshiai.user_home.UserPageActivity;

import java.util.List;

import butterknife.Bind;
import io.rong.imkit.RongIM;

public class VipSearchActivity extends BasesActivity implements BaseView<String> {

    @Bind(R.id.recycler_result)
    RecyclerView mRecyclerView;
    private Bundle mBundle;

    @Override
    protected int getContentViewX() {
        return R.layout.activity_search_result;
    }

    @Override
    protected void initView() {
        appBar("搜索结果");
        mBundle = getIntent().getExtras();
        String p1 = mBundle.getString("p1", "");
        String p2 = mBundle.getString("p2", "");
        String p3 = mBundle.getString("p3", "");
        String p4 = mBundle.getString("p4", "");
        String p5 = mBundle.getString("p5", "");
        String p10 = mBundle.getString("p10", "");
        String p11 = mBundle.getString("p11", "");
        String p12 = mBundle.getString("p12", "");
        String p13 = mBundle.getString("p13", "");
        String p14 = mBundle.getString("p14", "");
        String p15 = mBundle.getString("p15", "");
        String p16 = mBundle.getString("p16", "");
        String p17 = mBundle.getString("p17", "");
        String p18 = mBundle.getString("p18", "");
        String p19 = mBundle.getString("p19", "");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        BasePresenterImpl presenter = new BasePresenterImpl(this, new BaseModelImpl());
        mParams.clear();
        mParams.put("userid", UrlContent.USER_ID);
        mParams.put("page", 1);
        mParams.put("size", 6);
        if (!TextUtils.isEmpty(p1)) {
            mParams.put("p1", p1);
        }
        if (!TextUtils.isEmpty(p2)) {
            mParams.put("p2", p2);
        }
        if (!TextUtils.isEmpty(p3)) {
            mParams.put("p3", p3);
        }
        if (!TextUtils.isEmpty(p4)) {
            mParams.put("p4", p4);
        }
        if (!TextUtils.isEmpty(p5)) {
            mParams.put("p5", p5);
        }
        if (!TextUtils.isEmpty(p10)) {
            mParams.put("p10", p10);
        }
        if (!TextUtils.isEmpty(p11)) {
            mParams.put("p11", p11);
        }
        if (!TextUtils.isEmpty(p12)) {
            mParams.put("p12", p12);
        }
        if (!TextUtils.isEmpty(p13)) {
            mParams.put("p13", p13);
        }
        if (!TextUtils.isEmpty(p14)) {
            mParams.put("p14", p14);
        }
        if (!TextUtils.isEmpty(p15)) {
            mParams.put("p15", p15);
        }
        if (!TextUtils.isEmpty(p16)) {
            mParams.put("p16", p16);
        }
        if (!TextUtils.isEmpty(p17)) {
            mParams.put("p17", p17);
        }
        if (!TextUtils.isEmpty(p18)) {
            mParams.put("p18", p18);
        }
        if (!TextUtils.isEmpty(p19)) {
            mParams.put("p19", p19);
        }
        if (!TextUtils.isEmpty(p1)) {
        }

        mParams.put("p20", 1);

        presenter.getData(UrlContent.SEARCH_VIP_URL, mParams, BaseModel.DEFAULT_TYPE);
    }

    @Override
    public void success(String bean) {
        HomeBean homeBean = JsonUtils.GsonToBean(bean, HomeBean.class);
        List<HomeBean.HomeData> data = homeBean.data;

        //当前条件删选结果不存在时，提示VIP用户
        if(data.isEmpty()){
            Toast.makeText(VipSearchActivity.this,"没有找到相应用户，请重新进行筛选",Toast.LENGTH_SHORT).show();
            return;
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        HomeAdapter mHomeAdapter = new HomeAdapter(data);
        mRecyclerView.setAdapter(mHomeAdapter);

        mHomeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<HomeBean.HomeData> data1 = adapter.getData();
                mBundle.clear();
                mBundle.putString("userid", data1.get(position).userid);
                openActivity(UserPageActivity.class, mBundle);
            }
        });

        mHomeAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                List<HomeBean.HomeData> data1 = adapter.getData();
                HomeBean.HomeData homeData = data1.get(position);
                final String userid = homeData.userid;
                final String username = homeData.username;
                switch (view.getId()) {
                    case R.id.ll_chat:
//                        RongIM.getInstance().startPrivateChat(mContext, "2202","xu");
//                        RongIM.getInstance().startPrivateChat(mContext, "2203", "sfghg");
//                        RongIM.getInstance().startPrivateChat(VipSearchActivity.this, userid, username);
                        mParams.clear();
                        mParams.put("userid", UrlContent.USER_ID);
                        mParams.put("other_id", userid);
                        mParams.put("rdm", UrlContent.RDM);
                        mParams.put("sign", UrlContent.SIGN);

                        OkGo.<String>post(UrlContent.CHAT_COUNT_URL)
                                .params(mParams)
                                .execute(new StringCallback() {
                                    @Override
                                    public void onSuccess(Response<String> response) {
                                        BaseBean baseBean = JsonUtils.GsonToBean(response.body(), BaseBean.class);
                                        if (baseBean.code == 200) {
                                            RongIM.getInstance().startPrivateChat(VipSearchActivity.this, userid, username);
                                        } else {
                                            PopupWindowChatVip popupWindowChatVip = new PopupWindowChatVip(VipSearchActivity.this);
                                            popupWindowChatVip.showAtLocation(mRecyclerView, Gravity.CENTER, 0, 0);
                                        }
                                    }
                                });
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
