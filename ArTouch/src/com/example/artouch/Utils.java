package com.example.artouch;

import android.app.Activity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

/**
 * Static class that will contain all the methods generally used across the entire app. 
 * @author dorin_rautu
 * @version 1.0
 * @since 19.11.2013
 * @modifiedBy dorin_rautu
 */

public class Utils 
{

	/**
	 * Method that we'll set up the activity's mode to be full screen so that the status bar will
	 * not be displayed.
	 * @param theModifiedActivity the activity on which we'll remove the status bar.
	 * @author dorin_rautu
	 * @version 1.0
	 * @since 19.11.2013
	 * @modifiedBy dorin_rautu 
	 */
	public static void setFullScreenMode(Activity theModifiedActivity)
	{
		theModifiedActivity.requestWindowFeature(Window.FEATURE_NO_TITLE);
		theModifiedActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

}
