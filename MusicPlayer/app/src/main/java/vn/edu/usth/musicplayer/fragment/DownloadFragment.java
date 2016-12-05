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

        scanDeviceForMpFiles();
        TextView songItem = (TextView)view.findViewById(R.id.songItem1);

        for (int i = 0 ; i < scanDeviceForMpFiles().size() ; i++){
            String song1 = scanDeviceForMpFiles().get(i).toString();
            songItem.setText(song1);
        }

        return view;
    }

    private List<String> scanDeviceForMpFiles(){
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
