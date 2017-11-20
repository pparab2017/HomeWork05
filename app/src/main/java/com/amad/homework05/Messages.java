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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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
    private ImageView gender;
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private final OkHttpClient client = new OkHttpClient();
    private ArrayList<Message> res;
    private SwipeRefreshLayout swipeLayout;
    NavigationView navView;
    TextView noMsgs;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }





    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            Message message = intent.getParcelableExtra("message");

            if(res.size()==0){




                res = new ArrayList<Message>();

                res.add(0,message);
                noMsgs.setVisibility(View.GONE);
                recyclerView = (RecyclerView) findViewById(R.id.view_products);
                recyclerView.setLayoutManager(new LinearLayoutManager(Messages.this,LinearLayoutManager.VERTICAL,false));
                recyclerView.getItemAnimator().setRemoveDuration(200);
                productAdapter = new ProductAdapter(Messages.this,R.layout.item_each_question);
                productAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(productAdapter);
                productAdapter.SetProducts(res);
            }
            else{

                res.add(0,message);
            productAdapter.notifyItemInserted(0);
            //productAdapter.notifyDataSetChanged();
                LinearLayoutManager layoutManager = ((LinearLayoutManager)recyclerView.getLayoutManager());
                if (layoutManager.findFirstVisibleItemPosition() == 0) {
                   layoutManager.scrollToPosition(0);
                }



            }
            Log.d("receiver", "Got message: " + message);
        }
    };

    @Override
    protected void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    private void getMessages(){
        SharedPreferences mPrefs;
        mPrefs = getSharedPreferences(MainActivity.STORED,MODE_PRIVATE);
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
                            res = new ArrayList<Message>();
                            try {
                                res = JsonParser.MyMessages.Parse(responseData);

                                if(res.size() == 0){
                                    noMsgs.setVisibility(View.VISIBLE);
                                }else{
                                    noMsgs.setVisibility(View.GONE);
                                }
                                if(recyclerView != null){
                                    productAdapter.SetProducts(res);
                                    productAdapter.notifyDataSetChanged();
                                }

                                recyclerView = (RecyclerView) findViewById(R.id.view_products);
                                recyclerView.setLayoutManager(new LinearLayoutManager(Messages.this,LinearLayoutManager.VERTICAL,false));
                                recyclerView.getItemAnimator().setRemoveDuration(200);
                                productAdapter = new ProductAdapter(Messages.this,R.layout.item_each_question);
                                productAdapter.notifyDataSetChanged();
                                recyclerView.setAdapter(productAdapter);
                                productAdapter.SetProducts(res);
                                swipeLayout.setRefreshing(false);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });


                }
            });
        }
    }

    void refreshItems() {
        // Load items
        // ...
        getMessages();
        // Load complete
        //onItemsLoadComplete();
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        mToolBar = (Toolbar) findViewById(R.id.nav_Action_bar);
        setSupportActionBar(mToolBar);
        noMsgs = (TextView) findViewById(R.id.txt_noMsgs);

        mDrawer = (DrawerLayout) findViewById(R.id.appDrawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawer, R.string.open, R.string.close);
        mDrawer.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

         navView = (NavigationView) findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);

        View header=navView.getHeaderView(0);

        SharedPreferences mPrefs, nPref;
        mPrefs = getSharedPreferences(MainActivity.STORED,MODE_PRIVATE);
        if(mPrefs.contains("Name")) {
            lbl_Welcome = (TextView) header.findViewById(R.id.lbl_Welcome);
            String name = (String)  mPrefs.getString("Name",null);
            lbl_Welcome.setText("Welcome, " + name);
            //lbl_email
        }
        if(mPrefs.contains("usedID")) {
            lbl_Welcome = (TextView) header.findViewById(R.id.lbl_email);
            String email = (String)  mPrefs.getString("usedID",null);
            lbl_Welcome.setText(email);
        }

        if(mPrefs.contains("gender")) {
            gender = (ImageView) header.findViewById(R.id.img_gender);
            String g = (String)  mPrefs.getString("gender",null);
            if(g.equals("MALE")){
                gender.setImageResource(R.mipmap.boy);
            }

        }

        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshItems();
            }
        });



        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-event-name"));


        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //mRegistrationProgressBar.setVisibility(ProgressBar.GONE);
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);

                final String deviceToken = intent.getStringExtra("token");
                Log.d("receiver", "Got token: " + deviceToken);


                if (sentToken) {


                    Log.d("tag","recieved");
                    //Log.d(getString(R.string.gcm_send_message));
                    //mInformationTextView.setText(getString(R.string.gcm_send_message));
                    SharedPreferences mPrefs, nPref;
                    mPrefs = getSharedPreferences(MainActivity.STORED,MODE_PRIVATE);
                    nPref = getSharedPreferences(MainActivity.SHARED_TOKEN,MODE_PRIVATE);

                    if(!nPref.contains("TokenShared"))
                    if(mPrefs.contains("MyAppKey")) {
                        String token = (String)  mPrefs.getString("MyAppKey",null);

                        RequestBody formBody = new FormBody.Builder()
                                .add("token",deviceToken )
                                .build();

                        Request request = new Request.Builder()
                                .url(Utils.Api_url.SUBMIT_TOKEN.toString())
                                .addHeader("Authorization", "BEARER " + token)
                                .post(formBody)
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
                                        SharedPreferences mPrefs = getSharedPreferences(MainActivity.SHARED_TOKEN,MODE_PRIVATE);
                                        SharedPreferences.Editor prefsEditor = mPrefs.edit();

                                        prefsEditor.putString("TokenShared", deviceToken);
                                        prefsEditor.commit();
                                    }
                                });

                            }
                        });

                    }



                } else {
                    //mInformationTextView.setText(getString(R.string.token_error_message));
                }
            }
        };
        registerReceiver();
        getMessages();


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


    @Override
    protected void onResume() {

        super.onResume();
        navView.getMenu().getItem(1).setChecked(true);
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
            navView.getMenu().getItem(0).setChecked(true);
            Intent i = new Intent(Messages.this,myAccount.class);
            startActivity(i);
            Log.d("tag", "account");
        }
        else if(id == R.id.settings){
            navView.getMenu().getItem(1).setChecked(true);
            Log.d("tag", "settings");
        }
        else if(id == R.id.logout){
            Log.d("tag", "logout");

            SharedPreferences mPrefs = getSharedPreferences(MainActivity.STORED,MODE_PRIVATE);
            SharedPreferences.Editor prefsEditor = mPrefs.edit();
            SharedPreferences nPrefs = getSharedPreferences(MainActivity.SHARED_TOKEN,MODE_PRIVATE);

            if(mPrefs.contains("MyAppKey"))
            {
                String tokenToDelete = (String)  nPrefs.getString("TokenShared",null);
//
                RequestBody formBody = new FormBody.Builder()
                        .add("token",tokenToDelete )
                        .build();
                String token = (String)  mPrefs.getString("MyAppKey",null);

                Request request = new Request.Builder()
                        .url(Utils.Api_url.LOG_OUT.toString())
                        .addHeader("Authorization", "BEARER " + token)
                        .post(formBody)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String responseData = response.body().string();
                        Messages.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                SharedPreferences mPrefs = getSharedPreferences(MainActivity.STORED,MODE_PRIVATE);
                                SharedPreferences.Editor prefsEditor = mPrefs.edit();
                                SharedPreferences nPrefs = getSharedPreferences(MainActivity.SHARED_TOKEN,MODE_PRIVATE);
                                SharedPreferences.Editor refsEditor = nPrefs.edit();

                                refsEditor.remove("TokenShared");
                                prefsEditor.remove("MyAppKey");
                                prefsEditor.remove("Name");
                                prefsEditor.remove("usedID");
                                prefsEditor.remove("gender");
                                prefsEditor.commit();
                                refsEditor.commit();
                                Intent i = new Intent(Messages.this,MainActivity.class);
                                startActivity(i);
                            }
                        });
                    }
                });

            }
            else
            {

            }



        }
        return true;
    }

    @Override
    public void OnSubmitClick(int p, String response) {
        Log.d("tag", p + " -- " + response);
        final boolean isSuccess = false;
         final int locationToUpdate = p;
        final String _response = response;

        SharedPreferences mPrefs;
        mPrefs = getSharedPreferences(MainActivity.STORED,MODE_PRIVATE);
        if(mPrefs.contains("MyAppKey")) {
            String token = (String)  mPrefs.getString("MyAppKey",null);

            RequestBody formBody = new FormBody.Builder()
                    .add("ResponseID",res.get(p).getResponseId() +"")
                    .add("Response", response)
                    .build();

            Request request = new Request.Builder()
                    .url(Utils.Api_url.SUBMIT_RESPONSE.toString())
                    .addHeader("Authorization", "BEARER " + token)
                    .post(formBody)
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

                            try {

                                //productAdapter.SetProducts(res);
                                res.get(locationToUpdate).setResponse(_response);
                                productAdapter.notifyItemChanged(locationToUpdate);

                                //makeDisplayScreen(mLoginUser);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
        }


    }
}
