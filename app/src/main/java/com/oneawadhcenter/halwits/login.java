package com.oneawadhcenter.halwits;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.oneawadhcenter.halwits.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;


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

import com.facebook.FacebookSdk;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

public class login extends AppCompatActivity {
    EditText email1,pass1;
    CallbackManager callbackManager;
    public static String nameurl="http://www.oneawadhcenter.com/api/signup";
    private ProgressDialog pDialog;
    String mail2;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_login);


        if( getIntent().getBooleanExtra("Exit me", false)){
            finish();
        }
        if(isNetworkStatusAvialable (getApplicationContext()))
        {
            //Toast.makeText(getApplicationContext(), "Internet detected", Toast.LENGTH_SHORT).show();
            email1=(EditText)findViewById(R.id.emaileditText);

            pass1=(EditText)findViewById(R.id.passeditText);
            callbackManager = CallbackManager.Factory.create();
            LoginManager.getInstance().registerCallback(callbackManager,
                    new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            Intent intent=new Intent(login.this,navigation.class);
                            startActivity(intent);
                            // App code
                        }

                        @Override
                        public void onCancel() {
                            Toast.makeText(login.this,"Facebook login cancelled", Toast.LENGTH_LONG).show();
                            // App code
                        }

                        @Override
                        public void onError(FacebookException exception) {

                            Toast.makeText(login.this,"Facebook error try again later", Toast.LENGTH_LONG).show();
                            // App code
                        }
                    });
            SharedPreferences sp=getApplicationContext().getSharedPreferences("Login",0);
            int value=sp.getInt("Login",0);
            if(value==1)
            {
                Log.d("mail","xys");

                Intent otpIntent=new Intent(login.this,Promotions.class);
                startActivity(otpIntent);

            }

            Button xxx=(Button)findViewById(R.id.loginbutton);
            xxx.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    new loginDataTask().execute("http://www.oneawadhcenter.com/api/login");
                }
            });


            TextView tv=(TextView)findViewById(R.id.newaccount);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent registerIntent=new Intent(login.this,register.class);
                    startActivity(registerIntent);
                    overridePendingTransition(R.anim.from_r, R.anim.to_l);
                }
            });



            TextView forgot=(TextView)findViewById(R.id.forgot);
            forgot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent otpIntent=new Intent(login.this,forgetpass.class);
                    startActivity(otpIntent);
                    overridePendingTransition(R.anim.from_r, R.anim.to_l);
                }

            });
            //new FetchWebsiteData().execute();
        } else
        {
            //Display message in dialog box if you have not internet connection
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("No Internet Connection");
            alertDialogBuilder.setMessage("You are offline please check your internet connection");
            Toast.makeText(login.this, "No Internet Connection", Toast.LENGTH_LONG).show();
            alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    //Toast.makeText(MainActivity.this,"No Internet Connection",Toast.LENGTH_LONG).show();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

    }

    public static boolean isNetworkStatusAvialable (Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null)
        {
            NetworkInfo netInfos = connectivityManager.getActiveNetworkInfo();
            if(netInfos != null)
            {
                return netInfos.isConnected();
            }
        }
        return false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title
        alertDialogBuilder.setTitle("Are you sure you want to exit?");

        // set dialog message
        alertDialogBuilder
                .setMessage("Click yes to exit!")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);



                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();



    }

    class loginDataTask extends AsyncTask<String,Void,String>
    {
        ProgressDialog progressDialog;


        String mail=email1.getText().toString();

        String mail2=mail.replace("_",".");
        final String pass=pass1.getText().toString();

        String status="",message,name;
        int id;



        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog=new ProgressDialog(login.this);
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

                 message= c.getString("message");
                id=c.getInt("user_id");
                Log.d("id",id+" ");
                name=c.getString("name");
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
                SharedPreferences sp=getApplicationContext().getSharedPreferences("Login",Context.MODE_PRIVATE);
                SharedPreferences.Editor spe=sp.edit();
                spe.putInt("Login",1);
                spe.putInt("Id",id);
                spe.putString("name",name);
                spe.commit();
                Toast.makeText(login.this, "Successfully Logged In ", Toast.LENGTH_SHORT).show();
                Log.d("Firebase", "token "+ FirebaseInstanceId.getInstance().getToken());
                FirebaseMessaging.getInstance().subscribeToTopic("test");
                FirebaseInstanceId.getInstance().getToken();
                Intent eIntent=new Intent(login.this,Promotions.class);
                startActivity(eIntent);

            }
            else
            {

                Toast.makeText(login.this, message, Toast.LENGTH_LONG).show();

            }
        }

        private String postData(String urlPath) throws IOException,JSONException{
            StringBuilder result =new StringBuilder();
            BufferedWriter bufferedWriter=null;
            BufferedReader bufferedReader=null;
            StringBuilder builder;
            try{
                JSONObject dataToSend=new JSONObject();

                dataToSend.put("email",mail2);
                dataToSend.put("password",pass);
                dataToSend.put("token",FirebaseInstanceId.getInstance().getToken());




                //building connection to the server
                URL url=new URL(urlPath);
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

}
