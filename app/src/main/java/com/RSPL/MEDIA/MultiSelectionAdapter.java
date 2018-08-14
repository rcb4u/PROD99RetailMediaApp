package com.RSPL.MEDIA;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by rspl-rajeev on 1/3/18.
 */

public class MultiSelectionAdapter<ListFilePojo> extends BaseAdapter {


    Context mContext;


    LayoutInflater mInflater;


    ArrayList<com.RSPL.MEDIA.ListFilePojo> mList;


    SparseBooleanArray mSparseBooleanArray;


    public MultiSelectionAdapter(Context context, ArrayList<com.RSPL.MEDIA.ListFilePojo> list) {


        // TODO Auto-generated constructor stub


        this.mContext = context;


        mInflater = LayoutInflater.from(mContext);


        mSparseBooleanArray = new SparseBooleanArray();


        mList = new ArrayList<com.RSPL.MEDIA.ListFilePojo>();


        this.mList = list;


    }


    public ArrayList<com.RSPL.MEDIA.ListFilePojo> getCheckedItems() {


        ArrayList<com.RSPL.MEDIA.ListFilePojo> mTempArry = new ArrayList<com.RSPL.MEDIA.ListFilePojo>();


        for (int i = 0; i < mList.size(); i++) {


            if (mSparseBooleanArray.get(i)) {


                mTempArry.add(mList.get(i));


            }


        }


        return mTempArry;


    }


    @Override


    public int getCount() {


        // TODO Auto-generated method stub


        return mList.size();


    }


    @Override


    public Object getItem(int position) {


        // TODO Auto-generated method stub


        return mList.get(position);


    }


    @Override


    public long getItemId(int position) {


        // TODO Auto-generated method stub


        return position;


    }


    @Override


    public View getView(final int position, View convertView, ViewGroup parent) {


        // TODO Auto-generated method stub


        if (convertView == null) {


            convertView = mInflater.inflate(R.layout.view_file_details, null);


        }


        TextView tvTitle = (TextView) convertView.findViewById(R.id.tv_id_listFileName);


        tvTitle.setText(mList.get(position).getFileName());


        CheckBox mCheckBox = (CheckBox) convertView.findViewById(R.id.id_filecheckbox);


        mCheckBox.setTag(position);


        mCheckBox.setChecked(mSparseBooleanArray.get(position));


        mCheckBox.setOnCheckedChangeListener(mCheckedChangeListener);


        return convertView;


    }


    CompoundButton.OnCheckedChangeListener mCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {


        @Override


        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


            // TODO Auto-generated method stub


            mSparseBooleanArray.put((Integer) buttonView.getTag(), isChecked);


        }


    };


}
