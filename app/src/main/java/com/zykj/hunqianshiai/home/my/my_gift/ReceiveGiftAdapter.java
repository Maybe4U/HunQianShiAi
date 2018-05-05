package com.zykj.hunqianshiai.home.my.my_gift;

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
 * Created by xu on 2018/1/4.
 */

public class ReceiveGiftAdapter extends BaseQuickAdapter<SendGiftBean.SendGiftData, BaseViewHolder> {
    public ReceiveGiftAdapter(@Nullable List<SendGiftBean.SendGiftData> data) {
        super(R.layout.receive_gift_itme, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SendGiftBean.SendGiftData item) {
        Glide.with(mContext)
                .load(item.userinfo.headpic)
                .apply(BasesActivity.mCircleRequestOptions)
                .into((ImageView) helper.getView(R.id.iv_headpic));
        helper.setText(R.id.tv_username, item.userinfo.username);
        helper.setText(R.id.tv_time, item.addtime);
        helper.setText(R.id.tv_name, item.name);

        helper.setText(R.id.tv_money, "面值"+item.g_price + "元");
        Glide.with(mContext)
                .load(UrlContent.PIC_URL + item.url)
                .apply(BasesActivity.mOptions)
                .into((ImageView) helper.getView(R.id.iv_pic));

        helper.addOnClickListener(R.id.iv_headPic);
    }
}
