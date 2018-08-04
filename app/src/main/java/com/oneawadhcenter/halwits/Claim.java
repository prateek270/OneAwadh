package com.oneawadhcenter.halwits;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.oneawadhcenter.halwits.R;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Prateek on 7/1/2017.
 */
public class Claim extends Activity{
    Context context;
    private String TAG = Claim.class.getSimpleName();

    private ProgressDialog pDialog;
    ArrayList<couponDetail> arrayList=new ArrayList<couponDetail>();
    public static String endpoint = "http://oneawadhcenter.com/api/coupon_list/-/";
    public String url = "";

    public ArrayList<String> Name=new ArrayList<String>();
    public ArrayList<String> Desc=new ArrayList<String>();
    public ArrayList<String> Img=new ArrayList<String>();
    public ArrayList<String> Url=new ArrayList<String>();
    public ArrayList<String> Date=new ArrayList<String>();
    public ArrayList<String> Red=new ArrayList<String>();

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    int u_id;

    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.claim);
        SharedPreferences sp=getApplicationContext().getSharedPreferences("Login",Context.MODE_PRIVATE);
        u_id=sp.getInt("Id",0);
        url=endpoint+u_id;
        Log.d("id ",url+"");
        context = this;
        ImageButton back=(ImageButton)findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent RoomButtonIntent=new Intent(Claim.this,navigation.class);
                startActivity(RoomButtonIntent);
            }
        });

        new GetCoupon().execute();
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        Intent i=new Intent(Claim.this,navigation.class);
        startActivity(i);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Promotions Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.oneawadhcenter.halwits/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Promotions Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.oneawadhcenter.halwits/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }




    private class GetCoupon extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Claim.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.hide();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {

                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("list");

                    // looping through All Contacts
                    for (int i = 0; i <contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);


                        String name = c.getString("coupon_title");
                        String dtt;
                        String desc = c.getString("brief_description");
                        String temp1=name.replace("&#39;","'");
                        name=temp1;
                        String temp=desc.replace("&#39;","'");
                        desc=temp;
                        String img = c.getString("coupon_image1");
                        String cc=c.getString("validity_type");
                        if(cc.equalsIgnoreCase("First Claim"))
                        {
                            dtt=c.getString("number_of_claims");
                            dtt="Valid for first "+dtt+" users";
                        }
                        else
                        {
                            dtt=c.getString("formatted_coupon_date_start");
                            String dtt2=c.getString("formatted_coupon_date_end");
                            dtt="Valid from "+dtt+" to "+dtt2;
                        }
                        String urll = c.getString("coupon_id");
                        String redeem=c.getString("is_redeemed");

                        Name.add(name);
                        Desc.add(desc);
                        Img.add(img);
                        Url.add(urll);
                        Date.add(dtt);
                        Red.add(redeem);

                    }

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            for(int i=0;i<Name.size();i++)
            {
                couponDetail pd=new couponDetail(Name.get(i),Desc.get(i),Img.get(i),Url.get(i),Date.get(i),Red.get(i));

                arrayList.add(pd);
            }

            /*customAdapter = new custom_adapter_promo(getApplicationContext(),arrayList);
            lv.setAdapter(customAdapter);*/
            recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
            recyclerView.invalidate();
            layoutManager=new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            // recyclerView.setHasFixedSize(true);
            adapter=new custom_adapter_claim(Claim.this,getApplicationContext(),arrayList);
            recyclerView.setAdapter(adapter);
        }

    }

}

