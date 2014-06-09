package ro.upb.ing.guercino;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;

public class OverlayView extends View {
	private Paint mPaint;
	private Path[] polyPath = {new Path(), new Path(), new Path()};
	
	// Set the tap delay in milliseconds
	protected static final long TAP_MAX_DELAY = 1000L;
	// Radius to capture tap within bound
	private final static int RADIUS = 50;
	//Area used for 3 tap detection
	private RectF _lastTapArea;
	private int _lastTapCount = 0;
	// Store all points with tap count
	private ArrayList<CustomPoint> _points = new ArrayList<CustomPoint>();
	private TapCounter _tapCounter = new TapCounter(TAP_MAX_DELAY, TAP_MAX_DELAY);
	
	private MusicHandler mMusicFader;
	private int _playZone = -1;

    private RectF rectF = new RectF();
    private Region[] r = new Region[]{new Region(), new Region(), new Region()};
    private int mWidth;
    private int mHeight;
    
    private int SCREEN_NUMBER = 0;
	private int LANGUAGE = 2;
	private Bitmap mPaintingImage;
    private Bitmap mSplashImage;
	private Bitmap[] FLAGS = new Bitmap[] { null, null, null };
    private Rect dest;

	private final static String[] MY_LANGUAGE = new String[]{"ro", "fr", "en"};
    	
    private SparseArray<PointF> mActivePointers;

	public OverlayView(Context context) {
		super(context);
		init(context);
	}

