package com.RSPL.MEDIA;

import android.content.Context;
import android.os.Bundle;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by rspl-aman on 11/8/16.
 */
public class SecondSalesReturnAdapter extends ArrayAdapter<Salesreturndetail> {

    static ArrayList<Salesreturndetail> salearraylist;
    int layoutResourceId;
    ArrayList productarrayList;
    Context context;
    LayoutInflater layoutInflater;
    static int flags;

    Bundle bundle;
    static int posi, quant;


    public SecondSalesReturnAdapter(Context context, int resource, ArrayList<Salesreturndetail> objects) {
        super(context, resource, objects);
        this.layoutResourceId = resource;
        this.salearraylist = objects;
        this.context = context;
    }


    public long getItemId(int position) {
        Log.e("**********", "" + position);
        return position;
    }

    public int getCount() {
        if (salearraylist.size() < 0)
            return 1;
        Log.e("**get Count***", salearraylist.toString());
        return salearraylist.size();
    }

    public Salesreturndetail getItem(int position) {

        return salearraylist.get(position);
    }

    public int updateQuantityAtPosition(int position, int quan) {
        posi = position;
        quant = quan;
        return position;
    }


    public static class ViewHolder {
        public TextView ProductName;
        public TextView Quantity;

        public TextView Amount;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        final ViewHolder holder;

        if (convertView == null) {


            holder = new ViewHolder();

            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.second_screen_bill, parent, false);

            holder.ProductName = (TextView) convertView.findViewById(R.id.sec_productname);
            holder.Quantity = (TextView) convertView.findViewById(R.id.sec_quantity);
            holder.Amount = (TextView) convertView.findViewById(R.id.sec_sprice);


            //*********************new product***************************//


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.ProductName.setText(salearraylist.get(position).getSaleproductname());

        if ((holder.Quantity.getTag() != null) && (holder.Quantity.getTag() instanceof TextWatcher)) {
            holder.Quantity.removeTextChangedListener((TextWatcher) holder.Quantity.getTag());
        }

        holder.Quantity.setText(String.format("%d", salearraylist.get(position).getSaleqty()));


        // holder.SPrice.setText(String.valueOf(f.format(Float.parseFloat(holder.SPrice.getText().toString())/(Float.parseFloat(holder.conversionfactor.getText().toString())))));
        final int selling = Integer.parseInt(holder.Quantity.getText().toString());

        holder.Amount.setText(String.format("%.2f", salearraylist.get(position).getSaletotal()));


        // holder.Amount.setText(String.valueOf(( salearraylist.get(position).getSPrice()) * (Double.parseDouble(holder.Quantity.getText().toString()))));


        return convertView;

    }


    public int removeProductFromList(int salesproduct) {
        Log.e("&&&&&&&&", "Deleting " + salesproduct + " to product list");
        salearraylist.remove(salesproduct);
        return salesproduct;

    }

    public void clearAllRows() {
        salearraylist.clear();
        notifyDataSetChanged();
    }

    public float getGrandTotal() {
        ViewHolder holder = new ViewHolder();
        float finalamount = 0.0f;
        DecimalFormat f = new DecimalFormat("##.0");
        for (int listIndex = 0; listIndex < salearraylist.size(); listIndex++) {
            try {
                finalamount += Float.parseFloat(String.valueOf((salearraylist.get(listIndex).getSaletotal())));
                // holder.Total.setText(String.valueOf(f.format(Double.parseDouble(holder.SPrice.getText().toString())  * (Double.parseDouble(holder.Quantity.getText().toString())))));

            } catch (Exception e) {
                //ignore exeption for parse
            }
            Log.e("&&&&&^^^^^^^^", "$$$$$$$$" + finalamount);

        }
        Log.e("&&&&55555555&&&", "Total Price is:" + finalamount);
        return finalamount;
    }


    public void addProductToList(Salesreturndetail salesproduct) {

        salearraylist.add(salesproduct);


    }

    public ArrayList<Salesreturndetail> getList() {
        return salearraylist;
    }


    private Salesreturndetail findsalesitemInList(Salesreturndetail salesproduct) {
        Salesreturndetail returnSalesVal = null;

        for (Salesreturndetail productInList : salearraylist) {
            if (productInList.getSalebatchno().trim().equals(salesproduct.getSalebatchno().trim())) {
                //check batch number also (if batch number is different, we should not add to the
                // same row
                if (productInList.getSaleproductname().trim().equals(salesproduct.getSaleproductname().trim())) {

                    returnSalesVal = productInList;

                }
            }

        }
        return returnSalesVal;
    }

}
