package gti785.multi;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ArtworkFolder {
	File _folder;
	
	public ArtworkFolder(File folder){
		if(!folder.exists() && !folder.isDirectory()){
			folder.mkdir();
		}
		_folder = folder;
	}
	
	public boolean saveToFolder(BufferedImage img, String fileName){
		
		File file = new File(_folder.getPath()+"/"+fileName+".png");
		try {
			ImageIO.write(img, "png", file);  // ignore returned boolean
		} catch(IOException e) {
			System.out.println("Write error for " + file.getPath() +
		                               ": " + e.getMessage());
		}
		return false;
	}
	
	public boolean getFormFolder(int imgID){
		return false;
	}
}
