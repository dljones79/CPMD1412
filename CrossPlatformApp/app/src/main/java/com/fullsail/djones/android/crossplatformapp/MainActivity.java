package com.fullsail.djones.android.crossplatformapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

        Parse.initialize(this, "iiomkK2t6uz93Bq1ExnKlvvBF3iVlu7kjQnm3jKS", "1dYu9k3tsFO4jhnpF975u8IiWv0Gkl4bSPiUt7jd");

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null){
            MainFragment frag = new MainFragment();
            getFragmentManager().beginTransaction().replace(R.id.mainContainer, frag).commit();
        } else {
            ParseLoginBuilder builder = new ParseLoginBuilder(MainActivity.this);
            startActivityForResult(builder.build(), 0);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
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
}
