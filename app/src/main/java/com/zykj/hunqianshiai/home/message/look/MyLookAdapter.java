package com.zykj.hunqianshiai.home.message.look;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zykj.hunqianshiai.R;

import java.util.List;

/**
 * Created by xu on 2017/12/26.
 */

public class MyLookAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public MyLookAdapter(@Nullable List<String> data) {
        super(R.layout.my_look_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {

    }
}
