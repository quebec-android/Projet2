package gti785.multi;

public class Media {
	private int songID;
	private String title;
	private String album;
	private String length;
	private String mrl;
	
	public Media(int songID, String title, String album, String length, String mrl){
		this.songID = songID;
		this.title=title;
		this.album = album;
		this.length = length;
		this.mrl = mrl;
	}

	public int getSongID() {
		return songID;
	}

	public void setSongID(int songID) {
		this.songID = songID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getMrl() {
		return mrl;
	}

	public void setMrl(String mrl) {
		this.mrl = mrl;
	}
	
	
}
