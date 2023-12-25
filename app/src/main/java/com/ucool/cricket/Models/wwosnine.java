package com.example.samplestickerapp.Models;

public class wwosnine {
    private String tournamentName;
    private String tournamentId;
    private String matchId;
    private String matchState2;
    private String matchState1;
    private String eventName;
    private String venue;
    private String date;
    private String matchFormat;
    private String team1Name;
    private String team1Score;
    private String team2Name;
    private String team2Score;

    // Constructors
    public wwosnine() {
        // Default constructor
    }

    public wwosnine(String tournamentName, String tournamentId, String matchId,
                    String matchState2, String matchState1, String eventName,
                    String venue, String date, String matchFormat,
                    String team1Name, String team1Score, String team2Name,
                    String team2Score) {
        this.tournamentName = tournamentName;
        this.tournamentId = tournamentId;
        this.matchId = matchId;
        this.matchState2 = matchState2;
        this.matchState1 = matchState1;
        this.eventName = eventName;
        this.venue = venue;
        this.date = date;
        this.matchFormat = matchFormat;
        this.team1Name = team1Name;
        this.team1Score = team1Score;
        this.team2Name = team2Name;
        this.team2Score = team2Score;
    }

    // Getters and setters for each variable
    public String getTournamentName() {
        return tournamentName;
    }

    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }

    public String getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(String tournamentId) {
        this.tournamentId = tournamentId;
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public String getMatchState2() {
        return matchState2;
    }

    public void setMatchState2(String matchState2) {
        this.matchState2 = matchState2;
    }

    public String getMatchState1() {
        return matchState1;
    }

    public void setMatchState1(String matchState1) {
        this.matchState1 = matchState1;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMatchFormat() {
        return matchFormat;
    }

    public void setMatchFormat(String matchFormat) {
        this.matchFormat = matchFormat;
    }

    public String getTeam1Name() {
        return team1Name;
    }

    public void setTeam1Name(String team1Name) {
        this.team1Name = team1Name;
    }

    public String getTeam1Score() {
        return team1Score;
    }

    public void setTeam1Score(String team1Score) {
        this.team1Score = team1Score;
    }

    public String getTeam2Name() {
        return team2Name;
    }

    public void setTeam2Name(String team2Name) {
        this.team2Name = team2Name;
    }

    public String getTeam2Score() {
        return team2Score;
    }

    public void setTeam2Score(String team2Score) {
        this.team2Score = team2Score;
    }

    // Override toString() method to display the data
    @Override
    public String toString() {
        return "wwosnine{" +
                "tournamentName='" + tournamentName + '\'' +
                ", tournamentId='" + tournamentId + '\'' +
                ", matchId='" + matchId + '\'' +
                ", matchState2='" + matchState2 + '\'' +
                ", matchState1='" + matchState1 + '\'' +
                ", eventName='" + eventName + '\'' +
                ", venue='" + venue + '\'' +
                ", date='" + date + '\'' +
                ", matchFormat='" + matchFormat + '\'' +
                ", team1Name='" + team1Name + '\'' +
                ", team1Score='" + team1Score + '\'' +
                ", team2Name='" + team2Name + '\'' +
                ", team2Score='" + team2Score + '\'' +
                '}';
    }

}

