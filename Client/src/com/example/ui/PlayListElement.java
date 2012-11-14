package com.example.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
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
	
	@Override
	protected void onDraw(Canvas canvas) {
		// Color as paper
		//canvas.drawColor(paperColor);
		
		// Draw ruled lines //start x, start y, end x, end y
		canvas.drawLine(0, 0, getMeasuredWidth(), 0, linePaint); //vertical lign top
		canvas.drawLine(getMeasuredWidth(), 0, getMeasuredWidth(), getMeasuredHeight(), linePaint);//horizontal lign right
		
		//canvas.drawLine(0, getMeasuredHeight(), getMeasuredWidth(), getMeasuredHeight(), linePaint); //vertical lign bottom
		//canvas.drawLine(0, getMeasuredHeight(),	getMeasuredWidth(), getMeasuredHeight(),linePaint);
		
		
		// Draw margin
		//canvas.drawLine(margin, 0, margin, getMeasuredHeight(), marginPaint);
		
		// Move the text across from the margin
		canvas.save();
		//canvas.translate(margin, 0);
		// Use the TextView to render the text.
		super.onDraw(canvas);
	}

	
}
