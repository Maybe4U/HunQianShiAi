package com.zykj.hunqianshiai.user_home;

import com.zykj.hunqianshiai.bases.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xu on 2018/1/25.
 */

public class GiftBean extends BaseBean {
    public List<GiftData> data;

    public static class GiftData implements Serializable {
        public String addtime;
        public String id;
        public String name;
        public String price;
        public String url = "";
        public int select = 0;
    }
}
