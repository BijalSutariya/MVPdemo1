package com.example.matrixhive.mvpdemo1.accidentDetails.imageDetails;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.matrixhive.mvpdemo1.R;

import java.io.File;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class AccidentImageAdapter extends RecyclerView.Adapter<AccidentImageAdapter.MyViewHolder> {
    private List<AccidentImageModel> photoList;
    private Context context;
    private OnImageAddClick listener;

    /**
     * AccidentImageAdapter Constructor
     * @param photoList
     * @param context
     * @param listener
     */
    public AccidentImageAdapter(List<AccidentImageModel> photoList, Context context, OnImageAddClick listener) {
        this.photoList = photoList;
        this.context = context;
        this.listener = listener;
    }

    /**
     * onCreateViewHolder Inflate layout
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_image_row, parent, false);
        return new MyViewHolder(view);
    }

    /**
     * onBindViewHolder bind data
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        AccidentImageModel data = photoList.get(position);
        if (TextUtils.isEmpty(data.getImages())){
            holder.ivPhoto.setImageResource(R.drawable.ic_camera);
            holder.ivDelete.setVisibility(View.GONE);
        }
        else  {
            holder.ivDelete.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(new File(data.getImages()))
                    .into(holder.ivPhoto);
        }
    }

    /**
     * getItemCount list size
     * @return
     */
    @Override
    public int getItemCount() {
        return photoList.size();
    }

    /**
     * MyViewHolder view initialize
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPhoto, ivDelete;
        public MyViewHolder(View itemView) {
            super(itemView);

            ivPhoto = itemView.findViewById(R.id.ivImageProof);
            ivDelete = itemView.findViewById(R.id.ivDelete);

            ivPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AccidentImageModel data = photoList.get(getAdapterPosition());
                    if (TextUtils.isEmpty(data.getImages())) {
                        listener.onMyClick(v, getAdapterPosition());
                    }
                }
            });
            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AccidentImageModel data = photoList.get(getAdapterPosition());
                    if (!TextUtils.isEmpty(data.getImages())) {
                        listener.onMyClick(v, getAdapterPosition());
                    }
                }
            });

        }
    }
}
