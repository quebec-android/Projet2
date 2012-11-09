package gti785.multi;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class ArtworkFolder {
	private File _folder;
	private List<String> existingImage;
	
	public ArtworkFolder(File folder){
		existingImage = new ArrayList<String>();
		if(!folder.exists() && !folder.isDirectory()){
			folder.mkdir();
		}
		else{
			for(File file:folder.listFiles()){
				existingImage.add(file.getName());
			}
		}
		_folder = folder;
	}
	
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
	
	public boolean imageExist(String album){
		if(existingImage.contains(album))
			return true;
		else
			return false;
		
	}
}
