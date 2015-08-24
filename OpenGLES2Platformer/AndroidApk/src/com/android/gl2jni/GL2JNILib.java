package com.android.gl2jni;


public class GL2JNILib {

     static 
	 {
         System.loadLibrary("hello-gl2");
     }

    /**
     * @param width the current view width
     * @param height the current view height
     */
     public static native void init(int width, int height);
	 public static native void step();
	 public static native void touch(int x,int y);

	 
}
