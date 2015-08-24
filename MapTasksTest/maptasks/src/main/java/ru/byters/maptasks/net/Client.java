package ru.byters.maptasks.net;

import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;

import ru.byters.maptasks.BuildConfig;
import ru.byters.maptasks.R;
import ru.byters.maptasks.activities.MainActivity;
import ru.byters.maptasks.controllers.TaskController;
import ru.byters.maptasks.model.Task;

/**
 * Created by AlexMoto on 04.07.2015.
 */
public class Client extends AsyncTask<Void, Void, String> {
    private MainActivity activity;

    public Client(MainActivity activity) {
        setActivity(activity);
    }

    public void setActivity(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (TaskController.getInstance().getState() == TaskController.STATE.LOADING) {
            if (activity != null)
                Toast.makeText(activity, activity.getResources().getString(R.string.toast_downloading_yet), Toast.LENGTH_SHORT).show();
            cancel(true);
        } else {
            if (activity != null)
                Toast.makeText(activity, activity.getResources().getString(R.string.toast_downloading), Toast.LENGTH_SHORT).show();
            TaskController.getInstance().setState(TaskController.STATE.LOADING);
        }
    }

    @Override
    protected String doInBackground(Void... voids) {
        if (activity == null) return null;

        String uri = activity.getResources().getString(R.string.api_uri);
        if (BuildConfig.DEBUG)
            android.util.Log.v("client response: ", uri);
        try {
            return EntityUtils.toString(new DefaultHttpClient().execute(new HttpGet(uri)).getEntity(), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        android.util.Log.v("some","downlaoded");


        if (s != null) {
            List<Task> lResult = TaskController.parseJSON(s);
            if (activity != null) {
                if (lResult != null) {
                    Toast.makeText(activity, activity.getResources().getString(R.string.toast_downloaded), Toast.LENGTH_SHORT).show();
                    activity.drawTasks(lResult);
                    TaskController.getInstance().save(activity, lResult);
                } else
                    Toast.makeText(activity, activity.getResources().getString(R.string.toast_downloading_error), Toast.LENGTH_SHORT).show();
            }
        }
        TaskController.getInstance().setState(TaskController.STATE.IDLE);
    }
}
