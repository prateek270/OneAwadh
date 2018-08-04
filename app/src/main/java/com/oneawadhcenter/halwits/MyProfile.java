package com.oneawadhcenter.halwits;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.TextView;
import android.widget.Toast;

import com.oneawadhcenter.halwits.R;
import com.google.firebase.iid.FirebaseInstanceId;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MyProfile extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    int u_id;
    public static String endpoint = "http://oneawadhcenter.com/api/edit/";
    public String url = "";
    private String TAG = MyProfile.class.getSimpleName();
    EditText tname,tdob,tgender,temail,tpass,tpass2,tmob;
    String name,dob,gender,email,pass,pass2,mob;
    TextView back;
    Button update;
    Calendar myCalendar = Calendar.getInstance();


    List<String> categories;
    ArrayAdapter<String> dataAdapter;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        SharedPreferences sp=getApplicationContext().getSharedPreferences("Login", Context.MODE_PRIVATE);
        u_id=sp.getInt("Id",0);
        url=endpoint+u_id;
        //url=endpoint+19;
        Log.d("url",url);
        tname=(EditText) findViewById(R.id.nameet);

        tdob=(EditText) findViewById(R.id.dobet);

        //tgender=(EditText) findViewById(R.id.genet);

        temail=(EditText) findViewById(R.id.emailet);

        tpass=(EditText) findViewById(R.id.passet);
        tpass2=(EditText) findViewById(R.id.conpasset);
        tmob=(EditText) findViewById(R.id.mobileet);
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        back=(TextView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MyProfile.this,navigation.class);
                startActivity(i);
            }
        });


        ImageView imgv=(ImageView)findViewById(R.id.cal);
        imgv.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DatePickerDialog datePickerDialog=new DatePickerDialog(MyProfile.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });




        update=(Button) findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gender=spinner.getSelectedItem().toString();
                new updateProfile().execute("http://oneawadhcenter.com/api/update");
            }
        });


        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        categories = new ArrayList<String>();
        categories.add("Male");
        categories.add("Female");
        dataAdapter = new ArrayAdapter<String>(this, R.layout.s_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        new GetProfile().execute();
    }
    private void updateLabel() {

        //String myFormat = "dd/MM/yyyy";
        String myFormat = "yyyy-MM-dd";//In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        tdob.setText(sdf.format(myCalendar.getTime()));
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    private class GetProfile extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MyProfile.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

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
                    //JSONArray contacts = jsonObj.getJSONArray("list");



                    JSONObject c = jsonObj.getJSONObject("list");

                    email = c.getString("email");
                    name=c.getString("name");
                    //name=name.toLowerCase();
                    name=name.substring(0,1).toUpperCase()+name.substring(1).toLowerCase();

                    pass =c.getString("password");
                    dob=c.getString("birthday");
                    gender=c.getString("gender");
                    Log.d("gen",gender);



                    mob=c.getString("mobile_number");




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

            temail.setText(email);
            tname.setText(name);
            tpass.setText(pass);
            tdob.setText(dob);
            tpass2.setText(pass);
            tmob.setText(mob);
            tmob.setFocusable(false);
            tpass2.setText(pass);

            final Spinner spinner = (Spinner) findViewById(R.id.spinner);
            spinner.setAdapter(dataAdapter);
            if(gender.equals("male"))
            spinner.setSelection(0);
            else
                spinner.setSelection(1);
            if (pDialog.isShowing())
                pDialog.dismiss();




        }

    }
    class updateProfile extends AsyncTask<String,Void,String>
    {
        ProgressDialog progressDialog;

        String mail=temail.getText().toString();

        String mail2=mail.replace("_",".");

        final String name=tname.getText().toString();
        final String pass=tpass.getText().toString();
        final String conpass=tpass2.getText().toString();
        final String mobile=tmob.getText().toString();
        final String dob=tdob.getText().toString();

        String status,message,nameee;
        int id;



        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog=new ProgressDialog(MyProfile.this);
            progressDialog.setMessage("Updating details...");
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

            JSONObject jsonObj = null;
            try {
                jsonObj = new JSONObject(result);
                JSONObject c =jsonObj.getJSONObject("list");


                status = c.getString("status");

                message= c.getString("message");
                //id=c.getInt("user_id");
                nameee=c.getString("name");
            } catch (JSONException e) {
                e.printStackTrace();
            }



            if(progressDialog!=null)
            {
                progressDialog.dismiss();
            }
            if(status.equals("success"))
            {
                SharedPreferences sp=getApplicationContext().getSharedPreferences("Login",0);
                SharedPreferences.Editor spe=sp.edit();
                spe.putInt("Login",1);
                spe.putString("name",name);
                spe.commit();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MyProfile.this);
                alertDialogBuilder.setTitle("Thank You");

                // set dialog message
                alertDialogBuilder
                        .setMessage("Your profile has been successfully updated")
                        .setCancelable(false)
                        .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {

                                Intent intent = new Intent(getApplicationContext(), navigation.class);
                                startActivity(intent);


                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
            else
            {
                Toast.makeText(MyProfile.this, message, Toast.LENGTH_LONG).show();

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
                dataToSend.put("name",name);
                dataToSend.put("email",mail2);
                dataToSend.put("password",pass);
                dataToSend.put("confirm_password",conpass);
                dataToSend.put("mobile_number",mobile);
                dataToSend.put("birthday",dob);
                if(gender.equals("Male"))
                    gender="male";
                else
                    gender="female";
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
                Log.d("result ",result.toString());
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
