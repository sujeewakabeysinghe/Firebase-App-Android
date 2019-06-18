package com.example.firebasesapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdaptor extends RecyclerView.Adapter<ImageAdaptor.ImageViewHolder> {

    private Context mContext;
    private List<Upload> mUpload;

    public ImageAdaptor(Context mContext, List<Upload> mUpload) {
        this.mContext = mContext;
        this.mUpload = mUpload;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.image_item,viewGroup,false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder imageViewHolder, int i) {
        Upload uploadCurrent=mUpload.get(i);
        imageViewHolder.tv2.setText(uploadCurrent.getName());
        Picasso.get().load(uploadCurrent.getImageUrl()).fit().centerInside().into(imageViewHolder.iv2 );
    }

    @Override
    public int getItemCount() {
        return mUpload.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{
        public TextView tv2;
        public ImageView iv2;
        public ImageViewHolder(View itemView) {
            super(itemView);
            tv2=itemView.findViewById(R.id.tv2);
            iv2=itemView.findViewById(R.id.iv2);
        }
    }
}
