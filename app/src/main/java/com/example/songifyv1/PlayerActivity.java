package com.example.songifyv1;

import static com.example.songifyv1.MainActivity.musicFiles;
import static com.example.songifyv1.MainActivity.repeatBoolean;
import static com.example.songifyv1.MainActivity.shuffleBoolean;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Random;
//Making Playeractivity to connect the buttons and multiple features from the xml files and layout with their functions
//Learnt how to do this from https://www.youtube.com/watch?v=3HY1BpQJ-NY&list=PLwQLA73lSe1RfjMzbRLoIkcIJBu25FnVJ&index=7
public class PlayerActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {
    TextView song_name, artist_name, duration_played, duration_total;
    ImageView cover_art, nextBtn, prevBtn, backBtn, shuffleBtn, repeatBtn;
    FloatingActionButton playPauseBtn;
    SeekBar seekBar;
    int position = -1;
    static ArrayList<MusicFiles> listSongs = new ArrayList<>();
    static Uri uri;
    static MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    private Thread playThread, prevThread, nextThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        initViews();
        getIntentMethod();
        song_name.setText(listSongs.get(position).getTitle());
        artist_name.setText(listSongs.get(position).getArtist());
        mediaPlayer.setOnCompletionListener(this);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer !=null && fromUser){
                    mediaPlayer.seekTo(progress *1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        PlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                 if(mediaPlayer!= null){
                     int mCurrentPosition = mediaPlayer.getCurrentPosition()/1000;
                     seekBar.setProgress(mCurrentPosition);
                     duration_played.setText((formattedTime(mCurrentPosition)));
                 }
                 handler.postDelayed(this, 1000);
            }
        });

        //Shuffle and repeat button functionalities from https://www.youtube.com/watch?v=pLRBhUIy6H8&list=PLwQLA73lSe1RfjMzbRLoIkcIJBu25FnVJ&index=11 timestamp 3:15
        shuffleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shuffleBoolean){
                    shuffleBoolean = false;
                    shuffleBtn.setImageResource(R.drawable.ic_baseline_shuffle_off);
                }
                else{
                    shuffleBoolean = true;
                    shuffleBtn.setImageResource(R.drawable.ic_baseline_shuffle_on);
                }
            }
        });
        repeatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(repeatBoolean){
                    repeatBoolean = false;
                    repeatBtn.setImageResource(R.drawable.ic_baseline_repeatoff);
                }
                else{
                    repeatBoolean = true;
                    repeatBtn.setImageResource(R.drawable.ic_baseline_repeat_on);
                }
            }
        });

    }

    protected  void onResume (){
        playThreadBtn();
        nextThreadBtn();
        prevThreadBtn();
        super.onResume();
    }
