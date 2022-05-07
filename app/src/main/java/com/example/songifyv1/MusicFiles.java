package com.example.songifyv1;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

//Written by Shreyas
//Creating entities for database along with getters and setters
//Also used for the music items displayed in Music Fragment from https://www.youtube.com/watch?v=sAVXRkI2ihI&list=PLwQLA73lSe1RfjMzbRLoIkcIJBu25FnVJ&index=3 timestamp 5:00
@Entity(tableName = "musicfiles")
public class MusicFiles {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name="rowid")
    private int rowid;
    @ColumnInfo(name="path")
    private String path;
    @ColumnInfo(name="title")
    private String title;
    @ColumnInfo(name="artist")
    private String artist;
    @ColumnInfo(name="album")
    private String album;
    @ColumnInfo(name="duration")
    private String duration;

    public MusicFiles(String path, String title, String artist, String album, String duration) {
        this.path = path;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
    }

    public MusicFiles() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getRowid() {
        return rowid;
    }

    public void setRowid(int rowid) {
        this.rowid = rowid;
    }
}
