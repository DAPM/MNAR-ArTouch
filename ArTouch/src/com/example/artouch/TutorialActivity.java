package com.example.artouch;



import java.util.Locale;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;
import android.media.MediaPlayer;

public class TutorialActivity extends Activity {
	
    //the sound player
	MediaPlayer mp;
	
	//variables regarding the time we wait between taps
	public TapCounter counterBetweenTaps = new TapCounter(2000, 2000); //we calculate the seconds between taps
	
	//variables regarding the number of taps
	public final int TAP_LIMIT = 3;
	public int lastTapCount = 0;
	public String language;
	//variables regarding the text shown on screen
	public TextView findTheHandlesText;
	String currentHandleFound;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Utils.setFullScreenMode(this);
		setContentView(R.layout.activity_tutorial);
		
		Bundle creatingFromFirstScreen = getIntent().getExtras();
		
		findTheHandlesText = (TextView) findViewById(R.id.findHandlesText);
		
		//we initialise the text shown on screen with a string from strings.xml, the first message
		currentHandleFound = getResources().getString(R.string.left_handle);	
		findTheHandlesText.setText(currentHandleFound);	
		
		language=getResources().getString(R.string.language);
		play(getResources().getIdentifier(language+"lefth", "raw", getPackageName()));
	}
	
	public boolean onTouchEvent(MotionEvent event)
	{		    
		//we handle the touches 
		if(event.getAction() == MotionEvent.ACTION_UP)
		{ 
			lastTapCount++;
			counterBetweenTaps.resetCounter(); //reset counter between taps
			
			//we check whether we taped 3 times the screen
			if (lastTapCount == TAP_LIMIT) 
			{	
				//if we found the first handle, we must find the second one
				if (currentHandleFound == getResources().getString(R.string.left_handle))
				{
					stopp(getResources().getIdentifier(language+"lefth", "raw", getPackageName()));
					//Toast.makeText(this, "You found the left handle!", Toast.LENGTH_SHORT).show();
					currentHandleFound = getResources().getString(R.string.top_handle);
					findTheHandlesText.setText(currentHandleFound);
					play(getResources().getIdentifier(language+"toph", "raw", getPackageName()));
					lastTapCount = 0;
				}
				
				//if we found the second, handle, we must find the third and final handle
				else if(currentHandleFound == getResources().getString(R.string.top_handle))
					{
					stopp(getResources().getIdentifier(language+"toph", "raw", getPackageName()));
					//Toast.makeText(this, "You found the right handle!", Toast.LENGTH_SHORT).show();
					currentHandleFound = getResources().getString(R.string.right_handle);
					findTheHandlesText.setText(currentHandleFound);
					play(getResources().getIdentifier(language+"righth", "raw", getPackageName()));
					lastTapCount = 0;
					}
									
					else 
						if(currentHandleFound == getResources().getString(R.string.right_handle))
						{
							stopp(getResources().getIdentifier(language+"righth", "raw", getPackageName()));
							//here, we must enter the next screen
							Toast.makeText(this, "Welcome to the third screen", Toast.LENGTH_SHORT).show();
							Intent thirdScreen = new Intent(TutorialActivity.this,PaintingExplorerActivity.class);
							startActivity(thirdScreen);
						}
				}
			} 
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void play(int sound)
	 {
		 mp = MediaPlayer.create(this,sound);
		 mp.start();
		 
	 }
	
	private void stopp (int sound)
	 {
		 //mp = MediaPlayer.create(this,sound);
		 mp.stop();
		 
	 }

}
