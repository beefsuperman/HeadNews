package com.kunyang.android.headnews.gson;

import java.util.List;

/**
 * Created by 坤阳 on 2017/12/2.
 */

public class Data {
    public String title;
    public String desc;
    public String image;
    public String post_time;

    public String getTitle() {
        return title;
    }

    public Data setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDesc() {
        return desc;
    }

    public Data setDesc(String desc) {
        this.desc = desc;
        return this;
    }

    public String getImage() {
        return image;
    }

    public Data setImage(String image) {
        this.image = image;
        return this;
    }

    public String getPost_time() {
        return post_time;
    }

    public Data setPost_time(String post_time) {
        this.post_time = post_time;
        return this;
    }
}
