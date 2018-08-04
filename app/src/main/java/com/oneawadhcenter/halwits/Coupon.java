package com.oneawadhcenter.halwits;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.oneawadhcenter.halwits.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Coupon extends AppCompatActivity {

    private LinearLayout mGallery;

    private LayoutInflater mInflater;
    public String[] imgg;
    String URL="http://oneawadhcenter.com/api/coupon_list/";
    private ProgressDialog pDialog;
    private String TAG = Coupon.class.getSimpleName();
    String title,des,tnc,c_id,redeem;
    TextView ttext,dtext,tnctext,claim;
    HorizontalScrollView hsv;
    int u_id;
    ImageView img;
    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;
    ArrayList<String> image=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);
        Bundle bundle = getIntent().getExtras();
        c_id=bundle.getString("Url");
        redeem=bundle.getString("Redeem");
        ttext=(TextView)findViewById(R.id.title);
        dtext=(TextView)findViewById(R.id.des);
        tnctext=(TextView)findViewById(R.id.tnc);
        SharedPreferences sp=getApplicationContext().getSharedPreferences("Login",Context.MODE_PRIVATE);
        u_id=sp.getInt("Id",0);






        URL=URL+c_id+"/"+u_id;
        new GetData().execute();


               Log.d("id is ",u_id+"");
        //Toast.makeText(Coupon.this, "id is"+URL, Toast.LENGTH_LONG).show();

        mInflater = LayoutInflater.from(this);
        ImageButton back = (ImageButton) findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent RoomButtonIntent = new Intent(Coupon.this, Claim.class);
                startActivity(RoomButtonIntent);
            }
        });


        claim = (TextView) findViewById(R.id.claim_now);

        if(redeem.equals("No"))
        {
            claim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Coupon.this);

                    // set title
                    alertDialogBuilder.setTitle("Are you sure you want to claim the coupon?");

                    // set dialog message
                    alertDialogBuilder
                            .setMessage("Once claimed cannot be undone")
                            .setCancelable(false)
                            .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {

                                    new claimCoupon().execute("http://oneawadhcenter.com/api/claim_coupon");



                                }
                            })
                            .setNegativeButton("No",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            });

        }
        else
        {
            claim.setText("REDEEMED");
            claim.setBackgroundColor(Color.DKGRAY);


        }



    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        Intent i=new Intent(Coupon.this,Claim.class);
        startActivity(i);
    }



    private class GetData extends AsyncTask<Void, Void, Void> {


        String i1, i2, i3;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Coupon.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(URL);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {

                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("list");



                        JSONObject c = contacts.getJSONObject(0);

                        i1 = c.getString("coupon_image1");
                        image.add(i1);

                        i2 = c.getString("coupon_image2");
                    image.add(i2);

                        i3 = c.getString("coupon_image3");
                    image.add(i3);
                        title=c.getString("coupon_title");
                        des=c.getString("detail_description");
                        tnc=c.getString("terms_description");





                } catch (final JSONException e) {
                    Log.e("testing", "Json parsing error: " + e.getMessage());
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
                Log.e("testing", "Couldn't get json from server.");
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

            imgg = new String[]{i1, i2, i3};
            ttext.setText(title);
            dtext.setText(des);

            //tnctext.setText(Html.fromHtml(tnc));
            if (Build.VERSION.SDK_INT >= 24)
            {
                tnctext.setText(Html.fromHtml(tnc,Html.FROM_HTML_MODE_LEGACY));

            }
            else
            {
                tnctext.setText(Html.fromHtml(tnc));
            }

            viewPager = (ViewPager) findViewById(R.id.pager);
            CustomPagerAdapter mCustomPagerAdapter = new CustomPagerAdapter(Coupon.this,image);
            sliderDotspanel=(LinearLayout)findViewById(R.id.dots);



            viewPager.setAdapter(mCustomPagerAdapter);
            dotscount=mCustomPagerAdapter.getCount();
            dots=new ImageView[dotscount];
            for(int i=0;i<dotscount;i++)
            {
                dots[i]=new ImageView(Coupon.this);
                dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.nonactivedots));
                LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(8,0,8,0);
                sliderDotspanel.addView(dots[i],params);
            }
            dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.activedots));
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    for(int i=0;i<dotscount;i++) {

                        dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactivedots));
                    }
                    dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.activedots));

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            Timer timer=new Timer();
            timer.scheduleAtFixedRate(new MyTimerTask(),2000,4000);
            if (pDialog.isShowing())
                pDialog.dismiss();




        }
    }
    class claimCoupon extends AsyncTask<String,Void,String>
    {
        ProgressDialog progressDialog;
        String status,coupon_code,message;



        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog=new ProgressDialog(Coupon.this);
            progressDialog.setMessage("Please Wait...");
            progressDialog.show();
            //progressDialog.dismiss();
        }
        @Override
        protected String doInBackground(String... params) {
            try {

                return postData(params[0]);
            } catch (IOException ex) {
                return "Network Error!";
            } catch (JSONException ex) {
                return "Data Invalid !";
            }
        }
        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
            Log.d("result ",result );
            JSONObject jsonObj = null;
            try {
                jsonObj = new JSONObject(result);
                JSONObject c =jsonObj.getJSONObject("list");


                status = c.getString("status");
                coupon_code=c.getString("coupon_code");
                message= c.getString("message");

               /* message= c.getString("message");
                id=c.getInt("user_id");
                Log.d("id",id+" ");
                name=c.getString("name");*/
            } catch (JSONException e) {


                e.printStackTrace();
            }

            // Getting JSON Array node




            if(progressDialog!=null)
            {
                progressDialog.dismiss();
            }
            if(status.equals("success"))
            {

                Toast.makeText(Coupon.this,message, Toast.LENGTH_LONG).show();
                Intent eIntent=new Intent(Coupon.this,Claim_coupon.class);
                eIntent.putExtra("Code",coupon_code);
                startActivity(eIntent);

            }
            else
            {

                Toast.makeText(Coupon.this,message, Toast.LENGTH_LONG).show();

            }
        }

        private String postData(String urlPath) throws IOException,JSONException{
            StringBuilder result =new StringBuilder();
            BufferedWriter bufferedWriter=null;
            BufferedReader bufferedReader=null;
            StringBuilder builder;
            try{
                JSONObject dataToSend=new JSONObject();

                dataToSend.put("user_id",u_id);
                dataToSend.put("coupon_id",c_id);



                //building connection to the server
                java.net.URL url=new URL(urlPath);
                HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(20000);
                urlConnection.setConnectTimeout(20000);

                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                urlConnection.connect();


                //Writing data to server
                OutputStream outputStream=urlConnection.getOutputStream();
                bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream));
                bufferedWriter.write(dataToSend.toString());
                bufferedWriter.flush();
                Log.d("input ",dataToSend.toString());

                //read data response from server
                InputStream inputStream=urlConnection.getInputStream();
                bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while((line=bufferedReader.readLine())!=null){
                    result.append(line).append("\n");
                }

                builder = new StringBuilder();
                builder.append(urlConnection.getResponseCode());
            }finally {
                if(bufferedReader!=null)
                    bufferedReader.close();
                if(bufferedWriter!=null)
                    bufferedWriter.close();

            }
            return result.toString();
        }
    }
    public class MyTimerTask extends TimerTask
    {

        @Override
        public void run() {
            Coupon.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(viewPager.getCurrentItem()==0)
                    {
                        viewPager.setCurrentItem(1);
                    }
                    else if(viewPager.getCurrentItem()==1)
                    {
                        viewPager.setCurrentItem(2);
                    }
                    else
                    {
                        viewPager.setCurrentItem(0);
                    }


                }
            });
        }
    }
}
