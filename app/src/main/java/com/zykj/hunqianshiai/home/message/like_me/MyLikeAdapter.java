package com.zykj.hunqianshiai.home.message.like_me;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.home.message.MyLikeBean;

import java.util.List;

/**
 * Created by xu on 2017/12/26.
 */

public class MyLikeAdapter extends BaseQuickAdapter<MyLikeBean.MyLikeData, BaseViewHolder> {
    public MyLikeAdapter(@Nullable List<MyLikeBean.MyLikeData> data) {
        super(R.layout.my_like_item, data);
    }

    public MyLikeAdapter(int layoutResId, @Nullable List<MyLikeBean.MyLikeData> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyLikeBean.MyLikeData item) {
        MyLikeBean.Info info = item.info;
        Glide.with(mContext)
                .load(info.headpic)
                .apply(BasesActivity.mCircleRequestOptions)
                .into((ImageView) helper.getView(R.id.iv_head));
        helper.setText(R.id.tv_username, info.username + "  " + info.age + "岁");
        helper.setVisible(R.id.iv_isvip, info.isvip);
        if(info.userauth.equals("未认证")){
            helper.setTextColor(R.id.tv_userauth, Color.BLACK);
        }
        helper.setText(R.id.tv_userauth, info.userauth);
        helper.setText(R.id.tv_areaname, info.areaname);
        helper.setText(R.id.tv_yearmoney, info.yearmoney);
        helper.setText(R.id.tv_time, item.time);
    }

}
