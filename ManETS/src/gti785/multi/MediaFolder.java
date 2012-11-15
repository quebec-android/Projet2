package gti785.multi;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
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

import com.thoughtworks.xstream.XStream;

/**
 * Le MediaFolder permet de gérer les médias du dossier multimédia.
 * 
 * @author Cedric
 *
 */
public class MediaFolder {
	private static Map<Integer,Media> filesh = new HashMap<Integer,Media>();
	private static List<Media> files = new ArrayList();
	private File _folder;
	private ArtworkFolder artwork;
	
	/**
	 * Constructeur, remplit la liste files avec tous les médias du dossier
	 * multimédia.
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
				String poster = "http://localhost:8080/ManETS/Artwork/"+album+".png";
				String length = null;
				String mrl = file.toString();
				String[] title = mrl.split("/");
				
				if(!artwork.imageExist(album)){
					BufferedImage img = (BufferedImage)tag.getFirstArtwork().getImage();
					artwork.saveToFolder(img, album);//gérer if album = null
				}
				
				Media media = new Media(i, title[title.length-1], album, length, mrl, poster);

				files.add(media);
				i++;
			}
			
			System.out.println("test:" + files.get(0).getTitle());
		
		} catch (CannotReadException e) {
			System.out.println("Impossible de lire le fichier");
			//e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Erreur dans la lecture du fichier");
			//e.printStackTrace();
		} catch (TagException e) {
			System.out.println("Erreur dans le tag du fichier");
			//e.printStackTrace();
		} catch (ReadOnlyFileException e) {
			System.out.println("Erreur: fichier en lecture seule.");
			//e.printStackTrace();
		} catch (InvalidAudioFrameException e) {
			System.out.println("Frame audio invalide.");
			//e.printStackTrace();
		}
	}
	
	/**
	 * Envoi la liste des médias sous forme XML.
	 * @param response
	 */
	public void print(HttpServletResponse response, XStream xstream){
		try {				
			PrintWriter out = response.getWriter();
			out.write(xstream.toXML(files));
		} catch (IOException e) {
			System.out.println("Error while writing file list");
			//e.printStackTrace();
		}
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
