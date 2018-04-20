package com.zykj.hunqianshiai.user_home.personal_assistant;

import com.zykj.hunqianshiai.bases.BaseBean;

import java.io.Serializable;

/**
 * Created by xu on 2018/1/22.
 */

public class OnlineTimeBean extends BaseBean {
    public OnlineTimeData data;

    public static class OnlineTimeData implements Serializable{
        public String endtime;
        public long time;
        public String time2;
        public String type;
    }
}
