package com.oneawadhcenter.halwits;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.oneawadhcenter.halwits.R;
import com.google.firebase.iid.FirebaseInstanceId;


public class register extends Activity implements OnItemSelectedListener {

    Calendar myCalendar = Calendar.getInstance();
    EditText edittext,email1,name1,mob1,pass1,conpass1,dob1;
    String gender;

    Button regis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        email1=(EditText)findViewById(R.id.emailet);
        name1=(EditText)findViewById(R.id.nameet);
        mob1=(EditText)findViewById(R.id.mobileet);
        pass1=(EditText)findViewById(R.id.passet);
        conpass1=(EditText)findViewById(R.id.conpasset);
        dob1=(EditText)findViewById(R.id.dobet);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Male");
        categories.add("Female");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.s_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        gender=spinner.getSelectedItem().toString();

        TextView tv=(TextView)findViewById(R.id.oldaccount);
        ImageView imgv=(ImageView)findViewById(R.id.cal);
         edittext=(EditText)findViewById(R.id.dobet);
        imgv.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DatePickerDialog datePickerDialog=new DatePickerDialog(register.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });


         final String cmob =mob1.getText().toString();

        regis=(Button)findViewById(R.id.update);
        regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new postDataTask().execute("http://oneawadhcenter.com/api/signup");

                /*if(cmob.length()==10)
                {


                }
                else
                {
                    Toast.makeText(register.this,"Please enter a valid mobile number", Toast.LENGTH_LONG).show();

                }*/

            }
        });

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent=new Intent(register.this,login.class);
                startActivity(loginIntent);
                overridePendingTransition(R.anim.from_l, R.anim.to_r);
            }
        });

    }

    private void updateLabel() {

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        edittext.setText(sdf.format(myCalendar.getTime()));
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
        // On selecting a spinner item


        if(i==0)
        {
            gender="male";

        }
        else
        {
            gender="female";
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    class postDataTask extends AsyncTask<String,Void,String>
    {
        ProgressDialog progressDialog;

         String mail=email1.getText().toString();

        String mail2=mail.replace("_",".");

        final String name=name1.getText().toString();
        final String pass=pass1.getText().toString();
        final String conpass=conpass1.getText().toString();
        final String mobile=mob1.getText().toString();
        final String dob=dob1.getText().toString();
        String status="",message,nameee;
        int id;



        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog=new ProgressDialog(register.this);
            progressDialog.setMessage("Please Wait...");
            progressDialog.show();
            //progressDialog.dismiss();
        }
        @Override
        protected String doInBackground(String... params) {
            try {

                return postData(params[0]);
            } catch (IOException ex) {
                Log.d("result ","exception");

                return "Network Error!";
            } catch (JSONException ex) {
                return "Data Invalid !";
            }
        }
        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
            Log.d("result2 ",result );

            JSONObject jsonObj = null;
            try {
                jsonObj = new JSONObject(result);
                JSONObject c =jsonObj.getJSONObject("list");


                status = c.getString("status");
                message= c.getString("message");
                if(!status.equals("fail"))
                {

                    id=c.getInt("user_id");
                    nameee=c.getString("name");
                }




            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(progressDialog!=null)
            {
                progressDialog.dismiss();
            }
            if(status.equals("success"))
            {
                /*SharedPreferences sp=getApplicationContext().getSharedPreferences("Login",Context.MODE_PRIVATE);
                SharedPreferences.Editor spe=sp.edit();
                spe.putInt("Login",1);
                spe.putInt("Id",id);
                spe.putString("name",name);
                spe.putString("number",mobile);
                spe.commit();*/


                Intent eIntent=new Intent(register.this,otp.class);
                eIntent.putExtra("name",name);
                eIntent.putExtra("Id",id);
                eIntent.putExtra("number",mobile);
                startActivity(eIntent);



            }
            else
            {
                Toast.makeText(register.this, message, Toast.LENGTH_LONG).show();

            }
        }

        private String postData(String urlPath) throws IOException,JSONException{
            StringBuilder result =new StringBuilder();
            BufferedWriter bufferedWriter=null;
            BufferedReader bufferedReader=null;
            StringBuilder builder;
            try{

                JSONObject dataToSend=new JSONObject();
                Log.d("out ",name);
                Log.d("in ",gender);


                dataToSend.put("name",name);
                dataToSend.put("email",mail2);
                dataToSend.put("password",pass);
                dataToSend.put("confirm_password",conpass);
                dataToSend.put("mobile_number",mobile);
                dataToSend.put("birthday",dob);
                dataToSend.put("gender",gender);
                dataToSend.put("token", FirebaseInstanceId.getInstance().getToken());



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
                Log.d("result is ",result.toString());
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
