package com.zykj.hunqianshiai.home.my.wallet;

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
 * Created by xu on 2018/2/3.
 */

public class FenXiaoAdapter extends BaseQuickAdapter<FenXiaoBean.FenXiaoData, BaseViewHolder> {
    public FenXiaoAdapter(@Nullable List<FenXiaoBean.FenXiaoData> data) {
        super(R.layout.item_fen_xiao, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FenXiaoBean.FenXiaoData item) {
        Glide.with(mContext)
                .load(item.headpic)
                .apply(BasesActivity.mCircleRequestOptions)
                .into((ImageView) helper.getView(R.id.iv_head));
        helper.setText(R.id.tv_username, item.username);

        if (TextUtils.isEmpty(item.age)) {
            helper.setText(R.id.tv_age, "");
        } else {
            helper.setText(R.id.tv_age, item.age + "岁");
        }

        helper.setText(R.id.tv_areaname, item.areaname);
        helper.setText(R.id.tv_time, item.regtime);
    }
}
