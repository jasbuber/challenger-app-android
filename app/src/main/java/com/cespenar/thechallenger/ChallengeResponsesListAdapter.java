package com.cespenar.thechallenger;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cespenar.thechallenger.models.Challenge;
import com.cespenar.thechallenger.models.ChallengeResponse;
import com.cespenar.thechallenger.models.ChallengeWithParticipantsNr;

import java.util.List;

/**
 * Created by Jasbuber on 23/09/2015.
 */
public class ChallengeResponsesListAdapter extends BaseAdapter {

    Activity context;
    List<ChallengeResponse> responses;

    public ChallengeResponsesListAdapter(Activity context, List<ChallengeResponse> responses) {
        this.context = context;
        this.responses = responses;
    }

    @Override
    public int getCount() {
        return responses.size();
    }

    @Override
    public Object getItem(int position) {
        return responses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return responses.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {

            holder = new ViewHolder();

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.list_item_challenge_responses, null);

            holder.usernameView = (TextView) convertView.findViewById(R.id.challenge_responses_username);

            holder.thumbnailView = (ImageView) convertView.findViewById(R.id.challenge_responses_thumbnail);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ChallengeResponse response = responses.get(position);

        holder.id = response.getId();
        holder.usernameView.setText(response.getChallengeParticipation().getParticipator().getFormattedName());
        holder.thumbnailView.setImageResource(R.drawable.player);

        return convertView;
    }

    public class ViewHolder {
        long id;
        TextView usernameView;
        ImageView thumbnailView;
    }
}