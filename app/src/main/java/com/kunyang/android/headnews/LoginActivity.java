package com.kunyang.android.headnews;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kunyang.android.headnews.object.MD5Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
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

public class LoginActivity extends AppCompatActivity {

    private EditText userNameText,passWordText;

    private Button mButton;
    private Button nextButton;

    private String responses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userNameText=(EditText)this.findViewById(R.id.editText2);
        passWordText=(EditText)this.findViewById(R.id.editText);

        mButton=(Button)this.findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String in = "你好";
                try {
                    doPost(userNameText.getText().toString(),MD5Util.encrypt(passWordText.getText().toString()));
                    in=loginCondition(responses);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (in!=null&&in.equals("登录成功")){
                    SharedPreferences.Editor editor=getSharedPreferences("user",MODE_PRIVATE).edit();
                    editor.putString("username",userNameText.getText().toString());
                    editor.putString("password",MD5Util.encrypt(passWordText.getText().toString()));

                    editor.apply();
                    Intent i=new Intent(LoginActivity.this,MeActivity.class);
                    startActivity(i);
                    finish();
                }else {
                    Toast.makeText(LoginActivity.this,"登录不成功，请检查用户名或密码！",Toast.LENGTH_SHORT).show();
                }
            }
        });

        nextButton=(Button)this.findViewById(R.id.button2);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(LoginActivity.this,NewsActivity.class);
                startActivity(i);
                finish();
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.exit:
                finish();
                break;
            default:
        }
        return true;
    }

    private static String loginCondition(String response){
        String message=null;

        try {

            JSONObject mJsonObject=new JSONObject(response);

            message=mJsonObject.getString("message");
            return message;
        }catch (Exception e){
            e.printStackTrace();
        }
        return message;
    }

    private void doPost(String userName,String passWord)throws IOException{
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
                }else if(response.isSuccessful()){
                    Headers headers = response.headers();
                    List<String> cookies = headers.values("Set-Cookie");
                    Log.d("LoginActivity", "onResponse: " + cookies.size());
                    for (String str : cookies) {
                        if (str.startsWith("PHPSESSID")) {
                            //将sessionId保存到本地
                            System.out.println("onResponse: " + str.split(";")[0]);
                        }

                    }
                }
                responses = response.body().string();

                System.out.println(responses);
            }

        });
    }

}
