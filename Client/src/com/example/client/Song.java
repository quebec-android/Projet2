package com.example.client;

public class Song {
	public String title;
    public String album;
    public int id;
    
    public Song(Song song){
    	this.title = song.getTitle();
        this.album = song.getAlbum();
        this.id = song.getId();
    }
    public Song(String title, String album, int id) {
        this.title = title;
        this.album = album;
        this.id = id;
    }

	public String getTitle() {
		return title;
	}

	public String getAlbum() {
		return album;
	}

	public int getId() {
		return id;
	}

    
    
}
  
