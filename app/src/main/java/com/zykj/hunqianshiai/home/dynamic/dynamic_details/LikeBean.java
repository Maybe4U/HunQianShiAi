package com.zykj.hunqianshiai.home.dynamic.dynamic_details;

import com.zykj.hunqianshiai.bases.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xu on 2018/1/23.
 */

public class LikeBean extends BaseBean {
    public List<LikeData> data;

    public static class LikeData implements Serializable{
        public String addtime;
        public String frident_id;
        public String id;
        public String type;
        public String userid;
        public String username;
        public String username_src;
    }
}
