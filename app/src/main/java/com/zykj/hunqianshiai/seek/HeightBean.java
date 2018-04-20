package com.zykj.hunqianshiai.seek;

import com.bigkoo.pickerview.model.IPickerViewData;

/**
 * Created by xu on 2018/2/5.
 */

public class HeightBean implements IPickerViewData {

    public HeightBean(String id, String height) {
        this.id = id;
        this.height = height;
    }

    public String id;
    public String height;

    @Override
    public String getPickerViewText() {
        return height;
    }
}
