package com.example.testactivity;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MoreAdapter extends RecyclerView.Adapter<MoreAdapter.ViewHolder> {
    private static final String TAG = "MoreAdapter";

    private Context mContext;
    private ArrayList<ImageList> PictureList;
    private List<String> permissionList;
    int mposition;
    String type;
    View view;
    private int postion;
    static   class ViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        ImageView image1;
        ImageView image2;
        private ImageView download;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            image1=(ImageView)itemView.findViewById(R.id.image_item3);
            image2=(ImageView)itemView.findViewById(R.id.image_item4);
            download = (ImageView) itemView.findViewById(R.id.download);

        }
    }


    public MoreAdapter(ArrayList<ImageList> list,String s) {
        PictureList = list;
        type=s;
        Log.i(TAG, "getView: "+"进入Adapter");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        if(mContext==null){
            mContext=parent.getContext();
        }

            if(type.equals("竖图")){
        view = LayoutInflater.from(mContext).inflate(R.layout.picture_item,parent,false);
             }
            else if(type.equals("横图")) {
                view = LayoutInflater.from(mContext).inflate(R.layout.picture_item2,parent,false);
            }
        final ViewHolder holder= new ViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permissionList = new ArrayList<String>();
                mposition= holder.getAdapterPosition();
                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
                if (!permissionList.isEmpty()){
                    String[] permissions = permissionList.toArray(new String[permissionList.size()]);
                    ActivityCompat.requestPermissions((Activity) mContext,permissions,2);
                }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("设置壁纸");
                builder.setNegativeButton("锁屏壁纸", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Log.i(TAG, "position="+mposition+"");
                        Log.i(TAG, "壁纸链接="+PictureList.get(mposition).getUrl()+"");
                        GetWallPaper.setWallpaper((Activity) mContext,PictureList.get(mposition).getUrl(),"锁屏");

                    }
                });
                builder.setPositiveButton("主屏幕壁纸", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(TAG, "position="+mposition+"");
                        Log.i(TAG, "壁纸链接="+PictureList.get(mposition).getUrl()+"");
                        GetWallPaper.setWallpaper((Activity) mContext,PictureList.get(mposition).getUrl(),"主屏幕");

                    }
                });
                    builder.setNeutralButton("返回", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                    }
                    });

                builder.show();

            }
            }
        });

        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permissionList = new ArrayList<String>();
                postion = holder.getAdapterPosition();
                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
                if (!permissionList.isEmpty()){
                    String[] permissions = permissionList.toArray(new String[permissionList.size()]);
                    ActivityCompat.requestPermissions((Activity) mContext,permissions,2);
                }else {
                    Download.donwloadImage(mContext,PictureList.get(postion).getUrl());
                    ObjectAnimator ani = ObjectAnimator.ofFloat(holder.download,"translationY",0f,10f,20f,-10f,0f);
                    ani.setDuration(500);
                    ani.start();
                }
            }
        });


        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
            if(type.equals("竖图")) {
                Glide.with(mContext).load(PictureList.get(position).getUrl()).placeholder(R.drawable.jia).into(viewHolder.image1);  //加载图片
            }
            else{ Glide.with(mContext).load(PictureList.get(position).getUrl()).placeholder(R.drawable.jia).into(viewHolder.image2);  //加载图片
                 }
        int mPosition = position;
        Log.e("mPosition:",mPosition+"");
    }


    @Override
    public int getItemCount() {
        return PictureList.size();
    }
}
