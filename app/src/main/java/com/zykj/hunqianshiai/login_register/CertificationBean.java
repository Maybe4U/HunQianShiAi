package com.zykj.hunqianshiai.login_register;

import com.zykj.hunqianshiai.bases.BaseBean;

import java.io.Serializable;

/**
 * Created by xu on 2018/1/15.
 */

public class CertificationBean extends BaseBean{
    public Data data;

    public static class Data implements Serializable{
        public String RZF_PRICE;
        public String TUI_PRICE;
        public String VIP_PRICE;
        public String count;
    }
}
