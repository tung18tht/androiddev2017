package vn.edu.usth.musicplayer;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import vn.edu.usth.musicplayer.Model.Playlist;
import vn.edu.usth.musicplayer.Model.SongItem;
import vn.edu.usth.musicplayer.fragment.DownloadFragment;
import vn.edu.usth.musicplayer.fragment.HomeFragment;
import vn.edu.usth.musicplayer.fragment.PlayingFragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    Playlist pl;
    ListView l;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.tab_home:
                        loadFragment(new HomeFragment());
                        break;
                        
                    case R.id.tab_playing:
                        loadFragment(new PlayingFragment());
                        break;
                        
                    case R.id.tab_download:
                        loadFragment(new DownloadFragment());
                        break;
                }
            }
        });
        //create playlist
        pl = new Playlist("default");

        if (copyMusicToSdCard()) {
            playMusic(pl.getSong(1).getUrl());
        }

        //ListView
        String[] music = {"Songs", "Albums", "Artists", "Playlist"};
        l = (ListView) findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, music);
        l.setAdapter(adapter);
        
        Log.i("status", "Main Activity created");
    }
    
    private void loadFragment(Fragment frag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.contentContainer);
        if(fragment == null) {
            transaction.add(R.id.contentContainer, frag);
        } else {
            transaction.replace(R.id.contentContainer, frag);
        }
        transaction.addToBackStack(null);
        
        transaction.commit();
    }
    
    private boolean copyMusicToSdCard() {
        String[] files = {"Infatuation - Maroon 5 [MP3 128kbps].mp3",
                          "Lost Stars - Adam Levine [MP3 128kbps].mp3",
                          "Misery - Maroon 5 [MP3 128kbps].mp3",
                          "Stutter - Maroon 5 [MP3 128kbps].mp3"};
    
        try {
            for (String fileName: files) {
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC), fileName);
    
                FileOutputStream fos = new FileOutputStream(file);
                InputStream is = getResources().getAssets().open(fileName, Context.MODE_WORLD_READABLE);
                byte buf[] = new byte[1024];
                int numRead = 0;
                while ((numRead = is.read(buf)) > 0) {
                    fos.write(buf, 0, numRead);
                }
                is.close();
                fos.close();
                SongItem si = new SongItem(file);
                pl.addSong(si);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("music", "Copy to SD Card failed");
            return false;
        }
        Log.i("music", "Copied to SD Card");
        return true;
    }
    
    private boolean playMusic(String filename) {
        MediaPlayer player = new MediaPlayer();
        Uri musicURI = Uri.parse(filename);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            player.setDataSource(getApplicationContext(), musicURI);
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("music", "Background music failed to play");
            return false;
        }
        player.start();
        Log.i("music", "Background music played");
        return true;
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        
        Log.i("status", "Main Activity started");
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        
        Log.i("status", "Main Activity paused");
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        
        Log.i("status", "Main Activity resumed");
    }
    
    @Override
    protected void onStop() {
        super.onStop();
        
        Log.i("status", "Main Activity stopped");
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        
        Log.i("status", "Main Activity destroyed");
    }
}
