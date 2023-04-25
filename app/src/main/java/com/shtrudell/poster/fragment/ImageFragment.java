package com.shtrudell.poster.fragment;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.shtrudell.poster.R;

public class ImageFragment extends Fragment {
    private static final String SOURCE_PARAM = "source";

    private Uri source;

    public ImageFragment() {
        // Required empty public constructor
    }

    public static ImageFragment newInstance(Uri source) {
        ImageFragment fragment = new ImageFragment();
        Bundle args = new Bundle();
        args.putParcelable(SOURCE_PARAM, source);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            source = getArguments().getParcelable(SOURCE_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_image, container, false);

        ImageView imageView = view.findViewById(R.id.full_size_image_imageView);
        imageView.setImageURI(source);

        return view;
    }
}