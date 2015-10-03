package com.cespenar.thechallenger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cespenar.thechallenger.models.Challenge;
import com.cespenar.thechallenger.models.ChallengeResponse;
import com.cespenar.thechallenger.models.ChallengeWithParticipantsNr;
import com.cespenar.thechallenger.services.ChallengeService;
import com.cespenar.thechallenger.services.FacebookService;

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

            holder.submittedView = (TextView) convertView.findViewById(R.id.challenge_responses_submitted);

            holder.acceptView = (Button) convertView.findViewById(R.id.challenge_response_accept);

            holder.rejectView = (Button) convertView.findViewById(R.id.challenge_response_reject);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ChallengeResponse response = responses.get(position);

        holder.id = response.getId();
        holder.usernameView.setText(response.getChallengeParticipation().getParticipator().getFormattedName());
        holder.submittedView.setText(response.getSubmitted());
        holder.acceptView.setOnClickListener(getOnClickAcceptListener());
        holder.acceptView.setTag(holder);
        holder.rejectView.setOnClickListener(getOnClickRejectListener());
        holder.rejectView.setTag(holder);
        holder.response = response;
        holder.thumbnailView.setOnClickListener(getOnClickPlayListener());
        holder.thumbnailView.setTag(holder);

        if(response.isCurrentUserCanRate()){
            holder.acceptView.setVisibility(View.VISIBLE);
            holder.rejectView.setVisibility(View.VISIBLE);
        }
        FacebookService.getService().loadVideoThumbnail(holder.thumbnailView, response.getThumbnailUrl());

        return convertView;
    }

    public class ViewHolder {
        long id;
        TextView usernameView;
        TextView submittedView;
        ImageView thumbnailView;
        Button acceptView;
        Button rejectView;
        ChallengeResponse response;
    }

    private AdapterView.OnClickListener getOnClickAcceptListener(){
        return  new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewHolder holder = (ViewHolder) v.getTag();

                holder.acceptView.setVisibility(View.GONE);
                holder.rejectView.setVisibility(View.GONE);
                ChallengeService.getService().rateChallengeResponse(context, holder.id, 'Y');
            }
        };
    }

    private AdapterView.OnClickListener getOnClickRejectListener(){
        return  new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewHolder holder = (ViewHolder) v.getTag();

                holder.acceptView.setVisibility(View.GONE);
                holder.rejectView.setVisibility(View.GONE);
                ChallengeService.getService().rateChallengeResponse(context, holder.id, 'N');
            }
        };
    }

    private AdapterView.OnClickListener getOnClickPlayListener(){
        return  new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewHolder holder = (ViewHolder) v.getTag();

                Intent intent = new Intent(context, ChallengeResponseActivity.class);
                intent.putExtra("response", holder.response);
                context.startActivity(intent);
            }
        };
    }
}