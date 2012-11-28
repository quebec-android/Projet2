package gti785.model;


import gti785.param.Const;

import java.awt.image.BufferedImage;
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

/**
 * Le MediaFolder permet de gérer les médias du dossier multimédia.
 * 
 * @author Cedric
 *
 */
public class MediaFolder {
	private List<Media> files = null;
	private File _folder;
	private ArtworkFolder artwork;
	
	/**
	 * Constructeur, remplit la liste files avec tous les médias du dossier
	 * multimédia.
	 * 
	 * @param folder path to the folder which will contain medias
	 * @param artwork Object responsible for artwork
	 */
	public MediaFolder(File folder){
		_folder = folder;
		this.artwork = new ArtworkFolder(new File(Const.dossierImage));
		this.files = new ArrayList<Media>();
	}
	
	/**
	 * Checks if extension is .mp3 or .m4a
	 * @param filename path to check
	 * @return true if .mp3 or .m4a
	 */
	private boolean checkMP3(String filename) { //Déplacer vers utils?
		int pos = filename.lastIndexOf(".");
		String extension = filename.substring(pos);
		return extension.equals(".mp3") || extension.equals(".m4a");
	}
	
	/**
	 * GETTERS and SETTERS
	 * 
	 * @return
	 */
	public List<Media> getFiles() {
		return files;
	}

	/**
	 * build is called on creation of the mediaFolder. Retrieves song information 
	 * of the media folder and creates a media list and associated artwork.
	 * 
	 * @return the MediaFolder instance
	 */
	public MediaFolder build() {
		try {
			int i = 0;
			for(File file: _folder.listFiles()){//créer une fonction
				AudioFile f;
				String filename = file.toString();
				
				if( checkMP3(filename)){//if mp3 (or m4a)
					
					//jaudiotagger fonctions
					f = AudioFileIO.read(new File(filename));
					Tag tag = f.getTag();
					
					//retrive song information
					String album = tag.getFirst(FieldKey.ALBUM);
					String poster = Const.ARTWORK_URL+album+".png";
					int length = f.getAudioHeader().getTrackLength();
					String mrl = file.toString();
					String[] title = mrl.split("/");
					
					if(!artwork.imageExist(album) && tag.getFirstArtwork() != null){
						
						BufferedImage img = (BufferedImage)tag.getFirstArtwork().getImage(); //error if no artwork
						//if( img != null)
							artwork.saveToFolder(img, album);//gérer if album = null
					}
					else if(tag.getFirstArtwork() == null){//mettre image par def
						poster = Const.ARTWORK_URL+"default.png";
					}
					
					//create new Media
					Media media = new Media(i, title[title.length-1], album, length, mrl, poster);
					
					//add to list of media
					files.add(media);
					i++;
				}
			}
			
			System.out.println("test:" + files.get(0).getTitle());
			return this;
		
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
		
		return null;
	}
}
