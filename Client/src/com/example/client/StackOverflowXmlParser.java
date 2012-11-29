package com.example.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

public class StackOverflowXmlParser {
	private static final String ns = null;
	private List<Song> songs = null;

	public StackOverflowXmlParser( List<Song> songs) {
		this.songs = songs;
	} 
	
	public StackOverflowXmlParser() {
	}

	public String parseToString(InputStream in) throws XmlPullParserException, IOException {
		try {
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(in, null);
			parser.nextTag();
	        String name = parser.getName();
	        if (name.equals("port")) {
        		return readElement(parser);
	        }
		} finally {
			in.close();
		}
		return null;
	}
	
	public List<Song> parse(InputStream in) throws XmlPullParserException, IOException {
		try {
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(in, null);
			parser.nextTag();
			return readFeed(parser);
		} finally {
			in.close();
		}
	}
	
	private List<Song> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
	    List<Song> song = new ArrayList<Song>();

    	parser.require(XmlPullParser.START_TAG, ns, "list");
	    while (parser.next() != XmlPullParser.END_TAG) {
	        if (parser.getEventType() != XmlPullParser.START_TAG) {
	            continue;
	        }
	        String name = parser.getName();
	        if (name.equals("song")) {
	        	// Starts by looking for the entry tag
        		song.add(readEntry(parser));
	        } else if (name.equals("playlist")) {
        		song.add(readEntryPlaylist(parser));
	        } else {
        		skip(parser);
	        }
	    }  
	    return song;
	}
	
	// Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them off
	// to their respective "read" methods for processing. Otherwise, skips the tag.
	private Song readEntryPlaylist(XmlPullParser parser) throws XmlPullParserException, IOException {
		int playlistID = 0;
		Song s = null;
		
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals("SongPlayListID")) {
				playlistID = Integer.parseInt( readElement(parser));
			} else if (name.equals("songID")) {
				s = new Song(Utils.findSongById(songs,Integer.parseInt(readElement(parser))));
				s.setId(playlistID);
			} else {
				skip(parser);
			}
		}
		return s;
		
	}
	
	// Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them off
	// to their respective "read" methods for processing. Otherwise, skips the tag.
	private Song readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
		String title = null;
		String album = null;
		int id = 0;
		int length = 0;
		String url = null;
		
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals("title")) {
				title = readElement(parser);
			} else if (name.equals("album")) {
				album = readElement(parser);
			} else if (name.equals("songID")){
				id = Integer.parseInt(readElement(parser));
			} else if (name.equals("length")){
				length = Integer.parseInt(readElement(parser));
			} else if (name.equals("poster")){
				url = readElement(parser);
			} else {
				skip(parser);
			}
		}
		return new Song(title, album, id, length, url);
	}

	// For the tags title and summary, extracts their text values.
	private String readElement(XmlPullParser parser) throws IOException, XmlPullParserException {
		String result = "";
		if (parser.next() == XmlPullParser.TEXT) {
			result = parser.getText();
			parser.nextTag();
		}
		return result;
	}
	
	private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
	    if (parser.getEventType() != XmlPullParser.START_TAG) {
	        throw new IllegalStateException();
	    }
	    int depth = 1;
	    while (depth != 0) {
	        switch (parser.next()) {
	        case XmlPullParser.END_TAG:
	            depth--;
	            break;
	        case XmlPullParser.START_TAG:
	            depth++;
	            break;
	        }
	    }
	 }
}
