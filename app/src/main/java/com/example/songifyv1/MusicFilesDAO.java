package com.example.songifyv1;
//Written by Shreyas
//annotations for DAO
//Learnt about using a room database from https://developer.android.com/codelabs/android-room-with-a-view#0
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MusicFilesDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(MusicFiles musicFiles);

    @Query("SELECT * FROM musicfiles ORDER BY rowid ASC")
    List<MusicFiles> getMusicFiles();
    @Delete
    void deleteAll(List<MusicFiles> musicFiles);
}


