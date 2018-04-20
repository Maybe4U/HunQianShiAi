package com.zykj.hunqianshiai.home.my.online;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BasesActivity;

import java.util.List;

/**
 * Created by xu on 2018/1/22.
 */

public class OnlineAdapter extends BaseQuickAdapter<OnlineBean.OnlineData, BaseViewHolder> {
    public OnlineAdapter(@Nullable List<OnlineBean.OnlineData> data) {
        super(R.layout.item_online, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OnlineBean.OnlineData item) {
        helper.addOnClickListener(R.id.tv_remove);
        Glide.with(mContext)
                .load(item.headpic)
                .apply(BasesActivity.mCircleRequestOptions)
                .into((ImageView) helper.getView(R.id.iv_headpic));
        helper.setText(R.id.tv_username, item.username + "  " + item.age + "岁");
        helper.setText(R.id.tv_areaname, item.areaname);
        helper.setText(R.id.tv_yearmoney, item.yearmoney);
    }
}
