package com.zykj.hunqianshiai.login_register.hobby_interest;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;
import com.zykj.hunqianshiai.R;

import java.util.List;

/**
 * Created by xu on 2017/12/19.
 */
public class HobbyInterestAdapter extends BaseQuickAdapter<HobbyInterestBean.Label,BaseViewHolder> {
    private TagFlowLayout mFlowlayout;

    public HobbyInterestAdapter(@Nullable List<HobbyInterestBean.Label> data) {
        super(R.layout.hobby_interest_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HobbyInterestBean.Label item) {
        final LayoutInflater mInflater = LayoutInflater.from(mContext);
        helper.setText(R.id.tv_tag_title, item.title);
        helper.setImageResource(R.id.iv_tag_icon, item.image);
        mFlowlayout = helper.getView(R.id.id_flowlayout);
        mFlowlayout.setAdapter(new TagAdapter<String>(item.tag) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.tv, mFlowlayout, false);
                tv.setText(s);
                return tv;
            }
        });
    }
}
