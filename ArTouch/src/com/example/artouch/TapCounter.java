package com.example.mnar_task4;

import android.os.CountDownTimer;

public class TapCounter extends CountDownTimer  {
	
	public TapCounter(long millisInFuture, long countDownInterval) {
         super(millisInFuture, countDownInterval);
     }

     @Override
     public void onFinish() {
     }

     @Override
     public void onTick(long millisUntilFinished) {
     }

    
	public void resetCounter() {
		// TODO Auto-generated method stub
		start();
	} 
}
