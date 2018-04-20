package com.zykj.hunqianshiai.home.dynamic.dynamic_details;

import com.zykj.hunqianshiai.bases.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xu on 2018/1/23.
 */

public class DynamicDetailsBean extends BaseBean {
    public List<DynamicDetailsData> data;

    public static class DynamicDetailsData implements Serializable{
        public String address;
        public String addtime;
        public String addtime2;
        public String addtime3;
        public String age;
        public String areaid;
        public String arename;
        public String content;
        public String headpic;
        public String height;
        public String id;
        public String is_play;
        public String is_see;
        public String self_zan;
        public String sex;
        public String userid;
        public String username;
        public String zan;
        public List<String> img;
        public List<Comment> comment;
    }

    public static class Comment implements Serializable {
        public String addtime;
        public String content;
        public String friend_id;
        public String id;
        public String is_only;
        public String other_headpic;
        public String other_id;
        public String other_username;
        public String user_headpic;
        public String userid;
        public String username;
    }
}
