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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import org.json.JSONException;
import org.json.JSONObject;
import vn.edu.usth.musicplayer.R;

import java.util.ArrayList;

public class DownloadFragment extends Fragment {
    static ArrayList<JSONObject> downloading = new ArrayList<JSONObject>();
    static RecyclerView.Adapter downloadFragmentAdapter;

    public DownloadFragment() {
        super();
    }
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_download, container, false);
    }

    static void download(JSONObject songDownload) {
        try {
            songDownload.put("progress", "0");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        downloading.add(songDownload);
        downloadFragmentAdapter.notifyDataSetChanged();
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

        downloadFragmentAdapter = new Adapter(downloading);
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
            RelativeLayout v = (RelativeLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.view_download, parent, false);
            return new DownloadFragment.Adapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final DownloadFragment.Adapter.ViewHolder holder, int position) {
            TextView downloadingTitle = (TextView) holder.songView.findViewById(R.id.downloadingTitle);
            TextView downloadingPercent = (TextView) holder.songView.findViewById(R.id.downloadingPercent);
            ProgressBar downloadingProgress = (ProgressBar) holder.songView.findViewById(R.id.downloadingProgress);

            JSONObject downloadingInfoJSON = data.get(position);
            Object downloadingInfo = Configuration.defaultConfiguration().jsonProvider().parse(downloadingInfoJSON.toString());

            downloadingTitle.setText((String) JsonPath.read(downloadingInfo, "$.title"));
            String progress = JsonPath.read(downloadingInfo, "$.progress");
            downloadingPercent.setText(progress + "%");
            downloadingProgress.setProgress(Integer.valueOf(progress));

            String source = JsonPath.read(downloadingInfo, "$.sourceDownload");

//            AsyncTask<String, Integer, File> download = new AsyncTask<String, Integer, File>() {
//                @Override
//                protected File doInBackground(String... strings) {
//                    return null;
//                }
//            };
//
//            download.execute(sourceDownload);
            Log.i("downloadFragment", "Download url: " + source);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
}
