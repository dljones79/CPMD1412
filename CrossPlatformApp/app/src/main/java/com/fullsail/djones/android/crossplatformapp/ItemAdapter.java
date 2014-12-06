///////////////////////////
// David Jones           //
// CMD 1412              //
// Week 1                //
///////////////////////////

package com.fullsail.djones.android.crossplatformapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.ArrayList;

/**
 * Created by David on 12/6/14.
 * Custom Adapter to populate the listview
 */
public class ItemAdapter extends BaseAdapter {

    private static final long ID_CONSTANT = 0x01000000;

    Context mContext;
    ArrayList<ParseObject> mItems;

    public ItemAdapter(Context context, ArrayList<ParseObject> items){
        mContext = context;
        mItems = items;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return ID_CONSTANT + position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_item, parent, false);
        }

        ParseObject parseObject = (ParseObject) getItem(position);
        Integer qty = parseObject.getInt("quantity");
        String quantity = qty.toString();
        TextView itemNameView = (TextView) convertView.findViewById(R.id.itemText);
        TextView quantityNameView = (TextView) convertView.findViewById(R.id.quantityText);
        itemNameView.setText(parseObject.getString("item"));
        quantityNameView.setText(quantity);

        return convertView;
    }
}
