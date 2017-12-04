package com.kunyang.android.headnews;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ContentActivity extends AppCompatActivity {

    private ImageView mImageView;
    private TextView title,content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        mImageView=(ImageView)this.findViewById(R.id.user_img);
        mImageView.setImageResource(R.drawable.user_img);

        title=(TextView)this.findViewById(R.id.content_title);
        content=(TextView)this.findViewById(R.id.content);
        SharedPreferences pref=getSharedPreferences("data",MODE_PRIVATE);
        String ttitle=pref.getString("title","");
        String ccontent=pref.getString("content","");
        title.setText(ttitle);
        content.setText(ccontent);
    }
}
