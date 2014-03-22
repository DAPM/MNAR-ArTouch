package ro.upb.ing.signac;

import java.util.ArrayList;

import ro.upb.ing.signac.R;
import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class MainActivity extends Activity {
	
	// Set the tap delay in milliseconds
    protected static final long TAP_MAX_DELAY = 1000L;
 // Radius to capture tap within bound
    private final static int RADIUS = 50;
 // Store all points with tap count
    public ArrayList<CustomPoint> _points = new ArrayList<CustomPoint>();
    TapCounter _tapCounter = new TapCounter(TAP_MAX_DELAY, TAP_MAX_DELAY);
    private RectF _lastTapArea;
    private int _lastTapCount = 0;
    
	private int SCREEN_NUMBER = 0;
	private int LANGUAGE = 0;
	private Bitmap[] FLAGS = new Bitmap[]{null, null, null};
	private MediaPlayer  mMediaPlayer=new MediaPlayer();

	
	@Override
	protected void onDestroy() {
	    super.onDestroy();
	    
		mRunnableHandler.removeCallbacks(mSplashScreenRunnable);
		mRunnableHandler.removeCallbacks(mLanguageScreenRunnable);

	    
	    if (mMediaPlayer != null) {
	        mMediaPlayer.release();
	        mMediaPlayer = null;
	    }

	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (Build.VERSION.SDK_INT < 16) { //ye olde method
		    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		} else { 
			// Jellybean and up, new hotness
		    View decorView = getWindow().getDecorView();
		    // Hide the status bar.
		    int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN | 
		    		        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | 
		    		        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
		    decorView.setSystemUiVisibility(uiOptions);
		    // Remember that you should never show the action bar if the
		    // status bar is hidden, so hide that too if necessary.
		    ActionBar actionBar = getActionBar();
		    actionBar.hide();
		}
		setContentView(R.layout.activity_main);
		
		showSplashScreen(5000);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

	}

	/**
	 * Touch listener to use for in-layout UI controls to delay hiding the
	 * system UI. This is to prevent the jarring behavior of controls going away
	 * while interacting with activity UI.
	 */
	
		
	@Override
	public boolean onTouchEvent(MotionEvent motionEvent) {
		
        switch (motionEvent.getAction()) {
        case MotionEvent.ACTION_DOWN:
        	_tapCounter.resetCounter();
            float x = motionEvent.getX();
            float y = motionEvent.getY();

            if (_lastTapArea != null) {
                if (_lastTapArea.contains(x, y)) {
                    if (_lastTapCount < 2) {
                        _lastTapCount++;
                    } else {
                    	//3 taps in the same area
                    	//do something taking account of the screen number
                    	switch(SCREEN_NUMBER){
                    		case 0:
                    			mRunnableHandler.removeCallbacks(mLanguageScreenRunnable);
                    			ImageView image = (ImageView) findViewById(R.id.imageMainView);
                    			image.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.signac));

                    			break;
                    		default:
                    			
                    	}
                        addPoint(_lastTapArea.centerX(), _lastTapArea.centerY(), _lastTapCount);
                        _lastTapCount = 1;
                    }
                } else {
                    addPoint(_lastTapArea.centerX(), _lastTapArea.centerY(), _lastTapCount);
                    _lastTapCount = 1;
                    _lastTapArea = new RectF(x - RADIUS, y - RADIUS, x + RADIUS, y + RADIUS);
                }
            } else {
                _lastTapCount = 1;
                _lastTapArea = new RectF(x - RADIUS, y - RADIUS, x + RADIUS, y + RADIUS);
            }

            return true;
        }

        return false;
    };

	private int modulo( int m, int n ){
	    int mod =  m % n ;
	    return ( mod < 0 ) ? mod + n : mod;
	}
	
	Handler mRunnableHandler = new Handler();
	
	Runnable mLanguageScreenRunnable = new Runnable(){
		@Override
		public void run(){
			ImageView image = (ImageView) findViewById(R.id.imageMainView);
			image.setImageBitmap(FLAGS[LANGUAGE]);
			LANGUAGE = modulo(LANGUAGE+1, 3);
			
			mRunnableHandler.removeCallbacks(mLanguageScreenRunnable);
			mRunnableHandler.postDelayed(mLanguageScreenRunnable, 10000);
		}
	};
	
	Runnable mSplashScreenRunnable = new Runnable() {
		@Override
		public void run() {
			//Show splash screen
			Bitmap bitmap = Bitmap.createBitmap(768, 1024, Bitmap.Config.ARGB_8888);
			ImageView iv = (ImageView) findViewById(R.id.imageMainView);
			iv.setImageBitmap(bitmap);
			
	        FLAGS[0] = BitmapFactory.decodeResource(getResources(), R.drawable.romania);
	        FLAGS[1] = BitmapFactory.decodeResource(getResources(), R.drawable.france);
	        FLAGS[2] = BitmapFactory.decodeResource(getResources(), R.drawable.united);
	        

			mRunnableHandler.removeCallbacks(mLanguageScreenRunnable);
			mRunnableHandler.postDelayed(mLanguageScreenRunnable, 10);
		}
	};

	/**
	 * Schedules a call to hide() in [delay] milliseconds, canceling any
	 * previously scheduled calls.
	 */
	private void showSplashScreen(int delayMillis) {
		mRunnableHandler.removeCallbacks(mSplashScreenRunnable);
		mRunnableHandler.postDelayed(mSplashScreenRunnable, delayMillis);
	}
	
	
	void addPoint(float x, float y, int tapCount) {
        _points.add(new CustomPoint(new PointF(x, y), tapCount));
    }
	
	class CustomPoint{
		private PointF thePoint;
		private int tapCount;
		public CustomPoint(PointF thePoint, int tapCount){
			this.thePoint = thePoint;
			this.tapCount = tapCount;
		}
	}
	
	
	class TapCounter extends CountDownTimer {

        public TapCounter(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            if (_lastTapArea != null) {
                if (_lastTapCount > 0)
                    addPoint(_lastTapArea.centerX(), _lastTapArea.centerY(), _lastTapCount);
                _lastTapCount = 0;
                _lastTapArea = null;
            }
        }

        @Override
        public void onTick(long millisUntilFinished) {
        }

        public void resetCounter() {
            start();
        }
    }
	
}
