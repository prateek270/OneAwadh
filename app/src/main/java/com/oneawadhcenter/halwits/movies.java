package com.oneawadhcenter.halwits;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


import android.content.Context;


import com.oneawadhcenter.halwits.R;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class movies extends Activity {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    Context context;
    private String TAG = movies.class.getSimpleName();

    private ProgressDialog pDialog;
    ArrayList<Details> arrayList=new ArrayList<Details>();
    public static String url = "http://www.oneawadhcenter.com/api/movie_list";
    private GoogleApiClient client;
    public ArrayList<String> Name=new ArrayList<String>();
    public ArrayList<String> Date=new ArrayList<String>();
    public ArrayList<String> Desc=new ArrayList<String>();
    public ArrayList<String> Img=new ArrayList<String>();
    //String date;
    Calendar cc = Calendar.getInstance();

    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yy");
    String xx = df.format(cc.getTime());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_movies);

        context = this;
       // date = new SimpleDateFormat("dd-MM-yy").format(new Date());
        //Log.d("date","is "+date);

        //lv.setAdapter(new custom_adapter(this, prgmNameList, prgmImages, xyz));
        new GetMovies().execute();

        ImageButton back = (ImageButton) findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent RoomButtonIntent = new Intent(movies.this, navigation.class);
                startActivity(RoomButtonIntent);
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/
    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        Intent i=new Intent(movies.this,navigation.class);
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



    private class GetMovies extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(movies.this);
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
                    for (int i = 0; i <contacts.length(); i++)
                    {
                        JSONObject c = contacts.getJSONObject(i);


                        String name = c.getString("movie_name");

                        String desc = c.getString("movie_description");
                        String temp1=name.replace("&#39;","'");
                        name=temp1;
                        String temp=desc.replace("&#39;","'");
                        desc=temp;
                        String img = c.getString("movie_image");
                        String dateee=c.getString("formatted_movie_start");
                       // SimpleDateFormat dfDate  = new SimpleDateFormat("dd/MM/yyyy");
                        Log.d("name is",""+dateee);
                        try{

                            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy");


                            String str1 = dateee;
                            Date date1 = formatter.parse(str1);

                            String str2 = xx;
                            Date date2 = formatter.parse(str2);

                            if (date1.compareTo(date2)<=0)
                            {
                                Date.add("In Cinemas now");
                                //System.out.println("date2 is Greater than my date1");
                            }
                            else
                            {
                                Date.add("In Cinemas from "+dateee);

                            }

                        }catch (ParseException e1){
                            e1.printStackTrace();
                        }




                        Name.add(name);
                        Desc.add(desc);
                        Img.add(img);


                        // tmp hash map for single contact

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
                Details pd=new Details(Name.get(i),Desc.get(i),Img.get(i),Date.get(i));

                arrayList.add(pd);
            }

           /* customAdapter = new custom_adapter(getApplicationContext(),arrayList);
            lv.setAdapter(customAdapter);*/
            recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
            layoutManager=new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            // recyclerView.setHasFixedSize(true);
            adapter=new custom_adapter(movies.this,getApplicationContext(),arrayList);
            recyclerView.setAdapter(adapter);
        }

    }


}

