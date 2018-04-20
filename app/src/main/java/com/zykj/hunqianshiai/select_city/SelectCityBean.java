package com.zykj.hunqianshiai.select_city;

import com.zykj.hunqianshiai.bases.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xu on 2017/12/30.
 */

public class SelectCityBean extends BaseBean {
    public List<SelectCityData> data;

    public static class SelectCityData implements Serializable{
        public String areaid;
        public String areaname;
        public String parentid;
        public int select = 0;
    }
}
