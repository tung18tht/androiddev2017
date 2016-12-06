package vn.edu.usth.musicplayer;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabSelectListener;
import vn.edu.usth.musicplayer.Model.Playlist;
import vn.edu.usth.musicplayer.Model.SongItem;
import vn.edu.usth.musicplayer.fragment.DownloadFragment;
import vn.edu.usth.musicplayer.fragment.PlayingFragment;
import vn.edu.usth.musicplayer.fragment.SongsFragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {
    static Playlist playlist;
    static int index = 0;
    static MediaPlayer player;
    static boolean isPlaying = false;
    static boolean isRepeating = false;
    static int currentFrag = 0;
    RedirectTracer tracer = new RedirectTracer(player);
    static FragmentManager fm;

    public static int downloadingSongs = 0;
    public static BottomBarTab downloadTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fm = getSupportFragmentManager();

        playlist = new Playlist("default");

        copyMusicToSdCard();
        player = new MediaPlayer();

//        loadSong(getCurrentSong());

        downloadTab = (BottomBarTab) findViewById(R.id.tab_download);
        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.tab_home:
                        loadFragment(new SongsFragment(), 1);
                        currentFrag = 0;
                        break;

                    case R.id.tab_playing:
                        loadFragment(new PlayingFragment(), 1);
                        currentFrag = 1;
                        break;

                    case R.id.tab_download:
                        loadFragment(new DownloadFragment(), 1);
                        currentFrag = 2;
                        break;
                }
            }
        });

        loadSong(getCurrentSong());

        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                isPlaying = false;
                if (currentFrag == 1) {
                    //reloadPlayFragment();
                }
            }
        });

        new AsyncTask<Void, Integer, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                while (true) {
                    if (currentFrag == 1) {
                        reloadPlayFragment();
                        //refreshPlayFragment();
                    }
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.execute();

        Log.i("status", "Main Activity created");

    }

    public static void loadFragment(Fragment frag, int animation) {
        if (frag instanceof PlayingFragment) {
            Bundle state = new Bundle();
            state.putSerializable("currentSong", getCurrentSong());
            state.putInt("currentPos", player.getCurrentPosition());
            state.putBoolean("isPlaying", isPlaying);
            state.putBoolean("isRepeating", isRepeating);
            state.putInt("index", index+1);
            state.putInt("length", playlist.getNumOfSong());
            frag.setArguments(state);
        }

        FragmentTransaction transaction = fm.beginTransaction();

        switch (animation) {
            case 0:
                break;
            case 1:
                transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                break;
            case 2:
                transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case 3:
                transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
        }

        Fragment fragment = fm.findFragmentById(R.id.contentContainer);
        if (fragment == null) {
            transaction.add(R.id.contentContainer, frag);
        } else {
            transaction.replace(R.id.contentContainer, frag);
        }
        transaction.addToBackStack(null);

        transaction.commit();
    }

    public void reloadPlayFragment() {
        loadFragment(new PlayingFragment(), 0);
    }
    public void refreshPlayFragment() {
        Fragment frag = this.getSupportFragmentManager().findFragmentById(R.id.playingFragment);
        Bundle state = new Bundle();
        state.putInt("current_pos", player.getCurrentPosition());
        ((PlayingFragment) frag).updateUI(state);
    }

    private boolean copyMusicToSdCard() {
        String[] files = {"Infatuation - Maroon 5 [MP3 128kbps].mp3",
                "Lost Stars - Adam Levine [MP3 128kbps].mp3",
                "Misery - Maroon 5 [MP3 128kbps].mp3",
                "Stutter - Maroon 5 [MP3 128kbps].mp3"};

        try {
            for (String fileName : files) {
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC), fileName);

                FileOutputStream fos = new FileOutputStream(file);
                InputStream is = getResources().getAssets().open(fileName, Context.MODE_WORLD_READABLE);
                byte buf[] = new byte[1024];
                int numRead;
                while ((numRead = is.read(buf)) > 0) {
                    fos.write(buf, 0, numRead);
                }
                is.close();
                fos.close();
                SongItem si = new SongItem(file);
                playlist.addSong(si);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("music", "Copy to SD Card failed");
            return false;
        }
        Log.i("music", "Copied to SD Card");
        return true;
    }

    private boolean loadSong(SongItem song) {
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        Uri musicURI;
        if(player.isPlaying()){
            pauseMusic();
        }
        try {
            player.reset();
            if (song.isStream()){
                tracer.execute(song.getUrl());
            }
            else {
                player.setDataSource(song.getUrl());
            }
            try {
                player.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
            Log.i("music", "Illegal state exception");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("music", "Song loaded");
        return true;
    }

    public void onPlayClick(View v) {
        ImageButton imgPlay = (ImageButton) v.findViewById(R.id.imgPlay);
        if (player.isPlaying()) {
            pauseMusic();
            isPlaying = false;
            imgPlay.setImageResource(R.drawable.ic_play);
        } else {
            playMusic();
            isPlaying = true;
            imgPlay.setImageResource(R.drawable.ic_pause);
        }
    }

    public void onNextClick(View v) {
        nextSong();
        loadFragment(new PlayingFragment(), 0);
        if (player.isPlaying())
            playMusic();
    }

    public void onPrevClick(View v) {
        prevSong();
        loadFragment(new PlayingFragment(), 0);
        if (player.isPlaying())
            playMusic();
    }

    public void onRepeatClick(View view) {
        isRepeating = !isRepeating;
        player.setLooping(isRepeating);
    }

    public void addSongToPlaylist(SongItem item) {
        playlist.addSong(item);
        index = playlist.getNumOfSong() - 1;
        loadSong(getCurrentSong());
        playMusic();
//        ImageButton imgPlay = (ImageButton) findViewById(R.id.imgPlay);
        isPlaying = true;
//        imgPlay.setImageResource(R.drawable.ic_pause);
    }

    public void progressAdvance(int pos) {
        player.seekTo(pos);
    }

    private void playMusic() {
        player.start();
    }

    private void pauseMusic() {
        player.pause();
    }

    private void nextSong() {
        index++;
        if (index >= playlist.getNumOfSong()) {
            index = index % playlist.getNumOfSong();
        }
        player.reset();
        loadSong(getCurrentSong());
    }

    private void prevSong() {
        index--;
        if (index < 0) {
            index += playlist.getNumOfSong();
        }
        player.reset();
        loadSong(getCurrentSong());
    }

    private static SongItem getCurrentSong() {
        return playlist.getSong(index);
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

class RedirectTracer extends AsyncTask<String, Void, String> {

    private MediaPlayer player;
    private String initialUrl;

    public RedirectTracer(MediaPlayer player){
        this.player = player;
    }
    @Override
    protected String doInBackground(String... strings) {
        initialUrl = strings[0];
        String redirect = null;
        try {
            URL url = new URL(initialUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            redirect = String.valueOf(conn.getURL());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("Tracer", "set data source " + redirect);

        return redirect;
    }

    @Override
    protected void onPostExecute(String s) {
        if(s != null) {
            Log.i("Tracer", "set data source " + s);
            try {
                player.setDataSource(s);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            Log.i("Tracer", "set initial data source " + initialUrl);
            try {
                player.setDataSource(initialUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}