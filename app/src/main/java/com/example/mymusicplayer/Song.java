package com.example.mymusicplayer;

import java.io.Serializable;

public class Song implements Serializable {

    private String songLink;
    private String songImagePath;
    private String songName;
    private String artistName;

    public Song() {
    }

    public Song(String songLink, String songImagePath, String songName, String artistName) {
        this.songLink = songLink;
        this.songImagePath = songImagePath;
        this.songName = songName;
        this.artistName = artistName;
    }

    public String getSongLink() {
        return songLink;
    }

    public String getSongImagePath() {
        return songImagePath;
    }

    public String getSongName() {
        return songName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setSongLink(String songLink) {
        this.songLink = songLink;
    }

    public void setSongImagePath(String songImagePath) {
        this.songImagePath = songImagePath;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }
}
