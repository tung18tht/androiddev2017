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

        View view = inflater.inflate(R.layout.fragment_download, container, false);
        buttonDownload = (Button)view.findViewById(R.id.downloadMusicButton);
        buttonDownload.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                downloadManager = (DownloadManager)getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse("http://org3.s1.mp3.zdn.vn/871a49ac2fe8c6b69ff9/6036763354470939079?key=r-YWGIWypTz_FZeaVGri_Q&expires=1480915451&filename=Iridescent%20Linkin%20Park%20Linkin%20Park%20-%20Linkin%20Park.mp3");
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                downloadId = downloadManager.enqueue(request);

//                mProgressBar = (ProgressBar)view.findViewById(R.id.progress_bar1);
////                mProgressBar.setProgress(90);
//                Timer myTimer = new Timer();
//                myTimer.schedule(new TimerTask() {
//                    @Override
//                    public void run() {
//                        DownloadManager.Query q = new DownloadManager.Query();
//                        q.setFilterById(downloadId);
//                        Cursor cursor = downloadManager.query(q);
//                        cursor.moveToFirst();
//                        int bytes_downloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
//                        int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
//                        cursor.close();
//                        final int dl_progress = (bytes_downloaded * 100 / bytes_total);
//                        getActivity().runOnUiThread(new Runnable(){
//                            @Override
//                            public void run(){
//                                mProgressBar.setProgress(dl_progress);
//                            }
//                        });
//
//                    }
//
//                }, 0, 10);
            }
        });

        return view;
    }

    private class DownloadTask extends AsyncTask<String, Integer, Void> {

        ProgressDialog dialog = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
            //set message of the dialog
            dialog.setMessage("Loading...");
            //show dialog
            dialog.show();
            super.onPreExecute();
        }

        protected Void doInBackground(String... args) {
            // do background work here
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // Update download Progress
            super.onProgressUpdate(values);
        }

//        protected void onPostExecute(List<Transaction> result) {
//            // do UI work here
//            if(dialog != null && dialog.isShowing()){
//                dialog.dismiss()
//            }
//        }
    }

}
