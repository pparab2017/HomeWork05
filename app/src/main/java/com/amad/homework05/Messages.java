package com.amad.homework05;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Messages extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ProductAdapter.ItemClickCallBack {


    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private boolean isReceiverRegistered;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolBar;
    private TextView lbl_Welcome;
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private final OkHttpClient client = new OkHttpClient();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getMessages(){

        SharedPreferences mPrefs = getSharedPreferences(MainActivity.STORED,MODE_PRIVATE);
        if(mPrefs.contains("MyAppKey"))
        {
            String token = (String)  mPrefs.getString("MyAppKey",null);
            Request request = new Request.Builder()
                .url(Utils.Api_url.MY_MESSAGES.toString())
                .addHeader("Authorization", "BEARER " + token)
                .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    final String responseData = response.body().string();
                    Log.d("messages", responseData);

                    Messages.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ArrayList<Message> res =new ArrayList<Message>();
                            try {
                                res = JsonParser.MyMessages.Parse(responseData);
                                Log.d("msg", res.toString());
                                recyclerView = (RecyclerView) findViewById(R.id.view_products);
                                recyclerView.setLayoutManager(new LinearLayoutManager(Messages.this,LinearLayoutManager.VERTICAL,false));
                                recyclerView.getItemAnimator().setRemoveDuration(200);
                                productAdapter = new ProductAdapter(Messages.this,R.layout.item_each_question);
                                productAdapter.notifyDataSetChanged();
                                recyclerView.setAdapter(productAdapter);
                                productAdapter.SetProducts(res);

                                //makeDisplayScreen(mLoginUser);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });


                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        mToolBar = (Toolbar) findViewById(R.id.nav_Action_bar);
        setSupportActionBar(mToolBar);

        mDrawer = (DrawerLayout) findViewById(R.id.appDrawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawer, R.string.open, R.string.close);
        mDrawer.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);

        View header=navView.getHeaderView(0);

        lbl_Welcome = (TextView) header.findViewById(R.id.lbl_Welcome);
        lbl_Welcome.setText("Welcome, Pushparaj ");
        //lbl_email
        lbl_Welcome = (TextView) header.findViewById(R.id.lbl_email);
        lbl_Welcome.setText("pparab1@uncc.edu");

        //addRadioButtons(4);

        getMessages();

        ArrayList<Question> products = new ArrayList<Question>();
        Question q = new Question();
        q.setId(1);
        q.setQuestion("Rate you concentration today?");
        q.setChoices("Very Good,Good,Average,Bad,Very Bad");

        Question q1 = new Question();
        q.setId(2);
        q1.setQuestion("Did you Eat Healthy Today?");
        q1.setChoices("Yes,no");


        products.add(q);
        products.add(q1);
        //products = JsonParser.JsonParse.Parse(responseData);
        Log.d("test",products.toString());




        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //mRegistrationProgressBar.setVisibility(ProgressBar.GONE);
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
                    //mInformationTextView.setText(getString(R.string.gcm_send_message));
                } else {
                    //mInformationTextView.setText(getString(R.string.token_error_message));
                }
            }
        };
        registerReceiver();

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationService.class);
            startService(intent);
        }

    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
        super.onPause();
    }


    private void registerReceiver(){
        if(!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }

    public void addRadioButtons(int number) {

//        for (int row = 0; row < 1; row++) {
//            RadioGroup ll = (RadioGroup) findViewById(R.id.rdog);
//
//            ll.setOrientation(LinearLayout.VERTICAL);
//
//            for (int i = 1; i <= number; i++) {
//                RadioButton rdbtn = new RadioButton(this);
//                rdbtn.setId((row * 2) + i);
//                rdbtn.setText("Radio " + rdbtn.getId());
//                ll.addView(rdbtn);
//            }
//            //((ViewGroup) findViewById(R.id.radiogroup)).addView(ll);
//        }
    }


    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i("tocked", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.account){
            Intent i = new Intent(Messages.this,myAccount.class);
            startActivity(i);
            Log.d("tag", "account");
        }
        else if(id == R.id.settings){
            Log.d("tag", "settings");
        }
        else if(id == R.id.logout){
            Log.d("tag", "logout");

            SharedPreferences mPrefs = getSharedPreferences(MainActivity.STORED,MODE_PRIVATE);
            SharedPreferences.Editor prefsEditor = mPrefs.edit();

            if(mPrefs.contains("MyAppKey"))
            {
                prefsEditor.remove("MyAppKey");
                Intent i = new Intent(Messages.this,MainActivity.class);
                startActivity(i);
            }
            else
            {

            }
            prefsEditor.commit();


        }
        return true;
    }

    @Override
    public void OnSubmitClick(int p, String response) {
        Log.d("tag", p + " -- " + response);
    }
}
