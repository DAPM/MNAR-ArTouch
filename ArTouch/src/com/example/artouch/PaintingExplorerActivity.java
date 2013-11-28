package com.example.artouch;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PaintingExplorerActivity extends Activity{
	
    TextView countdownTimerText;
	ImageView[] zones;
	final int numberOfZones = 3;
	int end; // a 'checker' to see if every defined zone was visited
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_painting_explorer2);
		
		// hq image tests
//			ImageView img = (ImageView) findViewById(getResources().getIdentifier("imageView4", "id", getPackageName()));
//			img.setBackgroundResource(getResources().getIdentifier("hqimage4", "drawable", getPackageName()));
		
		
		//I add the pictures I need

		for (int i=1; i<=numberOfZones; i++){

			      ImageView img = (ImageView) findViewById(getResources().getIdentifier("imageView"+i, "id", getPackageName()));

			      img.setImageResource(getResources().getIdentifier("poza"+i, "drawable", getPackageName()));

			    }
		
		//countdown timer
		countdownTimerText = (TextView) findViewById(R.id.countdownText);		

	}

	/*I define the actions I want my app to perform each time I click on a zone;
	 while an element is being visited, we prevent all other zones from being clicked;
	*/
	public void clickZona1(View v)
	{   
		final Animation fadeOutAnimation= AnimationUtils.loadAnimation(this, R.anim.fadeout);
		final ImageView view1 = (ImageView) findViewById(R.id.imageView1);
		final ImageView view2 = (ImageView) findViewById(R.id.imageView2);
		final ImageView view3 = (ImageView) findViewById(R.id.imageView3);
		view2.setClickable(false);
		view3.setClickable(false);
		
		new CountDownTimer(10000, 1000) {

		     public void onTick(long millisUntilFinished) {
		        countdownTimerText.setText("seconds remaining: " + millisUntilFinished / 1000);
		     }

		     public void onFinish() {
		        countdownTimerText.setText("done-zone1 was visited!");
		      	view1.startAnimation(fadeOutAnimation);
				view1.setVisibility(View.INVISIBLE);
		        view1.setClickable(false);
		        end++;
		        if(view2.getVisibility()==0)
		        	view2.setClickable(true);
		        if(view3.getVisibility()==0)
		        	view3.setClickable(true);
		        checkEnd();
		     }
		  }.start();
		
	}
	public void clickZona2(View v)
	{   
		final Animation fadeOutAnimation= AnimationUtils.loadAnimation(this, R.anim.fadeout);
		final ImageView view1 = (ImageView) findViewById(R.id.imageView1);
		final ImageView view2 = (ImageView) findViewById(R.id.imageView2);
		final ImageView view3 = (ImageView) findViewById(R.id.imageView3);
		view1.setClickable(false);
		view3.setClickable(false);
		
		new CountDownTimer(10000, 1000) {
			
		     public void onTick(long millisUntilFinished) {
		        countdownTimerText.setText("seconds remaining: " + millisUntilFinished / 1000);
		      
		     }

		     public void onFinish() {
		        countdownTimerText.setText("done-zone2 was visited");
		    	view2.startAnimation(fadeOutAnimation);
		        view2.setVisibility(View.INVISIBLE);
		        view2.setClickable(false);
		        end++;
		        if(view1.getVisibility()==0)
		        	view1.setClickable(true);
		        if(view3.getVisibility()==0)
		        	view3.setClickable(true);
		        checkEnd();
		     }
		  }.start();
		
	}
	
	public void clickZona3(View v)
	{   
		final Animation fadeOutAnimation= AnimationUtils.loadAnimation(this, R.anim.fadeout);
		final ImageView view1 = (ImageView) findViewById(R.id.imageView1);
		final ImageView view2 = (ImageView) findViewById(R.id.imageView2);
		final ImageView view3 = (ImageView) findViewById(R.id.imageView3);
		view1.setClickable(false);
		view2.setClickable(false);
		new CountDownTimer(10000, 1000) {

		     public void onTick(long millisUntilFinished) {
		        countdownTimerText.setText("seconds remaining: " + millisUntilFinished / 1000);
		     }

		     public void onFinish() {
		        countdownTimerText.setText("done-zone3 was visited");
		    	view3.startAnimation(fadeOutAnimation);
		        view3.setVisibility(View.INVISIBLE);
		        view3.setClickable(false);
		        if(view2.getVisibility()==0)
		        	view2.setClickable(true);
		        if(view1.getVisibility()==0)
		        	view1.setClickable(true);
		        end++;
		        checkEnd();
		     }
		  }.start();
		  
	}
	
	public void checkEnd()
	{
		 if(end==3){
				//here, we must enter the next screen
				Toast.makeText(this, "Welcome to the forth screen", Toast.LENGTH_SHORT).show();
				//Intent thirdScreen = new Intent(TutorialActivity.this,x.class);
				//startActivity(forthScreen);
				}
		  
	}


}
