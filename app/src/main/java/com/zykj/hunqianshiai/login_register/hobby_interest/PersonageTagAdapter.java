package com.zykj.hunqianshiai.login_register.hobby_interest;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zykj.hunqianshiai.R;

import java.util.List;

/**
 * Created by xu on 2017/12/19.
 */

public class PersonageTagAdapter extends BaseQuickAdapter<PersonageTagBean.PersonageTag, BaseViewHolder> {
    public PersonageTagAdapter(@Nullable List<PersonageTagBean.PersonageTag> data) {
        super(R.layout.personage_tag_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PersonageTagBean.PersonageTag item) {
        helper.setText(R.id.tv_tag_title, item.interestclassname);
        ImageView select = helper.getView(R.id.iv_select);
        if (item.selet == 0) {
            select.setVisibility(View.GONE);
        } else {
            select.setVisibility(View.VISIBLE);
        }
    }
}
