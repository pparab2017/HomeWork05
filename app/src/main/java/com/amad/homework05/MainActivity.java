package com.amad.homework05;

import android.content.Intent;
import android.content.SharedPreferences;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_login;
    private User mLoginUser;
    final static String LOGGEDIN_USER = "LOGGED_IN_USER";
    final static String LOGGEDIN_TOKEN = "LOGGED_IN_USER_TOKEN";
    final static String STORED = "stored";
    final static String SHARED_TOKEN = "SHARED";
    private TextView txt_userName, txt_password, txt_errorMsg;
    private final OkHttpClient client = new OkHttpClient();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_login = (Button) findViewById(R.id.button_Login);
        btn_login.setOnClickListener(this);
        txt_errorMsg = (TextView) findViewById(R.id.text_errorMsg);

    }


    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences mPrefs = getSharedPreferences(MainActivity.STORED,MODE_PRIVATE);

        if(mPrefs.contains("MyAppKey"))
        {
            String token = (String)  mPrefs.getString("MyAppKey",null);
            Intent i = new Intent(MainActivity.this,Messages.class);
            i.putExtra(LOGGEDIN_TOKEN,token);
            startActivity(i);
        }
    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public void onClick(View v) {

        txt_errorMsg.setText("");
        txt_userName = (TextView)findViewById(R.id.text_userName);
        txt_password =(TextView)findViewById(R.id.text_password);

        RequestBody formBody = new FormBody.Builder()
                .add("email",txt_userName.getText().toString())
                .add("password", txt_password.getText().toString())
                .build();

        Request request = new Request.Builder()
                .url(Utils.Api_url.LOGIN.toString())
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {

                final String responseData = response.body().string();
                //Run view-related code back on the main thread
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mLoginUser = new User();
                        try {
                            mLoginUser = JsonParser.JsonParse.Parse(responseData);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("json",mLoginUser.toString() );
                        if(mLoginUser.getToken() != null)
                        {
                           Log.d("tag","Logged in Success!");
                            txt_errorMsg.setText("Logged in Success!");
                            SharedPreferences mPrefs = getSharedPreferences(MainActivity.STORED,MODE_PRIVATE);
                            SharedPreferences.Editor prefsEditor = mPrefs.edit();

                            prefsEditor.putString("MyAppKey", mLoginUser.getToken());
                            prefsEditor.putString("Name", mLoginUser.getfName());
                            prefsEditor.putString("usedID", mLoginUser.getEmail());
                            prefsEditor.putString("gender", mLoginUser.getGender());
                            prefsEditor.commit();

//                                if(mPrefs.contains("MyAppKey"))
//                                {prefsEditor.remove("MyAppKey");}
//                                else
//                                {}



                            Intent i = new Intent(MainActivity.this,Messages.class);
                            i.putExtra(LOGGEDIN_USER,mLoginUser);
                            startActivity(i);
                        }
                        else{
                            if(mLoginUser.getStatus().toLowerCase().equals("error")){
                                txt_errorMsg.setText(mLoginUser.getErrorMessage());
                            }
                        }
                    }
                });
            }
        });


    }
}
