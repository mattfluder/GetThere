/**
 *
 This file is part of the "Get There!" application for android developed
 for the SFWR ENG 4G06 Capstone course in the 2014/2015 Fall/Winter
 terms at McMaster University.


 Copyright (C) 2015 M. Fluder, T. Miele, N. Mio, M. Ngo, and J. Rabaya

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.

 */

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
