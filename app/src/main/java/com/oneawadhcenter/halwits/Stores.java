package com.oneawadhcenter.halwits;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import android.widget.AdapterView.OnItemSelectedListener;

import com.oneawadhcenter.halwits.R;

public class Stores extends AppCompatActivity implements OnItemSelectedListener {

    Context context;
    private String TAG = movies.class.getSimpleName();
    String choice;
    private ProgressDialog pDialog;
    ArrayList<promoDetail> arrayList=new ArrayList<promoDetail>();
    public static String url = "http://www.oneawadhcenter.com/api/brand_list";

    public ArrayList<String> Name=new ArrayList<String>();
    public ArrayList<String> Butt=new ArrayList<String>();
    public ArrayList<String> Desc=new ArrayList<String>();
    public ArrayList<String> Img=new ArrayList<String>();
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    int check;
    String ccc[]={"All","Anchor Store","Apparel","Bags","Beauty","Electronics","Food and Beverage","Footwear","Jewellery","Kids Store","Luggage","Mens Clothing","Sports","Watch Store","Womens Clothing","Women Lingerie","Ground Floor","First Floor","Second Floor","Third Floor","Fourth Floor"};
    ImageButton down;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stores);


        final Spinner spinner = (Spinner) findViewById(R.id.spinner2);


        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("All");
        categories.add("Anchor Store");
        categories.add("Apparel");
        categories.add("Bags");
        categories.add("Beauty");
        categories.add("Electronics");
        categories.add("Food and Beverage");
        categories.add("Footwear");
        categories.add("Jewellery");
        categories.add("Kids Store");
        categories.add("Luggage");
        categories.add("Mens Clothing");
        categories.add("Sports");
        categories.add("Watch Store");
        categories.add("Womens Clothing");
        categories.add("Women Lingerie");
        categories.add("Ground Floor");
        categories.add("First Floor");
        categories.add("Second Floor");
        categories.add("Third Floor");
        categories.add("Fourth Floor");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,R.layout.spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        choice=spinner.getSelectedItem().toString();

        context = this;



        //lv = (ListView) findViewById(R.id.listView);
        //lv.setAdapter(new custom_adapter(this, prgmNameList, prgmImages, xyz));
        ImageButton back=(ImageButton)findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent RoomButtonIntent=new Intent(Stores.this,navigation.class);
                startActivity(RoomButtonIntent);
            }
        });
        down=(ImageButton)findViewById(R.id.downButton);
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinner.performClick();
            }
        });




    }
    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        Intent i=new Intent(Stores.this,navigation.class);
        startActivity(i);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
    {

        Log.e("choice is ", ccc[i]);
        if(i==0)
        {


            url = "http://www.oneawadhcenter.com/api/brand_list";
            check=0;
            choice=ccc[i];
            Name.clear();
            Desc.clear();
            Img.clear();
            arrayList.clear();

            new GetStores().execute();

        }
        else if(i<=15)
        {

            url = "http://www.oneawadhcenter.com/api/brand_list";
            check=2;
            choice=ccc[i];
            Name.clear();
            Desc.clear();
            Img.clear();
            arrayList.clear();
            new GetStores().execute();
        }
        else
        {
            url = "http://www.oneawadhcenter.com/api/brand_list";
            check=3;
            choice=ccc[i];
            Name.clear();
            Desc.clear();
            Img.clear();
            arrayList.clear();
            new GetStores().execute();

        }



    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private class GetStores extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Stores.this);
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

                    JSONObject cc =jsonObj.getJSONObject("list");
                    JSONArray contacts = cc.getJSONArray("brand");
                    //JSONArray category = cc.getJSONArray("category");
                    Log.d("choice 2 ",check+"");
                    if(check==0)
                    {
                        for (int i = 0; i <contacts.length(); i++)
                        {
                            JSONObject c = contacts.getJSONObject(i);


                            String name = c.getString("brand_name");

                            String desc = c.getString("brand_description");
                            String temp1=name.replace("&#39;","'");
                            name=temp1;
                            String temp=desc.replace("&#39;","'");
                            desc=temp;
                            String img = c.getString("brand_image");
                            Name.add(name);
                            Desc.add(desc);
                            Img.add(img);

                        }

                    }
                    else if(check==2)
                    {

                        for (int i = 0; i <contacts.length(); i++)
                        {

                            JSONObject c = contacts.getJSONObject(i);


                            String name = c.getString("brand_name");

                            String desc = c.getString("brand_description");
                            String temp1=name.replace("&#39;","'");
                            name=temp1;
                            String temp=desc.replace("&#39;","'");
                            desc=temp;
                            String img = c.getString("brand_image");
                            String cat=c.getString("brand_category");
                            if(cat.equals(choice))
                            {
                                Name.add(name);
                                Desc.add(desc);
                                Img.add(img);

                            }



                        }

                    }
                    else
                    {
                        for (int i = 0; i <contacts.length(); i++)
                        {

                            JSONObject c = contacts.getJSONObject(i);


                            String name = c.getString("brand_name");

                            String desc = c.getString("brand_description");
                            String temp1=name.replace("&#39;","'");
                            name=temp1;
                            String temp=desc.replace("&#39;","'");
                            desc=temp;
                            String img = c.getString("brand_image");
                            String floor=c.getString("floor_no");
                            if(floor.equals(choice))
                            {
                                Name.add(name);
                                Desc.add(desc);
                                Img.add(img);

                            }



                        }

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
                promoDetail pd=new promoDetail(Name.get(i),Desc.get(i),Img.get(i));

                arrayList.add(pd);
            }
            recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
            layoutManager=new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            // recyclerView.setHasFixedSize(true);
            adapter=new custom_adapter_promo(Stores.this,getApplicationContext(),arrayList);
            recyclerView.setAdapter(adapter);
        }

    }
}

