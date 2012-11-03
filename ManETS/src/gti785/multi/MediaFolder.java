package gti785.multi;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

public class MediaFolder {
	private static List<File> files = new ArrayList<File>();
	private File _folder;
	
	public MediaFolder(File folder){
		_folder = folder;
		for(File file: _folder.listFiles()){
			files.add(file);
		}
	}
	
	public void print(){
		for(File file:files){
			try {
				AudioFile f = AudioFileIO.read(new File(file.toString()));
				Tag tag = f.getTag();
				String id = tag.getFirst(FieldKey.MUSICBRAINZ_TRACK_ID);
				
				System.out.println(file.toString());
				
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
}
