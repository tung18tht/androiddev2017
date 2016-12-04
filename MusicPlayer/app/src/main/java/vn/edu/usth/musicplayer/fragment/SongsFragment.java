package vn.edu.usth.musicplayer.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import org.json.JSONObject;
import vn.edu.usth.musicplayer.Model.SongAPI;
import vn.edu.usth.musicplayer.R;

import java.util.ArrayList;

import static vn.edu.usth.musicplayer.fragment.SongsFragment.queue;

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
}

class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private ArrayList<JSONObject> data;

    static class ViewHolder extends RecyclerView.ViewHolder {
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
        TextView songTitle = (TextView) holder.songView.findViewById(R.id.songTitle);
        TextView songArtist = (TextView) holder.songView.findViewById(R.id.songArtist);
        TextView songDuration = (TextView) holder.songView.findViewById(R.id.songDuration);

        JSONObject songInfoJSON = data.get(position);
        Object songInfo = Configuration.defaultConfiguration().jsonProvider().parse(songInfoJSON.toString());

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