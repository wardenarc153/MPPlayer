package com.example.mpplayer.MainPages;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mpplayer.MainPages.SongBase.SongAdapter;
import com.example.mpplayer.MainPages.SongBase.SongDataClass;
import com.example.mpplayer.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MusicFragment extends Fragment {
    private int currentSongPosition = 0;

    private MediaPlayer mediaPlayer;
    private List<SongDataClass> songList;
    private RecyclerView recyclerView;
    private SongAdapter songAdapter;
    private ImageView playPauseButton, previousButton, nextButton;
    private SeekBar seekBar;
    private TextView currentTimeTextView, totalTimeTextView;
    private Handler handler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_music, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final NavController navController = Navigation.findNavController(view);
        ImageView home = view.findViewById(R.id.main_menu_logo);
        ImageView author = view.findViewById(R.id.user_logo);
        ImageView instruction = view.findViewById(R.id.instruction_logo);
        ImageView infoProg = view.findViewById(R.id.about_prog_logo);
        ImageView git = view.findViewById(R.id.git_logo);
        ImageView exit = view.findViewById(R.id.exit_logo);

        ImageView songImageView = view.findViewById(R.id.songImage);
        TextView songTitleTextView = view.findViewById(R.id.songName);

        home.setOnClickListener(view1 -> {
            endMusicPlayer();
            navController.navigate(R.id.action_musicFragment_to_songListFragment);
        });
        git.setOnClickListener(view1 -> {
            endMusicPlayer();
            navController.navigate(R.id.action_musicFragment_to_gitFragment);
        });
        author.setOnClickListener(view1 -> {
            endMusicPlayer();
            navController.navigate(R.id.action_musicFragment_to_authorFragment);
        });
        instruction.setOnClickListener(view1 -> {
            endMusicPlayer();
            navController.navigate(R.id.action_musicFragment_to_instructionFragment);
        });
        infoProg.setOnClickListener(view1 -> {
            endMusicPlayer();
            navController.navigate(R.id.action_musicFragment_to_infoProgFragment);
        });
        exit.setOnClickListener(view1 -> {
            endMusicPlayer();
            requireActivity().finish();
        });

        playPauseButton = view.findViewById(R.id.play_button);
        previousButton = view.findViewById(R.id.previousButton);
        nextButton = view.findViewById(R.id.nextButton);

        playPauseButton.setOnClickListener(v -> togglePlayPause());

        seekBar = view.findViewById(R.id.seekBar);
        currentTimeTextView = view.findViewById(R.id.currentTimeTextView);
        totalTimeTextView = view.findViewById(R.id.totalTimeTextView);

        previousButton.setOnClickListener(v -> playPreviousSong());
        nextButton.setOnClickListener(v -> playNextSong());

        handler = new Handler();

        Bundle args = getArguments();
        if (args != null) {
            songList = args.getParcelableArrayList("songList");
            int position = args.getInt("position", -1);
            if (position != -1) {
                currentSongPosition = position;
            }
        }

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Необходимо для предотвращения обновления seekBar во время перемотки пользователем
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Необходимо для предотвращения обновления seekBar во время перемотки пользователем
            }
        });

        // Инициализация MediaPlayer
        mediaPlayer = new MediaPlayer();


        mediaPlayer.setOnCompletionListener(mp -> playSong(currentSongPosition));

        mediaPlayer.setOnPreparedListener(mp -> {
            Uri imageUri = Uri.parse(songList.get(currentSongPosition).getImagePath());

            String songTitle = songList.get(currentSongPosition).getTitle();

            Glide.with(requireContext())
                    .load(imageUri)
                    .placeholder(R.drawable.placeholder_album_cover) // Заглушка, если изображение не загружено
                    .error(R.drawable.default_image) // Заглушка, если возникает ошибка при загрузке изображения
                    .into(songImageView);
            songTitleTextView.setText(songTitle);
        });
        // Обновление seekBar и времени воспроизведения каждую секунду
        updateSeekBar();
    }

    private void endMusicPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void playSong(int position) {
        // Загрузка новой песни и начало воспроизведения
        if (mediaPlayer != null) {
            mediaPlayer.reset();
            try {
                String audioPath = songList.get(position).getAudioPath();
                Uri audioUri = Uri.parse(audioPath);
                mediaPlayer.setDataSource(requireContext(), audioUri);
                mediaPlayer.prepare();
                mediaPlayer.start();
                playPauseButton.setImageResource(R.drawable.pause_button);
                updateTotalTimeTextView();
                currentSongPosition = position;
            } catch (IOException e) {
                System.out.println(e);
                e.printStackTrace();
            }
        }
    }


    private void togglePlayPause() {
        // Переключение между воспроизведением и паузой
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                playPauseButton.setImageResource(R.drawable.play_button);
            } else {
                mediaPlayer.start();
                playPauseButton.setImageResource(R.drawable.pause_button);
            }
        }
    }

    private void playPreviousSong() {
        // Воспроизведение предыдущей песни
        if (currentSongPosition > 0) {
            playSong(currentSongPosition - 1);
        }
    }

    private void playNextSong() {
        // Воспроизведение следующей песни
        int currentPosition = getCurrentPosition();
        if (currentPosition < songList.size() - 1) {
            playSong(currentPosition + 1);
        }
    }

    private void updateSeekBar() {
        // Обновление seekBar и времени воспроизведения каждую секунду
        getActivity().runOnUiThread(() -> {
            if (mediaPlayer != null) {
                try {
                    int currentPosition = mediaPlayer.getCurrentPosition();
                    seekBar.setProgress(currentPosition);
                    currentTimeTextView.setText(formatTime(currentPosition));
                    handler.postDelayed(this::updateSeekBar, 1000);
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void updateTotalTimeTextView() {
        // Обновление общего времени воспроизведения
        if (mediaPlayer != null) {
            int totalTime = mediaPlayer.getDuration();
            totalTimeTextView.setText(formatTime(totalTime));
            seekBar.setMax(totalTime);
        }
    }

    private String formatTime(int milliseconds) {
        // Форматирование времени в формат "мм:сс"
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(milliseconds),
                TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds))
        );
    }

    private int getCurrentPosition() {
        // Получение текущей позиции воспроизведения
        return Math.max(currentSongPosition, 0);
    }

    @Override
    public void onDestroy() {
        // Освобождение ресурсов MediaPlayer при завершении фрагмента
        endMusicPlayer();
        super.onDestroy();
    }
}
