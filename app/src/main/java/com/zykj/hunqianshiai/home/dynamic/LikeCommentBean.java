package com.zykj.hunqianshiai.home.dynamic;

import com.zykj.hunqianshiai.bases.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xu on 2018/1/20.
 */

public class LikeCommentBean extends BaseBean {
    public List<LikeCommentData> data;

    public static class LikeCommentData implements Serializable {
        public String catogry;//1系统消息；2礼物；3点赞；4评论,5心动
        public String content;
        public String headpic;
        public String other_headpic;
        public String other_id;
        public String other_username;
        public String remark;
        public String time;
        public String title;
        public String userid;
        public String username;
        public String isonline;
        public Other_table other_table;
     }

    public static class Other_table implements Serializable{
        public String addtime2;
        public String content;
        public List<String> img;
        public String content_p;
        public String friend_id;
    }
}
