package com.zykj.hunqianshiai.home.my.set;

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
 * Created by xu on 2018/1/20.
 */

public class BlacklistAdapter extends BaseQuickAdapter<BlacklistBean.BlacklistData, BaseViewHolder> {
    public BlacklistAdapter(@Nullable List<BlacklistBean.BlacklistData> data) {
        super(R.layout.item_blacklist, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BlacklistBean.BlacklistData item) {
        helper.addOnClickListener(R.id.iv_yichu);
        Glide.with(mContext)
                .load(item.self.headpic)
                .apply(BasesActivity.mCircleRequestOptions)
                .into((ImageView) helper.getView(R.id.iv_headpic));
        String age = item.self.age;
        if (!TextUtils.isEmpty(age)) {
            helper.setText(R.id.tv_name, item.username + "  " + age + "岁");
        } else {
            helper.setText(R.id.tv_name, item.username);
        }

        helper.setText(R.id.tv_time, "拉黑时间：" + item.addtime);
    }
}
