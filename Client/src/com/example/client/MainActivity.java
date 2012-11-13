package com.example.client;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ui.PlayListElement;

public class MainActivity extends Activity {
	
	//private HttpURLConnection connection = null;
	ConnectivityManager connMgr;
	
	/*private Button play = null;
	private Button next = null; 
	private Button previous = null; 
	private Button stop = null; 
	private Button toBeginning = null;*/
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        /*play = (Button)findViewById(R.id.play);
        play.setOnClickListener(playListener);
        
        next = (Button)findViewById(R.id.next);
        next.setOnClickListener(nextListener);
        
        previous = (Button)findViewById(R.id.previous);
        previous.setOnClickListener(previousListener);
        
        stop = (Button)findViewById(R.id.stop);
        stop.setOnClickListener(stopListener);
        
        toBeginning = (Button)findViewById(R.id.toBeginning);
        toBeginning.setOnClickListener(toBeginningListener);*/
        
        connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        
        LinearLayout rl = (LinearLayout) findViewById(R.id.playList);
        for(int i=0;i<4;i++){
        	TextView tv = new PlayListElement(this);
        	//tv.setPadding(left, top, right, bottom)
        	tv.setText("texte");
        	rl.addView(tv);
        }
        
        LinearLayout rl2 = (LinearLayout) findViewById(R.id.files);
        for(int i=0;i<4;i++){
        	TextView tv = new PlayListElement(this);
        	tv.setText("texte");
        	rl2.addView(tv);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    // Play
   // private OnClickListener playListener = new OnClickListener() {
        //@Override
		public void playListener(View v) {
		    Log.d("ManETS","PLAY!!");
		    try{
				//Utils.sendGetRequest(connection, Const.GET,"play");
				//BufferedReader rd  = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		        //rd.readLine();
		    	Utils.getUrl("play",connMgr);
			}
			catch(Exception e){
				Log.d("ManETS","Exception : echec dans la connexion");
			}
		}
   // };
    
    // Next
   // private OnClickListener nextListener = new OnClickListener() {
     //   @Override
		public void nextListener(View v) {
		    Log.d("ManETS","NEXT!!");
		    try{
				/*Utils.sendGetRequest(connection, Const.GET,"next");
				BufferedReader rd  = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		        rd.readLine();*/
		    	Utils.getUrl("next",connMgr);
			}
			catch(Exception e){
				Log.d("ManETS",e.getMessage());
			}
		}
 //   };
    
    // Previous
    //private OnClickListener previousListener = new OnClickListener() {
        //@Override
		public void previousListener(View v) {
		    Log.d("ManETS","PREVIOUS!!");
		    Utils.getUrl("previous",connMgr);
		}
    //};
    
    // Stop
    //private OnClickListener stopListener = new OnClickListener() {
      //  @Override
		public void stopListener(View v) {
		    Log.d("ManETS","STOP!!");
		    Utils.getUrl("stop",connMgr);
		}
    //};
    
    // Play
   // private OnClickListener toBeginningListener = new OnClickListener() {
     //   @Override
		public void toBeginningListener(View v) {
		    Log.d("ManETS","toBeginning!!");
		}
    //};
    
}
