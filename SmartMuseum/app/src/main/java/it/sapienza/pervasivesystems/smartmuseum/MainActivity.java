package it.sapienza.pervasivesystems.smartmuseum;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.SystemRequirementsChecker;

import java.util.List;

import it.sapienza.pervasivesystems.smartmuseum.business.beacons.Ranging;
import it.sapienza.pervasivesystems.smartmuseum.business.beacons.RangingDetection;
import it.sapienza.pervasivesystems.smartmuseum.business.interlayercommunication.ILCMessage;
import it.sapienza.pervasivesystems.smartmuseum.business.slack.SlackBusiness;
import it.sapienza.pervasivesystems.smartmuseum.view.ListOfExhibitsActivity;
import it.sapienza.pervasivesystems.smartmuseum.view.LoginActivity;
import it.sapienza.pervasivesystems.smartmuseum.view.slack.ChatAsync;
import it.sapienza.pervasivesystems.smartmuseum.view.slack.ChatAsyncResponse;

public class MainActivity extends AppCompatActivity implements RangingDetection, ChatAsyncResponse {

    //This activity will range for beacons for some seconds to understand if the user is inside or outside
    //the museum;
    public static Ranging beaconsRanging;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //start ranging;
        this.beaconsRanging = new Ranging(this);
        Ranging.rangingDetection = this;
        this.beaconsRanging.initRanging();

        //Open Slack Session from here only if the user is already logged;
        if(SmartMuseumApp.slackSession == null && SmartMuseumApp.loggedUser != null) {
            new ChatAsync(this, SmartMuseumApp.loggedUser, SlackBusiness.SlackCommand.OPEN_SESSION, "", "").execute();

            //show progress popup
            this.progressDialog = new ProgressDialog(MainActivity.this,
                    R.style.AppTheme_Dark_Dialog);
            this.progressDialog.setIndeterminate(true);
            this.progressDialog.setMessage("Connecting to Slack... Please Wait.");
            this.progressDialog.show();
        }
        else { //if the user it not logget, we go to the loginactivity;
            this.goToLoginActivity();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        SystemRequirementsChecker.checkWithDefaultDialogs(this);

        Log.i("MainActivity", "Start Ranging");
        this.beaconsRanging.startRanging();
    }

    @Override
    protected void onPause() {
        Log.i("MainActivity", "Stop Ranging");
        this.beaconsRanging.stopRanging();

        super.onPause();
    }

    private void goToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void goToListOfExhibitsActivity() {
        Intent intent = new Intent(this, ListOfExhibitsActivity.class);
        startActivity(intent);
    }

    @Override
    public void beaconsDetected(ILCMessage message) {
        Log.i("MainActivity", message.getMessageText());
        List<Beacon> listOfBeaconsDetected = (List<Beacon>) message.getMessageObject();
        for (Beacon b : listOfBeaconsDetected) {
            Log.i("MainActivity", "Beacon detected: " + b.getMajor() + ":" + b.getMinor() + ", " + b.getMacAddress() + ", " + b.getRssi());
        }

        //stop ranging;
        Log.i("MainActivity", "Stop Ranging");
        this.beaconsRanging.stopRanging();

        SmartMuseumApp.isUserInsideMuseum = true;
    }

    @Override
    public void sessionOpened(ILCMessage message) {
        Log.i("CHATACTIVITY", message.getMessageText());

        //hide progress popup;
        this.progressDialog.dismiss();
        this.goToListOfExhibitsActivity();
    }

    @Override
    public void sessionClosed(ILCMessage message) {
        Log.i("CHATACTIVITY", message.getMessageText());
    }

    @Override
    public void messagesDownloaed(ILCMessage message) {
    }

    @Override
    public void messageSent(ILCMessage message) {

    }

    @Override
    public void channelCreated(ILCMessage message) {

    }

    @Override
    public void channelListFetched(ILCMessage message) {

    }
}
