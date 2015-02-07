package ru.byters.contactlist;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class LocalStorage {
	
	/**save contact info to preferences
	 * @param info contact info to save
	 * @param ctx activity to preferences save
	 * @see ProfileInfo
	 * **/
	public static void saveData(Activity ctx, ProfileInfo info)
	{
		SharedPreferences sharedPref = ctx.getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
				
		editor.putString(ctx.getString(R.string.saved_id), 			info.getID());
		editor.putString(ctx.getString(R.string.saved_name), 		info.getName());
		editor.putString(ctx.getString(R.string.saved_second_name), info.getSecondName());
		editor.putString(ctx.getString(R.string.saved_phone), 		info.getPhone());
				
		editor.commit();
	}
	
	/**load contact info from preferences
	 * @return contact info
	 * @see ProfileInfo**/
	public static ProfileInfo LoadData(Activity ctx)
	{
		ProfileInfo info = new ProfileInfo();
		
		SharedPreferences settings = ctx.getPreferences(Context.MODE_PRIVATE);

		info.setID( 		settings.getString(ctx.getString(R.string.saved_id)			,""));
		info.setName( 		settings.getString(ctx.getString(R.string.saved_name)		,ctx.getString(R.string.default_name)));
		info.setSecondName( settings.getString(ctx.getString(R.string.saved_second_name),ctx.getString(R.string.default_second_name)));
		info.setPhone( 		settings.getString(ctx.getString(R.string.saved_phone)		,ctx.getString(R.string.default_phone)));
		
		
		return info;
	}

}
