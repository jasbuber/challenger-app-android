package com.cespenar.thechallenger;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cespenar.thechallenger.models.User;
import com.cespenar.thechallenger.services.FacebookService;

import java.util.List;

/**
 * Created by Jasbuber on 23/09/2015.
 */
public class UserListAdapter extends BaseAdapter {

    Activity context;
    List<User> users;

    public UserListAdapter(Activity context, List<User> users) {
        this.context = context;
        this.users = users;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return users.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {

            holder = new ViewHolder();

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.list_item_best_users, null);

            holder.nameView = (TextView) convertView.findViewById(R.id.best_users_name);

            holder.ratingView = (TextView) convertView.findViewById(R.id.best_users_points);

            holder.pictureView = (ImageView) convertView.findViewById(R.id.best_users_profile_picture);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        User user = users.get(position);

        holder.id = user.hashCode();
        holder.username = user.getUsername();
        holder.nameView.setText(user.getFormattedName());
        holder.ratingView.setText(String.valueOf(user.getAllPoints()));
        FacebookService.getService().loadProfilePicture(context, holder.pictureView, user.getUsername());

        return convertView;
    }

    public class ViewHolder {
        long id;
        String username;
        TextView nameView;
        TextView ratingView;
        ImageView pictureView;
    }

    public List<User> getUsers() {
        return users;
    }
}
