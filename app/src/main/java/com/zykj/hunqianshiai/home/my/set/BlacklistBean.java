package com.zykj.hunqianshiai.home.my.set;

import com.zykj.hunqianshiai.bases.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xu on 2018/1/20.
 */

public class BlacklistBean extends BaseBean {
    public List<BlacklistData> data;

    public static class BlacklistData implements Serializable{
        public String blackuserid;
        public String username;
        public String birthdate;
        public String headpic;
        public String addtime;
        public Self self;
    }

    public static class Self implements Serializable{
        public String age;
        public String headpic;
    }
}
