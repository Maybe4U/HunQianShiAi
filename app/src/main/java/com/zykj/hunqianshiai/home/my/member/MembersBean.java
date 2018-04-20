package com.zykj.hunqianshiai.home.my.member;

import com.zykj.hunqianshiai.bases.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xu on 2018/1/19.
 */

public class MembersBean extends BaseBean {
    public MemberData data;

    public static class MemberData implements Serializable {
        public List<VipList> list;
        public String zhe;
    }

    public static class VipList implements Serializable {
        public String days;
        public String name;
        public String price;
        public double zhe_price;
        public String id;
        public boolean choose = false;
    }
}
