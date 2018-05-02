package com.zykj.hunqianshiai.home.message.look;

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
import com.zykj.hunqianshiai.chat.PopupWindowVip;
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

public class LookMeFragment extends BaseFragment implements BaseView<String> {
    @Bind(R.id.recycler_like_me)
    RecyclerView mRecyclerView;
    private View mHeadView;
    private ImageView mIv_headpic;
    private TextView mTv_head_username;
    private TextView mTv_areaname;
    private TextView mTv_yearmoney;
    private TextView mTv_time;
    private TextView mTv_isonline;
    private Bundle mBundle;

    public static LookMeFragment getInstance() {
        return LookMeFragment.Instance.mLookMeFragment;
    }


    private static class Instance {
        static LookMeFragment mLookMeFragment = new LookMeFragment();
    }

    public LookMeFragment() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_like_me;
    }

    @Override
    public void initView() {

        mBundle = new Bundle();
        mHeadView = LayoutInflater.from(mActivity).inflate(R.layout.look_me_header, null);
        mIv_headpic = mHeadView.findViewById(R.id.iv_headpic);
        mTv_head_username = mHeadView.findViewById(R.id.tv_head_username);
        mTv_areaname = mHeadView.findViewById(R.id.tv_areaname);
        mTv_yearmoney = mHeadView.findViewById(R.id.tv_yearmoney);
        mTv_time = mHeadView.findViewById(R.id.tv_time);
        mTv_isonline = mHeadView.findViewById(R.id.tv_isonline);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        BasePresenterImpl presenter = new BasePresenterImpl(this, new BaseModelImpl());
        mParams.clear();
        mParams.put("userid", UrlContent.USER_ID);
        mParams.put("type", 1);
        presenter.getData(UrlContent.GET_COMER_URL, mParams, BaseModel.DEFAULT_TYPE);
    }

    @Override
    public void success(String bean) {
        MyLikeBean myLikeBean = JsonUtils.GsonToBean(bean, MyLikeBean.class);
        List<MyLikeBean.MyLikeData> data = myLikeBean.data;
        if (data.isEmpty()) {
            toastShow("没有记录");
            return;
        }
        final MyLikeBean.MyLikeData myLikeData = data.get(0);
        MyLikeBean.Info info = myLikeData.info;
        Glide.with(mContext)
                .load(info.headpic)
                .apply(BasesActivity.mCircleRequestOptions)
                .into(mIv_headpic);
        mTv_head_username.setText(info.username + "  " + info.age + "岁");

        mTv_areaname.setText(info.areaname);
        mTv_yearmoney.setText(info.yearmoney);
        mTv_time.setText(myLikeData.time);

        String isonline = info.isonline;
        mTv_isonline.setText(isonline);
        if (isonline.equals("在线")) {
            mTv_isonline.setTextColor(getResources().getColor(R.color.default_color));
        } else {
            mTv_isonline.setTextColor(getResources().getColor(R.color.gray));
        }

        //过滤自己
        data.remove(myLikeData);
        List<MyLikeBean.MyLikeData> filterData = new ArrayList<>();
        for(int i=0;i<data.size();i++){
            String comeid = data.get(i).comeuserid;
            if(!comeid.equals(UrlContent.USER_ID)){
                filterData.add(data.get(i));
            }
        }

        //Collections.reverse(filterData);

        LookMeAdapter lookMeAdapter = new LookMeAdapter(filterData);
        mRecyclerView.setAdapter(lookMeAdapter);
        //lookMeAdapter.addHeaderView(mHeadView);

//        mHeadView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mBundle.clear();
//                mBundle.putString("userid", myLikeData.userid);
//                openActivity(UserPageActivity.class, mBundle);
//            }
//        });

        lookMeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //if (UrlContent.IS_MEMBER_KEY) {
                    List<MyLikeBean.MyLikeData> data1 = adapter.getData();
                    String userid = data1.get(position).comeuserid;
                    mBundle.clear();
                    mBundle.putString("userid", userid);
                    openActivity(UserPageActivity.class, mBundle);
                //}
//                else {
//                    PopupWindowVip popupWindowVip = new PopupWindowVip(getActivity());
//                    popupWindowVip.showAtLocation(mRecyclerView, Gravity.CENTER, 0, 0);
//                }
//                /*=====================修改==================*/
//                //取消只有会员才能查看
//                List<SecretBean.MyLikeData> data1 = adapter.getData();
//                String userid = data1.get(position).userid;
//                mBundle.clear();
//                mBundle.putString("userid", userid);
//                openActivity(UserPageActivity.class, mBundle);


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
