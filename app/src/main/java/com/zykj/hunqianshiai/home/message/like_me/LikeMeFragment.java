package com.zykj.hunqianshiai.home.message.like_me;

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
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.bases.BaseFragment;
import com.zykj.hunqianshiai.bases.BaseModel;
import com.zykj.hunqianshiai.bases.BaseModelImpl;
import com.zykj.hunqianshiai.bases.BasePresenterImpl;
import com.zykj.hunqianshiai.bases.BaseView;
import com.zykj.hunqianshiai.chat.ConversationActivity;
import com.zykj.hunqianshiai.chat.PopupWindowVip;
import com.zykj.hunqianshiai.home.message.MyLikeBean;
import com.zykj.hunqianshiai.net.UrlContent;
import com.zykj.hunqianshiai.tools.JsonUtils;
import com.zykj.hunqianshiai.user_home.UserPageActivity;

import java.util.List;

import butterknife.Bind;

/**
 * 最近对我心动的人
 * Created by xu on 2017/12/26.
 */

public class LikeMeFragment extends BaseFragment implements BaseView<String> {

    @Bind(R.id.recycler_like_me)
    RecyclerView mRecyclerView;
    private View mHeadView;
    private ImageView mIv_head;
    private TextView mTv_head_username;
    private ImageView mIv_isvip;
    private TextView mTv_time;
    private TextView mTv_userauth;
    private TextView mTv_areaname;
    private TextView mTv_yearmoney;
    private TextView mTv_like;
    private Bundle mBundle;

    public static LikeMeFragment getInstance() {
        return Instance.mLikeMeFragment;
    }

    private static class Instance {
        static LikeMeFragment mLikeMeFragment = new LikeMeFragment();
    }

    public LikeMeFragment() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_like_me;
    }

    @Override
    public void initView() {

        mBundle = new Bundle();
        mHeadView = LayoutInflater.from(mActivity).inflate(R.layout.like_me_header, null);
        mIv_head = mHeadView.findViewById(R.id.iv_head);
        mTv_head_username = mHeadView.findViewById(R.id.tv_head_username);
        mIv_isvip = mHeadView.findViewById(R.id.iv_isvip);
        mTv_time = mHeadView.findViewById(R.id.tv_time);
        mTv_userauth = mHeadView.findViewById(R.id.tv_userauth);
        mTv_areaname = mHeadView.findViewById(R.id.tv_areaname);
        mTv_yearmoney = mHeadView.findViewById(R.id.tv_yearmoney);
        mTv_like = mHeadView.findViewById(R.id.tv_like);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        BasePresenterImpl presenter = new BasePresenterImpl(this, new BaseModelImpl());
        mParams.clear();
        mParams.put("userid", UrlContent.USER_ID);
        presenter.getData(UrlContent.LIKE_ME_URL, mParams, BaseModel.DEFAULT_TYPE);

    }

    @Override
    public void success(String bean) {
        MyLikeBean myLikeBean = JsonUtils.GsonToBean(bean, MyLikeBean.class);
        List<MyLikeBean.MyLikeData> data = myLikeBean.data;
        if (data.isEmpty()) {
            return;
        }
        final MyLikeBean.MyLikeData myLikeData = data.get(0);
        MyLikeBean.Info info = myLikeData.info;
        Glide.with(mContext)
                .load(info.headpic)
                .apply(BasesActivity.mCircleRequestOptions)
                .into(mIv_head);
        mTv_head_username.setText(info.username + "  " + info.age + "岁");
        if (info.isvip) {
            mIv_isvip.setVisibility(View.VISIBLE);
        } else {
            mIv_isvip.setVisibility(View.GONE);
        }
        mTv_like.setText(info.username + "对你有好感，来了解下TA吧。");

        mTv_userauth.setText(info.userauth);
        mTv_areaname.setText(info.areaname);
        mTv_yearmoney.setText(info.yearmoney);
        mTv_time.setText(myLikeData.time);
        data.remove(myLikeData);
        LikeMeAdapter myLikeAdapter = new LikeMeAdapter(data);
        mRecyclerView.setAdapter(myLikeAdapter);
        myLikeAdapter.addHeaderView(mHeadView);

        mHeadView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBundle.clear();
                mBundle.putString("userid", myLikeData.userid);
                openActivity(UserPageActivity.class, mBundle);
            }
        });

        myLikeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                if (UrlContent.IS_MEMBER_KEY) {
//                    List<SecretBean.MyLikeData> data1 = adapter.getData();
//                    String userid = data1.get(position).userid;
//                    mBundle.clear();
//                    mBundle.putString("userid", userid);
//                    openActivity(UserPageActivity.class, mBundle);
//                } else {
//                    PopupWindowVip popupWindowVip = new PopupWindowVip(getActivity());
//                    popupWindowVip.showAtLocation(mRecyclerView, Gravity.CENTER, 0, 0);
//                }
                /*=====================修改==================*/
                //取消只有会员才能查看
                List<MyLikeBean.MyLikeData> data1 = adapter.getData();
                String userid = data1.get(position).userid;
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
