package vn.edu.usth.musicplayer.Model;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by lam on 11/21/2016.
 */

public class Playlist {
    private String name;
    private ArrayList<SongItem> list;

    public Playlist(String name) {
        this.name = name;
        this.list = new ArrayList<>();
    }

    public void addSong(SongItem song){
        this.list.add(song);
    }

    public void removeSong(int index){
        this.list.remove(index);
    }

    public void removeSong(SongItem song){
        this.list.remove(song);
    }

    public SongItem getSong(int index){
        return this.list.get(index);
    }

    public ArrayList<String> getAllTrackName(){
        ArrayList titles = new ArrayList();
        for (SongItem song: list) {
            titles.add(song.getTitle());
        }
        return titles;
    }

    public int getNumOfSong() {
        return list.size();
    }
}
