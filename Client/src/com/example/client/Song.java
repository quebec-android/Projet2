package com.example.client;

public class Song {
	private String title;
	private String url;
	private String album;
    private int id;
    private int length;
    
    public Song(Song song){
    	this.url = song.getUrl();
        this.album = song.getAlbum();
        this.id = song.getId();
        this.title=song.getTitle();
        if (title.lastIndexOf("\\")!=-1) {
        	this.title = this.title.substring(url.lastIndexOf("\\"));
        }
        this.length = song.getLength();
    }
    
	public Song(String title, String album, int id, int length, String url) {
        this.url = url;
        this.album = album;
        this.id = id;
        this.title = title;
        if (title.lastIndexOf("\\")!=-1) {
        	this.title = this.title.substring(url.lastIndexOf("\\")+1);
        }
        this.length = length;
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
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}   
}
  
