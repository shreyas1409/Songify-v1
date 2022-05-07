package com.example.songifyv1;

import static com.example.songifyv1.MainActivity.musicFiles;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//Makes an instance of musicadapter and recyclerview to be the fragment that holds music_files
//From https://www.youtube.com/watch?v=prxqc-B1h1w&list=PLwQLA73lSe1RfjMzbRLoIkcIJBu25FnVJ&index=4 timestamp 7:45


public class MusicFragment extends Fragment {

    RecyclerView recyclerView;
    MusicAdapter musicAdapter;
    public MusicFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_music, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        if(!(musicFiles.size() < 1)){
            musicAdapter = new MusicAdapter(getContext(), musicFiles);
            recyclerView.setAdapter((musicAdapter));
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        }
        return view;
    }
}