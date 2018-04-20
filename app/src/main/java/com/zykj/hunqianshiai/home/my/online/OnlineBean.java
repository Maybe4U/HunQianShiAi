package com.zykj.hunqianshiai.home.my.online;

import com.zykj.hunqianshiai.bases.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xu on 2018/1/22.
 */

public class OnlineBean extends BaseBean {
    public List<OnlineData> data;

    public static class OnlineData implements Serializable {
        public String age;
        public String areaname;
        public String constellation;
        public String headpic;
        public String height;
        public boolean isliker;
        public String isonline;
        public boolean isvip;
        public String marrytime;
        public String meinfo;
        public String nickname;
        public String sex;
        public String userauth;
        public String userid;
        public String username;
        public String yearmoney;

    }
}
