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
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import com.roughike.bottombar.BottomBar;
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

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {
    Playlist playlist;
    int index = 0;
    MediaPlayer player;
    boolean isPlaying = false;
    boolean isRepeating = false;
    int currentFrag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playlist = new Playlist("default");

        copyMusicToSdCard();
        player = new MediaPlayer();

//        loadSong(getCurrentSong());

        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.tab_home:
                        loadFragment(new SongsFragment());
                        currentFrag = 0;
                        break;

                    case R.id.tab_playing:
                        loadFragment(new PlayingFragment());
                        currentFrag = 1;
                        break;

                    case R.id.tab_download:
                        loadFragment(new DownloadFragment());
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
                        //reloadPlayFragment();
                        refreshPlayFragment();
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

    private void loadFragment(Fragment frag) {
        if (frag instanceof PlayingFragment) {
            Bundle state = new Bundle();
            state.putString("currentSongURL", getCurrentSong().getUrl());
            state.putInt("currentPos", player.getCurrentPosition());
            state.putBoolean("isPlaying", isPlaying);
            state.putBoolean("isRepeating", isRepeating);
            frag.setArguments(state);
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.contentContainer);
        if (fragment == null) {
            transaction.add(R.id.contentContainer, frag);
        } else {
            transaction.replace(R.id.contentContainer, frag);
        }
        transaction.addToBackStack(null);

        transaction.commit();
    }

    public void reloadPlayFragment() {
        loadFragment(new PlayingFragment());
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
        Uri musicURI = Uri.parse(song.getUrl());
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            player.setDataSource(getApplicationContext(), musicURI);
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("music", "Song has failed to load");
            return false;
        }
        Log.i("music", "Song loaded");
        return true;
    }

    public void onPlayClick(View v) {
        ImageButton imgPlay = (ImageButton) v.findViewById(R.id.imgPlay);
        if (isPlaying) {
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
        loadFragment(new PlayingFragment());
        if (isPlaying)
            player.start();
    }

    public void onPrevClick(View v) {
        prevSong();
        loadFragment(new PlayingFragment());
        if (isPlaying)
            player.start();
    }

    public void onRepeatClick(View view) {
        isRepeating = !isRepeating;
        player.setLooping(isRepeating);
    }

    public void addSongToPlaylist(SongItem item) {
        playlist.addSong(item);
        index = playlist.getNumOfSong() - 1;
        //loadSong(getCurrentSong());
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

    private SongItem getCurrentSong() {
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
