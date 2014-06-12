package ro.upb.ing.signac;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;

public class MusicHandler {

	public MediaPlayer mediaPlayer;
	private Context context;
	private int iVolume;

	private final static int INT_VOLUME_MAX = 100;
	private final static int INT_VOLUME_MIN = 0;
	private final static float FLOAT_VOLUME_MAX = 1f;
	private final static float FLOAT_VOLUME_MIN = 0f;
	
	private String nextMP3 = null;
	private int fadeIn = 0;
	
	private Handler mRunnableHandler = new Handler();
	
	public MusicHandler(Context context)
	{
	    this.context = context;
	}

	public void cleanUp(){
		stopMP3();
		mRunnableHandler.removeCallbacks(null);
	}
	
	public void load(String resourceID, boolean looping)
	{
		mediaPlayer = MediaPlayer.create(context, context.getResources().getIdentifier(resourceID, "raw",	context.getPackageName()));
	    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
	    mediaPlayer.setLooping(looping);
	}

	public int getMP3Duration(){
		if (mediaPlayer != null && mediaPlayer.isPlaying()) 
			return mediaPlayer.getDuration();
		return 0;
	}
	
	public void playMP3(){
		//if(mediaPlayer != null && mediaPlayer.isPlaying())
		//	stopMP3();
		mediaPlayer.start();
	}
	
	public void stopMP3(){
		if(mediaPlayer != null){
			mediaPlayer.stop();
			mediaPlayer.reset();
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}
	
	
	public void play(int fadeDuration)
	{
	    //Set current volume to min, and fade in
	    iVolume = INT_VOLUME_MIN;
	    updateVolume(0);

	    //Play music
	    if(!mediaPlayer.isPlaying()) mediaPlayer.start();

	    //Start increasing volume in increments
	    if(fadeDuration > 0)
	    {
	        // calculate delay, cannot be zero, set to 1 if zero
	        fadeIn = Math.max(1, (int)(fadeDuration/INT_VOLUME_MAX));
	        mRunnableHandler.postDelayed(mPlay, fadeIn*10);
	    }
	}

	public void stop(int fadeDuration)
	{
	    try {
	        // Set current volume, depending on fade or not
	        if (fadeDuration > 0)
	            iVolume = INT_VOLUME_MAX;
	        else
	            iVolume = INT_VOLUME_MIN;

	        updateVolume(0);

	        // Start increasing volume in increments
	        if (fadeDuration > 0)
	        {
	            final Timer timer = new Timer(true);
	            TimerTask timerTask = new TimerTask()
	            {
	                @Override
	                public void run()
	                {
	                    updateVolume(-5);
	                    if (iVolume == INT_VOLUME_MIN)
	                    {
	                        // Stop music
	                        mediaPlayer.stop();
	                        mediaPlayer.release();
	                        mediaPlayer = null;
	                        timer.cancel();
	                        timer.purge();
	                    }
	                }
	            };

	            // calculate delay, cannot be zero, set to 1 if zero
	            int delay = fadeDuration / INT_VOLUME_MAX;
	            if (delay == 0)
	                delay = 1;

	            timer.schedule(timerTask, delay, delay);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	}
	
	public void pause(int fadeDuration)
	{
	    //Set current volume, depending on fade or not
	    if (fadeDuration > 0) 
	        iVolume = INT_VOLUME_MAX;
	    else 
	        iVolume = INT_VOLUME_MIN;

	    updateVolume(0);

	    //Start increasing volume in increments
	    if(fadeDuration > 0)
	    {
	        final Timer timer = new Timer(true);
	        TimerTask timerTask = new TimerTask() 
	        {
	            @Override
	            public void run() 
	            {   
	                updateVolume(-5);
	                if (iVolume == INT_VOLUME_MIN)
	                {
	                    //Pause music
	                    if (mediaPlayer.isPlaying()) mediaPlayer.pause();
	                    timer.cancel();
	                    timer.purge();
	                }
	            }
	        };

	        // calculate delay, cannot be zero, set to 1 if zero
	        int delay = fadeDuration/INT_VOLUME_MAX;
	        if (delay == 0) delay = 1;

	        timer.schedule(timerTask, delay, delay);
	    }           
	}

	private void updateVolume(int change)
	{
	    //increment or decrement depending on type of fade
	    iVolume = iVolume + change;

	    //ensure iVolume within boundaries
	    if (iVolume < INT_VOLUME_MIN)
	        iVolume = INT_VOLUME_MIN;
	    else if (iVolume > INT_VOLUME_MAX)
	        iVolume = INT_VOLUME_MAX;

	    //convert to float value
	    float fVolume = 1 - ((float) Math.log(INT_VOLUME_MAX - iVolume) / (float) Math.log(INT_VOLUME_MAX));

	    //ensure fVolume within boundaries
	    if (fVolume < FLOAT_VOLUME_MIN)
	        fVolume = FLOAT_VOLUME_MIN;
	    else if (fVolume > FLOAT_VOLUME_MAX)
	        fVolume = FLOAT_VOLUME_MAX;     
	    if(mediaPlayer != null)	 mediaPlayer.setVolume(fVolume, fVolume);
	}
	
	public void stopAndRelease(int fadeDuration) {
    	mRunnableHandler.postDelayed(mStopAndRelease, 100);
	}
	
	public void fadeTo(String resourceId, int fadeDuration){
	    nextMP3 = resourceId;
	    fadeIn = fadeDuration;
		mRunnableHandler.postDelayed(mFadeToRunnable, fadeIn);
	}
	
	
	Runnable mPlay = new Runnable(){

		@Override
		public void run() {
			updateVolume(5);
            if (iVolume == INT_VOLUME_MAX)
            {
            	mRunnableHandler.removeCallbacks(mPlay);
            }else{
            	mRunnableHandler.postDelayed(mPlay, fadeIn*10);
            }
		}
		
	};
	
	Runnable mStopAndRelease = new Runnable(){

		@Override
		public void run() {
			updateVolume(-5);
            if (iVolume == INT_VOLUME_MIN)
            {
                // Stop and Release player after Pause music
                stopMP3();
                mRunnableHandler.removeCallbacks(mStopAndRelease);
            }else{
            	mRunnableHandler.postDelayed(mStopAndRelease, 100);
            }
			
		}
		
	};
	
	Runnable mFadeToRunnable = new Runnable() {
		@Override
		public void run() {
			updateVolume(-5);
			if (iVolume == INT_VOLUME_MIN){
				// Stop and Release player after Pause music
                stopMP3();
                load(nextMP3,true);
				play(fadeIn);
				mRunnableHandler.removeCallbacks(mFadeToRunnable);
			}else{
				mRunnableHandler.postDelayed(mFadeToRunnable, fadeIn);
			}
		}
	};
	
}
