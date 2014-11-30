package com.technotalkative.staggeredgriddemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.etsy.android.grid.util.DynamicHeightImageView;
import com.nostra13.universalimageloader.core.ImageLoader;


public class SgvAdapter extends BaseAdapter {
	
	private final LayoutInflater mLayoutInflater;
	
	public SgvAdapter(Context context) 
	{
		this.mLayoutInflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(final int position, View convertView, final ViewGroup parent) 
	{		
		final ViewHolder vh;
		if (convertView == null) 
		{
			convertView = mLayoutInflater.inflate(R.layout.row_grid_item, parent, false);
			vh = new ViewHolder();
			vh.imgView = (DynamicHeightImageView) convertView.findViewById(R.id.imgView);
			convertView.setTag(vh);
		} 
		else 
			vh = (ViewHolder) convertView.getTag();
		
		ImageLoader.getInstance()
				.displayImage(	getItem(position), vh.imgView, StaggeredGridActivity.options, 
								new MyImgLoadListener(vh.imgView,position,0)); 		
		
		return convertView;
	}
	
	static class ViewHolder 
	{
		DynamicHeightImageView imgView;
	}

	@Override
	public int getCount() {
		return StaggeredGridActivity.mData.size();
	}

	@Override
	public String getItem(int arg0) {
		return StaggeredGridActivity.mData.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	

}