package com.oneawadhcenter.halwits;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.oneawadhcenter.halwits.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by User on 7/1/2017.
 */
public class custom_adapter_claim extends RecyclerView.Adapter<custom_adapter_claim.CustomHolderClaim>
{
    public ArrayList<couponDetail> Listt=new ArrayList<couponDetail>();
    Context c;
    private Activity activity;
    public ArrayList<String> Urll=new ArrayList<String>();

    public custom_adapter_claim(Activity activity,Context context,ArrayList<couponDetail> Listt)
    {
        this.Listt=Listt;
        this.c=context;
        this.activity=activity;
    }


    @Override
    public CustomHolderClaim onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.coupon_row,parent,false);
        CustomHolderClaim detailViewHolder=new CustomHolderClaim(view);
        return detailViewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomHolderClaim holder, final int position) {
        couponDetail de=Listt.get(position);
        holder.dess.setText(Listt.get(position).getDesc());
        holder.nam.setText(Listt.get(position).getName());
        Urll.add(Listt.get(position).getUrl());
        if(Listt.get(position).getRed().equals("No")) {
            holder.url.setText("Claim");
        }

        else {
            holder.url.setText("Redeemed");
            holder.url.setBackgroundResource(R.drawable.greybutton);
        }

        holder.dat.setText(Listt.get(position).getDate());


        holder.imgg.setImageResource(R.drawable.gola2);
        //new DownloadImageTask(holder.imgg).execute("https:"+Listt.get(position).getImage());
        Picasso.with(c)
                .load("https:"+Listt.get(position).getImage())
                .placeholder(R.drawable.gola2)
                .into(holder.imgg);
        holder.imgg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {

                final Dialog dialog = new Dialog(activity);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.open_dialog);
                dialog.setCanceledOnTouchOutside(true);


                //dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                ImageView image = (ImageView) dialog.findViewById(R.id.imageView2);
                image.setImageResource(R.drawable.gola);
                Picasso.with(c)
                        .load("https:"+Listt.get(position).getImage())
                        .placeholder(R.drawable.gola)
                        .into(image);






                dialog.show();

                // your code here
            }
        });
        holder.url.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent i=new Intent(activity,Coupon.class);
                i.putExtra("Url",Listt.get(position).getUrl());
                i.putExtra("Redeem",Listt.get(position).getRed());
                v.getContext().startActivity(i);
            }
        });
        holder.dess.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent i=new Intent(activity,Coupon.class);
                i.putExtra("Url",Listt.get(position).getUrl());
                i.putExtra("Redeem",Listt.get(position).getRed());
                v.getContext().startActivity(i);
            }
        });
        holder.dat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent i=new Intent(activity,Coupon.class);
                i.putExtra("Url",Listt.get(position).getUrl());
                i.putExtra("Redeem",Listt.get(position).getRed());
                v.getContext().startActivity(i);
            }
        });
        holder.nam.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent i=new Intent(activity,Coupon.class);
                i.putExtra("Url",Listt.get(position).getUrl());
                i.putExtra("Redeem",Listt.get(position).getRed());
                v.getContext().startActivity(i);
            }
        });





    }

    @Override
    public int getItemCount() {
        return Listt.size();
    }
    public static class CustomHolderClaim extends  RecyclerView.ViewHolder
    {
        TextView nam;
        TextView dess;
        ImageView imgg;
        TextView url;
        TextView dat;


        public CustomHolderClaim(View itemView)
        {
            super(itemView);
            dess=(TextView)  itemView.findViewById(R.id.desc);
            imgg=(ImageView)  itemView.findViewById(R.id.img);
            nam=(TextView)  itemView.findViewById(R.id.name);
            url=(TextView)  itemView.findViewById(R.id.url);
            dat=(TextView)  itemView.findViewById(R.id.date);


        }

    }


}
