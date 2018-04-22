package com.zykj.hunqianshiai.activities.information;

import com.zykj.hunqianshiai.bases.BaseBean;
import com.zykj.hunqianshiai.home.dynamic.dynamic_details.DynamicDetailsBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xu on 2018/1/25.
 */

public class InformationDetailsBean extends BaseBean {
    public InformationDetailsData data;

    public static class InformationDetailsData implements Serializable{
        public String actid;
        public String areaid;
        public String info;
        public String ji_value;
        public String remark;
        public String see;
        public String thumb;
        public String time;
        public String title;
        public String video;
        public List<DynamicDetailsBean.Comment> comment;
        public String addtime;
        public int comment_num;

        @Override
        public String toString() {
            return "InformationDetailsData{" +
                    "actid='" + actid + '\'' +
                    ", areaid='" + areaid + '\'' +
                    ", info='" + info + '\'' +
                    ", ji_value='" + ji_value + '\'' +
                    ", remark='" + remark + '\'' +
                    ", see='" + see + '\'' +
                    ", thumb='" + thumb + '\'' +
                    ", time='" + time + '\'' +
                    ", title='" + title + '\'' +
                    ", video='" + video + '\'' +
                    ", comment=" + comment +
                    ", addtime='" + addtime + '\'' +
                    ", comment_num=" + comment_num +
                    '}';
        }
    }
}
