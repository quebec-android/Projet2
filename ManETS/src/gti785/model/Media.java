package gti785.model;

/**
 * La classe Media permet de stocker une chanson avec toutes les caractéristiques
 * utilisées.
 * @author Cedric
 *
 */
public class Media {
	private int songID;
	private String title;
	private String album;
	private int length;
	private String mrl;
	private String poster;
	
	/**
	 * Constructeur par défaut
	 * @param songID
	 * @param title
	 * @param album
	 * @param length
	 * @param mrl
	 * @param poster
	 */
	public Media(int songID, String title, String album, int length, String mrl, String poster){
		this.songID = songID;
		this.title=title;
		this.album = album;
		this.length = length;
		this.mrl = mrl;
		this.poster = poster;
	}

	/**
	 * GETTERS and SETTERS
	 * @return
	 */
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

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public String getMrl() {
		return mrl;
	}

	public void setMrl(String mrl) {
		this.mrl = mrl;
	}

	public String getPoster() {
		return poster;
	}

	public void setPoster(String poster) {
		this.poster = poster;
	}
	
	
}
