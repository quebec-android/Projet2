package com.example.client;

import android.os.AsyncTask;
import android.util.Log;

/**
 * triggered to update the progressBar progress. The thread will increment
 * the progressbar every 1sec with a value depending on the length of the song.
 * 
 * @author Cedric
 *
 */
public class ProgressBarThread extends AsyncTask<Void, Void, Void> {

	private MainActivity mainActivity = null;
	private float progressBarStatus = 0;
	private float increment = 1;
	private boolean inPause = false;
	
	/**
	 * Default constructor
	 * @param mainActivity
	 */
	public ProgressBarThread(MainActivity mainActivity) {
		this.setMainActivity(mainActivity);
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		progressBarStatus = 0;
		mainActivity.getProgressBar().setProgress(0);
		
		while (progressBarStatus < 100) {//song not ended

			if (!inPause) {
				progressBarStatus += increment;//increment progress
				// your computer is too fast, sleep 1 second
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
				
				mainActivity.getProgressBar().setProgress((int)progressBarStatus);
			}
		}

		Log.d("ManETS","fin de la chanson");
		return null;
	}
	
	/**
	 * fills progress bar
	 */
	public void cheat() {
		inPause = false;
		progressBarStatus = 100;
	}
	
	/**
	 * mainActivity getter
	 * @return
	 */
	public MainActivity getMainActivity() {
		return mainActivity;
	}

	/**
	 * mainActivity setter
	 * @return
	 */
	public void setMainActivity(MainActivity mainActivity) {
		this.mainActivity = mainActivity;
	}

	public void setMax(int max) {
		increment = (float)100/max;
	}

	public boolean isInPause() {
		return inPause;
	}

	public void setInPause(boolean inPause) {
		this.inPause = inPause;
	}
}
