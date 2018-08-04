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
import android.widget.ImageView;
import android.widget.TextView;

import com.oneawadhcenter.halwits.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
public class custom_adapter_event extends RecyclerView.Adapter<custom_adapter_event.CustomViewHolderE>
{

    public ArrayList<eventDetails> Listt=new ArrayList<eventDetails>();
    Context c;
    private Activity activity;

    public custom_adapter_event()
    {

    }
    public custom_adapter_event(Activity activity,Context context,ArrayList<eventDetails> Listt)
    {
        this.Listt=Listt;
        this.c=context;
        this.activity=activity;
    }

    @Override
    public CustomViewHolderE onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row3,parent,false);
        CustomViewHolderE detailViewHolder=new CustomViewHolderE(view);
        return detailViewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolderE holder, final int position) {
        eventDetails de=Listt.get(position);
        holder.dess.setText(Listt.get(position).getDesc());
        holder.nam.setText(Listt.get(position).getName());
        holder.date.setText(Listt.get(position).getDate());
        holder.time.setText(Listt.get(position).getTime());
        holder.url.setText("Follow our event on facebook");
        holder.imgg.setImageResource(R.drawable.gola2);
        holder.url.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {

                String url = Listt.get(position).getUrl();

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                v.getContext().startActivity(i);
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
    public static class CustomViewHolderE extends  RecyclerView.ViewHolder
    {
        TextView nam;
        TextView dess;
        ImageView imgg;
        TextView date;
        TextView time;
        TextView url;
        public CustomViewHolderE(View itemView)
        {
            super(itemView);
            dess=(TextView)  itemView.findViewById(R.id.desc);
            imgg=(ImageView)  itemView.findViewById(R.id.img);
            nam=(TextView)  itemView.findViewById(R.id.name);
            date=(TextView)  itemView.findViewById(R.id.date);
            time=(TextView)  itemView.findViewById(R.id.time);
            url=(TextView)  itemView.findViewById(R.id.url);
        }
    }
}
