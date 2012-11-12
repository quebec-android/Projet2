package com.example.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	
	private HttpURLConnection connection = null;
	
	private Button play = null;
	private Button next = null; 
	private Button previous = null; 
	private Button stop = null; 
	private Button toBeginning = null; 

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        play = (Button)findViewById(R.id.play);
        play.setOnClickListener(playListener);
        
        next = (Button)findViewById(R.id.next);
        next.setOnClickListener(nextListener);
        
        previous = (Button)findViewById(R.id.previous);
        previous.setOnClickListener(previousListener);
        
        stop = (Button)findViewById(R.id.stop);
        stop.setOnClickListener(stopListener);
        
        toBeginning = (Button)findViewById(R.id.toBeginning);
        toBeginning.setOnClickListener(toBeginningListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    // Play
    private OnClickListener playListener = new OnClickListener() {
        @Override
		public void onClick(View v) {
		    Log.d("ManETS","PLAY!!");
		    try{
				Utils.sendGetRequest(connection, Const.GET,"play");
				BufferedReader rd  = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		        rd.readLine();
			}
			catch(Exception e){
				Log.d("ManETS","Exception : echec dans la connexion");
			}
		}
    };
    
    // Play
    private OnClickListener nextListener = new OnClickListener() {
        @Override
		public void onClick(View v) {
		    Log.d("ManETS","NEXT!!");
		    try{
				Utils.sendGetRequest(connection, Const.GET,"next");
				BufferedReader rd  = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		        rd.readLine();
			}
			catch(Exception e){
				Log.d("ManETS",e.getMessage());
			}
		}
    };
    
    // Play
    private OnClickListener previousListener = new OnClickListener() {
        @Override
		public void onClick(View v) {
		    Log.d("ManETS","PREVIOUS!!");
		}
    };
    
    // Play
    private OnClickListener stopListener = new OnClickListener() {
        @Override
		public void onClick(View v) {
		    Log.d("ManETS","STOP!!");
		}
    };
    
    // Play
    private OnClickListener toBeginningListener = new OnClickListener() {
        @Override
		public void onClick(View v) {
		    Log.d("ManETS","toBeginning!!");
		}
    };
}
