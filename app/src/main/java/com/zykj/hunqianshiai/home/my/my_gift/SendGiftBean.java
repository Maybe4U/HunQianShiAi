package com.zykj.hunqianshiai.home.my.my_gift;

import com.zykj.hunqianshiai.bases.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xu on 2018/1/25.
 */

public class SendGiftBean extends BaseBean {
    public List<SendGiftData> data;

    public static class SendGiftData implements Serializable {
        public String addtime;
        public String beizhu;
        public String belong_userid;
        public String day;
        public String g_price;
        public String id;
        public String name;
        public String num;
        public String order_no;
        public String other;
        public String paytime;
        public String paytype;
        public String price;
        public String state;
        public String type;
        public String userid;
        public Userinfo userinfo;
        public String url;
    }

    public static class Userinfo implements Serializable {
        public String age;
        public String areaname;
        public String constellation;
        public String headpic;
        public String height;
        public String isauth;
        public String isonline;
        public boolean isvip;
        public String marrytime;
        public String meinfo;
        public String sex;
        public String userauth;

        public String userid;
        public String username;
        public String yearmoney;
    }
}
