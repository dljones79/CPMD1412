///////////////////////////
// David Jones           //
// CMD 1412              //
// Week 1                //
///////////////////////////

package com.fullsail.djones.android.crossplatformapp;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.parse.ParseUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    // Variables
    Button nViewButton;
    Button nAddButton;
    Button nLogButton;

    public static final String TAG = "MainFragment.TAG";

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        // Link variables to UI components
        nViewButton = (Button)getActivity().findViewById(R.id.viewButton);
        nAddButton = (Button)getActivity().findViewById(R.id.addButton);
        nLogButton = (Button)getActivity().findViewById(R.id.logButton);

        if (!networkConnected()){
            nViewButton.setEnabled(false);
            nAddButton.setEnabled(false);
            nLogButton.setEnabled(false);
        }

        // Set up listeners for buttons
        nViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Load view activity
                Intent intent = new Intent(getActivity(), ViewActivity.class);
                startActivity(intent);
            }
        });

        nAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Load add activity
                Intent intent = new Intent(getActivity(), AddActivity.class);
                startActivity(intent);
            }
        });

        nLogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Log current user out
                ParseUser.logOut();

                // Restart main activity and clear out activity stack


                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("clear_stack", true);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish();
            }
        });



    }

    // Custom method to test for network connection
    private boolean networkConnected() {
        ConnectivityManager cManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getActiveNetworkInfo();
        return nInfo != null && nInfo.isConnected();
    }
}
