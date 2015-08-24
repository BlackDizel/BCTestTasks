package ru.byters.maptasks.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import ru.byters.maptasks.R;
import ru.byters.maptasks.controllers.TaskController;
import ru.byters.maptasks.controllers.adapters.TaskPriceAdapter;
import ru.byters.maptasks.model.Task;

/**
 * Created by AlexMoto on 04.07.2015.
 */
public class TaskActivity extends Activity {
    public final static String INTENT_TASK_ID = "task_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        int id = getIntent().getIntExtra(INTENT_TASK_ID, 0);
        if (id != 0) {
            List<Task> list = TaskController.getInstance().getTasks(this);
            if (list != null) {
                Task task = null;
                for (Task item : list)
                    if (item.getId() == id) {
                        task = item;
                        break;
                    }

                if (task != null)
                    setData(task);

            }
        }
    }

    private void setData(Task task) {
        if (task.getDate() != null)
            ((TextView) findViewById(R.id.tvDate)).setText(new SimpleDateFormat("dd.MM.yyyy").format(task.getDate()));

        if (task.getText() != null)
            ((TextView) findViewById(R.id.tvText)).setText(task.getText());

        if (task.getLongText() != null)
            ((TextView) findViewById(R.id.tvLongText)).setText(task.getLongText());

        if (task.getLocationText() != null)
            ((TextView) findViewById(R.id.tvlocationText)).setText(task.getLocationText());

        if (task.getPrices() != null) {
            ((ListView) findViewById(R.id.lvPrices)).setAdapter(new TaskPriceAdapter(this, task.getPrices()));
        }

    }
}
