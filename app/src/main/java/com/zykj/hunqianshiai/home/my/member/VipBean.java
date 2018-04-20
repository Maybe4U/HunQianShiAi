package com.zykj.hunqianshiai.home.my.member;

import com.zykj.hunqianshiai.bases.BaseBean;

import java.io.Serializable;

/**
 * Created by xu on 2018/2/1.
 */

public class VipBean extends BaseBean {
    public VipData data;

    public static class VipData implements Serializable{
        public String endtime;
        public String id;
        public String regtime;
        public String state;
        public String userid;
    }
}
