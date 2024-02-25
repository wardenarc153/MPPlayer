package com.example.mpplayer.MainPages.Admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.mpplayer.R;

public class AdminFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button addSong = view.findViewById(R.id.add_song);

        final NavController navController = Navigation.findNavController(view);

        ImageView home = (ImageView) view.findViewById(R.id.main_menu_logo);
        ImageView author = (ImageView) view.findViewById(R.id.user_logo);
        ImageView instruction = (ImageView) view.findViewById(R.id.instruction_logo);
        ImageView infoProg = (ImageView) view.findViewById(R.id.about_prog_logo);
        ImageView git = (ImageView) view.findViewById(R.id.git_logo);
        ImageView exit = (ImageView) view.findViewById(R.id.exit_logo);

        home.setOnClickListener(view1 -> navController.navigate(R.id.action_adminFragment_to_songListFragment));
        git.setOnClickListener(view1 -> navController.navigate(R.id.action_adminFragment_to_gitFragment));
        instruction.setOnClickListener(view1 -> navController.navigate(R.id.action_adminFragment_to_instructionFragment));
        infoProg.setOnClickListener(view1 -> navController.navigate(R.id.action_adminFragment_to_infoProgFragment));
        author.setOnClickListener(view1 -> navController.navigate(R.id.action_adminFragment_to_authorFragment));
        addSong.setOnClickListener(view1 -> navController.navigate(R.id.action_adminFragment_to_addSongFragment));
        exit.setOnClickListener(view1 -> requireActivity().finish());
    }
}