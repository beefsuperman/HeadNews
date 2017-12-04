package com.kunyang.android.headnews.object;

/**
 * Created by 坤阳 on 2017/12/3.
 */

public class MyData {
    private String title;
    private int id;
    private String sense;

    public String getTitle() {
        return title;
    }

    public MyData setTitle(String title) {
        this.title = title;
        return this;
    }

    public int getId() {
        return id;
    }

    public MyData setId(int id) {
        this.id = id;
        return this;
    }

    public String getSense() {
        return sense;
    }

    public MyData setSense(String sense) {
        this.sense = sense;
        return this;
    }
}
