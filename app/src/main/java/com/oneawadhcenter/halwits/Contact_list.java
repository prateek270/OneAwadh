package com.oneawadhcenter.halwits;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.oneawadhcenter.halwits.R;

public class Contact_list extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        ImageButton back=(ImageButton)findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent RoomButtonIntent=new Intent(Contact_list.this,navigation.class);
                startActivity(RoomButtonIntent);
            }
        });
        ImageButton importc=(ImageButton)findViewById(R.id.contact_import);
        importc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent RoomButtonIntent=new Intent(Contact_list.this,Read_Contact.class);
                startActivity(RoomButtonIntent);
            }
        });
    }
    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        Intent i=new Intent(Contact_list.this,navigation.class);
        startActivity(i);
    }
}
