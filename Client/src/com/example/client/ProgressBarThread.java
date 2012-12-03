package com.example.client;

import android.os.AsyncTask;
import android.util.Log;

public class ProgressBarThread extends AsyncTask<Void, Void, Void> {

	private MainActivity mainActivity = null;
	private float progressBarStatus = 0;
	private float increment = 1;
	private int max;
	private boolean inPause = false;

	public ProgressBarThread(MainActivity mainActivity) {
		this.setMainActivity(mainActivity);
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		progressBarStatus = 0;
		mainActivity.getProgressBar().setProgress(0);
		
		while (progressBarStatus < 100) {

			if (!inPause) {
				progressBarStatus += increment;
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
	
	public void cheat() {
		inPause = false;
		progressBarStatus = 100;
	}
	
	public MainActivity getMainActivity() {
		return mainActivity;
	}


	public void setMainActivity(MainActivity mainActivity) {
		this.mainActivity = mainActivity;
	}

	public void setMax(int max) {
		this.max=max;
		increment = (float)100/max;
	}

	public boolean isInPause() {
		return inPause;
	}

	public void setInPause(boolean inPause) {
		this.inPause = inPause;
	}
}
