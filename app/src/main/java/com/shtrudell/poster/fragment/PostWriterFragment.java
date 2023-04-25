package com.shtrudell.poster.fragment;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.imageview.ShapeableImageView;
import com.shtrudell.poster.MainActivity;
import com.shtrudell.poster.Post;
import com.shtrudell.poster.R;
import com.shtrudell.poster.databinding.FragmentPostWriterBinding;

import java.util.Objects;

public class PostWriterFragment extends Fragment {

    private static final int TAKE_PICTURE_CODE = 24;
    private static final int TAKE_AUDIO_CODE = 25;
    private FragmentPostWriterBinding binding;
    private Post post;
    private OnSendPostListener onSendPostListener;
    public PostWriterFragment() {
        post = new Post(null, null);
    }

    public static PostWriterFragment newInstance() {
        return new PostWriterFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPostWriterBinding.inflate(inflater, container, false);

        binding.imageImageView.setOnClickListener(v -> {
            if(post.getImage() == null) return;

            MainActivity.openImage(post.getImage(), Objects.requireNonNull(getActivity()));
        });

        binding.addImageButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, TAKE_PICTURE_CODE);
        });

        binding.addSongButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, TAKE_AUDIO_CODE);
        });

        binding.sendPostButton.setOnClickListener(v -> {
            onSendPostListener.onSendPost(post);
            getActivity().getSupportFragmentManager().popBackStack();
        });

        binding.postWriterHeaderEditor.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0)
                    post.setHeader(s.toString());
            }
        });

        binding.postWriterTextEditor.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0)
                    post.setText(s.toString());
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        updateView();
    }

    private void updateView() {
        if(post.getImage() == null) {
            binding.imageImageView.setVisibility(View.GONE);
        }
        else {
            binding.imageImageView.setVisibility(View.VISIBLE);
            binding.imageImageView.setImageURI(post.getImage());
        }

        if(post.getSong() == null) {
            binding.compactPlayer.setVisibility(View.GONE);
        }
        else {
            binding.compactPlayer.setVisibility(View.VISIBLE);
            binding.compactPlayer.setSource(post.getSong());
        }

        binding.postWriterHeaderEditor.setText(post.getHeader());
        binding.postWriterTextEditor.setText(post.getText());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case TAKE_PICTURE_CODE:
                if(resultCode == Activity.RESULT_OK) {
                    Uri source = data.getData();

                    post.setImage(source);
                    updateView();
                }
                break;
            case TAKE_AUDIO_CODE:
                if(resultCode == Activity.RESULT_OK) {
                    Uri source = data.getData();

                    post.setSong(source);
                    updateView();
                }
                break;
        }
    }

    public void setOnSendPostListener(OnSendPostListener onSendPostListener) {
        this.onSendPostListener = onSendPostListener;
    }

    public interface OnSendPostListener {
        void onSendPost(Post post);
    }
}