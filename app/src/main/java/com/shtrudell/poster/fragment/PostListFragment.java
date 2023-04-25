package com.shtrudell.poster.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shtrudell.poster.MainActivity;
import com.shtrudell.poster.Post;
import com.shtrudell.poster.adapter.PostAdapter;
import com.shtrudell.poster.R;
import com.shtrudell.poster.databinding.FragmentPostListBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PostListFragment extends Fragment {

    private FragmentPostListBinding binding;
    private List<Post> posts;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPostListBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(posts == null)
            posts = initSampleData();

        PostAdapter postAdapter = new PostAdapter(getContext(), posts);
        postAdapter.setOnPostClickListener(post -> {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, FullSizePostFragment.newInstance(post), null)
                    .addToBackStack(null)
                    .commit();
        });

        postAdapter.setOnImageClickListener(source -> {
            MainActivity.openImage(source, Objects.requireNonNull(getActivity()));
        });

        RecyclerView rv = view.findViewById(R.id.post_list);
        rv.setAdapter(postAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rv.getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.post_list_divider));
        rv.addItemDecoration(dividerItemDecoration);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(linearLayoutManager);

        binding.addPostButton.setOnClickListener(v -> {
            PostWriterFragment postWriterFragment = PostWriterFragment.newInstance();
            postWriterFragment.setOnSendPostListener(post -> {
                posts.add(0, post);
                postAdapter.notifyDataSetChanged();
            });

            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, postWriterFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    private List<Post> initSampleData() {
        List<Post> posts = new ArrayList<>();
        posts.add(new Post(getResources().getString(R.string.header_placeholder), getResources().getString(R.string.text_placeholder), null, Uri.parse("android.resource://com.shtrudell.poster/" + R.raw.detroit_become_human_connor)));
        posts.add(new Post(getResources().getString(R.string.header_placeholder), getResources().getString(R.string.text_placeholder), Uri.parse("android.resource://com.shtrudell.poster/" + R.raw.nick_gindraux_landscape3)));
        posts.add(new Post(getResources().getString(R.string.header_placeholder), getResources().getString(R.string.text_placeholder)));
        posts.add(new Post(getResources().getString(R.string.header_placeholder), getResources().getString(R.string.text_placeholder)));
        posts.add(new Post(getResources().getString(R.string.header_placeholder), getResources().getString(R.string.text_placeholder)));
        posts.add(new Post(getResources().getString(R.string.header_placeholder), getResources().getString(R.string.text_placeholder)));
        posts.add(new Post(getResources().getString(R.string.header_placeholder), getResources().getString(R.string.text_placeholder)));
        posts.add(new Post(getResources().getString(R.string.header_placeholder), getResources().getString(R.string.text_placeholder)));
        posts.add(new Post(getResources().getString(R.string.header_placeholder), getResources().getString(R.string.text_placeholder)));

        return posts;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}