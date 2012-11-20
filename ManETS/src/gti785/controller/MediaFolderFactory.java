package gti785.controller;

import gti785.model.MediaFolder;
import gti785.param.Const;

import java.io.File;

/**
 * 
 * @author Cedric
 *
 */
public class MediaFolderFactory {
	private static MediaFolderFactory instance;
	
	private MediaFolderFactory(){}
	
	public static MediaFolderFactory getInstance(){
		if( instance == null)
			instance = new MediaFolderFactory();
			
		return instance;
	}
	
	
	public MediaFolder build(){
		return new MediaFolder(new File(Const.dossier)).build();
	}
}
