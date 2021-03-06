package vn.edu.usth.musicplayer.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

import vn.edu.usth.musicplayer.MainActivity;
import vn.edu.usth.musicplayer.Model.Playlist;
import vn.edu.usth.musicplayer.Model.SongItem;
import vn.edu.usth.musicplayer.R;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Locale;

import static vn.edu.usth.musicplayer.MainActivity.downloadTab;
import static vn.edu.usth.musicplayer.MainActivity.downloadingSongs;
import static vn.edu.usth.musicplayer.MainActivity.loadFragment;

public class DownloadFragment extends Fragment {
    static ArrayList<JSONObject> downloading = new ArrayList<JSONObject>();
    static ArrayList<SongItem> downloaded = new ArrayList<SongItem>();

    static Playlist downloadedPlaylist = new Playlist("downloaded");

    static RecyclerView.Adapter downloadingFragmentAdapter;
    static RecyclerView.Adapter downloadedFragmentAdapter;

    static TextView noDownloadText;
    static TextView noDownloadedText;

    public DownloadFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_download, container, false);
    }

    static void download(final JSONObject songDownload, final Context context) {
        try {
            songDownload.put("progress", "0");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        downloading.add(songDownload);
        downloadingFragmentAdapter.notifyDataSetChanged();

        Object downloadingInfo = Configuration.defaultConfiguration().jsonProvider().parse(songDownload.toString());
        final String title = JsonPath.read(downloadingInfo, "$.title");

        AsyncTask<String, Integer, String> download = new AsyncTask<String, Integer, String>() {
            @Override
            protected String doInBackground(String... urls) {
                InputStream input = null;
                OutputStream output = null;
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(urls[0]);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.connect();

                    if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        return "Fail to download " + title;
                    }

                    int fileLength = connection.getContentLength();

                    input = connection.getInputStream();
                    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC), title + ".mp3");
                    output = new FileOutputStream(file);

                    byte data[] = new byte[4096];
                    long total = 0;
                    int count;
                    while ((count = input.read(data)) != -1) {
                        total += count;
                        if (fileLength > 0)
                            publishProgress((int) (total * 100 / fileLength));
                        output.write(data, 0, count);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (output != null)
                            output.close();
                        if (input != null)
                            input.close();
                    } catch (IOException ignored) {
                    }

                    if (connection != null)
                        connection.disconnect();
                }
                return title + " downloaded";
            }

            @Override
            protected void onProgressUpdate(Integer... progress) {
                super.onProgressUpdate(progress);

                noDownloadText.setVisibility(View.GONE);

                try {
                    songDownload.put("progress", progress[0]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                downloadingFragmentAdapter.notifyDataSetChanged();
            }

            @Override
            protected void onPostExecute(String result) {
                if (result != null)
                    Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(context, "Fail to download " + title, Toast.LENGTH_SHORT).show();

                downloading.remove(songDownload);

                if (downloading.isEmpty()) {
                    noDownloadText.setVisibility(View.VISIBLE);
                }

                downloadTab.setBadgeCount(--downloadingSongs);

                downloadingFragmentAdapter.notifyDataSetChanged();
                getDownloadedSongs();
            }
        };

        String source = JsonPath.read(downloadingInfo, "$.sourceDownload");
        download.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, source);
        Log.i("downloadFragment", "Download url: " + source);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        noDownloadText = (TextView) getActivity().findViewById(R.id.noDownloadText);
        noDownloadedText = (TextView) getActivity().findViewById(R.id.noDownloadedText);

        RecyclerView downloadingRecyclerView;
        RecyclerView.LayoutManager downloadingLayoutManager;
        downloadingRecyclerView = (RecyclerView) getActivity().findViewById(R.id.downloadingView);
        downloadingRecyclerView.setHasFixedSize(true);
        downloadingLayoutManager = new LinearLayoutManager(getActivity());
        downloadingRecyclerView.setLayoutManager(downloadingLayoutManager);
        downloadingFragmentAdapter = new DownloadingAdapter(downloading);
        downloadingRecyclerView.setAdapter(downloadingFragmentAdapter);

        RecyclerView downloadedRecyclerView;
        RecyclerView.LayoutManager downloadedLayoutManager;
        downloadedRecyclerView = (RecyclerView) getActivity().findViewById(R.id.downloadedView);
        downloadedRecyclerView.setHasFixedSize(true);
        downloadedLayoutManager = new LinearLayoutManager(getActivity());
        downloadedRecyclerView.setLayoutManager(downloadedLayoutManager);
        downloadedFragmentAdapter = new DownloadedAdapter(downloaded);
        downloadedRecyclerView.setAdapter(downloadedFragmentAdapter);

        getDownloadedSongs();
    }

    private static void getDownloadedSongs() {
        String[] extensions = {"mp3"};
        Collection<File> mp3Files = FileUtils.listFiles(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC), extensions, false);

        downloaded.clear();
        for (File file: mp3Files) {
            SongItem song = new SongItem(file);
            if(song.getTitle() != null) {
                downloaded.add(song);
            }
        }
        downloaded.sort(new Comparator<SongItem>() {
            @Override
            public int compare(SongItem song1, SongItem song2) {
                return song1.getTitle().compareTo(song2.getTitle());
            }
        });

        for (SongItem song: downloaded) {
            downloadedPlaylist.addSong(song);
        }
        downloadedFragmentAdapter.notifyDataSetChanged();
    }

    class DownloadingAdapter extends RecyclerView.Adapter<DownloadingAdapter.ViewHolder> {
        private ArrayList<JSONObject> data;

        class ViewHolder extends RecyclerView.ViewHolder {
            RelativeLayout songView;

            ViewHolder(RelativeLayout v) {
                super(v);
                songView = v;
            }
        }

        DownloadingAdapter(ArrayList<JSONObject> data) {
            this.data = data;
        }

        @Override
        public DownloadingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RelativeLayout v = (RelativeLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.view_download, parent, false);
            return new DownloadingAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final DownloadingAdapter.ViewHolder holder, final int position) {
            TextView downloadingTitle = (TextView) holder.songView.findViewById(R.id.downloadingTitle);
            TextView downloadingPercent = (TextView) holder.songView.findViewById(R.id.downloadingPercent);
            ProgressBar downloadingProgress = (ProgressBar) holder.songView.findViewById(R.id.downloadingProgress);

            final JSONObject downloadingInfoJSON = data.get(position);
            Object downloadingInfo = Configuration.defaultConfiguration().jsonProvider().parse(downloadingInfoJSON.toString());

            final String title = JsonPath.read(downloadingInfo, "$.title");
            downloadingTitle.setText(title);
            String progress = String.valueOf(JsonPath.read(downloadingInfo, "$.progress"));
            downloadingPercent.setText(progress + "%");
            downloadingProgress.setProgress(Integer.valueOf(progress));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    class DownloadedAdapter extends RecyclerView.Adapter<DownloadedAdapter.ViewHolder> {
        private ArrayList<SongItem> data;

        class ViewHolder extends RecyclerView.ViewHolder {
            RelativeLayout songView;

            ViewHolder(RelativeLayout v) {
                super(v);
                songView = v;
            }
        }

        DownloadedAdapter(ArrayList<SongItem> data) {
            this.data = data;
        }

        @Override
        public DownloadedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RelativeLayout v = (RelativeLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.view_song, parent, false);
            return new DownloadedAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final DownloadedAdapter.ViewHolder holder, final int position) {
            ImageView songArtwork = (ImageView) holder.songView.findViewById(R.id.songArtwork);
            TextView songTitle = (TextView) holder.songView.findViewById(R.id.songTitle);
            TextView songArtist = (TextView) holder.songView.findViewById(R.id.songArtist);
            TextView songDuration = (TextView) holder.songView.findViewById(R.id.songDuration);

            final SongItem downloadedSong = data.get(position);

            songArtwork.setImageDrawable(downloadedSong.getArt());
            songTitle.setText(downloadedSong.getTitle());
            songArtist.setText(downloadedSong.getArtist());
            songDuration.setText(formatTime(downloadedSong.getDuration()));

            noDownloadedText.setVisibility(View.GONE);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MainActivity)getActivity()).addSongToPlaylist(downloadedSong);
                    loadFragment(new PlayingFragment(), 2);
                    Log.i("downloadFragment", "Play song: " + downloadedSong.getTitle() + " at " + downloadedSong.getUrl());
                }
            });
        }

        private String formatTime(String second) {
            long d = Long.parseLong(second)/1000;
            if(d>3600) {
                return String.format(Locale.FRANCE,"%01d:%02d:%02d", d/3600, (d/60)%60, d%60);
            }else {
                return String.format("%02d:%02d", (d/60)%60, d%60);
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
}
