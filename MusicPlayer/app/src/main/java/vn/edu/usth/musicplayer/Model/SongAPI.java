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
import java.util.Comparator;

import static vn.edu.usth.musicplayer.fragment.SongsFragment.songsFragmentAdapter;

/**
 * Created by MacbookPro on 4/12/16.
 */

public class SongAPI {
    private static com.android.volley.RequestQueue queue;
    private final static String[] songNames = {
            "Black Beatles",
            "Closer",
            "Starboy",
            "24k Magic",
            "Side To Side",
            "Heathens",
            "Let Me Love You",
            "Broccoli",
            "Don't Wanna Know",
            "Fake Love",
            "Caroline",
            "Starving",
            "I Hate U, I Love U",
            "Scars To Your Beautiful",
            "Cold Water",
            "Treat You Better",
            "The Greatest",
            "Unsteady",
            "Cheap Thrills",
            "Ooouuu",
            "Don't Let Me Down",
            "In The Name Of Love",
            "Blue Ain’t Your Color",
            "Bad Things",
            "This Town",
            "Chill Bill",
            "Ride",
            "All Time Low",
            "May We All",
            "Love On The Brain",
            "Mercy",
            "X Bitch",
            "One Dance",
            "Needed Me",
            "Party Monster",
            "Used To This"

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

    private static void getMP3ZingSongIDAndGetInfo(final String songName) {
        Response.Listener<JSONArray> songIDRequestListener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject songIDObject = null;
                try {
                    if (response == null || response.length() == 0) {
                        return;
                    }
                    songIDObject = response.getJSONObject(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Object document = Configuration.defaultConfiguration().jsonProvider().parse(songIDObject.toString());

                String songID = JsonPath.read(document, "$.SiteId");
                Log.i("songAPI", "Song: " + songName + " - ID:" + songID);

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
                    songInfoObject.put("sourceDownload", JsonPath.read(document, "$.link_download.128"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (!isSongExisted(songInfoObject)) {
                    songs.add(songInfoObject);
                    songs.sort(new Comparator<JSONObject>() {
                        @Override
                        public int compare(JSONObject song1, JSONObject song2) {
                            Object document1 = Configuration.defaultConfiguration().jsonProvider().parse(song1.toString());
                            Object document2 = Configuration.defaultConfiguration().jsonProvider().parse(song2.toString());
                            String song1Title = JsonPath.read(document1, "$.title");
                            String song2Title = JsonPath.read(document2, "$.title");
                            return song1Title.compareTo(song2Title);
                        }
                    });
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
