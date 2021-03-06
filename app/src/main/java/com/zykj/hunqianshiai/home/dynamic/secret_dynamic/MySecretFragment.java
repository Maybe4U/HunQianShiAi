package com.zykj.hunqianshiai.home.dynamic.secret_dynamic;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BaseFragment;
import com.zykj.hunqianshiai.bases.BaseModel;
import com.zykj.hunqianshiai.bases.BaseModelImpl;
import com.zykj.hunqianshiai.bases.BasePresenterImpl;
import com.zykj.hunqianshiai.bases.BaseView;
import com.zykj.hunqianshiai.home.message.MyLikeBean;
import com.zykj.hunqianshiai.home.message.look.LookMeAdapter;
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

public class MySecretFragment extends BaseFragment implements BaseView<String> {
    @Bind(R.id.recycler_like_me)
    RecyclerView mRecyclerView;
    private Bundle mBundle;
    private BasePresenterImpl mPresenter;

    public static MySecretFragment getInstance() {
        return MySecretFragment.Instance.mMySecretFragment;
    }

    private static class Instance {
        static MySecretFragment mMySecretFragment = new MySecretFragment();
    }

    public MySecretFragment() {
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
        mParams.put("type", 0);
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
                                toastShow("操作成功");
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode errorCode) {
                            }
                        });

                        mParams.clear();
                        mParams.put("userid", userid);
                        mParams.put("other_id", UrlContent.USER_ID);
                        mParams.put("type", 1);
                        mPresenter.getData(UrlContent.SEE_URL, mParams, BaseModel.REFRESH_TYPE);
                        break;
                    case R.id.iv_head:
                        mBundle.clear();
                        mBundle.putString("userid",userid);
                        openActivity(UserPageActivity.class,mBundle);
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
