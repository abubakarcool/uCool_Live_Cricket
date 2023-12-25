package com.example.samplestickerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;


public class ChatDetailActivity extends AppCompatActivity {
    PlayerView playerView;
    ProgressBar progressBar;
    ImageView btFullScreen;
    TextView channelName;
    ImageView ffBackArrow;
    View lineAbovePlayer;
    View lineBelowPlayer;
    SimpleExoPlayer simpleExoPlayer;
    boolean flag = false; // for fullscreen
    private boolean isControlsVisible = true; // for player controls
    public String name;
    public String num;
    public String logo;
    private int videoWidth;
    private int videoHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); // to hide the top actionbar
        setContentView(R.layout.activity_chat_detail);

        playerView = findViewById(R.id.player_view);
        progressBar = findViewById(R.id.progress_bar);
        btFullScreen = playerView.findViewById(R.id.bt_fullcreen);
        ffBackArrow = findViewById(R.id.back_ofchatdetail);
        channelName = findViewById(R.id.channelName);
        lineAbovePlayer = findViewById(R.id.lineAbovePlayer);
        lineBelowPlayer = findViewById(R.id.lineBelowPlayer);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) { // if activity is initially in landscape mode
            hideSystemUI();
            hideControls();
            flag = false;
        }

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        logo = intent.getStringExtra("logo");
        num = intent.getStringExtra("num");

        channelName.setText(name);
        String username = MyHelper.retrieveData(this, "username");
        String password = MyHelper.retrieveData(this, "password");
        num = getString(R.string.SERVER_IP)+":"+getString(R.string.SERVER_PORT)+"/"+username+"/"+password+"/"+num;

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Uri videoUrl = Uri.parse(num);
        LoadControl loadControl = new DefaultLoadControl();
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelector trackSelector = new DefaultTrackSelector(
                new AdaptiveTrackSelection.Factory(bandwidthMeter)
        );
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(
                ChatDetailActivity.this,trackSelector,loadControl
        );
        DefaultHttpDataSourceFactory factory = new DefaultHttpDataSourceFactory(
                "exo_player_video"
        );
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        MediaSource mediaSource = new ExtractorMediaSource(videoUrl,
                factory,extractorsFactory,null,null);
        playerView.setPlayer(simpleExoPlayer);
        playerView.setKeepScreenOn(true);
        simpleExoPlayer.prepare(mediaSource);
        simpleExoPlayer.setPlayWhenReady(true);
        simpleExoPlayer.addListener(new Player.EventListener(){

            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if(playbackState == Player.STATE_BUFFERING){
                    progressBar.setVisibility(View.VISIBLE);
                } else if(playbackState == Player.STATE_READY){
                    progressBar.setVisibility(View.GONE);
                    videoWidth = simpleExoPlayer.getVideoFormat().width;
                    videoHeight = simpleExoPlayer.getVideoFormat().height;
                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                        adjustPlayerViewHeight(videoWidth, videoHeight);
                    }
                }
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                Log.e("ExoPlayerError", "****Error**** during playback: " + error.getMessage(), error);
            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }
        });

        ffBackArrow.setOnClickListener(new View.OnClickListener() { // back button pressed
            @Override
            public void onClick(View view) {
                simpleExoPlayer.stop();
                simpleExoPlayer.release();
                Intent intent = new Intent(ChatDetailActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btFullScreen.setOnClickListener(new View.OnClickListener() { // TOGGLING SCREEN MODE
            @Override
            public void onClick(View view) {
                if (flag) {// Enter fullscreen mode
                    btFullScreen.setImageDrawable(getResources().getDrawable(R.drawable.ic_fullscreen_exit));
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    hideSystemUI(); // hide bottom navigation bar
                    hideControls(); // hide back button and player controls
                    flag = false;
                } else {// Exit fullscreen mode
                    btFullScreen.setImageDrawable(getResources().getDrawable(R.drawable.ic_fullscreen));
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    showSystemUI(); // show bottom navigation bar
                    showControls();
                    flag = true;
                }
            }
        });

        playerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!flag) { // if we are in landscape mode by clicking on the screen toggle the controls
                    toggleControls();
                }
            }
        });
    }


    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        );
    }

    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
    }

    @Override
    protected void onPause() { // play pause
        super.onPause();
        simpleExoPlayer.setPlayWhenReady(false);
        simpleExoPlayer.getPlaybackState();
    }

    @Override
    protected void onRestart() { // pause play
        super.onRestart();
        simpleExoPlayer.setPlayWhenReady(true);
        simpleExoPlayer.getPlaybackState();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        simpleExoPlayer.release();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        simpleExoPlayer.stop();
        simpleExoPlayer.release();
        finish();
    }
    private void hideControls() {// Add code to hide other controls if needed
        ffBackArrow.setVisibility(View.GONE);
        channelName.setVisibility(View.GONE);
        lineBelowPlayer.setVisibility(View.GONE);
        lineAbovePlayer.setVisibility(View.GONE);
    }

    private void showControls() {// Add code to show other controls if needed
        ffBackArrow.setVisibility(View.VISIBLE);
        channelName.setVisibility(View.VISIBLE);
        lineBelowPlayer.setVisibility(View.VISIBLE);
        lineAbovePlayer.setVisibility(View.VISIBLE);
    }

    private void toggleControls() {
        if (isControlsVisible) {
            hideControls();
        } else {
            showControls();
        }
        isControlsVisible = !isControlsVisible;
    }
    private void adjustPlayerViewHeight(int videoWidth, int videoHeight) {
        if (videoWidth > 0 && videoHeight > 0) {
            int screenWidth = getResources().getDisplayMetrics().widthPixels;
            int newHeight = (int) (screenWidth / (float) videoWidth * videoHeight);

            ViewGroup.LayoutParams params = playerView.getLayoutParams();
            params.height = newHeight;
            playerView.setLayoutParams(params);
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) { // Adjust playerView height here when switching to portrait
            adjustPlayerViewHeight(videoWidth, videoHeight);
            showSystemUI(); // show bottom navigation bar
            showControls();
            flag = true;
        } else{ // Adjust playerView height here when switching to landscape
            setPlayerViewDimensionsToFullscreen();
            hideSystemUI(); // hide bottom navigation bar
            hideControls(); // hide back button and player controls
            flag = false;
        }
    }
    private void setPlayerViewDimensionsToFullscreen() {
        // Get the screen width and height
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;

        // Set the PlayerView dimensions to match the screen width and height
        playerView.getLayoutParams().width = screenWidth;
        playerView.getLayoutParams().height = screenHeight;
        playerView.requestLayout(); // Request a layout update
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);
    }
}