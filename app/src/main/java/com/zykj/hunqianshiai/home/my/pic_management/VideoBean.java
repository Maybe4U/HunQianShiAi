package com.zykj.hunqianshiai.home.my.pic_management;

import com.zykj.hunqianshiai.bases.BaseBean;

import java.io.Serializable;

/**
 * Created by xu on 2018/2/2.
 */

public class VideoBean extends BaseBean {
    public VideoData data;

    public static class VideoData implements Serializable {
        public String video;
    }
}
