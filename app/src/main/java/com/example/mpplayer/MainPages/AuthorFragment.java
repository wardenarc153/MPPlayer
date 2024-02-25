package com.example.mpplayer.MainPages;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.mpplayer.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AuthorFragment extends Fragment {

    private DatabaseReference userAdminRoot;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        userAdminRoot = FirebaseDatabase.getInstance().getReference()
                .child("user_data")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_author, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final NavController navController = Navigation.findNavController(view);

        ImageView home = (ImageView) view.findViewById(R.id.main_menu_logo);
        ImageView author = (ImageView) view.findViewById(R.id.user_logo);
        ImageView instruction = (ImageView) view.findViewById(R.id.instruction_logo);
        ImageView infoProg = (ImageView) view.findViewById(R.id.about_prog_logo);
        ImageView git = (ImageView) view.findViewById(R.id.git_logo);
        ImageView exit = (ImageView) view.findViewById(R.id.exit_logo);
        Button adminButton = (Button) view.findViewById(R.id.adminButton);

        home.setOnClickListener(view1 -> navController.navigate(R.id.action_authorFragment_to_songListFragment));
        git.setOnClickListener(view1 -> navController.navigate(R.id.action_authorFragment_to_gitFragment));
        instruction.setOnClickListener(view1 -> navController.navigate(R.id.action_authorFragment_to_instructionFragment));
        infoProg.setOnClickListener(view1 -> navController.navigate(R.id.action_authorFragment_to_infoProgFragment));
        exit.setOnClickListener(view1 -> requireActivity().finish());

        userAdminRoot.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Boolean haveRoot = dataSnapshot.child("adminRoot").getValue(Boolean.class);

                if(haveRoot != null && haveRoot){
                    adminButton.setVisibility(View.VISIBLE);
                    adminButton.setOnClickListener(view1 -> navController.navigate(R.id.action_authorFragment_to_adminFragment));
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }
}