package com.example.samplestickerapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.samplestickerapp.ChatDetailActivity;
import com.example.samplestickerapp.Models.Friend;
import com.example.samplestickerapp.Models.MatchInfo;
import com.example.samplestickerapp.Models.Team;
import com.example.samplestickerapp.Models.Users;
import com.example.samplestickerapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.ViewHolder> {
    private List<MatchInfo> matchInfoList;
    private Context context;

    public FriendListAdapter(List<MatchInfo> matchInfoList, Context context) {
        this.matchInfoList = matchInfoList; // Initialize the MatchInfo list
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_friend_user,parent,false);
        return new FriendListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MatchInfo matchInfo = matchInfoList.get(position);
        ImageView firstTeamIcon = holder.itemView.findViewById(R.id.firstTeamIcon);
        ImageView secondTeamIcon = holder.itemView.findViewById(R.id.secondTeamIcon);
        TextView teamNamesVsInfo = holder.itemView.findViewById(R.id.teamNamesVsInfo);
        TextView venueTimeOrResultInfo = holder.itemView.findViewById(R.id.venueTimeOrResultInfo);

        try {
            String input = matchInfo.getMatch();
            Log.d("MY_TAG", "FriendListAdapter : input string is as : " + input);
            String[] parts = input.split("vs");
            String team1 = parts[0].trim();
            String team2 = parts[1].trim();

            if ("AFGHANISTAN".equals(team1)) {
                Picasso.get().load(R.drawable.afghanistan).into(firstTeamIcon);
            } else if ("AUSTRALIA".equals(team1)) {
                Picasso.get().load(R.drawable.australia).into(firstTeamIcon);
            } else if ("BANGLADESH".equals(team1)) {
                Picasso.get().load(R.drawable.bangladesh).into(firstTeamIcon);
            } else if ("ENGLAND".equals(team1)) {
                Picasso.get().load(R.drawable.england).into(firstTeamIcon);
            } else if ("INDIA".equals(team1)) {
                Picasso.get().load(R.drawable.india).into(firstTeamIcon);
            } else if ("NETHERLANDS".equals(team1)) {
                Picasso.get().load(R.drawable.netherlands).into(firstTeamIcon);
            } else if ("NEW ZEALAND".equals(team1)) {
                Picasso.get().load(R.drawable.newzealand).into(firstTeamIcon);
            } else if ("PAKISTAN".equals(team1)) {
                Picasso.get().load(R.drawable.pakistan).into(firstTeamIcon);
            } else if ("SOUTH AFRICA".equals(team1)) {
                Picasso.get().load(R.drawable.southafrica).into(firstTeamIcon);
            } else if ("SRI LANKA".equals(team1)) {
                Picasso.get().load(R.drawable.srilanka).into(firstTeamIcon);
            } else {
                Picasso.get().load(R.drawable.square).into(firstTeamIcon);
            }

            if ("AFGHANISTAN".equals(team2)) {
                Picasso.get().load(R.drawable.afghanistan).into(secondTeamIcon);
            } else if ("AUSTRALIA".equals(team2)) {
                Picasso.get().load(R.drawable.australia).into(secondTeamIcon);
            } else if ("BANGLADESH".equals(team2)) {
                Picasso.get().load(R.drawable.bangladesh).into(secondTeamIcon);
            } else if ("ENGLAND".equals(team2)) {
                Picasso.get().load(R.drawable.england).into(secondTeamIcon);
            } else if ("INDIA".equals(team2)) {
                Picasso.get().load(R.drawable.india).into(secondTeamIcon);
            } else if ("NETHERLANDS".equals(team2)) {
                Picasso.get().load(R.drawable.netherlands).into(secondTeamIcon);
            } else if ("NEW ZEALAND".equals(team2)) {
                Picasso.get().load(R.drawable.newzealand).into(secondTeamIcon);
            } else if ("PAKISTAN".equals(team2)) {
                Picasso.get().load(R.drawable.pakistan).into(secondTeamIcon);
            } else if ("SOUTH AFRICA".equals(team2)) {
                Picasso.get().load(R.drawable.southafrica).into(secondTeamIcon);
            } else if ("SRI LANKA".equals(team2)) {
                Picasso.get().load(R.drawable.srilanka).into(secondTeamIcon);
            } else {
                Picasso.get().load(R.drawable.square).into(secondTeamIcon);
            }
        } catch (Exception e){
            Picasso.get().load(R.drawable.square).into(firstTeamIcon);
            Picasso.get().load(R.drawable.square).into(secondTeamIcon);
        }

        teamNamesVsInfo.setText(matchInfo.getMatch() + " " + matchInfo.getVenue());
        venueTimeOrResultInfo.setText(matchInfo.getTime() + " " + matchInfo.getDate());
    }

    @Override
    public int getItemCount() {
        return matchInfoList.size();
    }

    public void updateData(List<MatchInfo> newMatchInfoList) {
        // Update the data and refresh the adapter
        matchInfoList.clear();
        matchInfoList.addAll(newMatchInfoList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView firstTeamIcon;
        public ImageView secondTeamIcon;
        public TextView teamNamesVsInfo;
        public TextView venueTimeOrResultInfo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            firstTeamIcon = itemView.findViewById(R.id.firstTeamIcon);
            secondTeamIcon = itemView.findViewById(R.id.secondTeamIcon);
            teamNamesVsInfo = itemView.findViewById(R.id.teamNamesVsInfo);
            venueTimeOrResultInfo = itemView.findViewById(R.id.venueTimeOrResultInfo);
        }
    }
}
