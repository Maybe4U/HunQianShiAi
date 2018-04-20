package com.zykj.hunqianshiai.seek;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

/**
 * 搜索结果
 */
public class SearchResultActivity extends BasesActivity implements BaseView<String> {

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
        String searchid = mBundle.getString("searchid");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        BasePresenterImpl presenter = new BasePresenterImpl(this, new BaseModelImpl());
        mParams.clear();
        mParams.put("userid", UrlContent.USER_ID);
        mParams.put("searchid", searchid);
        presenter.getData(UrlContent.SEARCH_ID_URL, mParams, BaseModel.DEFAULT_TYPE);
    }

    @Override
    public void success(String bean) {
        HomeBean homeBean = JsonUtils.GsonToBean(bean, HomeBean.class);
        List<HomeBean.HomeData> data = homeBean.data;

        /*================优化=======================*/
        //当前ID不存在时，提示用户
        if(data.isEmpty()){
            Toast.makeText(SearchResultActivity.this,"没有找到相应用户，请重新进行筛选",Toast.LENGTH_SHORT).show();
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
//                        RongIM.getInstance().startPrivateChat(SearchResultActivity.this, userid, username);
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
                                            RongIM.getInstance().startPrivateChat(SearchResultActivity.this, userid, username);                                        } else {
                                            PopupWindowChatVip popupWindowChatVip = new PopupWindowChatVip(SearchResultActivity.this);
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
