package com.zykj.hunqianshiai.activities.information;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.activities.activity.ActivitiesBean;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.net.UrlContent;

import java.util.List;

/**
 * Created by xu on 2017/12/25.
 */

public class InformationAdapter extends BaseQuickAdapter<ActivitiesBean.ActivitiesData, BaseViewHolder> {
    public InformationAdapter(@Nullable List<ActivitiesBean.ActivitiesData> data) {
        super(R.layout.fragment_information_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ActivitiesBean.ActivitiesData item) {
        helper.setText(R.id.tv_title, item.title);
        Glide.with(mContext)
                .load(UrlContent.PIC_URL + item.thumb)
                .apply(BasesActivity.mOptions)
                .into((ImageView) helper.getView(R.id.iv_image));
    }
}
