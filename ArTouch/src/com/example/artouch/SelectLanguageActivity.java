package com.example.artouch;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import com.example.artouch.R;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MotionEvent;
import android.widget.TextView;
import android.media.MediaPlayer;

public class SelectLanguageActivity extends Activity 
{	
	MediaPlayer mp;
	//variabile ptr succedare limbi
	public final int TAP_LIMIT = 3;
	public final String LANG_CHOICE[] = {"Apasati de " + TAP_LIMIT + " ori pe ecran pentru a selecta limba Romana","Tap " + TAP_LIMIT + " times to choose English language","Tappez"};
	public final String LANG_PICKED[] = {"Ati ales limba ROMANA","You have chosen ENGLISH","Vous avez choisi FRANCAIS"};
	public boolean tapLimitReached = false;
	private Handler mHandler = new Handler(); // a handler to wait for my sound to finish playing 
	
	public Timer timer = new Timer(); //TAP_LIMIT second timer
	public TextView languageTextView; 
		
	public int langSelectat=-1;
	/*
	0=for romanian
  	1=for english	 
 	2=for french	
	*/
	
	 //variabile counter tap
	 protected static final long TAP_MAX_DELAY = 2000;
	 private TapCounter tapCounter = new TapCounter(TAP_MAX_DELAY, TAP_MAX_DELAY);
	 private int lastTapCount = 0;
	 
	 	
	@Override	
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		Utils.setFullScreenMode(this);
		setContentView(R.layout.activity_select_language);
	    languageTextView = (TextView)findViewById(R.id.textLimba);	    
	    
		timer.scheduleAtFixedRate(new TimerTask() 
		{
		    @Override
		    public void run() 
		    {
		        //Called each time when 1000 milliseconds (1 second) (the period parameter)
		    	if(tapLimitReached == false)
		    	{
		    		if(langSelectat < TAP_LIMIT - 1) 
		    			langSelectat++;

		    		//nu intra niciodata pe aici?
                    else 
                    	langSelectat = 0;
		    		
		    		runOnUiThread(new Runnable() 
		    		{
		    			@Override
		    			public void run() 
		    			{
		    				lastTapCount = 0;
		    				languageTextView.setText(LANG_CHOICE[langSelectat] + lastTapCount);
		    				play(getResources().getIdentifier("choose"+langSelectat, "raw", getPackageName()));
		    			}
		    		});
		    	}		    	
		    }

		},
		//Set how long before to start calling the TimerTask (in milliseconds)
		1000,
		//Set the amount of time between each execution (in milliseconds)
		6000);
		//TimerTask schimbaLimba=new SchimbaLimba(MainActivity.this); ii zic ce actiune trebuie sa faca in fiecare interval
       // t.scheduleAtFixedRate(schimbaLimba, 0, 3000); il programez sa inceapa
	}	 	
	
	
	public boolean onTouchEvent(MotionEvent event)
	{		    
		   if(event.getAction() == MotionEvent.ACTION_UP)
		   { 
			   lastTapCount++;
			   tapCounter.resetCounter(); //reset counter between taps
			   if (lastTapCount == TAP_LIMIT) 
			   {
				   stopp(getResources().getIdentifier("choosen"+langSelectat, "raw", getPackageName()));
				   languageTextView.setText(LANG_PICKED[langSelectat] + lastTapCount);
				   play(getResources().getIdentifier("chosen"+langSelectat, "raw", getPackageName()));
				   timer.cancel();
				   
				  
				   tapLimitReached = true;
				   
				   //Change application's language
				   Configuration c = new Configuration(getResources().getConfiguration());
				   switch (langSelectat)
				   {				   		
				   		case 0:
				   		{				   			
							c.locale = new Locale("ro", "RO");
				   			break;
				   		}
				   		case 1:
				   		{
				   			c.locale = Locale.ENGLISH;						
				   			break;
				   		}
				   		case 2:
				   		{
							c.locale = Locale.FRENCH;
							break;
				   		}
				   		default:
				   		{
				   			c.locale = Locale.ENGLISH;
				   			break;
				   		}
				   }
				   
				   //ask the app to wait for my sound to finish being played before moving to the next screen
				   //!*same function should be implemented in the future for choose1,2,3;
				   mHandler.postDelayed(new Runnable() {
			            public void run() {
			            	stopp(getResources().getIdentifier("chosen"+langSelectat, "raw", getPackageName()));
			            	Intent secondScreen = new Intent(SelectLanguageActivity.this, TutorialActivity.class);
							   startActivity(secondScreen);
			            }
			        }, 3000);
				   
			   } 
		   }
		  
		   return true;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void play(int sound)
	 {
		 mp = MediaPlayer.create(this,sound);
		 mp.start();
		 	// to make sure the app waits for the played sound to end, before passing to the next
		   //scheduled activity
		
	 }
	
	private void stopp (int sound)
	 {
		 //mp = MediaPlayer.create(this,sound);
		 mp.stop();
		 
	 }
}
