package com.shtrudell.poster.view;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.shtrudell.poster.databinding.CompactPlayerBinding;

import java.io.IOException;

public class CompactMusicPlayer extends ConstraintLayout {
    private CompactPlayerBinding binding;
    private MediaPlayer mediaPlayer;
    private Uri source;
    private Context context;
    private OnMediaPlayerUpdatedListener listener;

    public CompactMusicPlayer(Context context) {
        super(context);
        init(context);
        this.context = context;
    }

    public CompactMusicPlayer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
        this.context = context;
    }

    private void init(Context context) {
        binding = CompactPlayerBinding.inflate(LayoutInflater.from(context), this, false);
        addView(binding.getRoot());

        binding.playButton.setOnClickListener(v -> {
            if(isPlaying())
                stop();
            else
                start();

            updateButtonState();
        });
    }

    private boolean createMusicPlayer(Context context, Uri source) {
        if(source == null) return false;

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(new AudioAttributes.Builder().
                setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).
                setUsage(AudioAttributes.USAGE_MEDIA).
                build());
        try {
            mediaPlayer.setDataSource(context, source);
            if(listener != null)
                listener.onMediaPlayerUpdated();
        } catch (IOException e) {
            Log.e("Error",e.getMessage());
        }
        return true;
    }

    public void start() {
        if(mediaPlayer == null)
            if(!createMusicPlayer(context, source)) return;

        mediaPlayer.prepareAsync();
        mediaPlayer.setOnPreparedListener(v -> {
            mediaPlayer.start();
            updateButtonState();
        });
    }

    public void stop() {
        if(mediaPlayer != null) {
            mediaPlayer.stop();
            updateButtonState();
        }
    }

    public void updateButtonState() {
        binding.playButton.setImageState(new int[] {isPlaying()? android.R.attr.state_activated : -android.R.attr.state_activated}, false);
    }

    public void updateSongDetails() {
        if(source == null) return;

        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(getContext(), source);

        String artist = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        String title = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);

        binding.songNameTextView.setText(String.format("%s - %s", artist, title));
    }

    public boolean isPlaying() {
        if(mediaPlayer == null) return false;

        return mediaPlayer.isPlaying();
    }

    public Uri getSource() {
        return source;
    }

    public void setSource(Uri source) {
        this.source = source;
        updateSongDetails();
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        if(this.mediaPlayer == mediaPlayer || mediaPlayer == null) return;

        this.mediaPlayer = mediaPlayer;
        listener.onMediaPlayerUpdated();
        updateButtonState();
    }

    public void setOnMediaPlayerUpdatedListener(OnMediaPlayerUpdatedListener listener) {
        this.listener = listener;
    }

    public interface OnMediaPlayerUpdatedListener {
        void onMediaPlayerUpdated();
    }
}
