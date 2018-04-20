package com.zykj.hunqianshiai.home.my.info;

import com.zykj.hunqianshiai.bases.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xu on 2018/1/6.
 */

public class HobbiesBean extends BaseBean {
    public HobbiesData data;

    public static class HobbiesData implements Serializable {
        public List<Biaoqian> biaoqian;
        public List<Biaoqian> book;
        public List<Biaoqian> food;
        public List<Biaoqian> music;
        public List<Biaoqian> video;
        public List<Biaoqian> yundong;
    }

    public static class Biaoqian implements Serializable {
        public String id;
        public String interestclassname;
    }
}
