package com.example.kenny.paragraphprocessingproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {

    public final static String HOST_NAME = "com.example.kenny.paragraphprocessingproject.HOSTNAME";
    public final static String PORT = "com.example.kenny.paragraphprocessingproject.PORT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Allow for network access on the main thread

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
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

    public void connectServer(View view)
    {
        //Declaration
        EditText et1;
        EditText et2;
        String hostname;
        int port;
        Intent intent = new Intent(this, Communicate.class);

        //Get hostname + port number
        et1 = (EditText) findViewById(R.id.edit_host);
        hostname = et1.getText().toString();

        et2 = (EditText) findViewById(R.id.edit_port);
        port = Integer.parseInt(et2.getText().toString());

        // start the activity for communicating with the client.  Pass it
        // the hostname and port in the intent
        intent.putExtra(HOST_NAME, hostname);
        intent.putExtra(PORT, port);

        startActivity(intent);


    } // end connectServer

} //End Main