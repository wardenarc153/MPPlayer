package com.example.mpplayer.MainPages.Admin;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.mpplayer.MainPages.SongBase.SongDatabaseHelper;
import com.example.mpplayer.R;

public class AddSongFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PICK_AUDIO_REQUEST = 2;

    private Uri selectedImageUri;
    private Uri selectedAudioUri;

    private EditText nameSongEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_song, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameSongEditText = view.findViewById(R.id.name_song);
        Button addImageButton = view.findViewById(R.id.add_image);
        Button addAudioButton = view.findViewById(R.id.add_song);
        Button finishAddSongButton = view.findViewById(R.id.finish_add_song);

        addImageButton.setOnClickListener(v -> openGallery(PICK_IMAGE_REQUEST));
        addAudioButton.setOnClickListener(v -> openGallery(PICK_AUDIO_REQUEST));
        finishAddSongButton.setOnClickListener(v -> saveSongLocally());

        final NavController navController = Navigation.findNavController(view);

        ImageView home = (ImageView) view.findViewById(R.id.main_menu_logo);
        ImageView author = (ImageView) view.findViewById(R.id.user_logo);
        ImageView instruction = (ImageView) view.findViewById(R.id.instruction_logo);
        ImageView infoProg = (ImageView) view.findViewById(R.id.about_prog_logo);
        ImageView git = (ImageView) view.findViewById(R.id.git_logo);
        ImageView exit = (ImageView) view.findViewById(R.id.exit_logo);

        home.setOnClickListener(view1 -> navController.navigate(R.id.action_addSongFragment_to_songListFragment));
        git.setOnClickListener(view1 -> navController.navigate(R.id.action_addSongFragment_to_gitFragment));
        instruction.setOnClickListener(view1 -> navController.navigate(R.id.action_addSongFragment_to_instructionFragment));
        infoProg.setOnClickListener(view1 -> navController.navigate(R.id.action_addSongFragment_to_infoProgFragment));
        author.setOnClickListener(view1 -> navController.navigate(R.id.action_addSongFragment_to_authorFragment));
        exit.setOnClickListener(view1 -> requireActivity().finish());
    }

    private void openGallery(int requestCode) {
        Intent intent;
        if (requestCode == PICK_IMAGE_REQUEST) {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        } else if (requestCode == PICK_AUDIO_REQUEST) {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        } else {
            return;
        }

        startActivityForResult(intent, requestCode);
    }

    private void saveSongLocally() {
        String songName = nameSongEditText.getText().toString().trim();

        if (songName.isEmpty()) {
            Toast.makeText(requireContext(), "Введите название песни", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedImageUri == null || selectedAudioUri == null) {
            Toast.makeText(requireContext(), "Выберите изображение и аудио", Toast.LENGTH_SHORT).show();
            return;
        }

        // Создайте экземпляр вашего SongDatabaseHelper (если у вас его нет)
        SongDatabaseHelper dbHelper = new SongDatabaseHelper(requireContext());

        // Вызовите метод сохранения в базе данных
        long result = dbHelper.saveSong(songName, selectedImageUri, selectedAudioUri);

        // Закройте соединение с базой данных (если нужно)
        dbHelper.close();

        if (result != -1) {
            Toast.makeText(requireContext(), "Песня успешно сохранена", Toast.LENGTH_SHORT).show();
            // Здесь вы также можете вызвать метод сохранения данных песни в базе данных.
        } else {
            Toast.makeText(requireContext(), "Ошибка при сохранении песни", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == PICK_IMAGE_REQUEST) {
                selectedImageUri = data.getData();
            } else if (requestCode == PICK_AUDIO_REQUEST) {
                selectedAudioUri = data.getData();
            }
        }
    }
}
