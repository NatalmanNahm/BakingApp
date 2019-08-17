package com.example.backingapp.Fragments;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.backingapp.R;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

/**
 *
 * Fragment that play Step Video
 */
public class VideoFragment extends Fragment {
    // the fragment initialization parameters
    private static final String ARG_PARAM1 = "param1";
    private String mVideoLink;
    private PlayerView mPlayerView;
    private ExoPlayer mExoPlayer;
    private View rootView;
    private boolean playWhenReady;
    private int currentWindow = 0;
    private int playbackPosition = 0;

    public VideoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null){
            mVideoLink = savedInstanceState.getString(ARG_PARAM1);

        }

        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_video, container, false);

        mPlayerView = (PlayerView) rootView.findViewById(R.id.video_exo_player);


        return rootView;
    }

    private void initializePlayer(){
        if (mExoPlayer == null){
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            mExoPlayer.setPlayWhenReady(playWhenReady);
            mExoPlayer.seekTo(currentWindow, playbackPosition);

            Uri uri = Uri.parse(mVideoLink);
            MediaSource mediaSource = buildMediaSource(uri);
            mExoPlayer.prepare(mediaSource,true,false);

        }
    }

    private MediaSource buildMediaSource(Uri uri) {

        String userAgent = Util.getUserAgent(getContext(), "BakingApp");

        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory(userAgent)).
                createMediaSource(uri);
    }

    public void setmVideoLink(String mVideoLink) {
        this.mVideoLink = mVideoLink;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

        outState.putString(ARG_PARAM1, mVideoLink);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
        if ((Util.SDK_INT <= 23 || mExoPlayer == null)) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        mPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            playbackPosition = (int) mExoPlayer.getCurrentPosition();
            currentWindow = mExoPlayer.getCurrentWindowIndex();
            playWhenReady = mExoPlayer.getPlayWhenReady();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }
}
