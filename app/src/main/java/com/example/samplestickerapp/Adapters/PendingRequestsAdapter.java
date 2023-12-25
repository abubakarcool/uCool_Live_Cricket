package com.example.samplestickerapp.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.samplestickerapp.Models.Friend;
import com.example.samplestickerapp.Models.Team;
import com.example.samplestickerapp.Models.Users;
import com.example.samplestickerapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PendingRequestsAdapter extends RecyclerView.Adapter<PendingRequestsAdapter.ViewHolder> {
    private List<Team> teamList;
    private Context context;

    public PendingRequestsAdapter(List<Team> teamList, Context context) {
        this.teamList = teamList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.table_column, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Team team = teamList.get(position);

        ImageView imageView = holder.itemView.findViewById(R.id.profile_image);
        TextView countryNameTextView = holder.itemView.findViewById(R.id.countryName);
        TextView matchesPlayedTextView = holder.itemView.findViewById(R.id.matchesPlayed);
        TextView matchesWinTextView = holder.itemView.findViewById(R.id.matchesWin);
        TextView matchesLoseTextView = holder.itemView.findViewById(R.id.matchesLose);
        TextView pointsTextView = holder.itemView.findViewById(R.id.points);

        String teamName = team.getTeamName();
        if("AFG".equals(teamName)){
            Picasso.get().load(R.drawable.afghanistan).into(imageView);
        } else if("AUS".equals(teamName)){
            Picasso.get().load(R.drawable.australia).into(imageView);
        } else if("BAN".equals(teamName)){
            Picasso.get().load(R.drawable.bangladesh).into(imageView);
        } else if("ENG".equals(teamName)){
            Picasso.get().load(R.drawable.england).into(imageView);
        } else if("IND".equals(teamName)){
            Picasso.get().load(R.drawable.india).into(imageView);
        } else if("NED".equals(teamName)){
            Picasso.get().load(R.drawable.netherlands).into(imageView);
        } else if("NZ".equals(teamName)){
            Picasso.get().load(R.drawable.newzealand).into(imageView);
        } else if("PAK".equals(teamName)){
            Picasso.get().load(R.drawable.pakistan).into(imageView);
        } else if("SA".equals(teamName)){
            Picasso.get().load(R.drawable.southafrica).into(imageView);
        } else if("SL".equals(teamName)){
            Picasso.get().load(R.drawable.srilanka).into(imageView);
        } else{
            Picasso.get().load(R.drawable.square).into(imageView);
        }
        countryNameTextView.setText(team.getTeamName());
        matchesPlayedTextView.setText(team.getPlayed());
        matchesWinTextView.setText(team.getWon());
        matchesLoseTextView.setText(team.getLost());
        pointsTextView.setText(team.getPoints());

        // Print the data
        Log.d("TeamData", "Team Name: " + team.getTeamName());
        Log.d("TeamData", "Matches Played: " + team.getPlayed());
        Log.d("TeamData", "Matches Won: " + team.getWon());
        Log.d("TeamData", "Matches Lost: " + team.getLost());
        Log.d("TeamData", "Points: " + team.getPoints());

    }

    @Override
    public int getItemCount() {
        return teamList.size();
    }

    public void updateData(List<Team> newTeamList) {
        // Update the data and refresh the adapter
        teamList.clear();
        teamList.addAll(newTeamList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView profileImageView;
        public TextView countryNameTextView;
        public TextView matchesPlayedTextView;
        public TextView matchesWinTextView;
        public TextView matchesLoseTextView;
        public TextView pointsTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImageView = itemView.findViewById(R.id.profile_image);
            countryNameTextView = itemView.findViewById(R.id.countryName);
            matchesPlayedTextView = itemView.findViewById(R.id.matchesPlayed);
            matchesWinTextView = itemView.findViewById(R.id.matchesWin);
            matchesLoseTextView = itemView.findViewById(R.id.matchesLose);
            pointsTextView = itemView.findViewById(R.id.points);
        }
    }
}
