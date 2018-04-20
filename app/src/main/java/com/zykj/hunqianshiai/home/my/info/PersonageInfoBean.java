package com.zykj.hunqianshiai.home.my.info;

import com.zykj.hunqianshiai.bases.BaseBean;
import com.zykj.hunqianshiai.user_home.UserPageBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xu on 2018/1/5.
 */

public class PersonageInfoBean extends BaseBean {
    public PersonageInfoData data;


    public static class PersonageInfoData implements Serializable{
        public String areaid;
        public String areaname;
        public String believe;
        public String birthdate;
        public String bk1;
        public String bk2;
        public String bk3;
        public String bk4;
        public String bk5;
        public String bk6;
        public String bk7;
        public String bk8;
        public String constellation;
        public String education;
        public String familyinfo;
        public String familyrank;
        public String feeling;
        public String headpic;
        public String height;
        public String house;
        public String household;
        public String id;
        public String ischild;
        public String isheadpic;
        public String istopshow;
        public String marryhistory;
        public String marryinfo;
        public String marrytime;
        public String meanimal;
        public String meinfo;
        public String nation;
        public String nickname;
        public String sex;
        public String smoke;
        public String userid;
        public String username;
        public String video;
        public String weight;
        public String wine;
        public String workplan;
        public String yearmoney;
        public String zhiye;
        public int like_num;
        public RenZheng renzheng;
        public String  shangxiantixing;
        public String ma;
        public String isonline;
        public UserPageBean.Auth auth;
        public String wx;
        public String Integrity;
        public int is_vip;
    }

    public static class RenZheng implements Serializable{
        public String address;
        public String car;
        public String car_auth;
        public String car_auth2;
        public String card_number;
        public String house;
        public String huose_auth;
        public String huose_auth2;
        public String realname;
        public String shenfen_auth;
        public String shenfen_auth2;
        public String verifytime;
        public String xueli;
        public String xueli_auth;
        public String xueli_auth2;
    }
}
