package com.zykj.hunqianshiai.home;

import com.zykj.hunqianshiai.bases.BaseBean;

import java.io.Serializable;

/**
 * Created by xu on 2018/1/16.
 */

public class TokenBean extends BaseBean {
    public TokenData data;

    public static class TokenData implements Serializable {
        public String userid;
        public String username;
        public String headpic;
        public String token;
    }
}
