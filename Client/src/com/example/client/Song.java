package com.example.client;

public class Song {
	public final String title;
    public final String album;
    public final int id;
    
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
  
