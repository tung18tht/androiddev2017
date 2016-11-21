package vn.edu.usth.musicplayer.Model;

import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;

import java.io.File;

/**
 * Created by lam on 11/21/2016.
 */

public class SongItem {
    private String url;
    private byte[] art;
    private String album;
    private String artish;
    private String genre;
    private String title;

    public void SongItem(){

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public SongItem(File file) {
        this.url = file.getPath();
        MediaMetadataRetriever mdr  = new MediaMetadataRetriever();
        mdr.setDataSource(url);
        this.art = mdr.getEmbeddedPicture();
        this.album = mdr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
        this.artish = mdr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        this.genre = mdr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE);
        this.title = mdr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public byte[] getArt() {
        return art;
    }

    public void setArt(byte[] art) {
        this.art = art;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtish() {
        return artish;
    }

    public void setArtish(String artish) {
        this.artish = artish;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
