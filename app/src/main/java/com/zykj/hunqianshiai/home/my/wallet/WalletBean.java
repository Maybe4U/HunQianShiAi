package com.zykj.hunqianshiai.home.my.wallet;

import com.zykj.hunqianshiai.bases.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xu on 2018/1/25.
 */

public class WalletBean extends BaseBean {
    public List<WalletData> data;

    public static class WalletData implements Serializable{
        public String add_time;
        public String id;
        public String price;
        public String remark;
        public String remark2;
        public String type;
        public String type_p;
        public String user_id;
    }
}
