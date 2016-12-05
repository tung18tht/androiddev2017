package vn.edu.usth.musicplayer.fragment;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import vn.edu.usth.musicplayer.MainActivity;
import vn.edu.usth.musicplayer.R;

import static android.content.Context.DOWNLOAD_SERVICE;

public class DownloadFragment extends Fragment {
    public DownloadFragment() {
        super();
    }

    Button buttonDownload;
    DownloadManager downloadManager;
    protected ProgressBar mProgressBar;
    protected long downloadId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_download, container, false);
        buttonDownload = (Button)view.findViewById(R.id.downloadMusicButton);
        buttonDownload.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startDownload();
                progressBar(view);
            }
        });

        return view;
    }

    public void startDownload(){
        downloadManager = (DownloadManager)getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse("http://org3.s1.mp3.zdn.vn/a72057b737f3dead87e2/4129267790077275525?key=AVRfSWxFTTzwuBUvMFWV5Q&expires=1480948723&filename=Numb%20linkin%20park%20Linkin%20Park%20-%20Linkin%20Park.mp3");
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        downloadId = downloadManager.enqueue(request);
    }

    public void progressBar(View view){
        mProgressBar = (ProgressBar)view.findViewById(R.id.progress_bar1);
        Timer myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                DownloadManager.Query q = new DownloadManager.Query();
                q.setFilterById(downloadId);
                Cursor cursor = downloadManager.query(q);
                cursor.moveToFirst();
                int bytes_downloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                cursor.close();
                final int dl_progress = (bytes_downloaded * 100 / bytes_total);
                getActivity().runOnUiThread(new Runnable(){
                    @Override
                    public void run(){
                        mProgressBar.setProgress(dl_progress);
                    }
                });

            }

        }, 0, 10);
    }
}
