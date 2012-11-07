package gti785.multi;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

public class MediaFolder {
	private static Map<Integer,File> files = new HashMap<Integer,File>();
	private File _folder;
	
	public MediaFolder(File folder){
		_folder = folder;
		int i = 1;
		for(File file: _folder.listFiles()){
			files.put(i,file);
			i++;
		}
	}
	
	public void print(HttpServletResponse response){
		for(int mapKey: files.keySet()){
			try {
				AudioFile f = AudioFileIO.read(new File(files.get(mapKey).toString()));
				
				Tag tag = f.getTag();
				String artist = tag.getFirst(FieldKey.ARTIST);
				String album = tag.getFirst(FieldKey.ALBUM);
				String title = tag.getFirst(FieldKey.TITLE);
				String composer = tag.getFirst(FieldKey.COMPOSER);
				String genre = tag.getFirst(FieldKey.GENRE);
				
				//System.out.println("Album: "+album+" Artist: "+artist+" Title: "+title);
				response.getWriter().write("Album: "+album+" Artist: "+artist+" Title: "+title);
			} catch (CannotReadException e) {
				System.out.println("Impossible de lire le fichier");
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("Erreur dans la lecture du fichier");
				e.printStackTrace();
			} catch (TagException e) {
				System.out.println("Erreur dans le tag du fichier");
				e.printStackTrace();
			} catch (ReadOnlyFileException e) {
				System.out.println("Erreur: fichier en lecture seule.");
				e.printStackTrace();
			} catch (InvalidAudioFrameException e) {
				System.out.println("Frame audio invalide.");
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * GETTERS and SETTERS
	 * 
	 * @return
	 */
	public static Map<Integer, File> getFiles() {
		return files;
	}

	public static void setFiles(Map<Integer, File> files) {
		MediaFolder.files = files;
	}
}
