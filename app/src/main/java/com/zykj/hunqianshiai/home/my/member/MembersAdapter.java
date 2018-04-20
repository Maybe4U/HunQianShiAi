package com.zykj.hunqianshiai.home.my.member;

import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zykj.hunqianshiai.R;

import java.util.List;

/**
 * Created by xu on 2018/1/19.
 */

public class MembersAdapter extends BaseQuickAdapter<MembersBean.VipList, BaseViewHolder> {

    private String zhe;

    public MembersAdapter(String zhe, @Nullable List<MembersBean.VipList> data) {
        super(R.layout.members_itme, data);
        this.zhe = zhe;
    }

    @Override
    protected void convert(BaseViewHolder helper, MembersBean.VipList item) {
        helper.setText(R.id.tv_name, item.name);
        if (item.zhe_price > 0) {
            helper.setText(R.id.tv_zhe_price, item.zhe_price + "人民币");
            helper.setText(R.id.tv_price, "￥" + item.price);
            helper.setText(R.id.tv_zhe, "限时秒杀" + zhe + " 折");
            TextView tv_price = helper.getView(R.id.tv_price);
            tv_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            helper.setText(R.id.tv_zhe_price, item.price + "人民币");
            helper.setVisible(R.id.tv_zhe, true);
        }

        if (item.name.equals("终身会员")) {
            helper.setBackgroundRes(R.id.rl_layout, R.drawable.shape_rounded_rectangle_hollow_gray_5_1);
        } else {
            helper.setBackgroundRes(R.id.rl_layout, R.drawable.shape_rounded_rectangle_hollow_gray_5);
        }

    }
}
