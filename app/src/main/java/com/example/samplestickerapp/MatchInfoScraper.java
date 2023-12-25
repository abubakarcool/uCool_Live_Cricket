package com.example.samplestickerapp;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MatchInfoScraper {
    public static List<String> scrapeMatchInfo(String url) {
        List<String> scrapedData = new ArrayList<>();

        try {
            Document document = Jsoup.connect(url).get();
            Elements matchElements = document.select("div.match-list > a");

            for (Element matchElement : matchElements) {
                String matchTitle = matchElement.select("span.txt.txt1").text();
                String matchDate = matchElement.select("span.txt.txt2").text();
                String teamA = matchElement.select("div.team-a div.team-name").text();
                String teamB = matchElement.select("div.team-b div.team-name").text();
                String teamAScore = matchElement.select("div.team-a span.si-first-inn").text();
                String teamBScore = matchElement.select("div.team-b span.si-first-inn").text();
                String matchStatus = matchElement.select("div.match-status span.txt").text();

                String matchInfo = "Match Title: " + matchTitle +
                        "\nMatch Date: " + matchDate +
                        "\nTeam A: " + teamA +
                        "\nTeam B: " + teamB +
                        "\nTeam A Score: " + teamAScore +
                        "\nTeam B Score: " + teamBScore +
                        "\nMatch Status: " + matchStatus;

                scrapedData.add(matchInfo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return scrapedData;
    }
}

