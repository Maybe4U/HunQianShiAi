package com.zykj.hunqianshiai.home.dynamic;

import com.zykj.hunqianshiai.bases.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xu on 2018/1/4.
 */

public class DynamicBean extends BaseBean {
    public List<DynamicData> data;

    public static class DynamicData implements Serializable{
        public String address;
        public String addtime;
        public String areaid;
        public String content;
        public String headpic;
        public String id;
        public String is_play;
        public String is_see;
        public String sex;
        public String time_befor;
        public String userid;
        public String username;
        public String zan;
        public List<String> img;
        public String age;
        public String arename;
        public String height;
        public String self_zan;
    }
}
