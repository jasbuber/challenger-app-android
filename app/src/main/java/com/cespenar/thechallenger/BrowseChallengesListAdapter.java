package com.cespenar.thechallenger;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.cespenar.thechallenger.models.Challenge;
import com.cespenar.thechallenger.services.FacebookService;

import java.util.List;

/**
 * Created by Jasbuber on 27/08/2015.
 */
public class BrowseChallengesListAdapter extends BaseAdapter {

    Activity context;
    List<Challenge> challenges;

    public BrowseChallengesListAdapter(Activity context, List<Challenge> challenges) {
        this.context = context;
        this.challenges = challenges;
    }

    @Override
    public int getCount() {
        return challenges.size();
    }

    @Override
    public Object getItem(int position) {
        return challenges.get(position);
    }

    @Override
    public long getItemId(int position) {
        return challenges.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {

            holder = new ViewHolder();

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.list_item_browse_challenges, null);

            holder.nameView = (TextView) convertView.findViewById(R.id.browse_challenges_name);
            holder.usernameView = (TextView) convertView.findViewById(R.id.browse_challenges_username);
            holder.pictureView = (ImageView) convertView.findViewById(R.id.browse_challenges_picture);
            holder.ratingView = (RatingBar) convertView.findViewById(R.id.browse_challenges_rating);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Challenge challenge = challenges.get(position);

        holder.id = challenge.getId();
        holder.challenge = challenge;
        holder.nameView.setText(challenge.getName());
        holder.usernameView.setText(challenge.getCreator().getFormattedName());
        holder.ratingView.setRating(challenge.getRating());
        FacebookService.getService().loadProfilePicture(holder.pictureView, holder.challenge.getCreator().getProfilePictureUrl());

        return convertView;
    }

    public class ViewHolder {
        Challenge challenge;
        long id;
        TextView nameView;
        TextView usernameView;
        ImageView pictureView;
        RatingBar ratingView;
    }

    public List<Challenge> getChallenges(){
        return this.challenges;
    }
}
