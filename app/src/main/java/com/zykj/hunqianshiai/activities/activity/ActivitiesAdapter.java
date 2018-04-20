package com.zykj.hunqianshiai.activities.activity;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.net.UrlContent;

import java.util.List;

/**
 * Created by xu on 2017/12/25.
 */

public class ActivitiesAdapter extends BaseQuickAdapter<ActivitiesBean.ActivitiesData, BaseViewHolder> {
    public ActivitiesAdapter(@Nullable List<ActivitiesBean.ActivitiesData> data) {
        super(R.layout.fragment_activities_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ActivitiesBean.ActivitiesData item) {
        helper.setText(R.id.tv_time, item.time);
        Glide.with(mContext)
                .load(UrlContent.PIC_URL + item.thumb)
                .apply(BasesActivity.mOptions)
                .into((ImageView) helper.getView(R.id.iv_image));
        helper.setText(R.id.tv_title, item.title);
        helper.setText(R.id.tv_jinxing, item.time_2);
        if (item.time_2.equals("进行中")) {
            helper.setBackgroundRes(R.id.tv_jinxing, R.drawable.shape_rounded_rectangle_default_12);
        } else {
            helper.setBackgroundRes(R.id.tv_jinxing, R.drawable.shape_rounded_rectangle_gray_12);
        }
    }
}
