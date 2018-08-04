package com.oneawadhcenter.halwits;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.oneawadhcenter.halwits.R;

public class navigation extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.openDrawer(Gravity.LEFT);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        SharedPreferences sp=getApplicationContext().getSharedPreferences("Login", Context.MODE_PRIVATE);
        String namess=sp.getString("name","");
        namess=namess.substring(0,1).toUpperCase()+namess.substring(1).toLowerCase();
        Log.d("name",namess);


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        name = (TextView) navigationView.getHeaderView(0).findViewById(R.id.text_name);
        name.setText(namess);

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.events) {
            Intent eIntent=new Intent(navigation.this,MainActivity.class);
            startActivity(eIntent);
            // Handle the camera action
        } else if (id == R.id.stores) {
            Intent sIntent=new Intent(navigation.this,Stores.class);
            startActivity(sIntent);

        }  else if (id == R.id.movies_running) {
            Intent mIntent=new Intent(navigation.this,movies.class);
            startActivity(mIntent);

        } else if (id == R.id.promotions) {
            Intent promoIntent=new Intent(navigation.this,Promotions.class);
            startActivity(promoIntent);

        } else if (id == R.id.help) {
            Intent helpIntent=new Intent(navigation.this,Help.class);
            startActivity(helpIntent);


        }
        else if (id == R.id.myprofile) {
            Intent helpIntent=new Intent(navigation.this,MyProfile.class);
            startActivity(helpIntent);


        }
        else if (id == R.id.invite) {
            Intent inviteIntent=new Intent(navigation.this,Contact_list.class);
            startActivity(inviteIntent);

        }
        else if (id == R.id.food_court) {
            Intent inviteIntent=new Intent(navigation.this,Food_Court.class);
            startActivity(inviteIntent);

        }
        else if (id == R.id.coupon) {
            Intent inviteIntent=new Intent(navigation.this,Claim.class);
            startActivity(inviteIntent);

        }
        else if (id == R.id.share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, " Download the One Awadh Center app,and enjoy a better shopping experience,whenever you visit the mall.\n"+"https://play.google.com/store/apps/details?id=com.oneawadhcenter.halwits&hl=en");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);

        }
        else if (id == R.id.logout) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

            // set title
            alertDialogBuilder.setTitle("Logout?");

            // set dialog message
            alertDialogBuilder
                    .setMessage("Click yes to exit!")
                    .setCancelable(false)
                    .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            SharedPreferences sp=getApplicationContext().getSharedPreferences("Login",0);
                            SharedPreferences.Editor spe=sp.edit();
                            spe.putInt("Login",0);
                            spe.putString("name","");
                            spe.putInt("Id",0);
                            spe.commit();
                            // if this button is clicked, close
                            // current activity

                            Intent intent = new Intent(getApplicationContext(), login.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("EXIT", true);
                            startActivity(intent);
                            overridePendingTransition(R.anim.from_r, R.anim.to_l);

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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
