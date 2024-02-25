package com.example.mpplayer.MainPages;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.mpplayer.MainPages.SongBase.SongAdapter;
import com.example.mpplayer.MainPages.SongBase.SongDataClass;
import com.example.mpplayer.MainPages.SongBase.SongDatabaseHelper;
import com.example.mpplayer.R;

import java.util.ArrayList;
import java.util.List;

public class SongListFragment extends Fragment {

    private List<SongDataClass> songList;
    private SongAdapter songListAdapter;
    private SongDatabaseHelper songDatabaseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_song_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final NavController navController = Navigation.findNavController(view);
        ListView listViewSongs = view.findViewById(R.id.listViewSongs);
        songList = loadSongsFromDatabase(); // Загружаем песни из базы данных

        // Инициализируем адаптер
        songListAdapter = new SongAdapter(requireContext(), R.layout.item_song, songList, position -> {
            // Переключаемся на фрагмент с плеером, передавая позицию выбранной песни
            switchToMusicPlayerFragment(position);
        });

        listViewSongs.setAdapter(songListAdapter);

        // Обработка нажатия на элемент списка
        listViewSongs.setOnItemClickListener((parent, view1, position, id) -> {
            // Переключаемся на фрагмент с плеером, передавая позицию выбранной песни
            switchToMusicPlayerFragment(position);
        });

        ImageView home = (ImageView) view.findViewById(R.id.main_menu_logo);
        ImageView author = (ImageView) view.findViewById(R.id.user_logo);
        ImageView instruction = (ImageView) view.findViewById(R.id.instruction_logo);
        ImageView infoProg = (ImageView) view.findViewById(R.id.about_prog_logo);
        ImageView git = (ImageView) view.findViewById(R.id.git_logo);
        ImageView exit = (ImageView) view.findViewById(R.id.exit_logo);

        git.setOnClickListener(view1 -> navController.navigate(R.id.action_songListFragment_to_gitFragment));
        author.setOnClickListener(view1 -> navController.navigate(R.id.action_songListFragment_to_authorFragment));
        instruction.setOnClickListener(view1 -> navController.navigate(R.id.action_songListFragment_to_instructionFragment));
        infoProg.setOnClickListener(view1 -> navController.navigate(R.id.action_songListFragment_to_infoProgFragment));
        exit.setOnClickListener(view1 -> requireActivity().finish());
    }

    private List<SongDataClass> loadSongsFromDatabase() {
        // Инициализируем SongDatabaseHelper
        songDatabaseHelper = new SongDatabaseHelper(requireContext());

        // Загружаем все песни из базы данных
        List<SongDataClass> songs = songDatabaseHelper.getAllSongs();

        // Закрываем соединение с базой данных
        songDatabaseHelper.close();

        return songs;
    }

    private void switchToMusicPlayerFragment(int position) {
        MusicFragment musicFragment = new MusicFragment();

        Bundle bundle = new Bundle();
        ArrayList<SongDataClass> songList = new ArrayList<>(this.songList);

        bundle.putParcelableArrayList("songList", songList);
        bundle.putInt("position", position);
        musicFragment.setArguments(bundle);


        // Используем NavController для переключения на MusicFragment с передачей данных
        NavController navController = Navigation.findNavController(requireView());
        navController.navigate(R.id.action_songListFragment_to_musicFragment, bundle);
    }
}
