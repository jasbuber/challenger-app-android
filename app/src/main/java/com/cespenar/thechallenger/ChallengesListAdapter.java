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
import com.cespenar.thechallenger.models.ChallengeWithParticipantsNr;

import java.util.List;

/**
 * Created by Jasbuber on 19/09/2015.
 */
public class ChallengesListAdapter extends BaseAdapter {

    Activity context;
    List<ChallengeWithParticipantsNr> challenges;

    public ChallengesListAdapter(Activity context, List<ChallengeWithParticipantsNr> challenges) {
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
        return challenges.get(position).getChallengeId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {

            holder = new ViewHolder();

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.list_item_challenges, null);

            holder.nameView = (TextView) convertView.findViewById(R.id.challenges_list_name);

            holder.popularityView = (ImageView) convertView.findViewById(R.id.challenges_list_popularity);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ChallengeWithParticipantsNr challenge = challenges.get(position);

        holder.id = challenge.getChallengeId();
        holder.nameView.setText(challenge.getChallengeName());
        holder.popularityView
                .setImageResource(getPopularityLevelImage(challenge.getParticipantsNr()));

        return convertView;
    }

    private int getPopularityLevelImage(long participantsNr) {
        int popularityLevel = Challenge.getPopularityLevel(participantsNr);

        switch (popularityLevel) {
            case 1:
                return R.drawable.speedometer_1_medium;
            case 2:
                return R.drawable.speedometer_2_medium;
            case 3:
                return R.drawable.speedometer_3_medium;
            case 4:
                return R.drawable.speedometer_4_medium;
            case 5:
                return R.drawable.speedometer_5_medium;
            default:
                return R.drawable.speedometer_1_medium;
        }
    }

    public class ViewHolder {
        long id;
        TextView nameView;
        ImageView popularityView;
    }
}