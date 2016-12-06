package vn.edu.usth.musicplayer.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.*;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import org.json.JSONException;
import org.json.JSONObject;

import vn.edu.usth.musicplayer.MainActivity;
import vn.edu.usth.musicplayer.Model.SongAPI;
import vn.edu.usth.musicplayer.Model.SongItem;
import vn.edu.usth.musicplayer.R;

import java.io.File;
import java.util.ArrayList;

import static vn.edu.usth.musicplayer.MainActivity.downloadTab;
import static vn.edu.usth.musicplayer.MainActivity.downloadingSongs;
import static vn.edu.usth.musicplayer.fragment.DownloadFragment.download;

public class SongsFragment extends Fragment {
    public static RecyclerView.Adapter songsFragmentAdapter;
    static RequestQueue queue;

    public SongsFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_songs, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        queue = vn.edu.usth.musicplayer.Model.RequestQueue.getQueue(getActivity());

        RecyclerView recyclerView;
        RecyclerView.LayoutManager layoutManager;

        recyclerView = (RecyclerView) getActivity().findViewById(R.id.songsFragment);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        songsFragmentAdapter = new Adapter(SongAPI.getSongs(getActivity()));
        recyclerView.setAdapter(songsFragmentAdapter);
    }

    class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
        private ArrayList<JSONObject> data;

        class ViewHolder extends RecyclerView.ViewHolder {
            RelativeLayout songView;
            ViewHolder(RelativeLayout v) {
                super(v);
                songView = v;
            }
        }

        Adapter(ArrayList<JSONObject> data) {
            this.data = data;
        }

        @Override
        public Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RelativeLayout v = (RelativeLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.view_song, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            final TextView songTitle = (TextView) holder.songView.findViewById(R.id.songTitle);
            TextView songArtist = (TextView) holder.songView.findViewById(R.id.songArtist);
            TextView songDuration = (TextView) holder.songView.findViewById(R.id.songDuration);

            JSONObject songInfoJSON = data.get(position);
            final Object songInfo = Configuration.defaultConfiguration().jsonProvider().parse(songInfoJSON.toString());

            songTitle.setText((String) JsonPath.read(songInfo, "$.title"));
            songArtist.setText((String) JsonPath.read(songInfo, "$.artist"));
            songDuration.setText(formatTime((Integer)JsonPath.read(songInfo, "$.duration")));

            Response.Listener<Bitmap> artworkRequestListener = new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap artwork) {
                    ImageView songArtwork = (ImageView) holder.songView.findViewById(R.id.songArtwork);
                    songArtwork.setImageBitmap(artwork);
                }
            };

            ImageRequest artworkRequest = new ImageRequest((String) JsonPath.read(songInfo, "$.artwork"), artworkRequestListener, 0, 0, ImageView.ScaleType.CENTER, Bitmap.Config.ARGB_8888, null);
            queue.add(artworkRequest);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("songFragment", "Play song: " + songTitle.getText() + JsonPath.read(songInfo, "$.source"));
                    Bundle data = new Bundle();
                    data.putBoolean("isStream", true);
                    data.putString("source","$.sourceDownload");
                    data.putInt("duration", (Integer)JsonPath.read(songInfo, "$.duration"));
                    data.putString("title", (String)JsonPath.read(songInfo, "$.title"));
                    data.putString("artist", (String)JsonPath.read(songInfo, "$.artist"));
                    //put Artwork drawable
                    SongItem item = new SongItem(data);
                    MainActivity activity = (MainActivity) getActivity();
//                    activity.addSongToPlaylist(item);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Log.i("songFragment", "Download song: " + songTitle.getText());

                    JSONObject songDownload = new JSONObject();
                    try {
                        songDownload.put("title", JsonPath.read(songInfo, "$.title"));
                        songDownload.put("sourceDownload", JsonPath.read(songInfo, "$.sourceDownload"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    download(songDownload, getActivity());
                    Toast.makeText(getActivity(), "Downloading " + songTitle.getText(), Toast.LENGTH_SHORT).show();
                    downloadTab.setBadgeCount(++downloadingSongs);
                    return true;
                }
            });
        }

        private String formatTime(Integer second) {
            Integer minutes = (second % 3600) / 60;
            Integer seconds = second % 60;

            return String.format("%02d:%02d", minutes, seconds);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
}

