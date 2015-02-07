package ru.byters.contactlist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ActivityMain extends Activity implements OnClickListener{

	String id;
	public static final int CONTACT_PICKER_RESULT = 0;
	BookUtils bookUtils;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	
		((Button)findViewById(R.id.btOpen)).setOnClickListener(this);
		id=getResources().getString(R.string.saved_id);
	}

	@Override
	protected void onResume() {
		super.onResume();
		fillData(this);
	}
	
	@Override
	protected void onPause() {
		saveData(this);
		super.onPause();
	}
	
	/**	try to get profile info from contacts. if id is not changed, load from local**/
	private void fillData(Activity ctx)
	{	
		SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
		
		String contactId;
		if (!sharedPref.contains(getString(R.string.saved_id))) contactId=id;
		else contactId = sharedPref.getString(getString(R.string.saved_id), id);
		
		ProfileInfo info;
		
		if (!contactId.equals(id)){	
			info = fillInfo();			
		}
		else{			
			info = LocalStorage.LoadData(ctx);
		}
				
		id = info.getID();
		((TextView)findViewById(R.id.tvFirstName	)).setText(info.getName());
		((TextView)findViewById(R.id.tvSecondName	)).setText(info.getSecondName());			
		((TextView)findViewById(R.id.tvPhone		)).setText(info.getPhone());
	
	}
	
	/**save contact data to preferences**/
	private void saveData(Activity ctx)
	{				
		ProfileInfo info = new ProfileInfo();
		
		info.setID(id);	
		
		info.setName		(((TextView)findViewById(R.id.tvFirstName	)).getText().toString());		
		info.setSecondName	(((TextView)findViewById(R.id.tvSecondName	)).getText().toString());		
		info.setPhone		(((TextView)findViewById(R.id.tvPhone		)).getText().toString());
			
		LocalStorage.saveData(ctx, info);
	}
	
	/**navigate to contact picker activity**/
	@Override
	public void onClick(View v) {
	    Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
	    startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);		
	}
	
	/**get result from contact picker activity**/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode==CONTACT_PICKER_RESULT)
			if (resultCode==Activity.RESULT_OK){				
				Uri contactData = data.getData();				
				Cursor c =  getContentResolver().query(contactData, null, null, null, null);				
				if (c.moveToFirst()) {
					id = c.getString(c.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
		        }		       
			}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	/**get contact info from contacts
	 * @return contact info
	 * @see ru.byters.contactlist.ProfileInfo**/
	ProfileInfo fillInfo()
	{
		ProfileInfo info = new ProfileInfo();
		info.setID(id);
				
		if (bookUtils==null) bookUtils = new BookUtils(this);	        	
    		
		Cursor ci = bookUtils.getFamilyInfo(info.getID());
    	if (ci.moveToFirst())
    	{
    		String family = ci.getString(ci.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME));
	        String given = ci.getString(ci.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME));
	        
	        if (given!=null)
    	    info.setName(given);
	        else 
	        	info.setName(getString(R.string.default_name));
			if (family!=null)
				info.setSecondName(family);
			else
				info.setSecondName(getString(R.string.default_second_name));				
    	}
    	else
    	{
    		info.setName(getString(R.string.default_name));
    		info.setSecondName(getString(R.string.default_second_name));    		
    	}
    	
    	String phone = bookUtils.getPhoneByID(info.getID());
    	if (phone!=null) info.setPhone(phone);
    	else info.setPhone(getString(R.string.default_phone));
    	
    	return info;
	}
	
	
}
