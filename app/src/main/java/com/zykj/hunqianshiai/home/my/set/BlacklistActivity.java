package com.zykj.hunqianshiai.home.my.set;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BaseModel;
import com.zykj.hunqianshiai.bases.BaseModelImpl;
import com.zykj.hunqianshiai.bases.BasePresenterImpl;
import com.zykj.hunqianshiai.bases.BaseView;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.net.UrlContent;
import com.zykj.hunqianshiai.tools.JsonUtils;
import com.zykj.hunqianshiai.tools.SPUtils;
import com.zykj.hunqianshiai.tools.TextUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * 黑名单
 */
public class BlacklistActivity extends BasesActivity implements BaseView<String> {

    @Bind(R.id.recycler_blacklist)
    RecyclerView mRecyclerView;
    @Bind(R.id.tv_right)
    TextView mTv_right;
    private BasePresenterImpl mPresenter;

    @Override
    protected int getContentViewX() {
        return R.layout.activity_blacklist;
    }

    @Override
    protected void initView() {
        appBar("黑名单");
        mPresenter = new BasePresenterImpl(this, new BaseModelImpl());

        mTv_right.setVisibility(View.VISIBLE);
        mTv_right.setText("解除");


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
        final BlacklistAdapter blacklistAdapter = new BlacklistAdapter(BlacklistActivity.this,data);
        mRecyclerView.setAdapter(blacklistAdapter);
        SPUtils.put(BlacklistActivity.this,"edit_mode",false);
        blacklistAdapter.notifyDataSetChanged();

//        blacklistAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
//            @Override
//            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//                List<BlacklistBean.BlacklistData> data1 = adapter.getData();
//                String blackuserid = data1.get(position).blackuserid;
//
//                switch (view.getId()) {
//                    case R.id.iv_yichu:
//
//                        blacklistAdapter.remove(position);
//
//                        RongIM.getInstance().removeFromBlacklist(blackuserid, new RongIMClient.OperationCallback() {
//                            @Override
//                            public void onSuccess() {
//                            }
//
//                            @Override
//                            public void onError(RongIMClient.ErrorCode errorCode) {
//                            }
//                        });
//
//                        mParams.clear();
//                        mParams.put("userid", UrlContent.USER_ID);
//                        mParams.put("ids", blackuserid);
//                        mPresenter.getData(UrlContent.REMOVE_BLACKLIST_URL, mParams, BaseModel.REFRESH_TYPE);
//                        break;
//                }
//            }
//        });

//        View childView = View.inflate(this,R.layout.item_blacklist,null);
//        CheckBox iv_yichu = childView.findViewById(R.id.iv_yichu);
//
//        iv_yichu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//
//            }
//        });

            mTv_right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String str = mTv_right.getText().toString().trim();
                    if (str.equals("解除")) {
                        showCheckBox(data, blacklistAdapter, mTv_right);
                    }else {
                        completeRemove(data, blacklistAdapter, mTv_right);
                    }
                }
            });
    }

    private void completeRemove(List<BlacklistBean.BlacklistData> data, BlacklistAdapter blacklistAdapter,TextView tv_right) {
        List<BlacklistBean.BlacklistData> removeData = new ArrayList<>();
        for (int i = 0; i < blacklistAdapter.getItemCount(); i++) {
            SparseArray<Boolean> map = blacklistAdapter.getMap();
            if(map.valueAt(i)){
                removeData.add(blacklistAdapter.getItem(i));
            }
        }
        removeBlacklist(removeData, blacklistAdapter);


        SPUtils.put(BlacklistActivity.this,"edit_mode",false);
        blacklistAdapter.notifyDataSetChanged();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_right.setText("解除");
            }
        });
    }

    private void showCheckBox(List<BlacklistBean.BlacklistData> data, BlacklistAdapter blacklistAdapter, TextView tv_right) {

        SPUtils.put(BlacklistActivity.this,"edit_mode",true);
        blacklistAdapter.notifyDataSetChanged();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_right.setText("完成");
            }
        });
    }

    private void removeBlacklist(List<BlacklistBean.BlacklistData> removeData, BlacklistAdapter blacklistAdapter) {
        String blackuserid = null;
        for (int i = 0; i < removeData.size(); i++) {
            blackuserid = removeData.get(i).blackuserid;
            for (int j = 0; j < blacklistAdapter.getItemCount(); j++) {
                if(blacklistAdapter.getItem(j).blackuserid.equals(blackuserid)){
                    blacklistAdapter.remove(j);
                }
            }
            toastShow("移除黑名单成功");
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
        }
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
