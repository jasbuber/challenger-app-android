package com.cespenar.thechallenger;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cespenar.thechallenger.models.Challenge;

/**
 * Created by Jasbuber on 20/11/2015.
 */
public class CategoriesListAdapter extends BaseAdapter {

    Activity context;
    private static final Challenge.CHALLENGE_CATEGORY[] categories =
            Challenge.CHALLENGE_CATEGORY.values();

    public CategoriesListAdapter(Activity context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return categories.length;
    }

    @Override
    public Object getItem(int position) {
        return categories[position];
    }

    @Override
    public long getItemId(int position) {
        return categories[position].hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {

            holder = new ViewHolder();

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.list_item_challenge_categories, null);

            holder.categoryView = (TextView) convertView.findViewById(R.id.categories_list_category);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.id = categories[position].hashCode();
        holder.category = categories[position];
        holder.categoryView.setText(context.getResources().getStringArray(R.array.challenge_categories)[position]);


        return convertView;
    }

    public class ViewHolder {
        long id;
        Challenge.CHALLENGE_CATEGORY category;
        TextView categoryView;
    }

}