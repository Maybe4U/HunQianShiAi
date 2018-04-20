package com.zykj.hunqianshiai.home.message.people_nearby;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BasesActivity;

import java.util.List;

/**
 * Created by xu on 2017/12/26.
 */

public class PeopleNearbyAdapter extends BaseQuickAdapter<PeopleNearbyBean.Data, BaseViewHolder> {
    public PeopleNearbyAdapter(@Nullable List<PeopleNearbyBean.Data> data) {
        super(R.layout.people_nearby_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PeopleNearbyBean.Data item) {
        Glide.with(mContext)
                .load(item.headpic)
                .apply(BasesActivity.mCircleRequestOptions)
                .into((ImageView) helper.getView(R.id.iv_head));
        helper.setText(R.id.tv_head_username, item.username + "  " + item.age + "岁");
        helper.setVisible(R.id.iv_isvip, item.isvip);


        helper.setText(R.id.tv_userauth, item.userauth);
        helper.setText(R.id.tv_areaname, item.areaname);
        helper.setText(R.id.tv_yearmoney, item.yearmoney);
        if (null!=item.juli&& !TextUtils.isEmpty(item.juli)) {
            helper.setText(R.id.tv_distance, item.juli + "km以内");
        }
    }
}
