package com.oneawadhcenter.halwits;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class ChangePass extends AppCompatActivity {


    EditText newpass,connewpass;
    Button cp;
    String checknp,checkcnp;
    int u_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        u_id=bundle.getInt("Id");
        setContentView(R.layout.activity_change_pass);
        newpass=(EditText)findViewById(R.id.newpasset);
        connewpass=(EditText)findViewById(R.id.connewpasset);
        cp=(Button)findViewById(R.id.confirm);
        cp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new updatepass().execute("http://oneawadhcenter.com/api/update_password");

            }
        });

    }
    class updatepass extends AsyncTask<String,Void,String>
    {
        ProgressDialog progressDialog;


        String p1=newpass.getText().toString();


         String p2=connewpass.getText().toString();

        String status="",message,name;




        @Override
        protected void onPreExecute() {
        super.onPreExecute();

        progressDialog=new ProgressDialog(ChangePass.this);
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

            Toast.makeText(ChangePass.this, message, Toast.LENGTH_LONG).show();
            Intent eIntent=new Intent(ChangePass.this,login.class);
            startActivity(eIntent);

        }
        else
        {

            Toast.makeText(ChangePass.this, message, Toast.LENGTH_LONG).show();

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
            dataToSend.put("password",p1);
            dataToSend.put("confirm_password",p2);




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
