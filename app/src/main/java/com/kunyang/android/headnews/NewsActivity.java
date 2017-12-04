package com.kunyang.android.headnews;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import com.kunyang.android.headnews.gson.Data;

import com.kunyang.android.headnews.gson.DataExtend;
import com.kunyang.android.headnews.gson.News;
import com.kunyang.android.headnews.util.HttpUtil;
import com.kunyang.android.headnews.util.Utility;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.support.v7.widget.RecyclerView.*;

public class NewsActivity extends AppCompatActivity {

    private ImageButton mImageButton1,mImageButton2;
    private RecyclerView mRecyclerView;

    private NewsAdapter mAdapter;
    private List<DataExtend> mDataExtends=new ArrayList<DataExtend>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);

        mImageButton1=(ImageButton)this.findViewById(R.id.news_Button);
        mImageButton2=(ImageButton)this.findViewById(R.id.user_Button);
        mImageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(NewsActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });
        mRecyclerView=(RecyclerView)this.findViewById(R.id.news_list);

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        requestData();
    }

    public void requestData(){

        String weatherUrl="http://interview.jbangit.com/news";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(NewsActivity.this,"网络连接不上",Toast.LENGTH_SHORT).show();

                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText=response.body().string();
                final List<Data> news= Utility.handleDatasResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (news!=null){
                            for(Data data:news){
                                String title=data.title;
                                String content=data.desc;
                                final String image=data.image;
                                final DataExtend dataExtend=new DataExtend();

                                new Thread(new Runnable(){
                                    @Override
                                    public void run() {
                                        try {
                                            Bitmap bitmap=getBitmap(image);
                                            dataExtend.setBitmapl(bitmap);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).start();


                                dataExtend.setTitle(title);
                                dataExtend.setContent(content);
                                mDataExtends.add(dataExtend);
                            }
                            mAdapter=new NewsAdapter(mDataExtends);
                            mRecyclerView.setAdapter(mAdapter);

                        }else {
                            Toast.makeText(NewsActivity.this,"555...连不上啊！",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });

    }


    private class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder>{

        private List<DataExtend> mDataExtends;

        class ViewHolder extends RecyclerView.ViewHolder{
            View contentView;
            ImageView newsImage;
            TextView title;
            TextView content;

            public ViewHolder(View view) {
                super(view);
                contentView=view;
                newsImage=(ImageView)view.findViewById(R.id.news_img);
                title=(TextView)view.findViewById(R.id.title);
                content=(TextView)view.findViewById(R.id.text);
            }
        }

        public NewsAdapter(List<DataExtend>dataExtends){
            mDataExtends=dataExtends;
        }

        public NewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_news,parent,false);
            final ViewHolder holder=new ViewHolder(view);
            holder.contentView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=holder.getAdapterPosition();
                    DataExtend dataExtend=mDataExtends.get(position);
                    SharedPreferences.Editor editor=getSharedPreferences("data",MODE_PRIVATE).edit();
                    editor.putString("title",dataExtend.getTitle());
                    editor.putString("content",dataExtend.getContent());

                    editor.apply();
                    Intent i=new Intent(NewsActivity.this,ContentActivity.class);
                    startActivity(i);
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(NewsAdapter.ViewHolder holder, int position) {
            DataExtend dataExtend=mDataExtends.get(position);

            holder.newsImage.setImageBitmap(dataExtend.getBitmapl());
            holder.title.setText(dataExtend.getTitle());
            holder.content.setText(dataExtend.getContent());
        }

        @Override
        public int getItemCount() {
            return mDataExtends.size();
        }
    }

    public Bitmap getBitmap(String path) throws IOException {
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
