package com.zykj.hunqianshiai.login_register.hobby_interest;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xu on 2017/12/19.
 */

public class HobbyInterestBean implements Serializable {
    public List<Label> label;

    public static class Label implements Serializable{
        public int image;
        public String title;
        public List<String> tag;
    }
}
