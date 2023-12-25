package com.example.samplestickerapp.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.samplestickerapp.ChatDetailActivity;
import com.example.samplestickerapp.Models.Friend;
import com.example.samplestickerapp.Models.Link;
import com.example.samplestickerapp.Models.Users;
import com.example.samplestickerapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder>{

    ArrayList<Link> list;
    Context context;
    FirebaseDatabase database;

    public UsersAdapter(ArrayList<Link> list, Context context) {
        this.list = list;
        this.context = context;
        database = FirebaseDatabase.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_show_user,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Link linkGotFromChatsFragment =  list.get(position);
        holder.userName.setText(linkGotFromChatsFragment.getName());
        holder.lastMessage.setText("live");
        Picasso.get().load(linkGotFromChatsFragment.getLogo()).into(holder.image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatDetailActivity.class);
                Link selectedItem = list.get(position);
                intent.putExtra("name", selectedItem.getName());
                intent.putExtra("logo", selectedItem.getLogo());
                intent.putExtra("num", selectedItem.getNum());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView userName, lastMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.profile_image);
            userName = itemView.findViewById(R.id.userNamelist);
            lastMessage = itemView.findViewById(R.id.lastMessage);
        }
    }
}
