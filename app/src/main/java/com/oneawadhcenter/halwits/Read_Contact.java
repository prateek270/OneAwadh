package com.oneawadhcenter.halwits;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
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

public class Read_Contact extends AppCompatActivity {

    String namecsv="";
    String phonecsv="";
    int i,value;
    ListView lv1;
    Button invites,select,deselect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read__contact);

        ActivityCompat.requestPermissions(Read_Contact.this, new String[]{Manifest.permission.READ_CONTACTS}, 1);

        ImageButton back = (ImageButton) findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent RoomButtonIntent = new Intent(Read_Contact.this, Contact_list.class);
                startActivity(RoomButtonIntent);
            }
        });


        SharedPreferences sp=getApplicationContext().getSharedPreferences("Login",0);
         value=sp.getInt("Id",1);
        Log.d("id is ",value+"");


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
// Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
// Handle action bar item clicks here. The action bar will
// automatically handle clicks on the Home/Up button, so long
// as you specify a parent activity in AndroidManifest.xml.
       /* int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {

                    //lv1 = (ListView) findViewById(R.id.listView1);


                    Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
                    while (phones.moveToNext())
                    {





                        String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));


                        String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                        if(name!=null && phoneNumber!=null)
                        {
                            namecsv += name + "::";
                            phonecsv += phoneNumber + "::";
                        }


                    }
                    phones.close();
                    new inviteDataTask().execute("http://www.oneawadhcenter.com/api/invite_friends");

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(Read_Contact.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    class inviteDataTask extends AsyncTask<String,Void,String>
    {
        ProgressDialog progressDialog;
        String status,message,name;
        int id;



        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog=new ProgressDialog(Read_Contact.this);
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
                Toast.makeText(Read_Contact.this, "Contacts Successfully Uploaded ", Toast.LENGTH_LONG).show();
                Intent eIntent=new Intent(Read_Contact.this,navigation.class);
                startActivity(eIntent);

            }
            else
            {

                Toast.makeText(Read_Contact.this, message, Toast.LENGTH_LONG).show();

            }
        }

        private String postData(String urlPath) throws IOException,JSONException{
            StringBuilder result =new StringBuilder();
            BufferedWriter bufferedWriter=null;
            BufferedReader bufferedReader=null;
            StringBuilder builder;


            try{
                JSONObject dataToSend=new JSONObject();

                dataToSend.put("user_id",value);
                dataToSend.put("names",namecsv);
                dataToSend.put("numbers",phonecsv);




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
