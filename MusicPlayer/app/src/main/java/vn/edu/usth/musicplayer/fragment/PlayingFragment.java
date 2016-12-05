package vn.edu.usth.musicplayer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import vn.edu.usth.musicplayer.MainActivity;
import vn.edu.usth.musicplayer.Model.SongItem;
import vn.edu.usth.musicplayer.R;

import java.io.File;
import java.util.Locale;

public class PlayingFragment extends Fragment {

    SongItem si;
    public PlayingFragment(){super();}
//    public PlayingFragment(SongItem si) {
//        super();
//        this.si = si;
//    }


    ImageView albumArt;
    TextView title;
    TextView album;
    TextView artist;
    TextView trackLength;
    ImageButton imgPlay;
    SeekBar prog;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final Bundle data = getArguments();
        String si_url = data.getString("currentSongURL");
        File file = new File(si_url);
        si = new SongItem(file);
        final MainActivity parent_activity = (MainActivity)getActivity();
        final View view = inflater.inflate(R.layout.fragment_playing, container, false);

        imgPlay = (ImageButton) view.findViewById(R.id.imgPlay);
        if (data.getBoolean("isPlaying")){
            imgPlay.setImageResource(R.drawable.ic_pause);
        }
        else {
            imgPlay.setImageResource(R.drawable.ic_play);
        }
        albumArt = (ImageView) view.findViewById(R.id.albumImage);
        albumArt.setImageDrawable(si.getArt());
        title = (TextView) view.findViewById(R.id.title);
        title.setText(si.getTitle());
        album = (TextView) view.findViewById(R.id.album);
        album.setText(si.getAlbum());
        artist = (TextView) view.findViewById(R.id.artist);
        artist.setText(si.getArtist());
        trackLength = (TextView) view.findViewById(R.id.track_length);
        trackLength.setText(durationToLength(si.getDuration()));
        final TextView current_pos = (TextView) view.findViewById(R.id.current_pos);
        current_pos.setText(durationToLength(data.getInt("currentPos")));
        prog = (SeekBar) view.findViewById(R.id.prog_bar);
        prog.setMax(Integer.parseInt(si.getDuration()));
        prog.setProgress(data.getInt("currentPos"));
        prog.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(b){
                    parent_activity.progressAdvance(i);
//                    parent_activity.reloadPlayFragment();
                    updateCurrentPos(view, i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                view.findViewById(R.id.imgPlay).callOnClick();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                view.findViewById(R.id.imgPlay).callOnClick();
            }
        });

        return view;
    }

    public void updateUI(Bundle state){
        View view = getView();
        updateCurrentPos(view, state.getInt("current_pos"));
        Log.i("update", "Current Position: "+state.getInt("current_pos"));
    }

    private void updateCurrentPos(View view, int pos){
        TextView current_pos = (TextView) view.findViewById(R.id.current_pos);
        current_pos.setText(durationToLength(pos));
        SeekBar prog = (SeekBar) view.findViewById(R.id.prog_bar);
        prog.setProgress(pos);
    }

    private String durationToLength(String duration){
        long d = Long.parseLong(duration)/1000;
        if(d>3600) {
            return String.format(Locale.FRANCE,"%01d:%02d:%02d", d/3600, (d/60)%60, d%60);
        }else {
            return String.format("%02d:%02d", (d/60)%60, d%60);
        }
    }

    private String durationToLength(int duration){
        duration/=1000;
        if(duration>3600) {
            return String.format("%01d:%02d:%02d", duration/3600, (duration/60)%60, duration%60);
        }else {
            return String.format("%02d:%02d", (duration/60)%60, duration%60);
        }
    }
}
