package vn.edu.usth.musicplayer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import org.json.JSONArray;
import vn.edu.usth.musicplayer.R;

public class SongsFragment extends Fragment {
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

        RecyclerView recyclerView;
        RecyclerView.Adapter adapter;
        RecyclerView.LayoutManager layoutManager;

        recyclerView = (RecyclerView) getActivity().findViewById(R.id.songsFragment);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        JSONArray data = new JSONArray();
        data.put("LMAO");
        data.put("LMAO");
        data.put("LMAO");
        adapter = new Adapter(data);
        recyclerView.setAdapter(adapter);
    }
}

class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private JSONArray data;

    static class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout songView;
        ViewHolder(RelativeLayout v) {
            super(v);
            songView = v;
        }
    }

    Adapter(JSONArray data) {
        this.data = data;
    }

    @Override
    public Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RelativeLayout v = (RelativeLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.view_song, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        holder.textView.setText(data[position]);
    }

    @Override
    public int getItemCount() {
        return data.length();
    }
}