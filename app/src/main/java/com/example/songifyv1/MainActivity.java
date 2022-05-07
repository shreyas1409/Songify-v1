package com.example.songifyv1;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 1;
    static ArrayList<MusicFiles> musicFiles = new ArrayList<>();
    static boolean shuffleBoolean = false, repeatBoolean = false;
    private MusicFilesDatabase musicFilesDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        musicFilesDatabase = MusicFilesDatabase.getDatabase(this);
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                createMusicFiles();
            }
        });



    }

    private void createMusicFiles() {
        MusicFilesDAO musicFilesDao = musicFilesDatabase.musicFilesDao();
        List<MusicFiles> musicFilesFromDB = musicFilesDao.getMusicFiles();
        Log.e("count", "" + musicFilesFromDB.size());
        if (musicFilesFromDB.isEmpty()) {

            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
            } else {
                musicFiles = getAllAudio(this);
                musicFiles.stream().forEach(musicFilesDao::insert);
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initViewPager();
                    }
                });
            }
        } else {
            musicFiles.clear();
            musicFiles.addAll(musicFilesFromDB);
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    initViewPager();
                }
            });
        }


    }
//Permission to read the phone for displaying music files from https://www.youtube.com/watch?v=sAVXRkI2ihI&list=PLwQLA73lSe1RfjMzbRLoIkcIJBu25FnVJ&index=3 timestamp 3:00
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                musicFiles = getAllAudio(this);
                initViewPager();
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
            }
        }
    }
//viewPager concept from learning video https://www.youtube.com/watch?v=IauxP5xgu_s timestamp 4:00
    private void initViewPager() {
        ViewPager viewPager = findViewById(R.id.viewpager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter((getSupportFragmentManager()));
        viewPagerAdapter.addFragments(new MusicFragment(), "Songs");
        viewPagerAdapter.addFragments(new SettingsFragment(), "Settings");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    public static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final ArrayList<Fragment> fragments;
        public ArrayList<String> titles;

        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        void addFragments(Fragment fragment, String title) {
            fragments.add(fragment);
            titles.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }
//Using Uri and getting the music file's metadata with cursor from https://www.youtube.com/watch?v=sAVXRkI2ihI&list=PLwQLA73lSe1RfjMzbRLoIkcIJBu25FnVJ&index=3 timestamp 6:15
    public static ArrayList<MusicFiles> getAllAudio(Context context) {
        ArrayList<MusicFiles> tempAudioList = new ArrayList<>();
        int e = 1;
        Log.e("Path  : ", "xyz");
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ARTIST
        };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        Log.e("ksjaasf", cursor == null ? "null" : "notnull");

        if (cursor != null) {
            Log.e("ksjaasf", "" + cursor.getCount());
            while (cursor.moveToNext()) {
                Log.e("ksjaasf", "dakfla");
                String album = cursor.getString(0);
                String title = cursor.getString(1);
                String duration = cursor.getString(2);
                String path = cursor.getString(3);
                String artist = cursor.getString(4);
                MusicFiles musicFiles = new MusicFiles(path, title, artist, album, duration);
                Log.e("Path  : " + path, "Album : " + album);
                tempAudioList.add(musicFiles);
            }
            cursor.close();
        }
        return tempAudioList;
    }

}