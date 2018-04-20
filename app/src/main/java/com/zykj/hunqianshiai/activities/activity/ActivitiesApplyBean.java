package com.zykj.hunqianshiai.activities.activity;

import com.zykj.hunqianshiai.bases.BaseBean;

import java.io.Serializable;

/**
 * Created by xu on 2018/1/26.
 */

public class ActivitiesApplyBean extends BaseBean {
    public ActivitiesApplyData data;

    public static class ActivitiesApplyData implements Serializable{
        public String actid;
        public String id;
        public String ji_value;
        public String name;
        public String phone;
        public String state_p;
        public String time;
    }
}
