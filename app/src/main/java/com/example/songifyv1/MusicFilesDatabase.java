package com.example.songifyv1;
//Written by Shreyas
import android.content.Context;
//making a Room database
//Learnt about using a room database from https://developer.android.com/codelabs/android-room-with-a-view#0

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {MusicFiles.class}, version = 1, exportSchema = false)
public abstract class MusicFilesDatabase extends RoomDatabase {
    public abstract MusicFilesDAO musicFilesDao();

    private static volatile MusicFilesDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static MusicFilesDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MusicFilesDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MusicFilesDatabase.class, "musicfiles")
                            .build();
                }
            }
        }
        return INSTANCE;
    }


}
