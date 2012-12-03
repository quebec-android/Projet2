package com.example.client;

import android.util.Log;
import android.widget.ProgressBar;

public class ProgressBarManager extends Thread {
	private int progressBarStatusInt = 0;
	private float increment = 0;
	private boolean pause = false;
	private float progressBarStatus = 0;
	private ProgressBar progressBar = null;
	
	public ProgressBarManager(float currentSongLength,ProgressBar progressBar) {
		increment = 100/currentSongLength;
		this.progressBar = progressBar;
	}
 
 
    public void run ()
    {
    	boolean aborted = false;
    	while(!aborted) {
    		try {
    			while (progressBarStatusInt < 100) {
					
					try {
						if(!pause){
							progressBarStatus = progressBarStatus + increment;
							progressBarStatusInt = (int) Math.floor(progressBarStatus);
							progressBar.setProgress(progressBarStatusInt);
						}
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						aborted = true;
					}
					if(Thread.interrupted())
						aborted = true;
					
					if(aborted)
						break;
    			}
    		} catch(Exception e) {
    			Log.d("ManETS","progressBar error: "+e.getMessage()+" "+e.getCause());
    			e.printStackTrace();
    		}
			
		} 
    	progressBar.setProgress(0);
    }
    
    public void pause(){
    	pause = true;
    }
    
    public void play(){
    	pause = false;
    }
}
