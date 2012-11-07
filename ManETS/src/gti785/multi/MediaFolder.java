package gti785.multi;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * Le MediaFolder permet de g屍er les m仕ias du dossier multim仕ia.
 * 
 * @author Cedric
 *
 */
public class MediaFolder {
	//private static Map<Integer,Media> files = new HashMap<Integer,Media>();
	private static List<Media> files = new ArrayList();
	private File _folder;
	private static final XStream xstream = new XStream(new DomDriver());
	private ArtworkFolder artwork;
	
	// Configuration de XStream
	static {
		xstream.setMode(XStream.NO_REFERENCES);
		xstream.alias("song", Media.class);
		xstream.alias("list", List.class);
	}
	
	/**
	 * Constructeur, remplit la liste files avec tous les m仕ias du dossier
	 * multim仕ia.
	 * 
	 * @param folder
	 */
	public MediaFolder(File folder, ArtworkFolder artwork){
		_folder = folder;
		this.artwork = artwork;
		
		int i = 1;
		try {
			
			for(File file: _folder.listFiles()){
				AudioFile f;
				
				f = AudioFileIO.read(new File(file.toString()));
				Tag tag = f.getTag();
				String album = tag.getFirst(FieldKey.ALBUM);
				String poster = i+".png";
				String length = null;
				String mrl = file.toString();
				String[] title = mrl.split("/");
				
				BufferedImage img = (BufferedImage)tag.getFirstArtwork().getImage();
				artwork.saveToFolder(img, String .valueOf(i));
				
				Media media = new Media(i, title[title.length-1], album, length, mrl, poster);
				//files.put(i,media);
				files.add(media);
				i++;
			}
			
			System.out.println("test:" + files.get(0).getTitle());
		
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
	
	/**
	 * Envoi la liste des m仕ias sous forme XML.
	 * @param response
	 */
	public void print(HttpServletResponse response){
		
		//for(int mapKey: files.keySet()){
			//Media media = files.get(mapKey);
			//System.out.println("Album: "+album+" Artist: "+artist+" Title: "+title);
			
			try {				
				PrintWriter out = response.getWriter();
				out.write(xstream.toXML(files));
			} catch (IOException e) {
				System.out.println("Error while writing file list");
				e.printStackTrace();
			}
			
		//}
	}
	
	/**
	 * GETTERS and SETTERS
	 * 
	 * @return
	 */

	public static List<Media> getFiles() {
		return files;
	}

	public static void setFiles(List<Media> files) {
		MediaFolder.files = files;
	}
}
