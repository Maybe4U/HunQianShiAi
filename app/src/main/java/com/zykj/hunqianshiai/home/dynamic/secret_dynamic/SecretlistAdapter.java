package com.zykj.hunqianshiai.home.dynamic.secret_dynamic;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.home.my.set.BlacklistBean;

import java.util.List;

/**
 * Created by xu on 2018/1/20.
 */

public class SecretlistAdapter extends BaseQuickAdapter<SecretBean.SecretData, BaseViewHolder> {
    public SecretlistAdapter(@Nullable List<SecretBean.SecretData> data) {
        super(R.layout.seret_me_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SecretBean.SecretData item) {
        helper.addOnClickListener(R.id.iv_yichu).addOnClickListener(R.id.iv_head);
        Glide.with(mContext)
                .load(item.headpic)
                .apply(BasesActivity.mCircleRequestOptions)
                .into((ImageView) helper.getView(R.id.iv_head));
        String age = item.age;
        if (!TextUtils.isEmpty(age)) {
            helper.setText(R.id.tv_head_username, item.username + "  " + age + "岁");
        } else {
            helper.setText(R.id.tv_head_username, item.username);
        }
        helper.setVisible(R.id.iv_isvip, item.isvip);

        helper.setText(R.id.tv_userauth, item.userauth);
        helper.setText(R.id.tv_areaname, item.areaname);
        helper.setText(R.id.tv_yearmoney, item.yearmoney);


        //helper.setText(R.id.tv_time, "拉黑时间：" + item.addtime);
    }
}
