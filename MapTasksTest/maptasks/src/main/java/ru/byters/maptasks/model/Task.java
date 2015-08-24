package ru.byters.maptasks.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by AlexMoto on 04.07.2015.
 */
public class Task implements Serializable {
    private double lat, lon;
    private String title;
    private int id;
    private Date date;
    private String text;
    private String longText;
    private String locationText;
    private List<TaskPrice> prices;

    public String getTitle() {
        return title;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public static Task parseJSON(String sJSON) {
        try {
            JSONObject jsonObject = new JSONObject(sJSON);
            Task task = new Task();
            if (jsonObject.isNull("ID") || jsonObject.isNull("location"))
                return null;

            JSONObject jsonLocation = jsonObject.getJSONObject("location");
            if (jsonLocation == null) return null;

            task.setId(jsonObject.getInt("ID"));

            if (!jsonObject.isNull("title"))
                task.setTitle(jsonObject.getString("title"));

            if (!jsonObject.isNull("date"))
                task.setDate(new Date(jsonObject.getLong("date")));

            if (!jsonObject.isNull("text"))
                task.setText(jsonObject.getString("text"));

            if (!jsonObject.isNull("longText"))
                task.setLongText(jsonObject.getString("longText"));

            if (!jsonObject.isNull("locationText"))
                task.setLocationText(jsonObject.getString("locationText"));

            if (!jsonLocation.isNull("lat"))
                task.setLat(jsonLocation.getDouble("lat"));

            if (!jsonLocation.isNull("lon"))
                task.setLon(jsonLocation.getDouble("lon"));

            if (!jsonObject.isNull("prices")) {
                JSONArray jsonPrices = jsonObject.getJSONArray("prices");
                List<TaskPrice> list = new ArrayList<TaskPrice>();
                for (int i = 0; i < jsonPrices.length(); ++i) {
                    JSONObject jsonItem = jsonPrices.getJSONObject(i);
                    if (!jsonItem.isNull("price") || !jsonItem.isNull("description")) {
                        TaskPrice item = new TaskPrice();

                        if (!jsonItem.isNull("price"))
                            item.setPrice(jsonItem.getInt("price"));
                        if (!jsonItem.isNull("description"))
                            item.setDescription(jsonItem.getString("description"));
                        list.add(item);
                    }
                }
                if (list.size() > 0)
                    task.setPrices(list);
            }

            return task;

        } catch (JSONException e) {
            return null;
        }
    }

    public int getId() {
        return id;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLongText() {
        return longText;
    }

    public void setLongText(String longText) {
        this.longText = longText;
    }

    public String getLocationText() {
        return locationText;
    }

    public void setLocationText(String locationText) {
        this.locationText = locationText;
    }

    public List<TaskPrice> getPrices() {
        return prices;
    }

    public void setPrices(List<TaskPrice> prices) {
        this.prices = prices;
    }
}
