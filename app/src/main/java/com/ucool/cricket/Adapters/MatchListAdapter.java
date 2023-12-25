package com.example.samplestickerapp.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.samplestickerapp.Models.wwosnine;
import com.example.samplestickerapp.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MatchListAdapter extends RecyclerView.Adapter<MatchListAdapter.ViewHolder> {
    private List<wwosnine> wwosnines;
    private Context context;

    public MatchListAdapter(List<wwosnine> wwosnines, Context context) {
        this.wwosnines = wwosnines;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.match_sample, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        wwosnine wwosnine = wwosnines.get(position);
        ImageView firstTeamIcon = holder.itemView.findViewById(R.id.TeamIcon1);
        ImageView secondTeamIcon = holder.itemView.findViewById(R.id.TeamIcon2);
        TextView teamName1 = holder.itemView.findViewById(R.id.team1_name);
        TextView teamName2 = holder.itemView.findViewById(R.id.team2_name);
        TextView DateAndVenue = holder.itemView.findViewById(R.id.DateAndVenue);
        TextView team1Score = holder.itemView.findViewById(R.id.team1_score);
        TextView team2Score = holder.itemView.findViewById(R.id.team2_score);
        TextView liveOrVs = holder.itemView.findViewById(R.id.liveOrVs);
        TextView timeLeftOrResult = holder.itemView.findViewById(R.id.TimeOrResult);

        // Set your data to the views here
        teamName1.setText(wwosnine.getTeam1Name());
        teamName2.setText(wwosnine.getTeam2Name());
        DateAndVenue.setText(extractDate(wwosnine.getDate()) + ", " + wwosnine.getVenue());
        team1Score.setText(wwosnine.getTeam1Score());
        team2Score.setText(wwosnine.getTeam2Score());
        if((wwosnine.getMatchState1()).equals("U")){
            timeLeftOrResult.setText("Match begins at "+extractTime(wwosnine.getDate()));
        } else{
            timeLeftOrResult.setText(wwosnine.getMatchState2());
        }
        if((wwosnine.getMatchState1()).equals("L")){
            liveOrVs.setText("LIVE");
            liveOrVs.setTextColor(Color.RED);
        } else {
            liveOrVs.setText("VS");
            liveOrVs.setTextColor(Color.WHITE);
        }

        // Load images using Picasso
        Picasso.get().load(getTeamImageResource(wwosnine.getTeam1Name())).into(firstTeamIcon);
        Picasso.get().load(getTeamImageResource(wwosnine.getTeam2Name())).into(secondTeamIcon);
    }

    @Override
    public int getItemCount() {
        return wwosnines.size();
    }

    public void updateData(List<wwosnine> wwosnines) {
        // Update the data and refresh the adapter
        this.wwosnines.clear();
        this.wwosnines.addAll(wwosnines);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView firstTeamIcon;
        public ImageView secondTeamIcon;
        public TextView teamName1;
        public TextView teamName2;
        public TextView DateAndVenue;
        public TextView team1Score;
        public TextView team2Score;
        public TextView liveOrVs;
        public TextView timeLeftOrResult;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            firstTeamIcon = itemView.findViewById(R.id.TeamIcon1);
            secondTeamIcon = itemView.findViewById(R.id.TeamIcon2);
            teamName1 = itemView.findViewById(R.id.team1_name);
            teamName2 = itemView.findViewById(R.id.team2_name);
            DateAndVenue = itemView.findViewById(R.id.DateAndVenue);
            team1Score = itemView.findViewById(R.id.team1_score);
            team2Score = itemView.findViewById(R.id.team2_score);
            liveOrVs = itemView.findViewById(R.id.liveOrVs);
            timeLeftOrResult = itemView.findViewById(R.id.TimeOrResult);
        }
    }

    // Helper method to get the team image resource based on team name
    private int getTeamImageResource(String teamName) {
        // Map team names to their corresponding image resources
        switch (teamName) {
            case "AFG":
                return R.drawable.afghanistan;
            case "AUS":
                return R.drawable.australia;
            case "BAN":
                return R.drawable.bangladesh;
            case "ENG":
                return R.drawable.england;
            case "IND":
                return R.drawable.india;
            case "NED":
                return R.drawable.netherlands;
            case "NZ":
                return R.drawable.newzealand;
            case "PAK":
                return R.drawable.pakistan;
            case "SA":
                return R.drawable.southafrica;
            case "SL":
                return R.drawable.srilanka;
            default:
                return R.drawable.square; // Default image resource
        }
    }
    public static String extractDate(String inputDate) {
        try {
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmXXX", Locale.US);
            SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd MMM", Locale.US);

            Date date = inputDateFormat.parse(inputDate);
            return outputDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return ""; // Return an empty string in case of parsing error
        }
    }
    public static String extractTime(String inputDate) {
        try {
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmXXX", Locale.US);
            SimpleDateFormat outputTimeFormat = new SimpleDateFormat("hh:mm a", Locale.US);

            Date date = inputDateFormat.parse(inputDate);
            return outputTimeFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return ""; // Return an empty string in case of parsing error
        }
    }
}
