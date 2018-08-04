package com.oneawadhcenter.halwits;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.oneawadhcenter.halwits.R;

/**
 * Created by User on 7/3/2017.
 */
public class Claim_coupon extends Activity {
    TextView ccc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.claim_coupon);
        Bundle bundle = getIntent().getExtras();
        String code=bundle.getString("Code");
        ccc=(TextView)findViewById(R.id.cc);
        ccc.setText(code);

        ImageButton back=(ImageButton)findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent RoomButtonIntent=new Intent(Claim_coupon.this,Claim.class);
                startActivity(RoomButtonIntent);
            }
        });



    }
    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        Intent i=new Intent(Claim_coupon.this,Claim.class);
        startActivity(i);
    }
}
