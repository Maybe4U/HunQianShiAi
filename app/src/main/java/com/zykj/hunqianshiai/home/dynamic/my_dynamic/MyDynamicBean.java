package com.zykj.hunqianshiai.home.dynamic.my_dynamic;

import com.zykj.hunqianshiai.bases.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xu on 2018/1/4.
 */

public class MyDynamicBean extends BaseBean {
    public List<MyDynamicData> data;

    public static class MyDynamicData implements Serializable{
        public String address;
        public String username;
        public String content;
        public String headpic;
        public List<String> img;
        public String self_zan;
        public String addtime3;
        public String id;
     }
}
