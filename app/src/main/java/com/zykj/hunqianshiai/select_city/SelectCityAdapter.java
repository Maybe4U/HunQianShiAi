package com.zykj.hunqianshiai.select_city;

import android.graphics.Color;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zykj.hunqianshiai.R;

import java.util.List;

/**
 * Created by xu on 2017/12/30.
 */

public class SelectCityAdapter extends BaseQuickAdapter<SelectCityBean.SelectCityData, BaseViewHolder> {
    public SelectCityAdapter(@Nullable List<SelectCityBean.SelectCityData> data) {
        super(R.layout.select_city_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SelectCityBean.SelectCityData item) {
        helper.setText(R.id.tv_city, item.areaname);

        if (item.select == 1) {
            helper.setTextColor(R.id.tv_city, Color.argb(255, 237, 189, 90));
        } else {
            helper.setTextColor(R.id.tv_city, Color.argb(255, 17, 17, 17));
        }

    }
}
