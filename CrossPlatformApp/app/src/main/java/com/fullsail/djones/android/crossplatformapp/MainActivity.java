///////////////////////////
// David Jones           //
// CMD 1412              //
// Week 1                //
///////////////////////////

package com.fullsail.djones.android.crossplatformapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.Parse;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // test network on application launch
        networkConnected();

        // present user with Wi-Fi settings if no network detected
        if (!networkConnected()){
            new AlertDialog.Builder(this)
                    .setTitle("Network Error")
                    .setMessage("Please connect to the internet to use application features.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else {

            boolean clearStack = getIntent().getBooleanExtra("clear_stack", false);
            if (clearStack) {
                clearFragmentStack();
                ParseLoginBuilder builder = new ParseLoginBuilder(MainActivity.this);
                startActivityForResult(builder.build(), 0);
            }

            // Initialize Parse
            Parse.initialize(this, "iiomkK2t6uz93Bq1ExnKlvvBF3iVlu7kjQnm3jKS", "1dYu9k3tsFO4jhnpF975u8IiWv0Gkl4bSPiUt7jd");

            // Get the current user if already logged in and load main fragment
            // If not logged in, load the ParseLoginBuilder UI
            ParseUser currentUser = ParseUser.getCurrentUser();
            if (currentUser != null) {
                MainFragment frag = new MainFragment();
                getFragmentManager().beginTransaction().replace(R.id.mainContainer, frag).commit();
            } else {
                ParseLoginBuilder builder = new ParseLoginBuilder(MainActivity.this);
                startActivityForResult(builder.build(), 0);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){

        // Load main fragment after user logs in
        MainFragment frag = new MainFragment();
        getFragmentManager().beginTransaction().replace(R.id.mainContainer, frag).commit();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Custom method to clear the back stack
    private void clearFragmentStack() {
        FragmentManager fragMan = getFragmentManager();
        if (fragMan.getBackStackEntryCount() > 0){
            FragmentManager.BackStackEntry entry = fragMan.getBackStackEntryAt(0);
            fragMan.popBackStack(entry.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    // Custom method to test for network connection
    private boolean networkConnected() {
        ConnectivityManager cManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getActiveNetworkInfo();
        return nInfo != null && nInfo.isConnected();
    }
}
