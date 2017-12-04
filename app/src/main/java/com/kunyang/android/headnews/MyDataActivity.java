package com.kunyang.android.headnews;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kunyang.android.headnews.object.MD5Util;
import com.kunyang.android.headnews.object.MyData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyDataActivity extends AppCompatActivity {

    private String responses;
    private String userData[]=new String[5];

    private RecyclerView mRecyclerView;
    private MyDataAdapter mAdapter;

    private Bitmap mBitmap;

    private List<MyData>mDataList=new ArrayList<MyData>();
    private int mId=R.drawable.user_img;
    private String mSense[]=new String[5];
    private String mTitle[]={"我的头像","手机号码","我的昵称","我的性别","个性签名"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_data);

        mRecyclerView=(RecyclerView)this.findViewById(R.id.my_data_list);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);


        SharedPreferences pref=getSharedPreferences("user",MODE_PRIVATE);
        String username=pref.getString("username","");
        String password=pref.getString("password","");
        try {
            doPost(username,password);
        }catch (Exception e){
            e.printStackTrace();
        }
        while (responses==null){

        }
        if(responses!=null) {
            setUserData(responses);
            initSense();

            Thread t=new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (userData!=null) {
                            String in=userData[4];
                            mBitmap = getBitmap(in);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            t.start();

        }


        while (mBitmap==null){

        }

        mAdapter=new MyDataAdapter(mDataList);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initSense(){
        if (userData!=null) {
            mSense[1] = userData[0];
            mSense[2] = userData[1];
            mSense[3] = userData[2];
            mSense[4] = userData[3];
        }
    }

    private class MyDataAdapter extends RecyclerView.Adapter<MyDataAdapter.ViewHolder>{

        private List<MyData> mMyDatas;

        class ViewHolder extends RecyclerView.ViewHolder{
            View contentView;
            ImageView userImg,turnImg;
            TextView title,sense;

            public ViewHolder(View view) {
                super(view);
                contentView=view;
                userImg=(ImageView)view.findViewById(R.id.imageView1);
                title=(TextView)view.findViewById(R.id.textView1);
                turnImg=(ImageView) view.findViewById(R.id.imageView2);
                sense=(TextView)view.findViewById(R.id.textView3);
            }
        }

        public MyDataAdapter(List<MyData>myDatas){
            mMyDatas=myDatas;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.my_data_itema,parent,false);
            final ViewHolder holder=new ViewHolder(view);
            holder.contentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=holder.getAdapterPosition();
                    if (position==2) {
                        Intent i = new Intent(MyDataActivity.this, ChangeNameActivity.class);
                        startActivity(i);
                    }
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            if (position==0){
                holder.title.setText(mTitle[0]);
                holder.userImg.setImageBitmap(mBitmap);
                holder.turnImg.setImageResource(R.drawable.enter);
            }else if (position==1){
                holder.title.setText(mTitle[position]);
                holder.sense.setText(userData[position-1]);
            } else{
                holder.title.setText(mTitle[position]);
                holder.sense.setText(userData[position-1]);
                holder.turnImg.setImageResource(R.drawable.enter);
            }
        }

        @Override
        public int getItemCount() {
            return 5;
        }

    }

    private void setUserData(String response){



        try {


            JSONObject mJsonObject=new JSONObject(response);

            JSONObject jsonObject=mJsonObject.getJSONObject("data");
            userData[0]=jsonObject.getString("username");
            userData[1]=jsonObject.getString("nickname");
            if (jsonObject.getInt("sex")==1){
                userData[2]="男";
            }else {
                userData[2]="女";
            }
            userData[3]=jsonObject.getString("whatisup");
            userData[4]=jsonObject.getString("avatar");

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void doPost(String userName,String passWord)throws IOException {
        OkHttpClient client =new OkHttpClient.Builder()
                .cookieJar(new CookieJar() {
                    private final HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();
                    @Override
                    public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
                        cookieStore.put(httpUrl, list);
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl httpUrl) {
                        List<Cookie> cookies = cookieStore.get(httpUrl);
                        return cookies != null ? cookies : new ArrayList<Cookie>();
                    }
                }).build();

        RequestBody requestBody=new FormBody.Builder().add("username",userName)
                .add("password",passWord).build();

        Request request = new Request.Builder()
                .url("http://interview.jbangit.com/user/login")
                .addHeader("Cookie","uTvQsAbtuRzbj5kDf9xbfmlmK9QvYN2rtWwCCbnQ")
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }else if (response.isSuccessful()){
                    Headers headers = response.headers();
                    List<String> cookies = headers.values("Set-Cookie");
                    Log.d("LoginActivity", "onResponse: " + cookies.size());
                    for (String str : cookies) {
                        if (str.startsWith("PHPSESSID")) {
                            //将sessionId保存到本地
                            SharedPreferences.Editor editor=getSharedPreferences("memory",MODE_PRIVATE).edit();
                            editor.putString("PHPSESSID",str.split(";")[0]);

                            editor.apply();
                            System.out.println("onResponse: " + str.split(";")[0]);
                        }

                    }
                }
                responses=response.body().string();
                System.out.println(responses);
            }


        });
    }

    private Bitmap getBitmap(String path) throws IOException {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if(conn.getResponseCode() == 200){
            InputStream inputStream = conn.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        }
        return null;
    }
}
