package com.example.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.client.R;

public class PlayListElement extends TextView {
	private Paint marginPaint;
	private Paint linePaint;
	private int paperColor;
	private float margin; 
	
	public PlayListElement(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public PlayListElement(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public PlayListElement(Context context) {
		super(context);
		init();
	}
	
	private void init(){
		Resources myResources = getResources();
		
		// Create the paint brushes we will use in the onDraw method.
		marginPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		marginPaint.setColor(myResources.getColor(R.color.playList_background));
		linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		linePaint.setColor(myResources.getColor(R.color.playList_separation));
		// Get the paper background color and the margin width.
		paperColor = myResources.getColor(R.color.playList_background);
		margin = myResources.getDimension(R.dimen.playList_element_marginTopBottom);
		
	}
	
}
