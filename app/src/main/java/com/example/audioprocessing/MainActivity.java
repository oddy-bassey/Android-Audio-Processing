package com.example.audioprocessing;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;
    private int progressCount;

    public void play(View view){
        if(mediaPlayer.isPlaying()){
            Toast.makeText(this, "Media file is playing already", Toast.LENGTH_SHORT).show();
        }else {

            if(mediaPlayer == null){
                mediaPlayer = MediaPlayer.create(this, R.raw.boca);
            }
            mediaPlayer.start();
        }
    }

    public void pause(View view){
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }else{
            Toast.makeText(this, "Cannot pause! No media is playing.", Toast.LENGTH_SHORT).show();
        }
    }

    public void stop(View view) {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer = MediaPlayer.create(this, R.raw.boca);
        } else{
            Toast.makeText(this, "Cannot stop! No media is playing.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mediaPlayer = MediaPlayer.create(this, R.raw.boca);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        SeekBar volumeSeekBar = (SeekBar)findViewById(R.id.seekBar);
        final SeekBar scrubSeekBar = (SeekBar)findViewById(R.id.seekBar2);

        //sets the max value for the volume seekBar, its current volume and change listener
        volumeSeekBar.setMax(maxVolume);
        volumeSeekBar.setProgress(currentVolume);
        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //sets the media volume of the device to the progress as user slides foeward or backward.
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        scrubSeekBar.setMax(mediaPlayer.getDuration());
        scrubSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                //set the music duartion to the progress as the user slides forward or backward.
                Log.i("scrub", "progress - "+progress);
                progressCount = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mediaPlayer.pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(progressCount);
                mediaPlayer.start();
            }
        });

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                scrubSeekBar.setProgress(mediaPlayer.getCurrentPosition());
            }
        }, 0, 300);
    }
}
