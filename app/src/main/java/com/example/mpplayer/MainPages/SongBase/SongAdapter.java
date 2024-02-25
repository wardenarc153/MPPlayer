package com.example.mpplayer.MainPages.SongBase;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mpplayer.R;

import java.util.List;

public class SongAdapter extends ArrayAdapter<SongDataClass> {

    private OnItemClickListener onItemClickListener;

    public SongAdapter(@NonNull Context context, int resource, @NonNull List<SongDataClass> objects, OnItemClickListener onItemClickListener) {
        super(context, resource, objects);
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_song, parent, false);
        }

        SongDataClass song = getItem(position);

        TextView songTitleTextView = convertView.findViewById(R.id.songTitleTextView);
        ImageView iconView = convertView.findViewById(R.id.icon_view);

        if (song != null) {
            songTitleTextView.setText(song.getTitle());
        }

        convertView.setOnClickListener(v -> onItemClickListener.onItemClick(position));

        return convertView;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}


