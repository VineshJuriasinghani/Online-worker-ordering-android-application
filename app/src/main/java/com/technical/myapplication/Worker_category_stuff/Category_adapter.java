package com.technical.myapplication.Worker_category_stuff;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.technical.myapplication.R;

import java.util.List;

public class Category_adapter extends BaseAdapter {

private    Context context;
private List<category_modelClass> data;

    public Category_adapter(Context context, List<category_modelClass> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data!=null ? data.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public String getItemName(int position){return data.get(position).getName();}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

    View rootview =LayoutInflater.from(context)
            .inflate(R.layout.workers_category,parent,false);

        TextView name = rootview.findViewById(R.id.item_name);
        ImageView image = rootview.findViewById(R.id.img);



        name.setText(data.get(position).getName());
        image.setImageResource(data.get(position).getImage());

        return rootview;

    }
}
