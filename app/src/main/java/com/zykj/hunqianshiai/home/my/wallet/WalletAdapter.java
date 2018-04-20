package com.zykj.hunqianshiai.home.my.wallet;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zykj.hunqianshiai.R;

import java.util.List;

/**
 * Created by xu on 2018/1/25.
 */

public class WalletAdapter extends BaseQuickAdapter<WalletBean.WalletData, BaseViewHolder> {
    public WalletAdapter(@Nullable List<WalletBean.WalletData> data) {
        super(R.layout.item_wallet, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WalletBean.WalletData item) {
        helper.setText(R.id.tv_remark, item.remark);
        helper.setText(R.id.tv_add_time, item.add_time);
        helper.setText(R.id.tv_remark2, item.remark2);
        helper.setText(R.id.tv_price, item.type + item.price);
    }
}