	public OverlayView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public OverlayView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	
	private void init(Context context){
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	    // set painter color to a color you like
	    mPaint.setColor(Color.BLUE);
	    mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
	    mActivePointers = new SparseArray<PointF>();
        mPaintingImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.guercino);
        mSplashImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.splash);
        FLAGS[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.romania);
		FLAGS[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.france);
		FLAGS[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.united);	
        mMusicFader = new MusicHandler(context);
        
	}
	
	public void closeCleanup(){
		mRunnableHandler.removeCallbacks(null);
		mMusicFader.cleanUp();
		mPaintingImage = null;
		mSplashImage = null;
		FLAGS[0] = null;
		FLAGS[1] = null;
		FLAGS[2] = null;		
	}
	
	@Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{		
		mWidth = MeasureSpec.getSize(widthMeasureSpec);
	    mHeight = MeasureSpec.getSize(heightMeasureSpec);
	    this.setMeasuredDimension(mWidth, mHeight);
	    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	@Override
	protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld) {
		super.onSizeChanged(xNew, yNew, xOld, yOld);
		mWidth = xNew;
		mHeight = yNew;

		dest = new Rect(0, 0, mWidth, mHeight);

		// Zone 1 - Angel
		// path
		polyPath[0].moveTo(0, 0);
		polyPath[0].lineTo(mWidth, 0);
		polyPath[0].lineTo(mWidth, (int) (mHeight * 0.45));
		polyPath[0].lineTo((int) (mWidth * 0.42), (int) (mHeight * 0.45));
		polyPath[0].lineTo(0, (int) (mHeight * 0.10));
		polyPath[0].close();
		
		 polyPath[0].computeBounds(rectF, true);
		r[0].setPath(polyPath[0], new Region((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom));
		//Zone 2 - St. Francisc
	    // path
	    polyPath[1].moveTo(mWidth, mHeight);
	    polyPath[1].lineTo(mWidth, (int)(mHeight*0.45));
	    polyPath[1].lineTo((int)(mWidth*0.42), (int)(mHeight*0.45));
	    polyPath[1].lineTo((int)(mWidth*0.42), mHeight);
	    polyPath[1].close();
	    polyPath[1].computeBounds(rectF, true);
		r[1].setPath(polyPath[1], new Region((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom));
		
	    //Zone 3 - St. Benedict
	    // path
	    polyPath[2].moveTo(0, mHeight);
	    polyPath[2].lineTo((int)(mWidth*0.42), mHeight);
	    polyPath[2].lineTo((int)(mWidth*0.42), (int)(mHeight*0.45));
	    polyPath[2].lineTo(0, (int)(mHeight*0.10));
	    polyPath[2].close();	
	    polyPath[2].computeBounds(rectF, true);
		r[2].setPath(polyPath[2], new Region((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom));
	    
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		// get pointer index from the event object
	    int pointerIndex = event.getActionIndex();

	    // get pointer ID
	    int pointerId = event.getPointerId(pointerIndex);

	    // get masked (not specific to a pointer) action
	    int maskedAction = event.getActionMasked();

	    switch (maskedAction) {

		    case MotionEvent.ACTION_DOWN:{
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
			    
			    	//break;
		    	
		    }
		    case MotionEvent.ACTION_POINTER_DOWN: {
		    	// We have a new pointer. Lets add it to the list of pointers
	
		      PointF f = new PointF();
		      f.x = event.getX(pointerIndex);
		      f.y = event.getY(pointerIndex);
		      mActivePointers.put(pointerId, f);
		      
		      break;
		    }
		    
		    case MotionEvent.ACTION_MOVE: { // a pointer was moved
		      for (int size = event.getPointerCount(), i = 0; i < size; i++) {
		        PointF point = mActivePointers.get(event.getPointerId(i));
		        if (point != null) {
		          point.x = event.getX(i);
		          point.y = event.getY(i);
		        }
		      }
		      break;
		    }
		    
		    case MotionEvent.ACTION_UP:
		    case MotionEvent.ACTION_POINTER_UP:
		    case MotionEvent.ACTION_CANCEL: {
		      mActivePointers.remove(pointerId);
		      if (mActivePointers.size()==0) processUp();
		      break;
		    }
	    }
		
		this.invalidate();
		return true;
	}
	
	private void processUp() {
		switch(SCREEN_NUMBER){
		case 7:
			//fix bug voice does not stop when touch stops
			playZone(-1);
			break;
		case 8:
			//fix bug music does not stop when touch stops
			playZone(-1);
			break;
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		int cx = 0;
	    int cy = 0;
		
		switch(SCREEN_NUMBER){
			case 0:
				//Splash screen
			    canvas.drawBitmap(mSplashImage, null, dest, null);
				break;
			case 1:
				//Language chooser screen
				//Draw ro, fr, en flags
				cx = (mWidth - FLAGS[LANGUAGE].getWidth()) >> 1;
			    cy = (mHeight - FLAGS[LANGUAGE].getHeight()) >> 1;
			    canvas.drawBitmap(FLAGS[LANGUAGE], cx, cy, null);
				break;
			case 6:
			    canvas.drawBitmap(mPaintingImage, null, dest, null);
				break;
			case 7:
				//Zone 1-2-3 explanation
				
				//Draw background image
			    //mPaint.setFilterBitmap(true);
			    canvas.drawBitmap(mPaintingImage, null, dest, null);

				//find center point
				int x = -1, y = -1;
				
				// draw all pointers
				for (int size = mActivePointers.size(), i = 0; i < size; i++) {
					PointF point = mActivePointers.valueAt(i);
					if(point != null){
						x+=(int)point.x;
						y+=(int)point.y;
					}
				}
				
				if (x >= 0 && y >= 0){
					x = (int)(x/mActivePointers.size());
					y = (int)(y/mActivePointers.size());
					mPaint.setColor(Color.argb(75, 0, 0, 100));		
				    canvas.drawCircle(x, y, 50, mPaint);
				}else{
					playZone(-1);
				}
				
				//draw zones
				mPaint.setColor(Color.argb(90, 160, 160, 160));
			    mPaint.setStyle(Style.FILL);
			    
			   
			    //Zone 1
			    if(! r[0].contains(x, y)){	   
			    // draw
			    canvas.drawPath(polyPath[0], mPaint);
			    }else{
			    	playZone(0);
			    }
			    
			    //Zone 2
			    if(! r[1].contains(x, y)){
			    // draw
			    canvas.drawPath(polyPath[1], mPaint);	
			    }else{
			    	playZone(1);
			    }
			    
			    //Zone 3
			    if(! r[2].contains(x, y)){
			    // draw
			    canvas.drawPath(polyPath[2], mPaint);
			    }else{
			    	playZone(2);
			    }
				break;
			case 8:
				//Zone 1-2-3 play music
				
				//Draw background image
			    canvas.drawBitmap(mPaintingImage, null, dest, null);

				//find center point
				x = -1; y = -1;
				
				// draw all pointers
				for (int size = mActivePointers.size(), i = 0; i < size; i++) {
					PointF point = mActivePointers.valueAt(i);
					if(point != null){
						x+=(int)point.x;
						y+=(int)point.y;
					}
				}
				
				if (x >= 0 && y >= 0){
					x = (int)(x/mActivePointers.size());
					y = (int)(y/mActivePointers.size());
					mPaint.setColor(Color.argb(75, 0, 0, 100));		
				    canvas.drawCircle(x, y, 50, mPaint);
				}else{
					playZone(-1);
				}
				
				//draw zones
				mPaint.setColor(Color.argb(25, 128, 128, 128));
			    mPaint.setStyle(Style.FILL);

			    //Zone 1 - Angel
			    if(! r[0].contains(x, y)){	   
			    // draw
			    canvas.drawPath(polyPath[0], mPaint);
			    }else{
			    	playZone(0);
			    }
			    
				//Zone 2 - St. Francisc
			    if(! r[1].contains(x, y)){
			    // draw
			    canvas.drawPath(polyPath[1], mPaint);	
			    }else{
			    	playZone(1);
			    }
			    
				//Zone 3 - St. Benedict
			    if(! r[2].contains(x, y)){
			    // draw
			    canvas.drawPath(polyPath[2], mPaint);
			    }else{
			    	playZone(2);
			    }
				
				break;
			default:
		}
	}
	
	private int modulo(int m, int n) {
		int mod = m % n;
		return (mod < 0) ? mod + n : mod;
	}

	public void playZone(int zone){
		if (SCREEN_NUMBER == 7){
			//Stop all - no touch of the screen
			if(zone == -1){
				mMusicFader.stopMP3();
				_playZone = zone;
				return;
			}
			
			//User starts touching
			if (_playZone == -1 && zone > -1){
				mMusicFader.load(MY_LANGUAGE[LANGUAGE]+"z" + zone, false);
				mMusicFader.playMP3();
				_playZone = zone;
				return;
			}
			
			//User navigates in another zone
			if(_playZone != zone){
				mMusicFader.stopMP3();
				mMusicFader.load(MY_LANGUAGE[LANGUAGE]+"z" + zone, false);
				mMusicFader.playMP3();
				_playZone = zone;
				return;
			}
			
			//User stays in the same zone - a replay maybe?!?
			if(_playZone == zone && !mMusicFader.mediaPlayer.isPlaying()){
				mMusicFader.load(MY_LANGUAGE[LANGUAGE]+"z" + zone, false);
				mMusicFader.playMP3();
				return;
			}
		}
		
		if (SCREEN_NUMBER == 8){
			//Stop all - no touch of the screen
			if(zone == -1){
				mMusicFader.stopMP3();
				_playZone = zone;
				return;
			}
			
			//User starts touching
			if (_playZone == -1 && zone > -1){
				switch(zone){
				case 0:
					mMusicFader.load("angel", true);
					mMusicFader.play(1000);
					break;
				case 1:
					mMusicFader.load("francisc", true);
					mMusicFader.play(1000);
					break;
				case 2:
					mMusicFader.load("benedict", true);
					mMusicFader.play(1000);
					break;
				}
				_playZone = zone;
				return;
			}
			
			//User navigates in another zone
			if(_playZone != zone){
				switch(zone){
				case 0:
					mMusicFader.fadeTo("angel", 100);
					break;
				case 1:
					mMusicFader.fadeTo("francisc", 100);
					break;
				case 2:
					mMusicFader.fadeTo("benedict", 100);
					break;
				}
				_playZone = zone;
				return;
			}
			
			//User stays in the same zone - a replay maybe?!?
			if(_playZone == zone && !mMusicFader.mediaPlayer.isPlaying()){
				switch(zone){
				case 0:
					mMusicFader.load("angel", true);
					mMusicFader.play(1000);
					break;
				case 1:
					mMusicFader.load("francisc", true);
					mMusicFader.play(1000);
					break;
				case 2:
					mMusicFader.load("benedict", true);
					mMusicFader.play(1000);
					break;
				}
				return;
			}
		}
		
	}
	
	
	private void process3Taps() {
		switch(SCREEN_NUMBER){
			case 1:
				// Am ales limba
				mRunnableHandler.removeCallbacks(mLanguageScreenRunnable);
				mMusicFader.stopMP3();
				mMusicFader.load("chosen"+LANGUAGE, false);
				mMusicFader.playMP3();
				mMusicFader.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
							public void onCompletion(MediaPlayer mp) {
								// Explic cum functioneaza aplicatia
								mMusicFader.stopMP3();
								mMusicFader.load("explain"+LANGUAGE, false);
								mMusicFader.playMP3();
								SCREEN_NUMBER=6;
								invalidate();
								mMusicFader.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
											public void onCompletion(MediaPlayer mp) {
												SCREEN_NUMBER=7;
												invalidate();
											}
										});
							}
						});
				break;
			case 7:
				SCREEN_NUMBER=6;
				invalidate();
				mMusicFader.stopMP3();
				mMusicFader.load("beforemusic"+LANGUAGE, false);
				mMusicFader.playMP3();
				mMusicFader.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
					public void onCompletion(MediaPlayer mp) {
						SCREEN_NUMBER=8;
						invalidate();
					}
				});
				break;
			case 8:
				SCREEN_NUMBER=6;
				invalidate();
				mMusicFader.stopMP3();
				mMusicFader.load("thx"+LANGUAGE, false);
				mMusicFader.playMP3();
				mMusicFader.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
					public void onCompletion(MediaPlayer mp) {
						SCREEN_NUMBER=0;
						invalidate();
					}
				});
				break;
			default:
		}
		
	}
	
	Handler mRunnableHandler = new Handler();
	
	Runnable mLanguageScreenRunnable = new Runnable() {
		@Override
		public void run() {
			LANGUAGE = modulo(LANGUAGE + 1, 3);
			if (SCREEN_NUMBER == 1) invalidate();
			mMusicFader.load("choose" + LANGUAGE, false);
			mMusicFader.playMP3();
			mMusicFader.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
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
			mRunnableHandler.removeCallbacks(mLanguageScreenRunnable);
			SCREEN_NUMBER++;
			mMusicFader.stopMP3();
			mRunnableHandler.postDelayed(mLanguageScreenRunnable, 10);
		}
	};

	/**
	 * Schedules a call to a runnable in [delay] milliseconds, canceling any
	 * previously scheduled calls.
	 */
	public void showSplashScreen(int delayMillis) {
		mRunnableHandler.removeCallbacks(mSplashScreenRunnable);
		invalidate();
		mMusicFader.load("angel", false);
		mMusicFader.play(1000);
		mRunnableHandler.postDelayed(mSplashScreenRunnable, Math.min(mMusicFader.getMP3Duration(), delayMillis));
	}	
	
	void addPoint(float x, float y, int tapCount) {
		_points.add(new CustomPoint(new PointF(x, y), tapCount));
	}
	
	private class CustomPoint {
		@SuppressWarnings("unused")
		private PointF thePoint;
		@SuppressWarnings("unused")
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
