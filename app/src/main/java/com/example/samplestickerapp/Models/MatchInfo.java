package com.example.samplestickerapp.Models;

public class MatchInfo {
    private String match;
    private String date;
    private String time;
    private String venue;

    public MatchInfo(String match, String date, String time, String venue) {
        this.match = match;
        this.date = date;
        this.time = time;
        this.venue = venue;
    }

    public String getMatch() {
        return match;
    }

    public void setMatch(String match) {
        this.match = match;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }
}

