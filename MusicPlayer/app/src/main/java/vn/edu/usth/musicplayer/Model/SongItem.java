package vn.edu.usth.musicplayer.Model;

import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;

import java.io.ByteArrayInputStream;
import java.io.File;

/**
 * Created by lam on 11/21/2016.
 */

public class SongItem {
    private String url;
    private Drawable art;
    private String album;
    private String artist;
    private String genre;
    private String title;
    private String duration;

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
