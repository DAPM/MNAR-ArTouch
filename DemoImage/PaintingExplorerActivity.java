package com.example.artouch;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.CountDownTimer;
import android.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.GridLayout.LayoutParams;

public class PaintingExplorerActivity extends Activity {
	
	Context context;
    TextView countdownTimerText;
//	ImageView[] zones;
	final int numberOfZones = 3;
	int end; // a 'checker' to see if every defined zone was visited
	
	private LruCache<String, Bitmap> mMemoryCache;

	public PaintingExplorerActivity() {
	
		

		// Get memory class of this device, exceeding this amount will throw an
		// OutOfMemory exception.
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

		// Use 1/8th of the available memory for this memory cache.
		final int cacheSize = maxMemory / 4;

		mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {

			protected int sizeOf(String key, Bitmap bitmap) {
				// The cache size will be measured in bytes rather than number
				// of items.
				return bitmap.getByteCount();
			}

		};
		
	}
	
	

	public long getItemId(int arg0) {
		return arg0;
	}

	public View getView(int arg0, View convertView, ViewGroup arg2) {
		ImageView img = null;

		if (convertView == null) {
			img = new ImageView(context);
			img.setScaleType(ImageView.ScaleType.CENTER_CROP);
			
		} else {
			img = (ImageView) convertView;
		}

//		int resId = context.getResources().getIdentifier("hqimage2", "drawable", getPackageName());
//
//		loadBitmap(resId, img);

		return img;
	}

	public void loadBitmap(int resId, ImageView imageView) {
		if (cancelPotentialWork(resId, imageView)) {
			final BitmapWorkerTask task = new BitmapWorkerTask(imageView);
			imageView.setBackgroundResource(R.drawable.hqimage4);
		task.execute(resId);
		}
	}

	static class AsyncDrawable extends BitmapDrawable {
		private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

		public AsyncDrawable(Resources res, Bitmap bitmap,
				BitmapWorkerTask bitmapWorkerTask) {
			super(res, bitmap);
			bitmapWorkerTaskReference = new WeakReference<BitmapWorkerTask>(
					bitmapWorkerTask);
		}

		public BitmapWorkerTask getBitmapWorkerTask() {
			return bitmapWorkerTaskReference.get();
		}
	}

	public static boolean cancelPotentialWork(int data, ImageView imageView) {
		final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

		if (bitmapWorkerTask != null) {
			final int bitmapData = bitmapWorkerTask.data;
			if (bitmapData != data) {
				// Cancel previous task
				bitmapWorkerTask.cancel(true);
			} else {
				// The same work is already in progress
				return false;
			}
		}
		// No task associated with the ImageView, or an existing task was
		// cancelled
		return true;
	}

	private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
		if (imageView != null) {
			final Drawable drawable = imageView.getDrawable();
			if (drawable instanceof AsyncDrawable) {
				final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
				return asyncDrawable.getBitmapWorkerTask();
			}
		}
		return null;
	}

	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		if (getBitmapFromMemCache(key) == null) {
			mMemoryCache.put(key, bitmap);
		}
	}

	public Bitmap getBitmapFromMemCache(String key) {
		return (Bitmap) mMemoryCache.get(key);
	}
	
	 class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap>{
		private final WeakReference<ImageView> imageViewReference;
		private int data = 0;

		public BitmapWorkerTask(ImageView imageView) {
		    // Use a WeakReference to ensure the ImageView can be garbage collected
		    imageViewReference = new WeakReference<ImageView>(imageView);
		}

		// Decode image in background.
		@Override
		protected Bitmap doInBackground(Integer... params) {
		    data = params[0];
		    final Bitmap bitmap = decodeSampledBitmapFromResource(
					getResources(), data, 100, 100);
			addBitmapToMemoryCache(String.valueOf(params[0]), bitmap);
			return bitmap;
		}

		// Once complete, see if ImageView is still around and set bitmap.
		@Override
		protected void onPostExecute(Bitmap bitmap) {
		    if (imageViewReference != null && bitmap != null) {
		        final ImageView imageView = imageViewReference.get();
		        if (imageView != null) {
		            imageView.setImageBitmap(bitmap);
		        }
		    }}}
		
	 public static Bitmap decodeSampledBitmapFromResource(Resources res,
				int resId, int reqWidth, int reqHeight) {

			// First decode with inJustDecodeBounds=true to check dimensions
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeResource(res, resId, options);

			// Calculate inSampleSize
			options.inSampleSize = 1;//calculateInSampleSize(options, reqWidth,reqHeight);

			// Decode bitmap with inSampleSize set
			options.inJustDecodeBounds = false;
			return BitmapFactory.decodeResource(res, resId, options);
		}

	 public static int calculateInSampleSize(BitmapFactory.Options options,
				int reqWidth, int reqHeight) {
			// Raw height and width of image
			final int height = options.outHeight;
			final int width = options.outWidth;
			int inSampleSize = 1;

			if (height > reqHeight || width > reqWidth) {

				// Calculate ratios of height and width to requested height and
				// width
				final int heightRatio = Math.round((float) height
						/ (float) reqHeight);
				final int widthRatio = Math.round((float) width / (float) reqWidth);

				// Choose the smallest ratio as inSampleSize value, this will
				// guarantee
				// a final image with both dimensions larger than or equal to the
				// requested height and width.
				inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
			}

			return inSampleSize;
		}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_painting_explorer3);
		findViewById(R.id.imageView4).setBackgroundResource(R.drawable.hqimage4);
//		loadBitmap(getResources().getIdentifier("hqimage4", "drawable", getPackageName()), (ImageView)findViewById(R.id.imageView4));
		
//		int resId = context.getResources().getIdentifier("hqimage2", "drawable", getPackageName());
//
//		loadBitmap(resId, img);

	}
		//I add the pictures I need
		
		//	ImageView img = (ImageView) findViewById(getResources().getIdentifier("imageView4", "id", getPackageName()));
			//img.setImageResource(getResources().getIdentifier("hqimage4", "drawable", getPackageName()));
		
		
		//countdown timer
		//countdownTimerText = (TextView) findViewById(R.id.countdownText);		

	}

	/*I define the actions I want my app to perform each time I click on a zone;
	 while an element is being visited, we prevent all other zones from being clicked;
	*/
	/*public void clickZona1(View v)
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
*/


