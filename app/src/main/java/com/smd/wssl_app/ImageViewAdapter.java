package com.smd.wssl_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.w3c.dom.Text;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ImageViewAdapter extends RecyclerView.Adapter<ImageViewAdapter.MyViewHolder> {
    Context c;
    List<ImageViewModel> ls;


    public ImageViewAdapter(List<ImageViewModel> ls, Context c) {
        this.ls = ls;
        this.c = c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row= LayoutInflater.from(c).inflate(R.layout.image_view,parent,false);
        return new ImageViewAdapter.MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

//        holder.img.setImageDrawable(ls.get(position).getDrawable());
//        holder.img.setImageResource(R.drawable.club_1);
        Picasso.get().load(ls.get(position).getImgurl()).into(holder.img);

//        holder.img.setImageResource(ls.get(position).getimage);

//        Glide.with(c)
//                .load("http://"+IP_server.getIpServer()+"/streetfeast/"+pic)
//                .asBitmap()
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .skipMemoryCache(true)
//                .into(holder.img);

    }



    @Override
    public int getItemCount() {
        return ls.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView img;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img= itemView.findViewById(R.id.image_view);


        }
    }
}
