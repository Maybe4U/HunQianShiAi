package com.zykj.hunqianshiai.home.message.system_message;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zykj.hunqianshiai.R;

import java.util.List;

/**
 * Created by xu on 2017/12/26.
 */

public class SystemMessageAdapter extends BaseQuickAdapter<SystemMessageBean.SystemMessageData, BaseViewHolder> {
    public SystemMessageAdapter(@Nullable List<SystemMessageBean.SystemMessageData> data) {
        super(R.layout.system_message_imte, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SystemMessageBean.SystemMessageData item) {
        helper.setText(R.id.tv_title, item.title);
        helper.setText(R.id.tv_content, item.content);
        helper.setText(R.id.tv_time, item.remark);
    }
}
