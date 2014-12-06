package com.fullsail.djones.android.crossplatformapp;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewFragment extends Fragment {

    ArrayList<String> items;
    ArrayAdapter<String> listAdapter;

    public ViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        /*
        ParseQueryAdapter<ParseObject> adapter = new ParseQueryAdapter<ParseObject>(getActivity(), "Item");
        adapter.setTextKey("item");

        ListView listView = (ListView)getActivity().findViewById(R.id.listView);
        listView.setAdapter(adapter);
        */

        ParseQuery<ParseObject> pQuery = ParseQuery.getQuery("Item");
        pQuery.whereExists("item");
        pQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                ListView listView = (ListView) getView().findViewById(R.id.listView);
                ItemAdapter itemAdapter = new ItemAdapter(getActivity(), (ArrayList<ParseObject>) parseObjects);
                listView.setAdapter(itemAdapter);
            }
        });

    }

}
