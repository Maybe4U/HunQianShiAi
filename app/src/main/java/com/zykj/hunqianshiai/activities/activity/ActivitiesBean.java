package com.zykj.hunqianshiai.activities.activity;

import com.zykj.hunqianshiai.bases.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xu on 2018/1/15.
 */

public class ActivitiesBean extends BaseBean {
    public List<ActivitiesData> data;

    public class ActivitiesData implements Serializable{
        public String actid;
        public String areaid;
        public String cost;
        public String info;
        public String thumb;
        public String thumb2;
        public String time;
        public String title;
        public String video;
        public String time_2;
        public String state;
    }
}
