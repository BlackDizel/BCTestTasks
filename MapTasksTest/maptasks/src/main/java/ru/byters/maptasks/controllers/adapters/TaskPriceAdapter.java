package ru.byters.maptasks.controllers.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ru.byters.maptasks.R;
import ru.byters.maptasks.model.TaskPrice;

/**
 * Created by AlexMoto on 06.07.2015.
 */
public class TaskPriceAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<TaskPrice> items;

    private String sPrice, sDesc;

    public TaskPriceAdapter(Context context, List<TaskPrice> items) {
        inflater = LayoutInflater.from(context);
        this.items = items;
        sPrice = context.getResources().getString(R.string.price_task_cost);
        sDesc = context.getResources().getString(R.string.price_task_description);
    }

    @Override
    public int getCount() {
        if (items != null)
            return items.size();
        return 0;
    }

    @Override
    public TaskPrice getItem(int i) {
        if (items != null)
            if (i < items.size())
                return items.get(i);
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        Holder h = null;
        if (v == null) {
            v = inflater.inflate(R.layout.item_task_price, viewGroup, false);

            h = new Holder(sPrice, sDesc);
            h.findViews(v);
            v.setTag(h);
        }

        if (h == null) h = (Holder) v.getTag();

        h.setData(getItem(i));

        return v;
    }

    private class Holder {
        private TextView tvDesc;
        private TextView tvPrice;

        private String sPrice, sDesc;

        public void findViews(View v){
            tvDesc = (TextView) v.findViewById(R.id.tvDescription);
            tvPrice = (TextView) v.findViewById(R.id.tvPrice);
        }

        public Holder(String sPrice, String sDesc) {
            this.sPrice = sPrice;
            this.sDesc = sDesc;
        }

        void setData(TaskPrice taskPrice) {
            tvPrice.setText(sPrice);
            tvDesc.setText(sDesc);

            if (taskPrice != null) {
                tvPrice.setText(String.valueOf(taskPrice.getPrice()));
                tvDesc.setText(taskPrice.getDescription());
            }
        }
    }
}
