package com.example.client;

public class Song {
	public String title;
	public String url;
    public String album;
    public int id;
    
    public Song(Song song){
    	this.url = song.getTitle();
        this.album = song.getAlbum();
        this.id = song.getId();
        this.title=this.url;
        if (url.lastIndexOf("\\")!=-1) {
        	this.title = this.title.substring(url.lastIndexOf("\\"));
        }
    }
    public Song(String title, String album, int id) {
        this.url = title;
        this.album = album;
        this.id = id;
        this.title=this.url;
        if (url.lastIndexOf("\\")!=-1) {
        	this.title = this.title.substring(url.lastIndexOf("\\")+1);
        }
    }

	public Song() {
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
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setAlbum(String album) {
		this.album = album;
	}
	public void setId(int id) {
		this.id = id;
	}

    
    
}
  
