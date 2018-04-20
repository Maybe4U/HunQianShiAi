package com.zykj.hunqianshiai.home.my.set;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
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
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * 黑名单
 */
public class BlacklistActivity extends BasesActivity implements BaseView<String> {

    @Bind(R.id.recycler_blacklist)
    RecyclerView mRecyclerView;
    private BasePresenterImpl mPresenter;

    @Override
    protected int getContentViewX() {
        return R.layout.activity_blacklist;
    }

    @Override
    protected void initView() {
        appBar("黑名单");
        mPresenter = new BasePresenterImpl(this, new BaseModelImpl());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mParams.clear();
        mParams.put("userid", UrlContent.USER_ID);
        mPresenter.getData(UrlContent.GET_BLACKLIST_URL, mParams, BaseModel.DEFAULT_TYPE);
    }

    @Override
    public void success(String bean) {
        BlacklistBean blacklistBean = JsonUtils.GsonToBean(bean, BlacklistBean.class);
        if (blacklistBean.code != 200) {
            return;
        }
        List<BlacklistBean.BlacklistData> data = blacklistBean.data;
        final BlacklistAdapter blacklistAdapter = new BlacklistAdapter(data);
        mRecyclerView.setAdapter(blacklistAdapter);

        blacklistAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                List<BlacklistBean.BlacklistData> data1 = adapter.getData();
                String blackuserid = data1.get(position).blackuserid;

                switch (view.getId()) {
                    case R.id.iv_yichu:

                        blacklistAdapter.remove(position);

                        RongIM.getInstance().removeFromBlacklist(blackuserid, new RongIMClient.OperationCallback() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode errorCode) {
                            }
                        });

                        mParams.clear();
                        mParams.put("userid", UrlContent.USER_ID);
                        mParams.put("ids", blackuserid);
                        mPresenter.getData(UrlContent.REMOVE_BLACKLIST_URL, mParams, BaseModel.REFRESH_TYPE);
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
