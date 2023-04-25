package com.shtrudell.poster.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.text.Spannable;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.shtrudell.poster.MainActivity;
import com.shtrudell.poster.Post;
import com.shtrudell.poster.R;
import com.shtrudell.poster.WebActivity;
import com.shtrudell.poster.databinding.FragmentFullSizePostBinding;

import java.util.Objects;

public class FullSizePostFragment extends Fragment {
    private static final String POST_PARAM = "post";

    private FragmentFullSizePostBinding binding;
    private Post post;

    public FullSizePostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FullSizePostFragment.
     */
    public static FullSizePostFragment newInstance(Post post) {
        FullSizePostFragment fragment = new FullSizePostFragment();
        Bundle args = new Bundle();
        args.putParcelable(POST_PARAM, post);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            post = getArguments().getParcelable(POST_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFullSizePostBinding.inflate(inflater, container, false);

        initPost();

        Fragment dis = this;

        Linkify.addLinks(binding.textTextView, Linkify.WEB_URLS);
        try {
            Spannable spannable = (Spannable) binding.textTextView.getText();

            URLSpan[] urlSpans = spannable.getSpans(0, spannable.length(), URLSpan.class);
            for (final URLSpan urlSpan : urlSpans) {
                ClickableSpan clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View view) {
                        String url = urlSpan.getURL();

                        Intent intent = new Intent(dis.getContext(), WebActivity.class);
                        intent.putExtra("URL", url);
                        startActivity(intent);
                    }
                };
                int start = spannable.getSpanStart(urlSpan);
                int end = spannable.getSpanEnd(urlSpan);
                spannable.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannable.removeSpan(urlSpan);
            }
            binding.textTextView.setMovementMethod(LinkMovementMethod.getInstance());

        } catch (Exception ignore) {
        }

        return binding.getRoot();
    }

    private void initPost() {
        if(post == null) return;

        if(post.getHeader() == null || post.getHeader().isEmpty()) {
            binding.headerTextView.setVisibility(View.GONE);
        }
        else {
            binding.headerTextView.setVisibility(View.VISIBLE);
            binding.headerTextView.setText(post.getHeader());
        }

        if(post.getText() == null || post.getText().isEmpty()) {
            binding.textTextView.setVisibility(View.GONE);
        }
        else {
            binding.textTextView.setVisibility(View.VISIBLE);
            binding.textTextView.setText(post.getText());
        }

        if(post.getImage() == null) {
            binding.imageImageView.setVisibility(View.GONE);
        }
        else {
            binding.imageImageView.setVisibility(View.VISIBLE);
            binding.imageImageView.setImageURI(post.getImage());
            binding.imageImageView.setOnClickListener(v -> {
                MainActivity.openImage(post.getImage(), Objects.requireNonNull(getActivity()));
            });
        }

        if(post.getSong() == null) {
            binding.compactPlayer.setVisibility(View.GONE);
        }
        else {
            binding.compactPlayer.setVisibility(View.VISIBLE);
            binding.compactPlayer.setSource(post.getSong());
        }
    }
}