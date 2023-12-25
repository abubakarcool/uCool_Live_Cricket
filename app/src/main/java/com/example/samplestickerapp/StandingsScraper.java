package com.example.samplestickerapp;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StandingsScraper {

    public static List<String[]> scrapeStandings() throws IOException {
        List<String[]> standingsData = new ArrayList<>();

        // Replace this URL with the actual URL of the website you want to scrape
        String url = "https://www.cricketworldcup.com/standings"; // Example URL

        // Send an HTTP GET request to the website and parse the HTML
        Document document = Jsoup.connect(url).get();

        // Extract the standings table
        Element standingsTable = document.selectFirst("table.table");

        // Extract and format the table's text for the specified columns
        Elements rows = standingsTable.select("tr");
        for (Element row : rows) {
            Elements cells = row.select("td");
            if (cells.size() >= 5) {
                // Extract only the desired columns (Team, Played, Won, Lost, Points)
                String teamText = cells.get(1).text();
                String[] teamTokens = teamText.split("\\s+");
                String teamName = teamTokens[teamTokens.length - 1]; // Get the last token

                String[] rowData = {
                        teamName,            // Team
                        cells.get(2).text(),  // Played
                        cells.get(3).text(),  // Won
                        cells.get(4).text(),  // Lost
                        cells.get(8).text()   // Points
                };
                standingsData.add(rowData);
            }
        }

        return standingsData;
    }
}

