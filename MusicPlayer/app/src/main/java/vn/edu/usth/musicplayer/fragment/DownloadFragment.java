package vn.edu.usth.musicplayer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import org.json.JSONObject;
import vn.edu.usth.musicplayer.R;

import java.util.ArrayList;

public class DownloadFragment extends Fragment {
    public DownloadFragment() {
        super();
    }
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_download, container, false);
    }

    static void download(String source) {
        Log.i("downloadFragment", "Download url: " + source);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RecyclerView recyclerView;
        RecyclerView.LayoutManager layoutManager;

        recyclerView = (RecyclerView) getActivity().findViewById(R.id.downloadingView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        RecyclerView.Adapter downloadFragmentAdapter = new DownloadFragment.Adapter(new ArrayList<JSONObject>());
        recyclerView.setAdapter(downloadFragmentAdapter);
    }

    class Adapter extends RecyclerView.Adapter<DownloadFragment.Adapter.ViewHolder> {
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
        public DownloadFragment.Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RelativeLayout v = (RelativeLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.view_song, parent, false);
            return new DownloadFragment.Adapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final DownloadFragment.Adapter.ViewHolder holder, int position) {
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
}
