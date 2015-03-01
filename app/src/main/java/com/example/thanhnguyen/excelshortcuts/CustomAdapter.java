package com.example.thanhnguyen.excelshortcuts;

import android.app.Activity;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomAdapter extends ArrayAdapter<Shortcut> implements Filterable {

    private Activity activity;
    private ArrayList<Shortcut> lshortcuts;
    private ArrayList<Shortcut> oriShortcuts;
    private static LayoutInflater mInflater = null;
    private ShortcutFilter shortcutFilter;

    public CustomAdapter(Activity activity, int resource, ArrayList<Shortcut> _shortcuts) {
        super(activity, resource, _shortcuts);
        try {
            this.activity = activity;
            this.lshortcuts = _shortcuts;
            this.oriShortcuts = _shortcuts;

            mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        } catch (Exception e) {}
    }

    public int getCount() {
        return lshortcuts.size();
    }

    public Shortcut getItem(Shortcut position) {
        return position;
    }

    public long getItemId(int position) {
        return lshortcuts.get(position).hashCode();
    }

    public void resetData() {
        lshortcuts = oriShortcuts;
    }
    public static class ViewHolder {
        public TextView shortcutButton1;
        public TextView shortcutButton2;
        public TextView shortcutButton3;
        public TextView shortcutButton4;
        public TextView descriptionText;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        final ViewHolder holder;
        try {
            if(convertView == null) {
                v = mInflater.inflate(R.layout.row_layout, null);
                holder = new ViewHolder();

                holder.shortcutButton1 = (TextView) v.findViewById(R.id.shortcutButton1);
                holder.shortcutButton2 = (TextView) v.findViewById(R.id.shortcutButton2);
                holder.shortcutButton3 = (TextView) v.findViewById(R.id.shortcutButton3);
                holder.shortcutButton4 = (TextView) v.findViewById(R.id.shortcutButton4);
                holder.descriptionText= (TextView) v.findViewById(R.id.descriptionText);
                v.setTag(holder);

            } else {
                holder = (ViewHolder) v.getTag();
            }
            holder.shortcutButton1.setText(lshortcuts.get(position).getShortcutButton1());

            if(lshortcuts.get(position).getShortcutButton2() != null) {
                holder.shortcutButton2.setText(lshortcuts.get(position).getShortcutButton2());
                holder.shortcutButton2.setVisibility(View.VISIBLE);
            } else{
                holder.shortcutButton2.setVisibility(View.GONE);
            }

            if(lshortcuts.get(position).getShortcutButton3()!= null) {
                holder.shortcutButton3.setText(lshortcuts.get(position).getShortcutButton3());
                holder.shortcutButton3.setVisibility(View.VISIBLE);
            } else{
                holder.shortcutButton3.setVisibility(View.GONE);
            }

            if(lshortcuts.get(position).getShortcutButton4()!= null) {
                holder.shortcutButton4.setText(lshortcuts.get(position).getShortcutButton4());
                holder.shortcutButton4.setVisibility(View.VISIBLE);
            } else {
                holder.shortcutButton4.setVisibility(View.GONE);
            }

            holder.descriptionText.setText(lshortcuts.get(position).getDescription());

        } catch (Exception e) {}

        return v;
    }


    /*
    *   Create the filter
    *
    * */

    @Override
    public Filter getFilter() {
        if (shortcutFilter == null) {
            shortcutFilter = new ShortcutFilter();
        }
        return shortcutFilter;
    }

    private class ShortcutFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if(constraint == null || constraint.length() == 0) {
                results.values = oriShortcuts;
                results.count = oriShortcuts.size();
            } else {
                ArrayList<Shortcut> mShortcutList = new ArrayList<Shortcut>();
                for (Shortcut s : lshortcuts) {
                    if (s.getDescription().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        mShortcutList.add(s);
                    }
                }
                results.values = mShortcutList;
                results.count = mShortcutList.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if(results.count == 0)
                notifyDataSetInvalidated();
            else {
                lshortcuts = (ArrayList<Shortcut>) results.values;
                notifyDataSetChanged();
            }
        }
    }

 }
