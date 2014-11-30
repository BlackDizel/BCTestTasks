package ru.byters.vktest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

public class MainActivity extends Activity {

	//список для вывода пользователей
	ListView lv;
	//индикатор загрузки данных
	ProgressBar pb;
	//источник данных для списка
	ArrayList<UserInfo> listData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		lv = (ListView)findViewById(R.id.listView1);
		pb = (ProgressBar)findViewById(R.id.progressBar1);
		pb.setVisibility(View.INVISIBLE);
		
		//по нажатию на список загрузить данные по id пользователя
		lv.setOnItemClickListener(new OnItemClickListener() 
		{			
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) 
			{
			    String uId = (String) arg1.getTag();		    	
			    new DownloadDataTask().execute(uId);
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		
		getMenuInflater().inflate(R.menu.main, menu);
		
		MenuItem item = menu.findItem(R.id.menu_search);
		SearchView searchView = (SearchView)item.getActionView();
		searchView.setOnQueryTextListener(new OnQueryTextListener() 
		{			
			@Override
			public boolean onQueryTextSubmit(String query) {
				//сменить видимость ui объектов
				lv.setVisibility(View.INVISIBLE);
				pb.setVisibility(View.VISIBLE);
				//по нажатию на кнопку поиска попытаться загрузить данные
				new DownloadDataTask().execute(query);
				return false;
			}
			
			@Override
			public boolean onQueryTextChange(String newText) {
				// TODO Auto-generated method stub
				return false;
			}
		});
        
        

		   return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
	
		return super.onOptionsItemSelected(item);
	}
	
	//Заглушка метода вывода ошибки загрузки данных
	void showError()
	{
		//for example show toast with error message
		pb.setVisibility(View.INVISIBLE);
	}
	
	void setAdapter(String data)
	{
		//парсим JSON, полученный от ВК
		try
    	{
			listData = new ArrayList<UserInfo>();
			JSONObject o= new JSONObject(data);
        	JSONArray a= o.getJSONArray("response");
        	    	
    		for (int i=0;i<a.length();++i)
    		{    			
    			UserInfo c = new UserInfo();
    			c.first_name = a.getJSONObject(i).getString("first_name");
    			c.last_name = a.getJSONObject(i).getString("last_name");    			
    			c.id = a.getJSONObject(i).getString("uid");
    			listData.add(c);
    		}	
    	}
    	catch (Exception e)
    	{
    		showError();
    	}
		
		//задаем адаптер списка с внесением ID пользователей в тэг
		@SuppressWarnings({ "rawtypes", "unchecked" })
        ArrayAdapter mAdapter = new ArrayAdapter(this, R.layout.list_item, R.id.tvName, listData) 
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) 
            {
                View view = super.getView(position, convertView, parent);
                TextView tvName = 	(TextView) view.findViewById(R.id.tvName);
                
                tvName.setText(	listData.get(position).first_name+" "+listData.get(position).last_name);	                
                
                view.setTag(	listData.get(position).id);
                
                return view;
            }
        };
        lv.setAdapter(mAdapter);
        //меняем видимость объектов
        lv.setVisibility(View.VISIBLE);
        pb.setVisibility(View.INVISIBLE);
        
    	
	}
	
	private class DownloadDataTask extends AsyncTask<String, Void, String> 
	{			
        protected String doInBackground(String... params) 
        {        	
        	StringBuilder builder = new StringBuilder();
        	HttpClient client = new DefaultHttpClient();
        		
        	StringBuilder s = new StringBuilder();
        	s.append("http://api.vk.com/method/friends.get?user_id=");
        	s.append(params[0]);
        	s.append("&fields=first_name,last_name");
        	
        	HttpGet httpGet = new HttpGet(s.toString());
            
        	try 
        	{
              HttpResponse response = client.execute(httpGet);
              StatusLine statusLine = response.getStatusLine();
              int statusCode = statusLine.getStatusCode();
              if (statusCode == 200) 
              {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) 
                {
                  builder.append(line);
                 	
                }
              } else
            	 return ""; //raise some error 
            } catch (ClientProtocolException e) 
            {
            	return "";
            }
             catch (IOException e) 
             {
            	return "";
             }
        	
            return builder.toString();
        }

        protected void onPostExecute(String result) 
        {	 
        	if (result!="")
        		setAdapter(result);
    		else
    			showError();
        }
        
       
	}
	

}
