package com.example.artouch;

import java.util.Timer;
import java.util.TimerTask;

import com.example.artouch.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	//variabile ptr succedare limbi
	public final int TAP_LIMIT = 3;
	public final String LANG_CHOICE[] = {"Apasati de " + TAP_LIMIT + " ori pe ecran pentru a selecta limba Romana","Tap " + TAP_LIMIT + " times to choose English language","Tappez"};
	public final String LANG_PICKED[] = {"Ati ales limba ROMANA","You have chosen ENGLISH","Vous avez choisi FRANCAIS"};
	public boolean tapLimitReached = false;
	
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
		setContentView(R.layout.activity_main);
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
                    else 
                    	langSelectat = 0;
		    		runOnUiThread(new Runnable() 
		    		{
		    			@Override
		    			public void run() 
		    			{
		    				lastTapCount = 0;
		    				languageTextView.setText(LANG_CHOICE[langSelectat] + lastTapCount);		    							    					
		    			}
		    		});
		    	}		    	
		    }

		},
		//Set how long before to start calling the TimerTask (in milliseconds)
		6000,
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
				   languageTextView.setText(LANG_PICKED[langSelectat] + lastTapCount);
				   timer.cancel();
				   
				   tapLimitReached = true;
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
}
