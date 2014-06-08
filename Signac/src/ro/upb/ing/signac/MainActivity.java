package ro.upb.ing.signac;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class MainActivity extends Activity {
		
	//myViews: the images and overlay for multitouch tracking and graphic overlay
	OverlayView myView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}	

	@Override
	protected void onPause() {
		super.onPause();
		finish();
		System.exit(0);
	}

	@Override
	protected void onStop() {
		super.onStop();
		finish();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		cleanUp();
	}

	@Override
	public void onBackPressed() {
	    super.onBackPressed();
		finish();
	}	
	
	private void init(){
		if (Build.VERSION.SDK_INT < 16) { // ye olde method			
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		} else {
			// Jellybean and up, new hotness

			View decorView = getWindow().getDecorView();
			// Hide the status bar.
			//View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
			
			int uiOptions =  View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
	                | View.SYSTEM_UI_FLAG_FULLSCREEN
	                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
			decorView.setSystemUiVisibility(uiOptions);

			getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
    			@Override
			    public void onSystemUiVisibilityChange(int visibility) {
    	    		if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
    	    			int uiOptions =  View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
	               				 | View.SYSTEM_UI_FLAG_FULLSCREEN
	              			     | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
						getWindow().getDecorView().setSystemUiVisibility(uiOptions);
					}
		    	}
			});
		}		
		setContentView(R.layout.activity_main);
		//Let the show begin !!!
		//Ta-tara-ta-tam-tam !!!
		myView = (OverlayView) findViewById(R.id.multitouchMask);
		myView.showSplashScreen(7000);
	}
	
	private void cleanUp(){
		myView.closeCleanup();
	}

}
