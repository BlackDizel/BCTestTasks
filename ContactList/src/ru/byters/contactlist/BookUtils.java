package ru.byters.contactlist;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

public class BookUtils {
	
	Context ctx;
	
	public BookUtils(Context ctx){
		this.ctx=ctx;
	}
	
	/**return contact phone by lookup_key
	 * @param id - lookup key of contact
	 * @return phone string**/
	public String getPhoneByID(String id)
	{
		Cursor secC = ctx.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY +" = ?",
                new String[]{id}, 
                null);
    	if (secC.moveToFirst())
    		return secC.getString(secC.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
    	
    	return null;
	}
	
	/**return cursor to get contact info: given and family names
	 * @param id - lookup key of contact **/
	public Cursor getFamilyInfo(String id)
	{
		String whereName = ContactsContract.Data.MIMETYPE + " = ? AND " + ContactsContract.CommonDataKinds.StructuredName.LOOKUP_KEY + " = ?";
		String[] whereNameParams = new String[] { ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE, id };
	    return ctx.getContentResolver().query(
	    		ContactsContract.Data.CONTENT_URI, 
	    		new String[] {	ContactsContract.CommonDataKinds.StructuredName.LOOKUP_KEY, 
	    						ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,
	    						ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME
	    						},
			    whereName, 
	    		whereNameParams, 
	    		null);
	    
	}


}
