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

        return inflater.inflate(R.layout.fragment_download, container, false);
    }

}
