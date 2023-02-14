package com.smd.wssl_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.w3c.dom.Text;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MyViewHolder> {
    List<ChatModel> ls;
    Context c;
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    FirebaseAuth mauth;
    private  String image_url;



    @Override
    public int getItemViewType(int position) {
        mauth = FirebaseAuth.getInstance();
        if(ls.get(position).getSender().equals(mauth.getUid())){
            return MSG_TYPE_RIGHT;
        }
        else {
            return MSG_TYPE_LEFT;
        }
    }

    public MessagesAdapter(List<ChatModel> ls, Context c) {
        this.ls = ls;
        this.c = c;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      if(viewType==MSG_TYPE_RIGHT){
        View row= LayoutInflater.from(c).inflate(R.layout.row_message_right,parent,false);
        return new MessagesAdapter.MyViewHolder(row);
    }
      else {
          View row= LayoutInflater.from(c).inflate(R.layout.row_message_left,parent,false);
          return new MessagesAdapter.MyViewHolder(row);
      }


    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
ChatModel chatModel = ls.get(position);
holder.textmsg.setText(chatModel.getMessage());
if(ls.get(position).getImg_url().equals("")){
    Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/w-app-46ce9.appspot.com/o/images%2Fimage%3A256102?alt=media&token=0e9cff61-998e-466f-a6a8-318be59e1bc3").into(holder.img);

}
else{
    Picasso.get().load(ls.get(position).getImg_url()).into(holder.img);

}


    }



    @Override
    public int getItemCount() {
        return ls.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView img;
        TextView textmsg;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
           img = itemView.findViewById(R.id.img);
           textmsg = itemView.findViewById(R.id.textmsg);


        }
    }
}
