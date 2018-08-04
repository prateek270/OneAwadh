package com.oneawadhcenter.halwits;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
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


public class custom_adapter_promo extends RecyclerView.Adapter<custom_adapter_promo.CustomHolder>
{
    public ArrayList<promoDetail> Listt=new ArrayList<promoDetail>();
    Context c;
    private Activity activity;
    Typeface font;

    public custom_adapter_promo(Activity activity,Context context,ArrayList<promoDetail> Listt)
    {
        this.Listt=Listt;
        this.c=context;
        this.activity=activity;
        //font = Typeface.createFromAsset(context.getAssets(), "fonts/Muli_Regular.ttf");
    }


    @Override
    public CustomHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row2,parent,false);
        CustomHolder detailViewHolder=new CustomHolder(view);
        return detailViewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomHolder holder, final int position) {
        promoDetail de=Listt.get(position);
        holder.dess.setText(Listt.get(position).getDesc());
        holder.nam.setText(Listt.get(position).getName());
        //holder.dess.setTypeface(font);
        //holder.butt.setText(Listt.get(position).getButt());


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





    }

    @Override
    public int getItemCount() {
        return Listt.size();
    }
    public static class CustomHolder extends  RecyclerView.ViewHolder
    {
        TextView nam;
        TextView dess;
        ImageView imgg;

        public CustomHolder(View itemView)
        {
            super(itemView);
            dess=(TextView)  itemView.findViewById(R.id.desc);
            imgg=(ImageView)  itemView.findViewById(R.id.img);
            nam=(TextView)  itemView.findViewById(R.id.name);
        }

    }
}