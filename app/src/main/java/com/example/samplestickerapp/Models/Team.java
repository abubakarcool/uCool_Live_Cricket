package com.example.samplestickerapp.Models;

public class Team {
    private String teamName;
    private String played;
    private String won;
    private String lost;
    private String points;

    public Team(String teamName, String played, String won, String lost, String points) {
        this.teamName = teamName;
        this.played = played;
        this.won = won;
        this.lost = lost;
        this.points = points;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getPlayed() {
        return played;
    }

    public String getWon() {
        return won;
    }

    public String getLost() {
        return lost;
    }

    public String getPoints() {
        return points;
    }
}

