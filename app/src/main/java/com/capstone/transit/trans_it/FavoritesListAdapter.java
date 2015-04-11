package com.capstone.transit.trans_it;

/**
 * Created by Nicholas on 2015-04-04.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

public class FavoritesListAdapter extends BaseExpandableListAdapter {

    private Activity context;
    private Map<String, List<String>> mapping;
    private List<String> groups;

    public FavoritesListAdapter(Activity context, List<String> groups,
                                 Map<String, List<String>> mapping) {
        this.context = context;
        this.mapping = mapping;
        this.groups = groups;
    }

    public Object getChild(int groupPosition, int childPosition) {
        return mapping.get(groups.get(groupPosition)).get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        String child = (String) getChild(groupPosition, childPosition);
        LayoutInflater inflater = context.getLayoutInflater();
        TextView item;

        if (child.equals(FavoritesManager.empty_stop_list) ||
                child.equals( FavoritesManager.empty_trip_list) )
        {
            convertView = inflater.inflate(R.layout.list_item, null);

            item = (TextView) convertView.findViewById(R.id.lblListItem);
            item.setText(child);
        } else if (groupPosition == 0) {
            //STOP LIST
            convertView = inflater.inflate(R.layout.fav_stop_child, null);
            item = (TextView) convertView.findViewById(R.id.stop_child);
            ImageView delete = (ImageView) convertView.findViewById(R.id.delete);
            item.setText(Html.fromHtml(child + " - <i>" + FavoritesManager.stop_descriptions.get(child) + "</i>"));
            delete.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Remove this favorite?");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    List<String> child =
                                            mapping.get(groups.get(groupPosition));
                                    //DELETE FROM FAVORITES HERE. DEF NEED FAVORITES MANAGER
                                    FavoritesManager.deleteFavoriteStop(child.get(childPosition), context);
                                    child.remove(childPosition);
                                    notifyDataSetChanged();
                                    Toast toast = Toast.makeText(context, "Stop Removed from Favorites", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            });
                    builder.setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });

        } else if (groupPosition == 1) {
            //TRIP LIST

            convertView = inflater.inflate(R.layout.fav_stop_child, null);
            item = (TextView) convertView.findViewById(R.id.stop_child);
            ImageView delete = (ImageView) convertView.findViewById(R.id.delete);
            item.setText(child);
            delete.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Remove this favorite?");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    List<String> childlist = mapping.get(groups.get(groupPosition));
                                    String child = childlist.get(childPosition);
                                    //DELETE FROM FAVORITES HERE. DEF NEED FAVORITES MANAGER
                                    FavoritesManager.deleteFavoriteTrip(FavoritesManager.trip_descriptions.get(child), context);
                                    childlist.remove(childPosition);
                                    notifyDataSetChanged();
                                    Toast toast = Toast.makeText(context, "Trip Removed from Favorites", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            });
                    builder.setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });

        }


        return convertView;
    }

    public int getChildrenCount(int groupPosition) {
        return mapping.get(groups.get(groupPosition)).size();
    }


    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    public int getGroupCount() {
        return groups.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String laptopName = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group,
                    null);
        }
        TextView item = (TextView) convertView.findViewById(R.id.lblListHeader);
        item.setTypeface(null, Typeface.BOLD);
        item.setText(laptopName);
        return convertView;
    }

    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
