package com.example.artouch;

import java.util.ArrayList;
import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PaintingExplorerActivity extends Activity{
	
    TextView countdownTimerText;
	final int numberOfZones = 3;
	ArrayList <ImageView> zones= new ArrayList<ImageView>(); //an array with all the zones that can be visited
	int end; // a 'checker' to see if every defined zone was visited
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Utils.setFullScreenMode(this);
		setContentView(R.layout.activity_painting_explorer2);
		
		 
		
		// hq image tests
//			ImageView img = (ImageView) findViewById(getResources().getIdentifier("imageView4", "id", getPackageName()));
//			img.setBackgroundResource(getResources().getIdentifier("hqimage4", "drawable", getPackageName()));
		
		
		for ( int i=1; i<=numberOfZones; i++){
			final ImageView imagine = (ImageView) findViewById(getResources().getIdentifier("imageView"+i, "id", getPackageName()));
			imagine.setImageResource(getResources().getIdentifier("poza"+i, "drawable", getPackageName()));
			imagine.setOnTouchListener(new OnTouchListener(){
				 public boolean onTouch(View arg0, MotionEvent arg1){
					 switch(arg1.getAction())
					 	{	
						 case MotionEvent.ACTION_DOWN:
						 		if(imagine.isClickable()) 
					 			{	
					 			click(arg0);
					 			}
						 		break;
						 		
						 case MotionEvent.ACTION_MOVE:
						 		if(imagine.isClickable()) 
						 			{
						 			click(arg0);
						 			}
						 		break;
					 	}             		            
			            return false;
				 	}
				});
			zones.add(imagine);
		 }
		//countdown timer
		countdownTimerText = (TextView) findViewById(R.id.countdownText);
		}

	/*I define the actions I want my app to perform each time I click on a zone;
	 while an element is being visited, we prevent all other zones from being clicked;
	*/
	
	public void click(View v)
	{
		if (v.getId()==R.id.imageView1) 
		{
			play(v,0);	
		}
		if (v.getId()==R.id.imageView2) 
		{
			play(v,1);	
		}
		if (v.getId()==R.id.imageView3) 
		{
			play(v,2);
		}
	}
	
	public void play(View v,final int x)
	{
		final Animation fadeOutAnimation= AnimationUtils.loadAnimation(this, R.anim.fadeout);
		final ImageView viewCurrent= zones.get(x); 
		zones.remove(x);
		
		
		for(int i=0;i<=zones.size()-1;i++)
			zones.get(i).setClickable(false);
		
		new CountDownTimer(10000, 1000) {

		     public void onTick(long millisUntilFinished) {
		        countdownTimerText.setText("seconds remaining: " + millisUntilFinished / 1000);
		        viewCurrent.setClickable(false);
		        }

		     public void onFinish() {
		     
		    	countdownTimerText.setText("done-zone "+x+" was visited!");
		        viewCurrent.startAnimation(fadeOutAnimation);
		        viewCurrent.setVisibility(View.INVISIBLE);
		        end++;
		    	for(int i=0;i<=zones.size()-1;i++)
		    		{
			    		if(zones.get(i).getVisibility()==0)
			    				zones.get(i).setClickable(true);
		    		}
		       zones.add(x, viewCurrent);
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
