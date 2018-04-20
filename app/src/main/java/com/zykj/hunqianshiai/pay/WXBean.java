package com.zykj.hunqianshiai.pay;

import com.zykj.hunqianshiai.bases.BaseBean;

import java.io.Serializable;

/**
 * Created by xu on 2018/1/15.
 */

public class WXBean extends BaseBean {
    public WXData data;

    public static class WXData implements Serializable{
        public String appid;
        public String noncestr;
        public String partnerid;
        public String prepayid;
        public String sign;
        public String timestamp;
    }
}
