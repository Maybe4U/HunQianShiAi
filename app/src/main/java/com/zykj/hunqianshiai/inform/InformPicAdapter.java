package com.zykj.hunqianshiai.inform;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.luck.picture.lib.entity.LocalMedia;
import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BasesActivity;

import java.util.List;

/**
 * Created by xu on 2017/12/21.
 */

public class InformPicAdapter extends BaseQuickAdapter<LocalMedia,BaseViewHolder> {
    public InformPicAdapter( @Nullable List<LocalMedia> data) {
        super(R.layout.inform_pic, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, LocalMedia item) {
        Glide.with(mContext)
                .load(item.getCompressPath())
                .apply(BasesActivity.mOptions)
                .into((ImageView) helper.getView(R.id.iv_inform_pic));
        helper.addOnClickListener(R.id.iv_delete_pic);
        helper.addOnClickListener(R.id.iv_inform_pic);
    }
}
