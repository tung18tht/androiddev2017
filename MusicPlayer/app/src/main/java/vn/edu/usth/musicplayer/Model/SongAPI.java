package vn.edu.usth.musicplayer.Model;

import android.content.Context;
import android.util.Log;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.jayway.jsonpath.JsonPath;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by MacbookPro on 4/12/16.
 */

public class SongAPI {
    private static com.android.volley.RequestQueue queue;
    private final static String[] songNames = {"Hello", "Grenade"};
    private static ArrayList<JSONObject> songs = null;

    private SongAPI() {
    }

    public static ArrayList<JSONObject> getSongs(Context context) {
        if(songs == null) {
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
        Response.Listener<JSONObject> songIDRequestListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String songID = JsonPath.read(response, "$[0]");
                Log.i("songAPI", "SongId: " + songID);
//                getMP3ZingSongInfo(songID);
            }
        };

        String url = "http://j.ginggong.com/jOut.ashx?h=mp3.zing.vn&code=77076dc6-6a6b-4f70-bbad-7888bb59cc7d&k=" + songName;
        JsonObjectRequest songIDRequest = new JsonObjectRequest(url, null, songIDRequestListener, null);
        queue.add(songIDRequest);
    }

    private static void getMP3ZingSongInfo(String songID) {
        Response.Listener<JSONObject> songInfoRequestListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                songs.add(response);
            }
        };

        String url = "http://api.mp3.zing.vn/api/mobile/song/getsonginfo?requestdata={\"id\":\"" + songID + "\"}";
        JsonObjectRequest songInfoRequest = new JsonObjectRequest(url, null, songInfoRequestListener, null);
        queue.add(songInfoRequest);
    }
}
