package com.smd.wssl_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatMembersAdapter extends RecyclerView.Adapter<ChatMembersAdapter.MyViewHolder> {
    List<ChatMemberModel> ls;
    Context c;

    public ChatMembersAdapter(List<ChatMemberModel> ls, Context c) {
        this.ls = ls;
        this.c = c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row= LayoutInflater.from(c).inflate(R.layout.row_member,parent,false);
        return new ChatMembersAdapter.MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.name.setText(ls.get(position).getName());
        holder.title.setText(ls.get(position).getTitle());
        String s = ls.get(position).getStatus();
        if(s=="online"){
            holder.status.setImageResource(R.drawable.circle);
        }
        else{
            holder.status.setImageResource(R.drawable.circle_black);
        }
if(!(ls.get(position).getImgurl().isEmpty()))
        Picasso.get().load(ls.get(position).getImgurl()).into(holder.img);
else
    holder.img.setImageResource(R.mipmap.ic_launcher);

holder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent i = new Intent(c,MemberProfile.class);
        i.putExtra("uid",ls.get(position).getUid());
        c.startActivity(i);
    }
});


    }


    @Override
    public int getItemCount() {
        return ls.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView img;
        TextView name;
        TextView title;
        ImageView status;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img= itemView.findViewById(R.id.profile_image);
            status = itemView.findViewById(R.id.status);
            name = itemView.findViewById(R.id.name);
            title  = itemView.findViewById(R.id.title);


        }
    }


}
