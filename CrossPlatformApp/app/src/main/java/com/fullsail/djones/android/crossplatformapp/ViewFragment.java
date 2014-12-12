///////////////////////////
// David Jones           //
// CMD 1412              //
// Week 1                //
///////////////////////////

package com.fullsail.djones.android.crossplatformapp;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewFragment extends Fragment {

    // Variables
    ArrayList<String> items;
    ArrayAdapter<String> listAdapter;
    ListView listView;
    ItemAdapter itemAdapter;
    Button editButton;
    Timer timer;
    TimerTask timerTask;
    final Handler handler = new Handler();

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

        listView = (ListView) getView().findViewById(R.id.listView);
        editButton = (Button) getView().findViewById(R.id.editButton);

        // Query Parse and by using ItemAdapter, populate the listview
        ParseQuery<ParseObject> pQuery = ParseQuery.getQuery("Item");
        pQuery.whereExists("item");
        pQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                //ListView listView = (ListView) getView().findViewById(R.id.listView);
                itemAdapter = new ItemAdapter(getActivity(), (ArrayList<ParseObject>) parseObjects);
                listView.setAdapter(itemAdapter);
            }
        });

        // Start a timer that polls Parse in intervals
        // Only if there is a network connection
        if (networkConnected()) {
            startTimer();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("Item: ", "Pressed");
                ParseObject parseObject = (ParseObject) itemAdapter.getItem(position);
                final String objId = parseObject.getObjectId().toString();
                Log.i("Object Id: ", objId);
                final AlertDialog.Builder editAlert = new AlertDialog.Builder(getActivity());
                final EditText quantity = new EditText(getActivity());
                editAlert.setView(quantity);
                editAlert.setMessage("Enter a new Quantity.");
                editAlert.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        final String qty = quantity.getText().toString().trim();
                        if (!testQuantity(qty) || quantity.getText().toString() == "") {
                            AlertDialog.Builder errorAlert = new AlertDialog.Builder(getActivity());
                            errorAlert.setMessage("You must enter a valid quantity.");
                            errorAlert.setPositiveButton("Quantity Error!", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            errorAlert.show();
                        } else {
                            Log.i("Quantity: ", "Is Numeric");
                            ParseQuery<ParseObject> pQuery = ParseQuery.getQuery("Item");
                            pQuery.getInBackground(objId, new GetCallback<ParseObject>() {
                                @Override
                                public void done(ParseObject parseObject, ParseException e) {
                                    if (e == null) {
                                        Integer convertedInt = Integer.parseInt(qty);
                                        parseObject.put("quantity", convertedInt);
                                        parseObject.saveInBackground();
                                    }
                                }
                            });
                            ViewFragment frag = new ViewFragment();
                            getFragmentManager().beginTransaction().replace(R.id.viewContainer, frag).commit();
                        }
                    }
                });
                editAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                editAlert.show();
            }
        });

        // User may longclick to delete an item
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
           @Override
           public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

               ParseObject parseObject = (ParseObject) itemAdapter.getItem(position);
               final String objId = parseObject.getObjectId().toString();
               ParseQuery<ParseObject> pQuery = ParseQuery.getQuery("Item");
               pQuery.getInBackground(objId, new GetCallback<ParseObject>() {
                   @Override
                   public void done(ParseObject parseObject, ParseException e) {
                       parseObject.deleteInBackground();
                       ViewFragment frag = new ViewFragment();
                       getFragmentManager().beginTransaction().replace(R.id.viewContainer, frag).commit();
                   }
               });

               Log.i("Long Press: ", "Item deleted.");

               return true;
           }
         });
    }

    // When application opens from background
    // Start timer if it isn't already running
    @Override
    public void onResume(){
        super.onResume();

        if (timer == null) {
            startTimer();
        }
    }

    // Validate numeric text
    private boolean testQuantity(String quantity){
        String QUANTITY_PATTERN = "^([1-9][0-9]{0,2})?(\\.[0-9]?)?$";
        Pattern pattern = Pattern.compile(QUANTITY_PATTERN);
        Matcher matcher = pattern.matcher(quantity);
        return matcher.matches();
    }

    // Start polling timer
    public void startTimer() {
        timer = new Timer();
        initializeTimer();
        timer.schedule(timerTask, 5000, 10000);
    }

    // Initialize the timer task and perform Parse polling
    public void initializeTimer(){
        timerTask = new TimerTask(){
            public void run(){
                handler.post(new Runnable(){
                    public void run() {
                        // Query Parse and by using ItemAdapter, populate the listview
                        ParseQuery<ParseObject> pQuery = ParseQuery.getQuery("Item");
                        pQuery.whereExists("item");
                        pQuery.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> parseObjects, ParseException e) {
                                //ListView listView = (ListView) getView().findViewById(R.id.listView);
                                itemAdapter = new ItemAdapter(getActivity(), (ArrayList<ParseObject>) parseObjects);
                                listView.setAdapter(itemAdapter);
                            }
                        });
                    }
                });

            }
        };
    }

    // Custom method to test for network connection
    private boolean networkConnected() {
        ConnectivityManager cManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getActiveNetworkInfo();
        return nInfo != null && nInfo.isConnected();
    }
}
