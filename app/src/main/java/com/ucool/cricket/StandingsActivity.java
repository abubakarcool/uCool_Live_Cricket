package com.example.samplestickerapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.List;

public class StandingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standings);

        // Perform web scraping in an AsyncTask
        new ScrapeDataTask().execute();
    }

    private class ScrapeDataTask extends AsyncTask<Void, Void, List<String[]>> {

        @Override
        protected List<String[]> doInBackground(Void... voids) {
            try {
                return StandingsScraper.scrapeStandings();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<String[]> data) {
            super.onPostExecute(data);

            // Log the data in the logcat
            if (data != null) {
                for (String[] row : data) {
                    StringBuilder rowData = new StringBuilder();
                    for (String column : row) {
                        rowData.append(column).append(" | ");
                    }
                    Log.d("ScrapedData", rowData.toString());
                }
            } else {
                Log.e("Scraping", "Error while scraping data");
            }

            // Create a TableLayout to display the data
            TableLayout tableLayout = new TableLayout(StandingsActivity.this);
            tableLayout.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

            // Create table rows and add them to the table
            for (String[] row : data) {
                TableRow tableRow = new TableRow(StandingsActivity.this);
                for (String column : row) {
                    TextView textView = new TextView(StandingsActivity.this);
                    textView.setText(column);
                    textView.setPadding(8, 8, 8, 8);
                    tableRow.addView(textView);
                }
                tableLayout.addView(tableRow);
            }

            // Display the table in a ScrollView
            ScrollView scrollView = new ScrollView(StandingsActivity.this);
            scrollView.addView(tableLayout);

            // Add the ScrollView to your layout
            LinearLayout container = findViewById(R.id.container);
            container.addView(scrollView);
        }
    }
}
