package com.example.mnar_task4;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.MotionEventCompat;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	public String alegeLang[]= {"Apasati de 3 ori pe ecran pentru a selecta limba Romana","Tap 3 times to choose English language","Tappez"};
	public String lang[]={"Ati ales limba ROMANA","You have chosen ENGLISH","Vous avez choisi FRANCAIS"}; // cum e mai eficient: sa am un vector de string sau sa fac cu if la afisare?

	public Timer t=new Timer();//creez un nou timer
	public TextView myTextView;
	public boolean conditie=true;
	
	/*0=for romanian
	  1=for english
	  2=for french	
		*/
	public int langSelectat=-1;
	 	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	    myTextView=(TextView)findViewById(R.id.textLimba);
	    
		
		t.scheduleAtFixedRate(new TimerTask() {

		    @Override
		    public void run() {
		        //Called each time when 1000 milliseconds (1 second) (the period parameter)
		    	if(conditie==true)
		    		{if(langSelectat<2) langSelectat++;
                    else langSelectat=0;
		    	runOnUiThread(new Runnable() {

		    	    @Override
		    	    public void run() {
		    	        myTextView.setText(alegeLang[langSelectat]);
		    	  	    }

		    	});}
		    
    			
		    }

		},
		//Set how long before to start calling the TimerTask (in milliseconds)
		3000,
		//Set the amount of time between each execution (in milliseconds)
		3000);
		//TimerTask schimbaLimba=new SchimbaLimba(MainActivity.this);// ii zic ce actiune trebuie sa faca in fiecare interval
       // t.scheduleAtFixedRate(schimbaLimba, 0, 3000);// il programez sa inceapa
	
	 	}
	 	
	 	public void onStart ()
		{
			super.onStart();
		}
		
		public void onResume()
		{
			super.onResume();
			
		}
		
		public void onStop ()
		{
			super.onStop();
		}
		
	public boolean onTouchEvent(MotionEvent event){ 
	        
		    int action = MotionEventCompat.getActionMasked(event);
		    conditie=false;
		    if(conditie==false)
			{	myTextView.setText(lang[langSelectat]);
				t.cancel();}
		    return conditie;
			
		
		    
		}
		
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	
}
