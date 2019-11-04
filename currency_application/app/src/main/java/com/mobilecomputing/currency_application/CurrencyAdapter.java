package com.mobilecomputing.currency_application;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CurrencyAdapter extends ArrayAdapter<CurrencyItem> {
    private ArrayList<CurrencyItem> currencyList;

    public CurrencyAdapter(Context context, int textViewResourceId, ArrayList<CurrencyItem> currencyList) {
        super(context, textViewResourceId, currencyList);
        this.currencyList = currencyList;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        View v = convertView;
        if(v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_item, null);
        }

        CurrencyItem currencyItem = currencyList.get(position);

        if(currencyItem != null) {
            TextView tvBase = v.findViewById(R.id.tvBase);
            TextView tvValue = v.findViewById(R.id.tvValue);

            tvBase.setText(currencyItem.getBase());
            tvValue.setText(String.valueOf(currencyItem.getValue()));
        }
        return v;
    }
}
