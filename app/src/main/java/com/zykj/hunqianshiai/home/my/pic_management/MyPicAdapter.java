package com.zykj.hunqianshiai.home.my.pic_management;

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
 * Created by xu on 2018/1/8.
 */

public class MyPicAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public MyPicAdapter(@Nullable List<String> data) {
        super(R.layout.my_pic_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        Glide.with(mContext)
                .load(UrlContent.PIC_URL + item)
                .apply(BasesActivity.mOptions)
                .into((ImageView) helper.getView(R.id.iv_pic));
    }
}
