package com.zykj.hunqianshiai.home.my.wallet;

import com.zykj.hunqianshiai.bases.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xu on 2018/2/3.
 */

public class FenXiaoBean extends BaseBean {
    public List<FenXiaoData> data;

    public static class FenXiaoData implements Serializable {
        public String areaname;
        public String headpic;
        public String username;
        public String regtime;
        public String userid;
        public String age;
        public String isvip;

    }
}
