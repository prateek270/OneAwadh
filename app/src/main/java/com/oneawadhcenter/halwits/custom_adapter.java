package com.oneawadhcenter.halwits;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.oneawadhcenter.halwits.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class custom_adapter extends RecyclerView.Adapter<custom_adapter.CustomViewHolder>
{
    public ArrayList<Details> Listt=new ArrayList<Details>();
    Context c;
    private Activity activity;

    public custom_adapter()
    {

    }
    public custom_adapter(Activity activity,Context context,ArrayList<Details> Listt)
    {
        this.Listt=Listt;
        this.c=context;
        this.activity=activity;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row,parent,false);
        CustomViewHolder detailViewHolder=new CustomViewHolder(view);
        return detailViewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {
        Details de=Listt.get(position);
        holder.dess.setText(Listt.get(position).getDesc());
        holder.nam.setText(Listt.get(position).getName());
        holder.date.setText(Listt.get(position).getDate());
        holder.imgg.setImageResource(R.drawable.gola2);
        holder.book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri uri = Uri.parse("http://www.cinepolisindia.com/now-playing/lucknow?gclid=CJOm8r7wstMCFQojaAodWAIBhg");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                view.getContext().startActivity(intent);
            }
        });
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



    }

    @Override
    public int getItemCount() {
        return Listt.size();
    }
    public static class CustomViewHolder extends  RecyclerView.ViewHolder
    {
        TextView nam;
        TextView dess;
        ImageView imgg;
        TextView date;
        TextView book;
        public CustomViewHolder(View itemView)
        {
            super(itemView);
            dess=(TextView)  itemView.findViewById(R.id.desc);
            imgg=(ImageView)  itemView.findViewById(R.id.img);
            nam=(TextView)  itemView.findViewById(R.id.name);
            date=(TextView)  itemView.findViewById(R.id.date);
            book=(TextView)itemView.findViewById(R.id.booknow);
        }
    }
}