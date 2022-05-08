package com.example.songifyv1;
//Written by Shreyas
//annotations for DAO
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


