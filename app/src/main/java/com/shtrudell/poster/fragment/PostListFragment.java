package com.shtrudell.poster.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shtrudell.poster.MainActivity;
import com.shtrudell.poster.Post;
import com.shtrudell.poster.adapter.PostAdapter;
import com.shtrudell.poster.R;
import com.shtrudell.poster.databinding.FragmentPostListBinding;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PostListFragment extends Fragment {

    private static final String FILE_NAME = "posts.txt" ;
    private FragmentPostListBinding binding;
    private List<Post> posts;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPostListBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Objects.requireNonNull(getActivity()).deleteFile(FILE_NAME);
        if(posts == null) {
            posts = load();

            if(posts == null)
                posts = initSampleData();
        }

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
        dividerItemDecoration.setDrawable(Objects.requireNonNull(AppCompatResources.getDrawable(Objects.requireNonNull(getContext()), R.drawable.post_list_divider)));
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

    @Override
    public void onDestroy() {
        super.onDestroy();

        save();
    }

    private void save(){

        FileOutputStream fos = null;
        try {
            fos = Objects.requireNonNull(getActivity()).openFileOutput(FILE_NAME, Context.MODE_PRIVATE);

            JSONArray postsJson = new JSONArray();

            for(Post post : posts) {
                postsJson.add(post.parseToJSON());
            }

            fos.write(postsJson.toJSONString().getBytes(StandardCharsets.UTF_8));

            Toast.makeText(getContext(), "Файл сохранен", Toast.LENGTH_SHORT).show();
        }
        catch(IOException ex) {
            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        finally{
            try{
                if(fos!=null)
                    fos.close();
            }
            catch(IOException ex){
                Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private List<Post> load(){

        FileInputStream fis = null;
        JSONParser jsonParser = new JSONParser();

        try {
            fis = Objects.requireNonNull(getActivity()).openFileInput(FILE_NAME);

            JSONArray postsJson = (JSONArray)jsonParser.parse(new InputStreamReader(fis));

            List<Post> postsLoaded = new ArrayList<>();

            postsJson.forEach( post -> {
                postsLoaded.add(Post.parseFromJSON((JSONObject)post));
            });

            Toast.makeText(getContext(), "Файл загружен", Toast.LENGTH_SHORT).show();

            return postsLoaded;
        } catch (IOException | ParseException e) {
            Toast.makeText(getContext(), "Не удалось загрузить файл", Toast.LENGTH_SHORT).show();
        } finally{
            try{
                if(fis!=null)
                    fis.close();
            }
            catch(IOException ex){
                Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        return null;
    }

    private List<Post> initSampleData() {
        List<Post> posts = new ArrayList<>();

        posts.add(new Post(getResources().getString(R.string.header_placeholder2), getResources().getString(R.string.text_placeholder), null, Uri.parse("android.resource://com.shtrudell.poster/" + R.raw.detroit_become_human_connor)));
        posts.add(new Post(getResources().getString(R.string.header_placeholder), getResources().getString(R.string.short_text_placeholder), Uri.parse("android.resource://com.shtrudell.poster/" + R.raw.nick_gindraux_landscape3)));
        posts.add(new Post(getResources().getString(R.string.header_placeholder3), getResources().getString(R.string.text_placeholder), Uri.parse("android.resource://com.shtrudell.poster/" + R.raw.nick_gindraux_tree)));
        posts.add(new Post(getResources().getString(R.string.header_placeholder2), getResources().getString(R.string.long_text_placeholder), null, Uri.parse("android.resource://com.shtrudell.poster/" + R.raw.project_zomboid_what_was_lost)));
        posts.add(new Post(getResources().getString(R.string.header_placeholder), getResources().getString(R.string.text_placeholder), Uri.parse("android.resource://com.shtrudell.poster/" + R.raw.nick_gindraux_roman_study)));
        posts.add(new Post(getResources().getString(R.string.header_placeholder3), getResources().getString(R.string.long_text_placeholder)));
        posts.add(new Post(getResources().getString(R.string.header_placeholder), getResources().getString(R.string.text_with_links_placeholder)));
        posts.add(new Post(getResources().getString(R.string.header_placeholder2), getResources().getString(R.string.short_text_placeholder), Uri.parse("android.resource://com.shtrudell.poster/" + R.raw.greece)));
        return posts;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}