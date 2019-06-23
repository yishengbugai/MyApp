package com.example.testactivity;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class Main2Adapter extends RecyclerView.Adapter<Main2Adapter.ViewHolder> {
    private static final String TAG = "Main2Adapter";

    private Context mContext;
    int mPosition;
    private ArrayList<PictureInfo> imagelist;
    String type;

    static   class ViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        ImageView image1;
        TextView textViewone;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            image1=(ImageView)itemView.findViewById(R.id.image_item2);
            textViewone=(TextView)itemView.findViewById(R.id.variety);
        }
    }


    public Main2Adapter(ArrayList<PictureInfo> list,String s) {
        imagelist = list;
        type=s;
        Log.i(TAG, "getView: "+"进入Adapter");
    }


    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int i) {
        if(mContext==null){
            mContext=parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item2,parent,false);
        final ViewHolder holder= new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int postion = holder.getAdapterPosition();
                Intent intent = new Intent(mContext,Main3Activity.class);
                intent.putExtra("Url",imagelist.get(postion).getUrl());
                intent.putExtra("Type",type);
                mContext.startActivity(intent);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.textViewone.setText(imagelist.get(position).getTitle());
        Glide.with(mContext).load(imagelist.get(position).getImage()).placeholder(R.drawable.jia).into(viewHolder.image1);
        mPosition = position;
        Log.e("mPosition:",mPosition+"");
    }


    @Override
    public int getItemCount() {
        return imagelist.size();
    }



}
