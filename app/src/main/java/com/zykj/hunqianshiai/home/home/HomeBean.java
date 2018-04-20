package com.zykj.hunqianshiai.home.home;

import com.zykj.hunqianshiai.bases.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xu on 2018/1/3.
 */

public class HomeBean extends BaseBean {
    public List<HomeData> data;

    public static class HomeData implements Serializable {
        public String age;
        public String areaname;
        public String constellation;
        public String headpic;
        public String height;
        public String isauth;
        public boolean isliker;
        public boolean isvip;
        public String marrytime;
        public String meinfo;
        public String sex;
        public String userauth;
        public String userid;
        public String username;
        public String yearmoney;
        public String isonline;
        public String nickname;
    }
}
