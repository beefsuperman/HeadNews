package com.kunyang.android.headnews.gson;

import android.graphics.Bitmap;

/**
 * Created by 坤阳 on 2017/12/2.
 */

public class DataExtend {
    private String title;
    private String content;
    private Bitmap bitmapl;

    public String getTitle() {
        return title;
    }

    public DataExtend setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public DataExtend setContent(String content) {
        this.content = content;
        return this;
    }

    public Bitmap getBitmapl() {
        return bitmapl;
    }

    public DataExtend setBitmapl(Bitmap bitmapl) {
        this.bitmapl = bitmapl;
        return this;
    }
}
