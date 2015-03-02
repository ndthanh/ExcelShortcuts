package com.example.thanhnguyen.excelshortcuts;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CustomAdapter extends ArrayAdapter<Shortcut> implements Filterable, Comparator<Shortcut> {

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

    @Override
    public int compare(Shortcut lhs, Shortcut rhs) {
        return 0;
    }
    public void sortByDescription(ArrayList<Shortcut> shortcutArrayList) {
        Comparator<Shortcut> ShortcutComparatorByDescription = new Comparator<Shortcut>() {
            @Override
            public int compare(Shortcut lhs, Shortcut rhs) {
                String descriptionShortcut1 = lhs.getDescription().toLowerCase();
                String descriptionShortcut2 = rhs.getDescription().toLowerCase();

                return descriptionShortcut1.compareTo(descriptionShortcut2);
            }
        };
        Collections.sort(shortcutArrayList,ShortcutComparatorByDescription);
        notifyDataSetChanged();
    }

    public void sortByCategory(ArrayList<Shortcut> shortcutArrayList) {
        Comparator<Shortcut> ShortcutComparatorByCategory = new Comparator<Shortcut>() {
            @Override
            public int compare(Shortcut lhs, Shortcut rhs) {
                String categoryShortcut1 = lhs.getCategory().toLowerCase();
                String categoryShortcut2 = rhs.getCategory().toLowerCase();

                return categoryShortcut1.compareTo(categoryShortcut2);
            }
        };
        Collections.sort(shortcutArrayList,ShortcutComparatorByCategory);
        notifyDataSetChanged();
    }

    public static class ViewHolder {
        public TextView shortcutButton1;
        public TextView shortcutButton2;
        public TextView shortcutButton3;
        public TextView shortcutButton4;
        public TextView descriptionText;
        public TextView categoryText;
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
                holder.categoryText= (TextView) v.findViewById(R.id.categoryText);
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
            holder.categoryText.setText(lshortcuts.get(position).getCategory());

        } catch (Exception e) {}

        return v;
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static CharSequence highlight(String search, String originalText) {
        // ignore case and accents
        // the same thing should have been done for the search text
        String normalizedText = Normalizer.normalize(originalText, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();

        int start = normalizedText.indexOf(search);
        if (start < 0) {
            // not found, nothing to to
            return originalText;
        } else {
            // highlight each appearance in the original text
            // while searching in normalized text
            Spannable highlighted = new SpannableString(originalText);
            while (start >= 0) {
                int spanStart = Math.min(start, originalText.length());
                int spanEnd = Math.min(start + search.length(), originalText.length());

                highlighted.setSpan(new BackgroundColorSpan(Color.BLUE), spanStart, spanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                start = normalizedText.indexOf(search, spanEnd);
            }

            return highlighted;
        }
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
        String stringSearch;
        String originalString1;
        String originalString2;

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if(constraint == null || constraint.length() == 0) {
                results.values = oriShortcuts;
                results.count = oriShortcuts.size();
            } else {
                ArrayList<Shortcut> mShortcutList = new ArrayList<Shortcut>();
                for (Shortcut s : lshortcuts) {
                    stringSearch = constraint.toString().toLowerCase();
                    originalString1 = (s.getDescription()
                                   + s.getCategory()
                                   + s.getShortcutButton1() + "+"
                                   + s.getShortcutButton2() + "+"
                                   + s.getShortcutButton3() + "+"
                                   + s.getShortcutButton4()).toLowerCase();
                    originalString2 = (s.getDescription()
                                + s.getCategory()
                                + s.getShortcutButton1() + " "
                                + s.getShortcutButton2() + " "
                                + s.getShortcutButton3() + " "
                                + s.getShortcutButton4()).toLowerCase();
                    if (originalString1.contains(stringSearch) || originalString2.contains(stringSearch)){
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
            if(results.count == 0) {
                notifyDataSetInvalidated();
//                Toast.makeText(activity, "No result", Toast.LENGTH_SHORT).show();
            }
            else {
                lshortcuts = (ArrayList<Shortcut>) results.values;
                notifyDataSetChanged();
            }
        }
    }

 }