//Next, prev and play/pause buttons from https://www.youtube.com/watch?v=r7B9S3S1xPI&list=PLwQLA73lSe1RfjMzbRLoIkcIJBu25FnVJ&index=8 4:45
//Defining the functionalities for these buttons and also making them work with shuffle/repeat/what to do when the song duration finishes and next song plays
    private void nextThreadBtn() {
        nextThread = new Thread(){
            @Override
            public void run() {
                super.run();
                nextBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nextBtnClicked();
                    }
                });
            }
        };
        nextThread.start();
    }

    private void nextBtnClicked() {
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
            //shuffle and repeat button functionalities from https://www.youtube.com/watch?v=pLRBhUIy6H8&list=PLwQLA73lSe1RfjMzbRLoIkcIJBu25FnVJ&index=11 timestamp 5:30
            if(shuffleBoolean&& !repeatBoolean){
                position = getrandom(listSongs.size()-1);
            }
            else if(!shuffleBoolean && !repeatBoolean){
                position = ((position+1)%listSongs.size());
            }
            uri = Uri.parse(listSongs.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            metaData(uri);
            song_name.setText(listSongs.get(position).getTitle());
            artist_name.setText(listSongs.get(position).getArtist());
            seekBar.setMax(mediaPlayer.getDuration()/1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer!= null){
                        int mCurrentPosition = mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            mediaPlayer.setOnCompletionListener(this);
            playPauseBtn.setBackgroundResource(R.drawable.ic_baseline_pause);
            mediaPlayer.start();
        }
        else{
            mediaPlayer.stop();
            mediaPlayer.release();
            //shuffle and repeat button functionalities from https://www.youtube.com/watch?v=pLRBhUIy6H8&list=PLwQLA73lSe1RfjMzbRLoIkcIJBu25FnVJ&index=11 timestamp 5:30
            if(shuffleBoolean&& !repeatBoolean){
                position = getrandom(listSongs.size()-1);
            }
            else if(!shuffleBoolean && !repeatBoolean){
                position = ((position+1)%listSongs.size());
            }
            uri = Uri.parse(listSongs.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            metaData(uri);
            song_name.setText(listSongs.get(position).getTitle());
            artist_name.setText(listSongs.get(position).getArtist());
            seekBar.setMax(mediaPlayer.getDuration()/1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer!= null){
                        int mCurrentPosition = mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            mediaPlayer.setOnCompletionListener(this);
            playPauseBtn.setBackgroundResource(R.drawable.ic_baseline_play);
        }
    }

    private int getrandom(int i) {
        Random random = new Random();
        return random.nextInt(i+1);
    }

    private void prevThreadBtn() {
        prevThread = new Thread(){
            @Override
            public void run() {
                super.run();
                prevBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        prevBtnClicked();
                    }
                });
            }
        };
        prevThread.start();
        
    }

    private void prevBtnClicked() {
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
            //shuffle and repeat button functionalities from https://www.youtube.com/watch?v=pLRBhUIy6H8&list=PLwQLA73lSe1RfjMzbRLoIkcIJBu25FnVJ&index=11 timestamp 5:30
            if(shuffleBoolean&& !repeatBoolean){
                position = getrandom(listSongs.size()-1);
            }
            else if(!shuffleBoolean && !repeatBoolean){
                position = ((position-1)<0?(listSongs.size()-1):(position-1));
            }
            uri = Uri.parse(listSongs.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            metaData(uri);
            song_name.setText(listSongs.get(position).getTitle());
            artist_name.setText(listSongs.get(position).getArtist());
            seekBar.setMax(mediaPlayer.getDuration()/1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer!= null){
                        int mCurrentPosition = mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            mediaPlayer.setOnCompletionListener(this);
            playPauseBtn.setBackgroundResource(R.drawable.ic_baseline_pause);
            mediaPlayer.start();
        }
        else{
            mediaPlayer.stop();
            mediaPlayer.release();
            //shuffle and repeat button functionalities from https://www.youtube.com/watch?v=pLRBhUIy6H8&list=PLwQLA73lSe1RfjMzbRLoIkcIJBu25FnVJ&index=11 timestamp 5:30
            if(shuffleBoolean&& !repeatBoolean){
                position = getrandom(listSongs.size()-1);
            }
            else if(!shuffleBoolean && !repeatBoolean){
                position = ((position-1)<0?(listSongs.size()-1):(position-1));
            }
            uri = Uri.parse(listSongs.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            metaData(uri);
            song_name.setText(listSongs.get(position).getTitle());
            artist_name.setText(listSongs.get(position).getArtist());
            seekBar.setMax(mediaPlayer.getDuration()/1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer!= null){
                        int mCurrentPosition = mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            mediaPlayer.setOnCompletionListener(this);
            playPauseBtn.setBackgroundResource(R.drawable.ic_baseline_play);
        }
    }

    private void playThreadBtn() {
        playThread = new Thread(){
            @Override
            public void run() {
                super.run();
                playPauseBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playPauseBtnClicked();
                    }
                });
            }
        };
        playThread.start();
    }

    private void playPauseBtnClicked() {
        if(mediaPlayer.isPlaying()){
            playPauseBtn.setImageResource((R.drawable.ic_baseline_play));
            mediaPlayer.pause();
            seekBar.setMax(mediaPlayer.getDuration()/1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer!= null){
                        int mCurrentPosition = mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
        }
        else{
            playPauseBtn.setImageResource(R.drawable.ic_baseline_pause);
            mediaPlayer.start();
            seekBar.setMax(mediaPlayer.getDuration()/1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer!= null){
                        int mCurrentPosition = mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
        }
    }
//from https://www.youtube.com/watch?v=3HY1BpQJ-NY&list=PLwQLA73lSe1RfjMzbRLoIkcIJBu25FnVJ&index=7 timestamp 7:30
//Code for the duration bar being able to display the song at the relevant position being able to display it's duration correctly
    private String formattedTime(int mCurrentPosition) {
        String totalout = "";
        String totalNew = "";
        String seconds = String.valueOf(mCurrentPosition %60);
        String minutes = String.valueOf(mCurrentPosition/60);
        totalout = minutes+ ":" + seconds;
        totalNew = minutes +":" +"0" + seconds;
        if(seconds.length()==1){
            return totalNew;
        }
        else{
            return totalout;
        }
    }
//Intentmethod from https://www.youtube.com/watch?v=3HY1BpQJ-NY&list=PLwQLA73lSe1RfjMzbRLoIkcIJBu25FnVJ&index=7 timestamp 2:40
//When the phone has songs, this will make the duration bar generate with the song's duration and call metadata to retrieve the song's metadata according to position in song list
    private void getIntentMethod() {
        position = getIntent().getIntExtra("position", -1);
        listSongs = musicFiles;
        if(listSongs!= null){
            playPauseBtn.setImageResource(R.drawable.ic_baseline_pause);
            uri = Uri.parse(listSongs.get(position).getPath());
        }
        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.start();
        }
        else{
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.start();
        }
        seekBar.setMax(mediaPlayer.getDuration()/1000);
        metaData(uri);

    }

    private void initViews() {
        song_name=  findViewById(R.id.song_name);
        artist_name = findViewById(R.id.song_artist);
        duration_played = findViewById(R.id.durationPlayed);
        duration_total = findViewById(R.id.durationTotal);
        cover_art = findViewById(R.id.cover_art);
        nextBtn = findViewById(R.id.id_next);
        prevBtn = findViewById(R.id.id_prev);
        backBtn = findViewById(R.id.back_btn);
        shuffleBtn =  findViewById(R.id.id_shuffle);
        repeatBtn =  findViewById(R.id.id_repeat);
        playPauseBtn =  findViewById(R.id.play_pause);
        seekBar =  findViewById(R.id.seekBar);
    }
//from https://www.youtube.com/watch?v=r7B9S3S1xPI&list=PLwQLA73lSe1RfjMzbRLoIkcIJBu25FnVJ&index=8 0:25
//This method uses the Uri to retrieve the song's duration
    private void metaData(Uri uri){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri.toString());
        int durationTotal = Integer.parseInt(listSongs.get(position).getDuration())/1000;
        duration_total.setText(formattedTime((durationTotal)));
        //unused code below
        byte[] art = retriever.getEmbeddedPicture();
        if(art!= null){
            Glide.with(this)
                    .asBitmap()
                    .load(art)
                    .into(cover_art);
        }
        else{
            Glide.with(this)
                    .asBitmap()
                    .load(R.drawable.icon1)
                    .into(cover_art);
            }
        }
//Method that is called when a song finishes it calls next  button clicked method to start next song and start a new on completion listener for new song
//from https://www.youtube.com/watch?v=YLV35ZbU26s&list=PLwQLA73lSe1RfjMzbRLoIkcIJBu25FnVJ&index=10 timestamp 5:00
    @Override
    public void onCompletion(MediaPlayer mp) {
        nextBtnClicked();
        if(mediaPlayer != null){
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(this);
        }
    }
}
