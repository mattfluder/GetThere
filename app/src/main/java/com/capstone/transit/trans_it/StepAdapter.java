package com.capstone.transit.trans_it;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Thomas on 4/22/2015.
 */
public class StepAdapter extends BaseAdapter{

    private Context mContext;
    private Step[] mSteps;

    public StepAdapter(Context context, Step[] steps){

        mContext = context;
        mSteps = steps;
    }

    @Override
    public int getCount() {
        return mSteps.length;
    }

    @Override
    public Object getItem(int position) {
        return mSteps[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if(convertView == null){

            convertView = LayoutInflater.from(mContext).inflate(R.layout.directions_list_item,null);
            holder = new ViewHolder();
            holder.distanceLabel = (TextView) convertView.findViewById(R.id.distanceText);
            holder.instructionsLabel = (TextView) convertView.findViewById(R.id.instructionText);
            holder.durationLabel = (TextView) convertView.findViewById(R.id.durationText);

            convertView.setTag(holder);
        }
        else{

            holder = (ViewHolder)convertView.getTag();
        }

        Step step = mSteps[position];

        holder.distanceLabel.setText(step.getDistance());
        holder.instructionsLabel.setText(step.getHtmlInstructions());
        holder.durationLabel.setText(step.getDuration());
        holder.distanceLabel.setTextColor(Color.BLACK);
        holder.instructionsLabel.setTextColor(Color.BLACK);
        holder.durationLabel.setTextColor(Color.BLACK);


        return convertView;
    }

    private static class ViewHolder {

        TextView distanceLabel;
        TextView instructionsLabel;
        TextView durationLabel;
    }
}
