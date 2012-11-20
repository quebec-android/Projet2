package gti785.model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

/**
 * ArtworkFolder is responsible for the artwork folder. 
 * It adds new images to the folder and checks for existing image in folder.
 * 
 * @author Cedric
 *
 */
public class ArtworkFolder {
	private File _folder;
	private List<String> existingImage;
	
	/**
	 * Construtor: Instanciate the list of existing images and the folder object
	 * 
	 * @param folder to store artwork images
	 */
	public ArtworkFolder(File folder){
		existingImage = new ArrayList<String>();
		
		if(!folder.exists() && !folder.isDirectory()){//if folder does not exist create it
			folder.mkdir();
		}
		else{//retriver all images in folder
			for(File file:folder.listFiles()){
				existingImage.add(file.getName());//should check if is image
			}
		}
		_folder = folder;
	}
	
	/**
	 * Adds image to the folder
	 * @param img to store
	 * @param fileName to give image
	 */
	public void saveToFolder(BufferedImage img, String fileName){
		
		File file = new File(_folder.getPath()+"/"+fileName+".png");
		try {
			ImageIO.write(img, "png", file);  // ignore returned boolean
			existingImage.add(fileName);
		} catch(IOException e) {
			System.out.println("Artwork write error for " + file.getPath() +
		                               ": " + e.getMessage());
		}
	}
	
	/**
	 * Checks if folder contains the given image.
	 * @param album
	 * @return boolean whether image exist or not
	 */
	public boolean imageExist(String album){
		if(existingImage.contains(album))//should check if image
			return true;
		else
			return false;
		
	}
}
