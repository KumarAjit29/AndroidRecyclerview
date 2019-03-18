package com.example.myvideoplayer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class RetrofitAdapter extends RecyclerView.Adapter<RetrofitAdapter.MyViewHolder> {

    private String videoURLs;
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<ModelRecycler> dataModelArrayList;

    public RetrofitAdapter(Context context, ArrayList<ModelRecycler> dataModelArrayList) {
        this.context=context;
        inflater = LayoutInflater.from(context);
        this.dataModelArrayList = dataModelArrayList;
    }

    @Override
    public RetrofitAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.retro_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(RetrofitAdapter.MyViewHolder holder, int position) {

        Picasso.get().load(dataModelArrayList.get(position).getImgURL()).into(holder.iv);
        holder.title.setText(dataModelArrayList.get(position).getTitle());
        holder.description.setText(dataModelArrayList.get(position).getDescription());
        holder.videoUrl.setText(dataModelArrayList.get(position).getVideoUrl());
        videoURLs=(dataModelArrayList.get(position).getVideoUrl());


    }

    @Override
    public int getItemCount() {
        return dataModelArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title, description,videoUrl;
        ImageView iv;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);

            description = (TextView) itemView.findViewById(R.id.country);
            title = (TextView) itemView.findViewById(R.id.name);
            iv = (ImageView) itemView.findViewById(R.id.iv);
            videoUrl=(TextView) itemView.findViewById(R.id.videourl);
            cardView=itemView.findViewById(R.id.card_view);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String valueString = videoUrl.getText().toString();
                    Bundle sendBundle = new Bundle();
                    sendBundle.putString("value",valueString);
                    Intent intent= new Intent(context, Main2Activity.class);
                   // System.out.println("valueString===="+sendBundle);
                   // System.out.println("valueString===="+intent);
                    intent.putExtra("your_extra",videoUrl.getText().toString());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }

    }
}