package vn.edu.usth.musicplayer.Model;

import android.content.Context;
import com.android.volley.toolbox.Volley;

/**
 * Created by MacbookPro on 4/12/16.
 */

public class RequestQueue {
    private static com.android.volley.RequestQueue queue = null;
    private RequestQueue() {
    }

    public static com.android.volley.RequestQueue getQueue(Context context) {
        if(queue == null) {
            queue = Volley.newRequestQueue(context);
        }
        return queue;
    }
}
