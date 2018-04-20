package com.zykj.hunqianshiai.home.my.info;

import com.zykj.hunqianshiai.bases.BaseBean;

import java.io.Serializable;

/**
 * Created by xu on 2018/1/6.
 */

public class SpouseStandardsBean extends BaseBean {
    public SpouseStandardsData data;

    public static class SpouseStandardsData implements Serializable {
        public String id;
        public String userid;
        public String age = "不限";
        public String height = "不限";
        public String yearmoney = "不限";
        public String areaid = "不限";
        public String household = "不限";
        public String marryinfo = "不限";
        public String believe = "不限";
        public String wine = "不限";
        public String smoke = "不限";
        public String ischild = "不限";
        public String weight = "不限";
        public String constellation = "不限";
        public String familyinfo = "不限";
        public String workplan = "不限";
        public String feeling = "不限";
        public String info = "不限";
        public String meanimal = "不限";
        public String areaname = "不限";
        public String bk1 = "不限";
        public String bk2 = "不限";
        public String bk3 = "不限";
        public String bk4 = "不限";
        public String bk5 = "不限";
        public String bk6 = "不限";
    }
}
