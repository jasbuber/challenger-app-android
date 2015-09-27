package com.cespenar.thechallenger;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cespenar.thechallenger.models.Challenge;

import java.util.List;

/**
 * Created by Jasbuber on 25/09/2015.
 */
public class RankingsChallengeListAdapter extends BaseAdapter {

    Activity context;
    List<Challenge> challenges;

    public RankingsChallengeListAdapter(Activity context, List<Challenge> challenges) {
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

            convertView = inflater.inflate(R.layout.list_item_best_challenges, null);
            holder.nameView = (TextView) convertView.findViewById(R.id.best_challenges_name);
            holder.ratingView = (TextView) convertView.findViewById(R.id.best_challenges_rating);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Challenge challenge = challenges.get(position);

        holder.id = challenge.getId();
        holder.challenge = challenge;
        holder.nameView.setText(challenge.getName());
        holder.ratingView.setText(String.valueOf(challenge.getRating()));

        return convertView;
    }

    public class ViewHolder {
        Challenge challenge;
        long id;
        TextView nameView;
        TextView ratingView;
    }
}

