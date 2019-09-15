package com.example.backingapp.Fragments;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.backingapp.R;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import static android.support.constraint.Constraints.TAG;

/**
 *
 * Fragment that play Step Video
 */
public class VideoFragment extends Fragment implements Player.EventListener {
    // the fragment initialization parameters
    private static final String ARG_PARAM1 = "param1";
    private static final String PLAYER_IS_READY_KEY = "Ready to play";
    private static final String PLAYER_CURRENT_POS_KEY = "Curent Position";
    private static final String CURRENT_WIN_KEY = "Current Window";
    private String mVideoLink;
    private PlayerView mPlayerView;
    private ExoPlayer mExoPlayer;
    private View rootView;
    private boolean playWhenReady;
    private int currentWindow;
    private long playbackPosition;
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;

    public VideoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setRetainInstance(true);

        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_video, container, false);

        mPlayerView = (PlayerView) rootView.findViewById(R.id.video_exo_player);

        if (savedInstanceState != null){
            mVideoLink = savedInstanceState.getString(ARG_PARAM1);
            playbackPosition = savedInstanceState.getLong(PLAYER_CURRENT_POS_KEY);
            playWhenReady = savedInstanceState.getBoolean(PLAYER_IS_READY_KEY);
            currentWindow = savedInstanceState.getInt(CURRENT_WIN_KEY);

        }

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
            mPlayerView.hideController();
            mExoPlayer.prepare(mediaSource,false,false);

        }
    }
    public void initMediaSession (){
        mExoPlayer.addListener(this);

        mMediaSession = new MediaSessionCompat(getContext(), TAG);
        mMediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS);
        mMediaSession.setMediaButtonReceiver(null);

        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY | PlaybackStateCompat.ACTION_PAUSE | PlaybackStateCompat.ACTION_PLAY_PAUSE
                );
        mMediaSession.setPlaybackState(mStateBuilder.build());
        mMediaSession.setCallback(new MySessionCallback());
        mMediaSession.setActive(true);
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
        super.onSaveInstanceState(outState);
        outState.putString(ARG_PARAM1, mVideoLink);
        outState.putLong(PLAYER_CURRENT_POS_KEY, playbackPosition);
        outState.putBoolean(PLAYER_IS_READY_KEY, playWhenReady);
        outState.putInt(CURRENT_WIN_KEY, currentWindow);
    }

    @Override
    public void onPause() {
        super.onPause();

        if (Util.SDK_INT <= 23 && mExoPlayer != null) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        super.onStop();
        if (Util.SDK_INT > 23 && mExoPlayer != null) {
            releasePlayer();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if (Util.SDK_INT > 23) {
            initializePlayer();
            initMediaSession();
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        if ((Util.SDK_INT <= 23 || mExoPlayer == null)) {
            initializePlayer();
            initMediaSession();
        }

    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            playbackPosition =  mExoPlayer.getCurrentPosition();
            currentWindow = mExoPlayer.getCurrentWindowIndex();
            playWhenReady = mExoPlayer.getPlayWhenReady();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }


    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == Player.STATE_READY) && playWhenReady) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING, playbackPosition, 1f);
        } else if ((playbackState == Player.STATE_READY)){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED, playbackPosition, 1f);

        }

        mMediaSession.setPlaybackState(mStateBuilder.build());
        Log.d("HOVNOOOO", "Playback State Changed");
    }

    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }
    }
}
