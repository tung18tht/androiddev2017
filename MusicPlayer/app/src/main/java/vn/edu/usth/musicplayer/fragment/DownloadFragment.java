package vn.edu.usth.musicplayer.fragment;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import vn.edu.usth.musicplayer.MainActivity;
import vn.edu.usth.musicplayer.R;

import static android.content.Context.DOWNLOAD_SERVICE;

public class DownloadFragment extends Fragment {
    public DownloadFragment() {
        super();
    }

    Button buttonDownload;
    DownloadManager downloadManager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_download, container, false);
        buttonDownload = (Button)view.findViewById(R.id.downloadMusicButton);
        buttonDownload.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                downloadManager = (DownloadManager)getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse("http://s1mp3.hot1.cache31.vcdn.vn/985969740630ef6eb621/1972237400245003279?key=wiL044SMvU54ZVfg43hjbg&expires=1480740142&filename=Linkin%20Park%20-%20Linkin%20Park.mp3");
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                Long reference = downloadManager.enqueue(request);
            }
        });

        return view;
    }

}
