package com.zykj.hunqianshiai.home.message;

import com.zykj.hunqianshiai.bases.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xu on 2018/1/11.
 */

public class MyLikeBean extends BaseBean {
    public List<MyLikeData> data;

    public static class MyLikeData implements Serializable{
        public String id;
        public String likeuserid;
        public String time;
        public String userid;
        public Info info;
        public String comeuserid;
    }

    public static class Info implements Serializable{
        public String age;
        public String areaname;
        public String constellation;
        public String headpic;
        public String height;
        public String isauth;
        public String isonline;
        public boolean isvip;
//        public String marrytime;
        public String meinfo;
        public String nickname;
        public String sex;
        public String userauth;
        public String userid;
        public String username;
        public String yearmoney;
    }
}
