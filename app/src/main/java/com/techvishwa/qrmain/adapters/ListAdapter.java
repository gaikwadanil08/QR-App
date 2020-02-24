package com.techvishwa.qrmain.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.techvishwa.qrmain.pojo.PojoSave;
import com.techvishwa.qrmain.R;

import java.util.List;

public class ListAdapter extends ArrayAdapter<PojoSave> {

    public Activity context;
    private List<PojoSave> list;

    public ListAdapter(Activity context, List<PojoSave> list) {
        super(context, R.layout.list_layout, list);
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.list_layout, null, true);

        TextView id = listViewItem.findViewById(R.id.id);
        TextView name = listViewItem.findViewById(R.id.name);

        PojoSave pojoSave = list.get(position);

        id.setText(pojoSave.getId());
        name.setText(pojoSave.getName());

        return listViewItem;
    }
}

