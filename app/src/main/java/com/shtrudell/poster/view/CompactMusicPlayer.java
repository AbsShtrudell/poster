package com.shtrudell.poster.view;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.AttributeSet;
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

    private void createMusicPlayer(Context context,Uri source) {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(new AudioAttributes.Builder().
                setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).
                setUsage(AudioAttributes.USAGE_MEDIA).
                build());
        try {
            mediaPlayer.setDataSource(context, source);
            listener.onMediaPlayerUpdated();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void start() {
        if(mediaPlayer == null)
            createMusicPlayer(context, source);

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

    public boolean isPlaying() {
        if(mediaPlayer == null) return false;

        return mediaPlayer.isPlaying();
    }

    public Uri getSource() {
        return source;
    }

    public void setSource(Uri source) {
        this.source = source;
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
