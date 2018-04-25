package com.zykj.hunqianshiai.home.dynamic.secret_dynamic;

import com.zykj.hunqianshiai.bases.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xu on 2018/1/11.
 */

public class SecretBean extends BaseBean {
    public List<SecretData> data;


    public static class SecretData implements Serializable{
        public String userid;
        public String username;
        public String regtime;
        public boolean isvip;
        public String marrytime;
        public String isonline;
        public String areaname;
        public String age;
        public String height;
        public String constellation;
        public String yearmoney;
        public String meinfo;
        public String headpic;
        public boolean isliker;
        public String userauth;
        public String sex;
        public String nickname;
    }
}
