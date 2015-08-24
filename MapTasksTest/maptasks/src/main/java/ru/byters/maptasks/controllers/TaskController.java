package ru.byters.maptasks.controllers;

import android.app.Activity;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import ru.byters.maptasks.activities.MainActivity;
import ru.byters.maptasks.model.Task;
import ru.byters.maptasks.net.Client;

/**
 * Created by AlexMoto on 04.07.2015.
 */
public class TaskController {
    private List<Task> data;
    private static TaskController instance;
    private STATE state;
    private Client client;
    private final static String filename = "tasklist";

    public enum STATE {LOADING, IDLE}


    public static List<Task> parseJSON(String sJSON) {
        try {
            JSONObject jsonObject = new JSONObject(sJSON);
            if (!jsonObject.isNull("tasks")) {
                List<Task> list = new ArrayList<Task>();
                JSONArray jsonArray = jsonObject.getJSONArray("tasks");
                for (int i = 0; i < jsonArray.length(); ++i) {
                    Task task = Task.parseJSON(jsonArray.getJSONObject(i).toString());
                    if (task != null)
                        list.add(task);
                }
                if (list.size() > 0)
                    return list;
            }
        } catch (JSONException e) {
            return null;
        }
        return null;
    }

    public void save(Context context, List<Task> list) {
        data = list;

        ObjectOutputStream objectOut = null;

        try {
            FileOutputStream fileOut = context.openFileOutput(filename, Activity.MODE_PRIVATE);
            objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(list);
            fileOut.getFD().sync();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (objectOut != null)
                try {
                    objectOut.close();
                } catch (IOException e) {
                }
        }

    }

    private List<Task> load(Context context) {
        ObjectInputStream objectIn = null;
        List<Task> object = null;
        try {

            FileInputStream fileIn = context.getApplicationContext().openFileInput(filename);
            objectIn = new ObjectInputStream(fileIn);
            object = (List<Task>) objectIn.readObject();

        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (objectIn != null)
                try {
                    objectIn.close();
                } catch (IOException e) {
                }
        }
        return object;
    }

    public void downloadTasks(MainActivity activity) {
        android.util.Log.v("some","state "+state);
        if (state == STATE.IDLE) {
            client = new Client(activity);
            client.execute();
        }
    }

    public void update(MainActivity activity) {
        if (state == STATE.LOADING)
            if (client != null)
                client.setActivity(activity);
    }

    public List<Task> getTasks(Context context) {
        if (data == null)
            data = load(context);
        return data;
    }

    public static TaskController getInstance() {
        if (instance == null) instance = new TaskController();
        return instance;
    }

    private TaskController() {
        state = STATE.IDLE;
    }

    public void setState(STATE state) {
        this.state = state;
    }

    public STATE getState() {
        return state;
    }
}
