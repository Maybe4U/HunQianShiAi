package com.zykj.hunqianshiai.user_home;

import com.zykj.hunqianshiai.bases.BaseBean;
import com.zykj.hunqianshiai.home.my.info.HobbiesBean;
import com.zykj.hunqianshiai.home.my.info.PersonageInfoBean;
import com.zykj.hunqianshiai.home.my.info.SpouseStandardsBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xu on 2018/1/11.
 */

public class UserPageBean extends BaseBean {
    public UserPageData data;

    public static class UserPageData implements Serializable {
        public String age;
        public String areaid;
        public String areaname;
        public String believe;
        public String birthdate;
        public String comment_num;
        public String constellation;
        public String familyinfo;
        public String familyrank;
        public String feeling;
        public String headpic;
        public String height;
        public String household;
        public String id;
        public String ischild;
        public String isheadpic;
        public String isonline;
        public String istopshow;
        public int like_num;
        public String ma;
        public String marryinfo;
        public String marrytime;
        public String meinfo;
        public String nation;
        public String nickname;
        public int pic_num;
        public String sex;
        public String shangxiantixing;
        public String smoke;
        public String userid;
        public String username;
        public String weight;
        public String wine;
        public String workplan;
        public String yearmoney;
        public String zhiye;
        public List<String> pic;
        public PersonageInfoBean.RenZheng renzheng;
        public HobbiesBean.HobbiesData aihao;
        public SpouseStandardsBean.SpouseStandardsData like_biaozhun;
        public Auth auth;
        public String self_shangxiantixing_state;
        public String is_like;
        public String balance;
        public String video;
        public String is_blacklist;
    }

    public static class Auth implements Serializable {
        public int code;
        public String endtime;
        public String id;
        public String regtime;
        public String state;
        public String type;
        public String userid;
    }
}
