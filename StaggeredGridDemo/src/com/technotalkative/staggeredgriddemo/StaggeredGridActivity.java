//based on StaggeredGridDemo project: http://www.technotalkative.com/lazy-productive-android-developer-part-6-staggered-gridview/
package com.technotalkative.staggeredgriddemo;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.Button;

import com.etsy.android.grid.StaggeredGridView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class StaggeredGridActivity extends Activity implements AbsListView.OnScrollListener, OnClickListener  {

    private String SAVED_DATA_KEY = "SAVED_DATA";

    private Button btReset;
    private StaggeredGridView mGridView;
    private SgvAdapter mAdapter;
    private static Random r;
    private static float btAlpha=(float)1;
    
    public static DisplayImageOptions options;
	
    /**
     * list with image uri's to use in offline mode and adapter. can be edited by image load listener if server do not return image
     */
    public static ArrayList<String> mData;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sgv);
        
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).build();
        ImageLoader.getInstance().init(config);
        
        options = new DisplayImageOptions.Builder()
	    .cacheInMemory(true).cacheOnDisc(true)
	    .bitmapConfig(Bitmap.Config.RGB_565).build();
        r = new Random();
        
        if (savedInstanceState != null) 
            mData = savedInstanceState.getStringArrayList(SAVED_DATA_KEY);
        
        if (mData == null) 
            mData = generateData();
        
        mAdapter = new SgvAdapter(this);

        mGridView = (StaggeredGridView) findViewById(R.id.grid_view);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnScrollListener(this);
        
        btReset = (Button)findViewById(R.id.btReset);
        btReset.setOnClickListener(this);
        btReset.setAlpha(btAlpha);
    }
	
    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(SAVED_DATA_KEY, mData);
    }

    @Override
    public void onScrollStateChanged(final AbsListView view, final int scrollState) 
    {}

    @Override
    public void onScroll(
    		final AbsListView view, 
    		final int firstVisibleItem, 
    		final int visibleItemCount, 
    		final int totalItemCount) 
    {
    	//change button Reset visibility    	
    	if (firstVisibleItem<=10)
    	{
    		if (btReset.getVisibility()!=View.VISIBLE)
    			btReset.setVisibility(View.VISIBLE);
    		
    		btReset.setAlpha(1-firstVisibleItem/(float)10);    
    		btAlpha = 1-firstVisibleItem/(float)10;
   		}
    	else btReset.setVisibility(View.INVISIBLE);

    }

	@Override
	public void onClick(View arg0) 
	{
		ImageLoader.getInstance().stop();
		mData=generateData();
		mAdapter.notifyDataSetChanged();
	}

	/**
	 * generate random image uri
	 * @return image uri
	 */
	public static String generateUri()
	{
		int w = 100+r.nextInt(925);
		int h = 100+r.nextInt(925);
		String s = "http://placekitten.com/g/"+String.valueOf(w)+"/"+String.valueOf(h);		
		return s;
	}

	
    /**generate image uri's for display in list
     * @return list of image uri's
     */
	private ArrayList<String> generateData() 
	{
		ArrayList<String> listData = new ArrayList<String>();
    	for (int i=0;i<50;++i)
    		listData.add(generateUri());
    	return listData;
    }

	


}
