package com.oneawadhcenter.halwits;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.oneawadhcenter.halwits.R;

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

public class otp extends AppCompatActivity {

    int u_id;
    EditText otp,et1,et2,et3,et4;
    String namess,num;
    TextView resend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            namess = bundle.getString("name");
            u_id = bundle.getInt("Id");
            num = bundle.getString("number");
        }

        /*SharedPreferences sp=getApplicationContext().getSharedPreferences("Login", Context.MODE_PRIVATE);
        u_id=sp.getInt("Id",0);


        SharedPreferences spp=getApplicationContext().getSharedPreferences("Login",Context.MODE_PRIVATE);
        namess=spp.getString("name","");*/
        Button verify=(Button)findViewById(R.id.verify);
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new verifyOtp().execute("http://oneawadhcenter.com/api/verify_otp");


            }
        });

        resend=(TextView)findViewById(R.id.resend);
        resend.setVisibility(View.INVISIBLE);
        resend.postDelayed(new Runnable() {
            public void run() {
                resend.setVisibility(View.VISIBLE);
            }
        }, 10000);

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("resend","oneawadhcenter.com/api/send_otp/"+u_id+"/"+num);

                new resendOtp().execute("http://oneawadhcenter.com/api/send_otp/"+u_id+"/"+num);


            }
        });

        et1=(EditText)findViewById(R.id.editText1);
        et2=(EditText)findViewById(R.id.editText2);
        et3=(EditText)findViewById(R.id.editText3);
        et4=(EditText)findViewById(R.id.editText4);
        et1.addTextChangedListener(new GenericTextWatcher(et1));
        et2.addTextChangedListener(new GenericTextWatcher(et2));
        et3.addTextChangedListener(new GenericTextWatcher(et3));
        et4.addTextChangedListener(new GenericTextWatcher(et4));


    }
    public class GenericTextWatcher implements TextWatcher
    {
        private View view;
        private GenericTextWatcher(View view)
        {
            this.view = view;
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // TODO Auto-generated method stub
            String text = editable.toString();
            switch(view.getId())
            {

                case R.id.editText1:
                    if(text.length()==1)
                        et2.requestFocus();

                    break;
                case R.id.editText2:
                    if(text.length()==1)
                        et3.requestFocus();
                    break;
                case R.id.editText3:
                    if(text.length()==1)
                        et4.requestFocus();
                    break;
                case R.id.editText4:
                    break;
            }
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }
    }
    class verifyOtp extends AsyncTask<String,Void,String>
    {
        ProgressDialog progressDialog;

        String x1=et1.getText().toString();

        String x2=et2.getText().toString();

        String x3=et3.getText().toString();

        String x4=et4.getText().toString();


        String otp2=x1+x2+x3+x4;
        String message,status="";




        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog=new ProgressDialog(otp.this);
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
                SharedPreferences sp=getApplicationContext().getSharedPreferences("Login",Context.MODE_PRIVATE);
                SharedPreferences.Editor spe=sp.edit();
                spe.putInt("Login",1);
                spe.putInt("Id",u_id);
                spe.putString("name",namess);
                spe.commit();
                Toast.makeText(otp.this, "Successfull", Toast.LENGTH_SHORT).show();

                Intent eIntent=new Intent(otp.this,Promotions.class);
                startActivity(eIntent);

            }
            else
            {
                Intent eIntent=new Intent(otp.this,otp.class);
                startActivity(eIntent);

                Toast.makeText(otp.this,"OTP Incorrect Please Try Again", Toast.LENGTH_LONG).show();

                //Toast.makeText(otp.this, message, Toast.LENGTH_LONG).show();

            }
        }

        private String postData(String urlPath) throws IOException,JSONException{
            StringBuilder result =new StringBuilder();
            BufferedWriter bufferedWriter=null;
            BufferedReader bufferedReader=null;
            StringBuilder builder;
            try{
                JSONObject dataToSend=new JSONObject();

                dataToSend.put("otp",otp2);
                dataToSend.put("user_id",u_id);




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

    class resendOtp extends AsyncTask<String,Void,String>
    {
        ProgressDialog progressDialog;
        String message,status;




        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog=new ProgressDialog(otp.this);
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
            if(progressDialog!=null)
            {
                progressDialog.dismiss();
            }

        }

        private String postData(String urlPath) throws IOException,JSONException{
            StringBuilder result =new StringBuilder();
            BufferedWriter bufferedWriter=null;
            BufferedReader bufferedReader=null;
            StringBuilder builder;
            try{
                JSONObject dataToSend=new JSONObject();
                //building connection to the server
                URL url=new URL(urlPath);
                HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(20000);
                urlConnection.setConnectTimeout(20000);

                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                urlConnection.connect();

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
            return "true";
        }
    }
}
