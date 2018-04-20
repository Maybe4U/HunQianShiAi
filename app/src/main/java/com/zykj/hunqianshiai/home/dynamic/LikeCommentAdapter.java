package com.zykj.hunqianshiai.home.dynamic;

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

public class LikeCommentAdapter extends BaseQuickAdapter<LikeCommentBean.LikeCommentData, BaseViewHolder> {
    public LikeCommentAdapter(@Nullable List<LikeCommentBean.LikeCommentData> data) {
        super(R.layout.like_comment_itme, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, LikeCommentBean.LikeCommentData item) {
        Glide.with(mContext)
                .load(item.other_headpic)
                .apply(BasesActivity.mCircleRequestOptions)
                .into((ImageView) helper.getView(R.id.iv_headpic));
        helper.setText(R.id.tv_username, item.other_username);
        if (null != item.isonline) {
            if (item.isonline.equals("1")) {
                helper.setText(R.id.tv_isonline, "在线");
            } else {
                helper.setText(R.id.tv_isonline, "离线");
            }
        }


        String catogry = item.catogry;
        if (catogry.equals("2")) {
            helper.setVisible(R.id.iv_like, false);
            helper.setVisible(R.id.tv_content, true);
            helper.setVisible(R.id.rl_layout, false);

//            helper.setText(R.id.tv_content,)
        } else if (catogry.equals("3")) {
            helper.setVisible(R.id.iv_like, true);
            helper.setVisible(R.id.tv_content, false);
            helper.setVisible(R.id.rl_layout, true);

            if (null != item.other_table) {
                List<String> img = item.other_table.img;
                if (null != img && !img.isEmpty()) {
                    helper.setVisible(R.id.iv_image, true);
                    helper.setVisible(R.id.tv_title, false);
                    Glide.with(mContext)
                            .load(UrlContent.PIC_URL + img.get(0))
                            .apply(BasesActivity.mOptions)
                            .into((ImageView) helper.getView(R.id.iv_image));
                } else {
                    helper.setVisible(R.id.iv_image, false);
                    helper.setVisible(R.id.tv_title, true);
                    helper.setText(R.id.tv_title, item.other_table.content_p);
                }
            }

            helper.setImageResource(R.id.iv_like, R.mipmap.dong_zanping);
        } else if (catogry.equals("4")) {
            helper.setVisible(R.id.iv_like, false);
            helper.setVisible(R.id.tv_content, true);
            helper.setVisible(R.id.rl_layout, true);

            if (null != item.other_table) {
                List<String> img = item.other_table.img;
                if (null != img && !img.isEmpty()) {
                    helper.setVisible(R.id.iv_image, true);
                    helper.setVisible(R.id.tv_title, false);
                    Glide.with(mContext)
                            .load(UrlContent.PIC_URL + img.get(0))
                            .apply(BasesActivity.mOptions)
                            .into((ImageView) helper.getView(R.id.iv_image));
                } else {
                    helper.setVisible(R.id.iv_image, false);
                    helper.setVisible(R.id.tv_title, true);
                    helper.setText(R.id.tv_title, item.other_table.content_p);

                }
                helper.setText(R.id.tv_content, item.other_table.content);
            }


        } else {
            helper.setVisible(R.id.iv_like, true);
            helper.setVisible(R.id.tv_content, false);
            helper.setVisible(R.id.rl_layout, false);
            helper.setImageResource(R.id.iv_like, R.mipmap.tuisong_xin);
        }
        helper.setText(R.id.tv_time, item.time);
    }
}
