package com.zykj.hunqianshiai.home.message.people_nearby;

import com.zykj.hunqianshiai.bases.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xu on 2018/1/12.
 */

public class PeopleNearbyBean extends BaseBean {
    public List<Data> data;

    public static class Data implements Serializable{
        public String age;
        public String areaname;
        public String constellation;
        public String headpic;
        public String height;
        public String isauth;
        public String isliker;
        public String isonline;
        public boolean isvip;
        public String juli;
        public String lat;
        public String lng;
        public String marrytime;
        public String meinfo;
        public String nickname;
        public String sex;
        public String userauth;
        public String userid;
        public String username;
        public String yearmoney;

        @Override
        public String toString() {
            return "Data{" +
                    "areaname='" + areaname + '\'' +
                    ", juli='" + juli + '\'' +
                    ", lat='" + lat + '\'' +
                    ", lng='" + lng + '\'' +
                    '}';
        }
    }
}
