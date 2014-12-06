///////////////////////////
// David Jones           //
// CMD 1412              //
// Week 1                //
///////////////////////////

package com.fullsail.djones.android.crossplatformapp;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddFragment extends Fragment {

    // Declare Variables
    TextView nItemText;
    TextView nQtyText;
    String item;
    String quantity;
    Integer qty;
    Button nAddButton;

    public AddFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        // Link variables to ui components
        nItemText = (TextView)getActivity().findViewById(R.id.itemText);
        nQtyText = (TextView)getActivity().findViewById(R.id.qtyText);
        nAddButton = (Button)getActivity().findViewById(R.id.saveButton);

        // Set listeners
        nAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nItemText.getText() == "" || nQtyText.getText() == "") {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Error")
                            .setMessage("You must enter an item and a quantity.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                } else {

                    // Create a new ParseObject to save to server
                    ParseObject shoppingItem = new ParseObject("Item");
                    quantity = nQtyText.getText().toString();
                    qty = Integer.parseInt(quantity);
                    item = nItemText.getText().toString();
                    shoppingItem.put("item", item);
                    shoppingItem.put("quantity", qty);
                    shoppingItem.setACL(new ParseACL(ParseUser.getCurrentUser()));
                    shoppingItem.saveInBackground();
                    Toast.makeText(getActivity(), "Item Saved", Toast.LENGTH_SHORT).show();
                    nItemText.setText("");
                    nQtyText.setText("");
                }
            }
        });
    }



}
