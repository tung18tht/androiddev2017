package vn.edu.usth.musicplayer.Model;

import android.content.Context;
import android.util.Log;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static vn.edu.usth.musicplayer.fragment.SongsFragment.songsFragmentAdapter;

/**
 * Created by MacbookPro on 4/12/16.
 */

public class SongAPI {
    private static com.android.volley.RequestQueue queue;
    private final static String[] songNames = {
            "Hello",
            "Grenade",
            "Everyday",
            "Greedy",
            "Moonlight",
            "It will rain",
            "Not afraid",
            "Love the Way You Lie"
    };
    private static ArrayList<JSONObject> songs = new ArrayList<>();

    private SongAPI() {
    }

    public static ArrayList<JSONObject> getSongs(Context context) {
        if(songs.isEmpty()) {
            queue = RequestQueue.getQueue(context);
            getSongsFromZingAPI();
        }
        return songs;
    }

    private static void getSongsFromZingAPI() {
        for (String songName: songNames) {
            getMP3ZingSongIDAndGetInfo(songName);
        }
    }

    private static void getMP3ZingSongIDAndGetInfo(String songName) {
        Response.Listener<JSONArray> songIDRequestListener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject songIDObject = null;
                try {
                    songIDObject = response.getJSONObject(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Object document = Configuration.defaultConfiguration().jsonProvider().parse(songIDObject.toString());

                String songID = JsonPath.read(document, "$.SiteId");
                Log.i("songAPI", "SongID: " + songID);

                getMP3ZingSongInfo(songID);
            }
        };

        String url = "http://j.ginggong.com/jOut.ashx?h=mp3.zing.vn&code=77076dc6-6a6b-4f70-bbad-7888bb59cc7d&k=" + songName;
        JsonArrayRequest songIDRequest = new JsonArrayRequest(url, songIDRequestListener, null);
        queue.add(songIDRequest);
    }

    private static void getMP3ZingSongInfo(String songID) {
        Response.Listener<JSONObject> songInfoRequestListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Object document = Configuration.defaultConfiguration().jsonProvider().parse(response.toString());
                JSONObject songInfoObject = new JSONObject();

                try {
                    songInfoObject.put("title", JsonPath.read(document, "$.title"));
                    songInfoObject.put("artist", JsonPath.read(document, "$.artist"));
                    songInfoObject.put("duration", JsonPath.read(document, "$.duration"));
                    songInfoObject.put("artwork", "http://image.mp3.zdn.vn//thumb/240_240/" +  JsonPath.read(document, "$.thumbnail"));
                    songInfoObject.put("source", JsonPath.read(document, "$.source.128"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (!isSongExisted(songInfoObject)) {
                    songs.add(songInfoObject);
                    songsFragmentAdapter.notifyDataSetChanged();
                }
            }

            private boolean isSongExisted(JSONObject newSong) {
                for (JSONObject song: songs) {
                    Object newSongDocument = Configuration.defaultConfiguration().jsonProvider().parse(newSong.toString());
                    Object songDocument = Configuration.defaultConfiguration().jsonProvider().parse(song.toString());
                    String songSrc = JsonPath.read(songDocument, "$.source");
                    String newSongSrc = JsonPath.read(newSongDocument, "$.source");
                    if (songSrc.compareTo(newSongSrc) == 0) {
                        return true;
                    }
                }
                return false;
            }
        };

        String url = "http://api.mp3.zing.vn/api/mobile/song/getsonginfo?requestdata={\"id\":\"" + songID + "\"}";
        JsonObjectRequest songInfoRequest = new JsonObjectRequest(url, null, songInfoRequestListener, null);
        queue.add(songInfoRequest);
    }
}
