package com.android.gl2jni;


import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;


public class GL2JNIActivity extends Activity {

    GL2JNIView mView;
    @Override protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        mView = new GL2JNIView(getApplication());
    	setContentView(mView);
    }

    @Override protected void onPause() {
        super.onPause();
        mView.onPause();
    }

    @Override protected void onResume() {
        super.onResume();
        mView.onResume();
    }
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		for (int i=0;i<event.getPointerCount();++i)
			GL2JNILib.touch((int)event.getX(i),(int)event.getY(i));
        return true;
    }
    //w 12 = 1/5  
	//h 18 = 1/3 



}
