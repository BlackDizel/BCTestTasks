package ru.byters.maptasks.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import ru.byters.maptasks.R;
import ru.byters.maptasks.controllers.TaskController;
import ru.byters.maptasks.controllers.stuff.TaskBalloonItem;
import ru.byters.maptasks.model.Task;
import ru.yandex.yandexmapkit.MapController;
import ru.yandex.yandexmapkit.MapView;
import ru.yandex.yandexmapkit.OverlayManager;
import ru.yandex.yandexmapkit.overlay.Overlay;
import ru.yandex.yandexmapkit.overlay.OverlayItem;
import ru.yandex.yandexmapkit.overlay.balloon.BalloonItem;
import ru.yandex.yandexmapkit.overlay.balloon.OnBalloonListener;
import ru.yandex.yandexmapkit.utils.GeoPoint;

/**
 * Created by AlexMoto on 04.07.2015.
 */
public class MainActivity extends Activity implements OnBalloonListener {

    private OverlayManager mOverlayManager;
    private Overlay overlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitYandexMap();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (TaskController.getInstance().getState() == TaskController.STATE.IDLE) {
            List<Task> list = TaskController.getInstance().getTasks(this);
            if (list != null)
                drawTasks(list);
            else TaskController.getInstance().downloadTasks(this);
        } else TaskController.getInstance().update(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_update:
                TaskController.getInstance().downloadTasks(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //region map
    void InitYandexMap() {
        MapController mapController = ((MapView) findViewById(R.id.map)).getMapController();
        mOverlayManager = mapController.getOverlayManager();
        overlay = new Overlay(mapController);
    }

    @Override
    public void onBalloonHide(BalloonItem arg0) {
    }

    @Override
    public void onBalloonShow(BalloonItem arg0) {
    }

    @Override
    public void onBalloonAnimationEnd(BalloonItem arg0) {
    }

    @Override
    public void onBalloonAnimationStart(BalloonItem arg0) {
    }

    @Override
    public void onBalloonViewClick(BalloonItem arg0, View arg1) {
        Intent intent = new Intent(this, TaskActivity.class);
        intent.putExtra(TaskActivity.INTENT_TASK_ID, ((TaskBalloonItem) arg0).getId());
        startActivity(intent);
    }

    public void drawTasks(List<Task> list) {
        if (list == null) return;

        for (Task task : list) {
            OverlayItem oItem = new OverlayItem(new GeoPoint(task.getLat(), task.getLon()),
                    getResources().getDrawable(R.drawable.ymk_balloon_black));

            TaskBalloonItem bItem = new TaskBalloonItem(this, oItem.getGeoPoint());
            bItem.setText(task.getTitle());
            bItem.setId(task.getId());
            bItem.setOnBalloonListener(this);

            oItem.setBalloonItem(bItem);

            overlay.addOverlayItem(oItem);
        }
        if (mOverlayManager.getOverlays().contains(overlay))
            mOverlayManager.removeOverlay(overlay);
        mOverlayManager.addOverlay(overlay);

    }
    //endregion
}



