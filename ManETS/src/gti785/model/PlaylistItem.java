package gti785.model;

public class PlaylistItem {
	private int SongPlayListID = 0;
	private int songID = 0;

	public PlaylistItem(int songPlayListID, int songID) {
		SongPlayListID = songPlayListID;
		this.songID = songID;
	}

	public int getSongPlayListID() {
		return SongPlayListID;
	}

	public void setSongPlayListID(int songPlayListID) {
		SongPlayListID = songPlayListID;
	}

	public int getSongID() {
		return songID;
	}

	public void setSongID(int songID) {
		this.songID = songID;
	}
	
	public void decrementPlaylistID(){
		SongPlayListID--;
	}
}
