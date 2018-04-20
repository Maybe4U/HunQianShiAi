package com.zykj.hunqianshiai.home.message;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zykj.hunqianshiai.R;

import java.util.List;

/**
 * Created by xu on 2017/12/26.
 */

public class MessageAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public MessageAdapter( @Nullable List<String> data) {
        super(R.layout.message_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {

    }
}
