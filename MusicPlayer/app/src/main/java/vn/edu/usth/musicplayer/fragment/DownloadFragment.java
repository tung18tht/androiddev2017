package vn.edu.usth.musicplayer.fragment;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import vn.edu.usth.musicplayer.R;

public class DownloadFragment extends Fragment {
    public DownloadFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_download, container, false);

        scanDeviceForMusic();

        TextView songItem1 = (TextView)view.findViewById(R.id.songItem1);
        String song1 = scanDeviceForMusic().get(0).toString();
        songItem1.setText(song1);
        TextView artist1 = (TextView)view.findViewById(R.id.artist1);
        String art1 = scanDeviceForMusic().get(1).toString();
        artist1.setText(art1);

        TextView songItem2 = (TextView)view.findViewById(R.id.songItem2);
        String song2 = scanDeviceForMusic().get(2).toString();
        songItem2.setText(song2);
        TextView artist2 = (TextView)view.findViewById(R.id.artist2);
        String art2 = scanDeviceForMusic().get(3).toString();
        artist2.setText(art2);

        TextView songItem3 = (TextView)view.findViewById(R.id.songItem3);
        String song3 = scanDeviceForMusic().get(4).toString();
        songItem3.setText(song3);
        TextView artist3 = (TextView)view.findViewById(R.id.artist3);
        String art3 = scanDeviceForMusic().get(5).toString();
        artist3.setText(art3);

        TextView songItem4 = (TextView)view.findViewById(R.id.songItem4);
        String song4 = scanDeviceForMusic().get(6).toString();
        songItem4.setText(song4);
        TextView artist4 = (TextView)view.findViewById(R.id.artist4);
        String art4 = scanDeviceForMusic().get(7).toString();
        artist4.setText(art4);

        return view;
    }

    private List<String> scanDeviceForMusic(){
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION
        };
        final String sortOrder = MediaStore.Audio.AudioColumns.TITLE + " COLLATE LOCALIZED ASC";
        List<String> mp3Files = new ArrayList<>();

        Cursor cursor = null;
        try {
            Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            cursor = getActivity().getContentResolver().query(uri, projection, selection, null, sortOrder);
            if( cursor != null){
                cursor.moveToFirst();

                while( !cursor.isAfterLast() ){
                    String title = cursor.getString(0);
                    String artist = cursor.getString(1);
                    String path = cursor.getString(2);
                    String displayName  = cursor.getString(3);
                    String songDuration = cursor.getString(4);
                    cursor.moveToNext();
                    if(path != null && path.endsWith(".mp3")) {
                        mp3Files.add(title);
                        mp3Files.add(artist);
                    }
                }

            }

            // print to see list of mp3 files
            for( String file : mp3Files) {
                Log.i("TAG", file);
            }

        } catch (Exception e) {
            Log.e("TAG", e.toString());
        }finally{
            if( cursor != null){
                cursor.close();
            }
        }
        return mp3Files;
    }
}
