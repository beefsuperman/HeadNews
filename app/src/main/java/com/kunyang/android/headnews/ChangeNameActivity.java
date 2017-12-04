package com.kunyang.android.headnews;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
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

public class ChangeNameActivity extends AppCompatActivity {

    private String cookie;

    private String responses;

    private EditText nameText;
    private Button mButton;
    private ImageButton mImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_name);

        SharedPreferences pref=getSharedPreferences("memory",MODE_PRIVATE);
        cookie=pref.getString("PHPSESSID","");
        //SharedPreferences prefs=getSharedPreferences("user",MODE_PRIVATE);
        //final String username=prefs.getString("username","");
        //final String password=prefs.getString("password","");

        mImageButton=(ImageButton)this.findViewById(R.id.delete_name);
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameText.setText("");
            }
        });
        nameText=(EditText)this.findViewById(R.id.my_name);
        mButton=(Button)this.findViewById(R.id.save_name);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=nameText.getText().toString();
                try {
                    //doPost(username,password);
                    doPost(name);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (responses!=null){
                    String in=message(responses);
                    if (in!=null&&in.equals("修改昵称成功")){
                        Toast.makeText(ChangeNameActivity.this,"修改成功了!不过你的名字依然没变化...",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(ChangeNameActivity.this,"修改不成功了!再试一次看看？",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private String message(String response){

        String msg=null;

        try {


            JSONObject mJsonObject=new JSONObject(response);

            msg=mJsonObject.getString("message");


        }catch (Exception e){
            e.printStackTrace();
        }
        return msg;
    }

    private void doPost(String nickname)throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
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

        RequestBody requestBody=new FormBody.Builder().add("nickname",nickname)
                .build();

        Request request = new Request.Builder()
                .url("http://interview.jbangit.com/user/nickname")
                .addHeader("Cookie",cookie)
                .post(requestBody)
                .build();



        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                responses=response.body().string();
                System.out.println(responses);
            }


        });
    }

}
