package com.example.artouch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


/**
 * Class extending BroadcastReceiver. It will be notified when the device booted up. The
 * method onRecive is called automatically after boot.  
 * @author dorin_rautu
 * @version 1.0
 * @since 19.11.2013
 * @modifiedBy dorin_rautu
 */

public class BootUpReceiver extends BroadcastReceiver{

	
    @Override
    public void onReceive(Context context, Intent intent) {
            Intent i = new Intent(context, MainActivity.class);  
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);  
    }
}