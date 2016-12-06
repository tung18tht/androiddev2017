package vn.edu.usth.musicplayer.Model;

import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by lam on 11/21/2016.
 */

public class SongItem implements Serializable {
    private String url;
    private Drawable art;
    private String album = "Unknown Album";
    private String artist = "VA";
    private String genre = "Unknown";
    private String title;
    private String duration;
    private boolean isStream = false;

    public void SongItem(){

    }
    public SongItem(File file) {
        this.url = file.getPath();
        MediaMetadataRetriever mdr  = new MediaMetadataRetriever();
        mdr.setDataSource(url);
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(mdr.getEmbeddedPicture());
            this.art = Drawable.createFromStream(bis, "art");
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.album = mdr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
        this.artist = mdr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        this.genre = mdr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE);
        this.title = mdr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        this.duration = mdr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
    }

    public SongItem(String fileUrl) {
        this.url = fileUrl;
        MediaMetadataRetriever mdr  = new MediaMetadataRetriever();
        mdr.setDataSource(url, new HashMap<String, String>());
        //ByteArrayInputStream bis = new ByteArrayInputStream(mdr.getEmbeddedPicture());
        //this.art = Drawable.createFromStream(bis, "art");
        this.album = mdr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
        this.artist = mdr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        this.genre = mdr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE);
        this.title = mdr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        this.duration = mdr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
    }

    public SongItem(Bundle data) {
        this.url = data.getString("source");
        this.artist = data.getString("artist");
        this.title = data.getString("title");
        this.duration = Integer.toString(data.getInt("duration"));
        this.isStream = data.getBoolean("isStream");
    }

    public String getTitle() {
        return title;
    }

    public String getDuration() {
        return duration;
    }

    public String getUrl() {
        return url;
    }

    public Drawable getArt() {
        return art;
    }

    public boolean isStream() {return isStream;}

    public String getAlbum() {
        return album;
    }

    public String getArtist() {
        return artist;
    }

    public String getGenre() {
        return genre;
    }
}
