package gti785.multi;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
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
	private static Map<Integer,Media> files = new HashMap<Integer,Media>();
	private File _folder;
	
	public MediaFolder(File folder){
		_folder = folder;
		int i = 1;
		try {
			
			for(File file: _folder.listFiles()){
				AudioFile f;
				
				f = AudioFileIO.read(new File(file.toString()));
				Tag tag = f.getTag();
				String album = tag.getFirst(FieldKey.ALBUM);
				String title = tag.getFirst(FieldKey.TITLE);
				String length = null;
				String mrl = file.toString();
				
				Media media = new Media(i, title, album, length, mrl);
				files.put(i,media);
				i++;
			}
		
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
	
	public void print(HttpServletResponse response){
		for(int mapKey: files.keySet()){
			Media media = files.get(mapKey);
			//System.out.println("Album: "+album+" Artist: "+artist+" Title: "+title);
			try {
				response.getWriter().write("Album: "+media.getAlbum()+" Artist: "+media.getTitle());
			} catch (IOException e) {
				System.out.println("Error while writing file list");
				e.printStackTrace();
			}
			
		}
	}
	
	/**
	 * GETTERS and SETTERS
	 * 
	 * @return
	 */

	public static Map<Integer, Media> getFiles() {
		return files;
	}

	public static void setFiles(Map<Integer, Media> files) {
		MediaFolder.files = files;
	}
}
