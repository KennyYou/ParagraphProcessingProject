package com.example.kenny.paragraphprocessingproject;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Communicate extends AppCompatActivity {

    //Declare
    public static Socket socket = null;
    public static PrintWriter out = null;
    public static Scanner in = null;
    public static TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communicate);
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

        //Declaration
        int port;
        String hostname;

        // Get the hostname from the intent

        Intent intent = getIntent();
        hostname = intent.getStringExtra(MainActivity.HOST_NAME);

        // Get the port from the intent.  Default port is 4000

        port = intent.getIntExtra(MainActivity.PORT, 4000);

        // get a handle on the TextView for displaying the status

        tv = (TextView) findViewById(R.id.text_answer);

        // Try to open the connection to the server

        try
        {
            socket = new Socket(hostname, port);

            out = new PrintWriter(socket.getOutputStream(), true);
            in = new Scanner(new InputStreamReader(socket.getInputStream()));

            tv.setText("Connected to server.");
        }
        catch (IOException e)  // socket problems
        {
            tv.setText("Problem: " + e.toString());
            socket = null;
        }

    } //End onCreate

    public void SendParagraph(View view) throws IOException
    {
        //Declaration

        EditText edittxt = (EditText) findViewById(R.id.edit_txt); //txt file
        EditText editop = (EditText) findViewById(R.id.edit_op); //operation to expect
        String user_txtfile;
        String user_operation;
        List<String> paragraph = new LinkedList<String>();
        List<String> answer = new LinkedList<String>();

        //Check Connection
        if(socket == null)
        {
            tv.setText("Not Connected to server");
        }
        else //Send the txt file to server for processing
        {
            user_txtfile = edittxt.getText().toString(); //txt.file
            user_operation = editop.getText().toString(); //Operation

            //Open Asset manager
            AssetManager assetManager = getAssets();
            Scanner scan = new Scanner(assetManager.open(user_txtfile));


            //Go through the file
            int count = 0;
            while(scan.hasNext())
            {
                paragraph.add(scan.nextLine());
                count++;
            }

            //Send Question to server
            out.println(user_operation);
            out.println(count); //Send num of lines to server

            //Send lines to server
            for(int i = 0; i < paragraph.size(); i++)
            {
                out.println(paragraph.get(i));
            }


            //Get answer from server
            int totallines =  in.nextInt(); //Return number of lines from server
            tv.append("# of lines: " + totallines + "\n\r");
            for(int k = 0; k < totallines + 1; k++)
            {
                answer.add(in.nextLine());
            }
            for (int i = 0; i < answer.size(); i++){
                tv.append(answer.get(i) + "\n\r");
            }

            //Close Connection after sending
            scan.close();
            try
            {
                out.close();
                in.close();
                socket.close();
                socket = null;

                tv.append("Finished.  Connection closed.");
            }
            catch (IOException e)  // socket problems
            {
                tv.setText("Problem: " + e.toString());
            }
        }

    } //End SendParagraph

} //End Communicate