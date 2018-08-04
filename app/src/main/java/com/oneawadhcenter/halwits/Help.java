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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import java.util.ArrayList;
import java.util.List;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

import com.oneawadhcenter.halwits.R;

import org.json.JSONException;
import org.json.JSONObject;


public class Help extends AppCompatActivity implements OnItemSelectedListener{


    EditText username,msg;
    String cat,type;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private ImageButton btnDisplay;
    int u_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        SharedPreferences sp=getApplicationContext().getSharedPreferences("Login", Context.MODE_PRIVATE);
        u_id=sp.getInt("Id",0);

        ImageButton back=(ImageButton)findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent RoomButtonIntent=new Intent(Help.this,navigation.class);
                startActivity(RoomButtonIntent);
            }
        });
        //username=(EditText)findViewById(R.id.nameet);
        msg=(EditText)findViewById(R.id.messageet);



        // Spinner element
        Spinner spinner = (Spinner) findViewById(R.id.spinner2);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("About Cinepolis Experience");
        categories.add("About Food Court Experience");
        categories.add("About Shopping Experience");
        categories.add("Other");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        btnDisplay = (ImageButton) findViewById(R.id.subButton);

        btnDisplay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // get selected radio button from radioGroup
                int selectedId = radioGroup.getCheckedRadioButtonId();



                // find the radiobutton by returned id

                if(selectedId==-1)
                {
                    Toast.makeText(Help.this,"Please select Message type", Toast.LENGTH_LONG).show();
                }
                else
                {
                    radioButton = (RadioButton) findViewById(selectedId);
                    type=(String)radioButton.getText();
                    Log.d("type",type);



                    new helpDataTask().execute("http://www.oneawadhcenter.com/api/feedback");
                }


            }

        });

    }
    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        Intent i=new Intent(Help.this,navigation.class);
        startActivity(i);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        // On selecting a spinner item
        cat= parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    class helpDataTask extends AsyncTask<String,Void,String>
    {
        ProgressDialog progressDialog;
        String status="xyz";
         //String name1=username.getText().toString();

        String msg1=msg.getText().toString();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog=new ProgressDialog(Help.this);
            progressDialog.setMessage("Please Wait...");
            progressDialog.show();
            //progressDialog.dismiss();
        }
        @Override
        protected String doInBackground(String... params) {
            try {

                return postData(params[0]);
            } catch (IOException ex) {
                return "Network Error!!";
            } catch (JSONException ex) {
                return "Data Invalid !";
            }
        }
        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
            Log.e("result ",result );
            JSONObject jsonObj = null;
            try {
                jsonObj = new JSONObject(result);
                JSONObject c =jsonObj.getJSONObject("list");


                status = c.getString("status");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Getting JSON Array node




            if(progressDialog!=null)
            {
                progressDialog.dismiss();
            }
            Log.d("result is",status);
            if(status.equals("success"))
            {

                Toast.makeText(Help.this, "Thank you your Feedback is submitted", Toast.LENGTH_LONG).show();
                Intent eIntent=new Intent(Help.this,Promotions.class);
                startActivity(eIntent);

            }
            else
            {

                Toast.makeText(Help.this, "Error in submitting Feedback", Toast.LENGTH_LONG).show();

            }
        }

        private String postData(String urlPath) throws IOException,JSONException{
            StringBuilder result =new StringBuilder();
            BufferedWriter bufferedWriter=null;
            BufferedReader bufferedReader=null;
            StringBuilder builder;
            try{
                JSONObject dataToSend=new JSONObject();


                //dataToSend.put("name","app");
                Log.d("type ",type);
                dataToSend.put("category",cat);
                dataToSend.put("type",type);
                dataToSend.put("message",msg1);
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
}
