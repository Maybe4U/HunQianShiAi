package com.zykj.hunqianshiai.home.dynamic.dynamic_details;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.net.UrlContent;

import java.util.List;

/**
 * Created by xu on 2018/1/23.
 */

public class DynamicDetailsAdapter extends BaseQuickAdapter<DynamicDetailsBean.Comment, BaseViewHolder> {
    public DynamicDetailsAdapter(@Nullable List<DynamicDetailsBean.Comment> data) {
        super(R.layout.tiem_dynamic_details, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DynamicDetailsBean.Comment item) {
        Glide.with(mContext)
                .load(item.user_headpic)
                .apply(BasesActivity.mCircleRequestOptions)
                .into((ImageView) helper.getView(R.id.iv_headpic));
        helper.setText(R.id.tv_username, item.username);
        helper.setText(R.id.tv_addtime, item.addtime);
        if (item.userid.equals(UrlContent.USER_ID)) {
            if (item.userid.equals(item.other_id) || TextUtils.isEmpty(item.other_username)) {
                helper.setText(R.id.tv_content, item.content);
            } else {
                helper.setText(R.id.tv_content, "回复 " + item.other_username + " : " + item.content);
            }
        } else {
            helper.setText(R.id.tv_content, item.content);
        }
    }
}
