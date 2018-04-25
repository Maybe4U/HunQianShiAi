package com.zykj.hunqianshiai.home.dynamic.secret_dynamic;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BaseFragment;
import com.zykj.hunqianshiai.bases.BaseModel;
import com.zykj.hunqianshiai.bases.BaseModelImpl;
import com.zykj.hunqianshiai.bases.BasePresenterImpl;
import com.zykj.hunqianshiai.bases.BaseView;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.chat.PopupWindowVip;
import com.zykj.hunqianshiai.home.message.MyLikeBean;
import com.zykj.hunqianshiai.home.message.look.LookMeAdapter;
import com.zykj.hunqianshiai.home.my.set.BlacklistBean;
import com.zykj.hunqianshiai.net.UrlContent;
import com.zykj.hunqianshiai.tools.JsonUtils;
import com.zykj.hunqianshiai.user_home.UserPageActivity;

import java.util.List;

import butterknife.Bind;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * Created by xu on 2017/12/27.
 */

public class SecretMeFragment extends BaseFragment implements BaseView<String> {
    @Bind(R.id.recycler_like_me)
    RecyclerView mRecyclerView;
    private Bundle mBundle;
    private BasePresenterImpl mPresenter;

    public static SecretMeFragment getInstance() {
        return SecretMeFragment.Instance.mLookMeFragment;
    }


    private static class Instance {
        static SecretMeFragment mLookMeFragment = new SecretMeFragment();
    }

    public SecretMeFragment() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_secret_me;
    }

    @Override
    public void initView() {

        mBundle = new Bundle();
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mPresenter = new BasePresenterImpl(this, new BaseModelImpl());
        mParams.clear();
        mParams.put("userid", UrlContent.USER_ID);
        mParams.put("type", 1);
        mParams.put("size", 15);
        mParams.put("page", 1);
        mPresenter.getData(UrlContent.SECRET_URL, mParams, BaseModel.DEFAULT_TYPE);
    }

    @Override
    public void success(String bean) {
        SecretBean secretMeBean = JsonUtils.GsonToBean(bean, SecretBean.class);
        if (secretMeBean.code != 200) {
            return;
        }
        List<SecretBean.SecretData> data = secretMeBean.data;
        if (data.isEmpty()) {
            toastShow("没有记录");
            return;
        }

        SecretlistAdapter secretlistAdapter = new SecretlistAdapter(data);
        mRecyclerView.setAdapter(secretlistAdapter);

        secretlistAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                List<SecretBean.SecretData> data1 = adapter.getData();
                String userid = data1.get(position).userid;

                switch (view.getId()) {
                    case R.id.iv_yichu:

                        secretlistAdapter.remove(position);

                        RongIM.getInstance().removeFromBlacklist(userid, new RongIMClient.OperationCallback() {
                            @Override
                            public void onSuccess() {
                                //toastShow("");
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode errorCode) {
                            }
                        });

                        mParams.clear();
                        mParams.put("userid", UrlContent.USER_ID);
                        mParams.put("other_id", userid);
                        mParams.put("type", 1);
                        mPresenter.getData(UrlContent.SEE_URL, mParams, BaseModel.REFRESH_TYPE);
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
