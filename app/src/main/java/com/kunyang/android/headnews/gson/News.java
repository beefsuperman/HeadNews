package com.kunyang.android.headnews.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by 坤阳 on 2017/12/2.
 */

public class News {
    @SerializedName("data")
    public List<Data>mDataList;
}
