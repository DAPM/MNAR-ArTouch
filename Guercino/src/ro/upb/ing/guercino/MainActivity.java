package ro.upb.ing.guercino;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
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
	private int LANGUAGE = 2;
	private Bitmap[] FLAGS = new Bitmap[] { null, null, null };
	private String[] MY_LANGUAGE = new String[]{"ro", "fr", "en"};
	private MediaPlayer mMediaPlayer = new MediaPlayer();
	
	private static final int DEFALT_TARGET_WIDTH =768,DEFALT_TARGET_HEIGHT=1024;
	private int              targetWidth;
	private int              targetHeight;
	private static int       screenWidth,screenHeight;
	
	OverlayView myView;

	ImageView image;
	
	@Override
	protected void onDestroy() {
		super.onDestroy();

		mRunnableHandler.removeCallbacks(mSplashScreenRunnable);
		mRunnableHandler.removeCallbacks(mLanguageScreenRunnable);

		if (mMediaPlayer != null) {
			mMediaPlayer.stop();
			mMediaPlayer.release();
			mMediaPlayer = null;
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		DisplayMetrics metrics=new DisplayMetrics();
	    getWindowManager().getDefaultDisplay().getMetrics(metrics);
	    screenWidth=metrics.widthPixels;
	    screenHeight=metrics.heightPixels;
	    this.targetWidth=DEFALT_TARGET_WIDTH;
	    this.targetHeight=DEFALT_TARGET_HEIGHT;

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
		image = (ImageView) findViewById(R.id.imageMainView);
		showSplashScreen(5000);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

	}

	/**
	 * Touch listener to use for triple tap and multi finger navigation
	 */

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		//Process 3 x tap for screens 1, 3, 4, 5
		if (SCREEN_NUMBER == 1 || SCREEN_NUMBER == 3 || SCREEN_NUMBER == 4 || SCREEN_NUMBER == 5 ) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				// Process 3 taps
					_tapCounter.resetCounter();
					float x = event.getX();
					float y = event.getY();
	
					if (_lastTapArea != null) {
						if (_lastTapArea.contains(x, y)) {
							if (_lastTapCount < 2) {
								_lastTapCount++;
							} else {
								// 3 taps in the same area
								// do something taking account of the screen number
								process3Taps();
								addPoint(_lastTapArea.centerX(),_lastTapArea.centerY(), _lastTapCount);
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
		}
		
		//Precess multitouch and move on screens 6, 7
		if (SCREEN_NUMBER == 6 || SCREEN_NUMBER == 7){
			return myView.onTouchEvent(event);
		}

		return true;
	};

	
	private void process3Taps() {
		final ImageView image = (ImageView) findViewById(R.id.imageMainView);

		switch(SCREEN_NUMBER){
			case 1:
				// Am ales limba
				SCREEN_NUMBER++;
				if (mMediaPlayer.isPlaying()) 
					mMediaPlayer.stop();
				playMP3("chosen" + LANGUAGE);
				mRunnableHandler.removeCallbacks(mLanguageScreenRunnable);
				mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
							public void onCompletion(MediaPlayer mp) {
								//LANGUAGE = modulo(LANGUAGE+2, 3);
								image.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.splash));
								// Explic cum functioneaza aplicatia
								playMP3("intro");
								mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
											public void onCompletion(MediaPlayer mp) {
												SCREEN_NUMBER++;
												mRunnableHandler.removeCallbacks(mLTRDetectRunnable);
												mRunnableHandler.postDelayed(mLTRDetectRunnable, 10);
											}
										});
							}
						});
				break;
			case 3:
				if (mMediaPlayer.isPlaying())
					mMediaPlayer.stop();
				SCREEN_NUMBER++;
				mRunnableHandler.removeCallbacks(mLTRDetectRunnable);
				mRunnableHandler.postDelayed(mLTRDetectRunnable, 10);
				break;
			case 4:
				if (mMediaPlayer.isPlaying())
					mMediaPlayer.stop();
				mRunnableHandler.removeCallbacks(mLTRDetectRunnable);
				mRunnableHandler.postDelayed(mLTRDetectRunnable, 10);
				SCREEN_NUMBER++;
				break;
			case 5:
				if (mMediaPlayer.isPlaying())
					mMediaPlayer.stop();
				SCREEN_NUMBER++;
				mRunnableHandler.removeCallbacks(mLTRDetectRunnable);
				image.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.guercino));
				myView = (OverlayView) findViewById(R.id.multitouchMask);
				myView.setVisibility(View.VISIBLE);
				
				break;
			default:
		}
		
	}

	private void playMP3(String resourceID) {
		if (mMediaPlayer.isPlaying())
			mMediaPlayer.stop();
		mMediaPlayer = MediaPlayer.create(this,	getResources().getIdentifier(resourceID, "raw",	getPackageName()));
		mMediaPlayer.start();
	}

	Handler mRunnableHandler = new Handler();

	Runnable mLTRDetectRunnable = new Runnable(){
        
		@Override
		public void run(){
			Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.arrow);
	        Matrix mat = new Matrix();
			switch(SCREEN_NUMBER){
				case 3:
					image.setImageBitmap(bMap);
					playMP3(MY_LANGUAGE[LANGUAGE]+"lefth");
					break;
				case 4:
			        mat.postRotate(90);
			        Bitmap bMapRotate = Bitmap.createBitmap(bMap, 0, 0, bMap.getWidth(), bMap.getHeight(), mat, true);
			        image.setImageBitmap(bMapRotate);
					playMP3(MY_LANGUAGE[LANGUAGE]+"toph");
					break;
				case 5:
			        mat.postRotate(180);
			        bMapRotate = Bitmap.createBitmap(bMap, 0, 0, bMap.getWidth(), bMap.getHeight(), mat, true);
			        image.setImageBitmap(bMapRotate);
					playMP3(MY_LANGUAGE[LANGUAGE]+"righth");
					break;
			}
			mRunnableHandler.removeCallbacks(mLTRDetectRunnable);
			mRunnableHandler.postDelayed(mLTRDetectRunnable, (mMediaPlayer.isPlaying()) ? (mMediaPlayer.getDuration() + 3000) : 10000);
		}
	};
	
	Runnable mLanguageScreenRunnable = new Runnable() {
		@Override
		public void run() {
			LANGUAGE = modulo(LANGUAGE + 1, 3);
			image.setImageBitmap(FLAGS[LANGUAGE]);
			playMP3("choose" + LANGUAGE);
			mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
				public void onCompletion(MediaPlayer mp) {
					mRunnableHandler.removeCallbacks(mLanguageScreenRunnable);
					if (SCREEN_NUMBER == 1){
						mRunnableHandler.postDelayed(mLanguageScreenRunnable, 3000);
					}
				}
			});
			
		}
	};

	Runnable mSplashScreenRunnable = new Runnable() {
		@Override
		public void run() {
			// Show splash screen
			// Create bitmap with the device size in landscape mode
			Bitmap bitmap = Bitmap.createBitmap(screenHeight, screenWidth, Bitmap.Config.ARGB_8888);
			image.setImageBitmap(bitmap);

			FLAGS[0] = BitmapFactory.decodeResource(getResources(),	R.drawable.romania);
			FLAGS[1] = BitmapFactory.decodeResource(getResources(),	R.drawable.france);
			FLAGS[2] = BitmapFactory.decodeResource(getResources(),	R.drawable.united);

			mRunnableHandler.removeCallbacks(mLanguageScreenRunnable);
			mRunnableHandler.postDelayed(mLanguageScreenRunnable, 10);
			SCREEN_NUMBER++;
		}
	};

	/**
	 * Schedules a call to a runnable in [delay] milliseconds, canceling any
	 * previously scheduled calls.
	 */
	private void showSplashScreen(int delayMillis) {
		mRunnableHandler.removeCallbacks(mSplashScreenRunnable);
		playMP3("intro");
		mRunnableHandler.postDelayed(mSplashScreenRunnable, (mMediaPlayer.isPlaying()) ? mMediaPlayer.getDuration() : delayMillis);
	}

	private int modulo(int m, int n) {
		int mod = m % n;
		return (mod < 0) ? mod + n : mod;
	}
	
	void addPoint(float x, float y, int tapCount) {
		_points.add(new CustomPoint(new PointF(x, y), tapCount));
	}
	
	private class CustomPoint {
		private PointF thePoint;
		private int tapCount;

		public CustomPoint(PointF thePoint, int tapCount) {
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
