package com.technotalkative.staggeredgriddemo;


import java.util.ArrayList;

import android.graphics.Bitmap;
import android.view.View;

import com.etsy.android.grid.util.DynamicHeightImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.FailReason.FailType;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

/**
 * custom class with error handling and tries limit count to download image
 */
public class MyImgLoadListener implements ImageLoadingListener 
{
	private DynamicHeightImageView imgView;
	private int pos;
	private int tryNum;
	public static int maxTriesNum=3;
	public MyImgLoadListener(DynamicHeightImageView imgView,int pos, int tryNum)
	{
		this.imgView=imgView;
		this.pos=pos;	
		this.tryNum=tryNum;
	}

	@Override
	public void onLoadingCancelled(String arg0, View arg1) 
	{}

	@Override
	public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) 
	{
		imgView.setHeightRatio((double)arg2.getHeight()/(double)arg2.getWidth());		
	}

	@Override
	public void onLoadingFailed(String arg0, View arg1, FailReason arg2) 
	{
		if ((arg2.getType()==FailType.DECODING_ERROR)&&(tryNum<maxTriesNum))
		{
			ArrayList<String> l =StaggeredGridActivity.mData;
			l.set(pos, StaggeredGridActivity.generateUri());
			ImageLoader.getInstance().displayImage(l.get(pos), imgView, StaggeredGridActivity.options,
					new MyImgLoadListener (imgView,pos,++tryNum)); 
		}
	}

	@Override
	public void onLoadingStarted(String arg0, View arg1) 
	{}

}
