package ru.byters.maptasks.controllers.stuff;

import android.content.Context;

import ru.yandex.yandexmapkit.overlay.balloon.BalloonItem;
import ru.yandex.yandexmapkit.utils.GeoPoint;

/**
 * Created by Sony on 04.07.2015.
 */
public class TaskBalloonItem extends BalloonItem {

    private int id;

    public TaskBalloonItem(Context context, GeoPoint geoPoint) {
        super(context, geoPoint);
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
