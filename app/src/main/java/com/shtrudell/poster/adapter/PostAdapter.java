package com.shtrudell.poster.adapter;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.shtrudell.poster.MainActivity;
import com.shtrudell.poster.Post;
import com.shtrudell.poster.R;
import com.shtrudell.poster.fragment.ImageFragment;
import com.shtrudell.poster.fragment.PostWriterFragment;
import com.shtrudell.poster.view.CompactMusicPlayer;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{

    private final LayoutInflater inflater;
    private final List<Post> posts;
    private final Map<Integer, MediaPlayer> musicPlayerDictionary = new Hashtable<>();
    private final List<CompactMusicPlayer> players= new ArrayList<>();
    private OnPostClickListener onPostClickListener;
    private OnImageClickListener onImageClickListener;

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

        players.add(holder.musicPlayer);
        holder.itemView.setOnClickListener(v -> {
            onPostClickListener.onPostClick(post);
        });

        holder.musicPlayer.setListenerStart(() ->{
            musicPlayerDictionary.forEach((integer, mediaPlayer) -> {
                if(holder.musicPlayer.getMediaPlayer() != mediaPlayer) {
                    for(CompactMusicPlayer player : players){
                        if(player != holder.musicPlayer)
                            player.stop();
                    }
                }
            });
        });

        //header
        if(post.getHeader() == null || post.getHeader().isEmpty())
            holder.headerTextView.setVisibility(View.GONE);
        else
            holder.headerTextView.setText(post.getHeader());
        //text
        if(post.getText() == null || post.getText().isEmpty())
            holder.textTextView.setVisibility(View.GONE);
        else
            holder.textTextView.setText(post.getText());
        //image
        if(post.getImage() == null)
            holder.imageImageView.setVisibility(View.GONE);
        else
        {
            holder.imageImageView.setOnClickListener(v -> {
                if(onImageClickListener != null)
                    onImageClickListener.onImageClick(post.getImage());
            });

            holder.imageImageView.setVisibility(View.VISIBLE);
            holder.imageImageView.setImageURI(post.getImage());
        }
        //music player
        if(post.getSong() == null)
            holder.musicPlayer.setVisibility(View.GONE);
        else {
            holder.musicPlayer.setVisibility(View.VISIBLE);
            holder.musicPlayer.setSource(post.getSong());

            holder.musicPlayer.setOnMediaPlayerUpdatedListener(() -> {
                if(musicPlayerDictionary.containsKey(position) || holder.musicPlayer.getMediaPlayer() == null) return;

                musicPlayerDictionary.put(position, holder.musicPlayer.getMediaPlayer());
            });
            holder.musicPlayer.setMediaPlayer(musicPlayerDictionary.get(position));
            holder.musicPlayer.updateButtonState();
        }

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void setOnPostClickListener(OnPostClickListener onPostClickListener) {
        this.onPostClickListener = onPostClickListener;
    }

    public void setOnImageClickListener(OnImageClickListener onImageClickListener) {
        this.onImageClickListener = onImageClickListener;
    }

    public interface OnPostClickListener {
        void onPostClick(Post post);
    }

    public interface OnImageClickListener {
        void onImageClick(Uri source);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageImageView;
        final TextView headerTextView, textTextView;
        final CompactMusicPlayer musicPlayer;

        ViewHolder(View view){
            super(view);

            imageImageView = view.findViewById(R.id.image_imageView);
            headerTextView = view.findViewById(R.id.header_textView);
            textTextView = view.findViewById(R.id.text_textView);
            musicPlayer = view.findViewById(R.id.compact_player);
        }
    }
}
