package ro.upb.ing.signac;

import android.app.ActionBar;
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
		System.exit(0);
		finish();
		super.onPause();
	}

	@Override
	protected void onStop() {
		finish();
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		cleanUp();
	}

	private void init(){
		if (Build.VERSION.SDK_INT < 16) { // ye olde method			
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		} else {
			// Jellybean and up, new hotness
			View decorView = getWindow().getDecorView();
			// Hide the status bar.
			int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN
					| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
					| View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
			decorView.setSystemUiVisibility(uiOptions);
			// Remember that you should never show the action bar if the
			// status bar is hidden, so hide that too if necessary.
			ActionBar actionBar = getActionBar();
			actionBar.hide();
		}
		setContentView(R.layout.activity_main);
		//Let the show begin !!!
		//Ta-tara-ta-tam-tam !!!
		myView = (OverlayView) findViewById(R.id.multitouchMask);
		myView.showSplashScreen(3000);
	}
	
	private void cleanUp(){
		myView.closeCleanup();
	}


	@Override
	public void onBackPressed() {
		finish();
	    super.onBackPressed();
	}
}
