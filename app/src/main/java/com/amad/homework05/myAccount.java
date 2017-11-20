package com.amad.homework05;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class myAccount extends AppCompatActivity implements View.OnClickListener {
    private Toolbar mToolBar;
    private CheckBox chkBox;
    private Button btn_subscribe;

    private final OkHttpClient client = new OkHttpClient();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        mToolBar = (Toolbar) findViewById(R.id.nav_Action_bar);
        setSupportActionBar(mToolBar);
        chkBox = (CheckBox) findViewById(R.id.checkBox);
        btn_subscribe = (Button) findViewById(R.id.btn_subscribe);


        btn_subscribe.setOnClickListener(this);
        SharedPreferences mPrefs;
        mPrefs = getSharedPreferences(MainActivity.STORED,MODE_PRIVATE);


            if(mPrefs.contains("MyAppKey")) {

                String token = (String)  mPrefs.getString("MyAppKey",null);
                Request request = new Request.Builder()
                        .url(Utils.Api_url.GET_SUBSCRIPTION.toString())
                        .addHeader("Authorization", "BEARER " + token)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                        Log.d("Subscription", "error");


                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {


                        final String responseData = response.body().string();



                        myAccount.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {



                                String res = responseData.replaceAll("^\"|\"$", "");
                                Log.d("Subscription", res);
                                if(res.equals("YES")){
                                    chkBox.setChecked(true);
                                }else{
                                    chkBox.setChecked(false);
                                }
                            }
                        });

                    }
                });
            }
    }

    @Override
    public void onClick(View view) {
        String sub = "NO";
        if(chkBox.isChecked()){
            sub = "YES";
        }


        SharedPreferences mPrefs;
        mPrefs = getSharedPreferences(MainActivity.STORED,MODE_PRIVATE);


        if(mPrefs.contains("MyAppKey")) {
        }

        String token = (String)  mPrefs.getString("MyAppKey",null);
        RequestBody formBody = new FormBody.Builder()
                .add("val",sub )
                .build();

        Request request = new Request.Builder()
                .url(Utils.Api_url.SUBSCRIBE.toString())
                .addHeader("Authorization", "BEARER " + token)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.d("Subscription", "error");


            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {


                final String responseData = response.body().string();



                myAccount.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {



                        String res = responseData.replaceAll("^\"|\"$", "");
                        Log.d("Subscription", res);
                        if(res.equals("YES")){
                            chkBox.setChecked(true);
                        }else{
                            chkBox.setChecked(false);
                        }
                        Log.d("tag","done");
                        Toast.makeText(getBaseContext(),"Updated Successfully",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        }
}
