package com.zykj.hunqianshiai.user_home.personal_assistant;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.home.my.member.MembersBean;

import java.util.List;

/**
 * Created by xu on 2018/1/19.
 */

public class OpenCounselorAdapter extends BaseQuickAdapter<MembersBean.VipList,BaseViewHolder> {
    public OpenCounselorAdapter(@Nullable List<MembersBean.VipList> data) {
        super(R.layout.item_open_counselor, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MembersBean.VipList item) {
        helper.setText(R.id.tv_price, item.price+"元");
        helper.setText(R.id.tv_time, item.name);
        if (item.choose) {
            helper.setImageResource(R.id.iv_choose, R.mipmap.zhifu_xuanzhong);
        } else {
            helper.setImageResource(R.id.iv_choose, R.mipmap.zhifu_weixuanzhong);
        }
    }
}
