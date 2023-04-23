package com.shtrudell.poster;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{

    private final LayoutInflater inflater;
    private final List<Post> posts;

    public PostAdapter(Context context, List<Post> posts) {
        this.posts = posts;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.post_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);

        if(post.getHeader() == null || post.getHeader().isEmpty())
            holder.headerTextView.setVisibility(View.GONE);
        else
            holder.headerTextView.setText(post.getHeader());
        if(post.getText() == null || post.getText().isEmpty())
            holder.textTextView.setVisibility(View.GONE);
        else
            holder.textTextView.setText(post.getText());
        if(post.getImage() == null)
            holder.imageImageView.setVisibility(View.GONE);
        if(post.getSong() == null)
            holder.compactPlayerContainer.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageImageView;
        final TextView headerTextView, textTextView;
        final View compactPlayerContainer;
        final Button playButton;
        final TextView songNameTextView;

        ViewHolder(View view){
            super(view);

            imageImageView = view.findViewById(R.id.image_imageView);
            headerTextView = view.findViewById(R.id.header_textView);
            textTextView = view.findViewById(R.id.text_textView);
            playButton = view.findViewById(R.id.play_button);
            songNameTextView = view.findViewById(R.id.song_name_textView);
            compactPlayerContainer = view.findViewById(R.id.compact_player_container);
        }
    }
}
