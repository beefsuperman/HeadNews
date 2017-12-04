package com.kunyang.android.headnews.util;

import com.google.gson.Gson;
import com.kunyang.android.headnews.gson.Data;
import com.kunyang.android.headnews.gson.News;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 坤阳 on 2017/12/2.
 */

public class Utility {
    public static List<Data> handleDatasResponse(String response){
        List<Data>dataList=new ArrayList<Data>();
        try {
            JSONObject mJsonObject=new JSONObject(response);
            JSONArray jsonArray=mJsonObject.getJSONArray("data");
            for (int i=0;i<jsonArray.length();i++){

                Data data=new Data();
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                String title=jsonObject.getString("title");
                String content=jsonObject.getString("desc");
                String image=jsonObject.getString("image");
                data.setTitle(title);
                data.setDesc(content);
                data.setImage(image);
                dataList.add(data);
            }
            return dataList;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
