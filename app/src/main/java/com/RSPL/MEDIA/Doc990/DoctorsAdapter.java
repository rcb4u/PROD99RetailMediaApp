package com.RSPL.MEDIA.Doc990;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.RSPL.MEDIA.R;

import java.util.ArrayList;

/**
 * Created by rspl-richa on 23/11/17.
 */


public class DoctorsAdapter extends ArrayAdapter<DoctorPojo> implements Filterable {
    private final int layoutResourceId;
    private Context mcontext;
    DocMainActivity activity;
    private LayoutInflater mInflater;
    TextView tv;
    TextView v;
    private ArrayList<DoctorPojo> doctorlist;
    private ArrayList<DoctorPojo> suggestions = new ArrayList<>();
    Filter filter = new CustomFilter();

    public DoctorsAdapter(Context activity, int layoutResourceId, ArrayList<DoctorPojo> mdoctorlist) {
        super(activity, layoutResourceId, mdoctorlist);
        this.mcontext = activity;
        this.doctorlist = mdoctorlist;
        this.layoutResourceId = layoutResourceId;

    }

    public void setDoctorList(ArrayList<DoctorPojo> doctorList) {
        this.doctorlist = doctorList;
    }

    public int getCount() {
        if (doctorlist.size() < 0)
            return 1;
        return doctorlist.size();
    }


    public DoctorPojo getItem(int position) {
        return doctorlist.get(position);
    }


    public long getItemId(int position) {

        //.getCustomermobileno();
        return position;
    }

    public static class ViewHolder {

        public TextView name;

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        DoctorsAdapter.ViewHolder holder;
        if (convertView == null) {
            holder = new DoctorsAdapter.ViewHolder();
            LayoutInflater mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = mInflater.inflate(R.layout.doctor_items, parent, false);
            holder.name = (TextView) convertView.findViewById(R.id.doctorlist_name);

            convertView.setTag(holder);
        } else {
            holder = (DoctorsAdapter.ViewHolder) convertView.getTag();
        }
        holder.name.setText(doctorlist.get(position).getName());


        return convertView;


    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private class CustomFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            doctorlist.clear();
// Check if the Original List and Constraint aren't null.
            if (doctorlist != null && constraint != null) {
                for (int i = 0; i < doctorlist.size(); i++) {
// Compare item in original list if it contains constraints.
                    if (doctorlist.get(i).getName().toLowerCase().contains(constraint)) {
// If TRUE add item in Suggestions.
                        suggestions.add(doctorlist.get(i));
                    }
                }
            }
// Create new Filter Results and return this to publishResults;
            FilterResults results = new FilterResults();
            results.values = suggestions;
            results.count = suggestions.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}
