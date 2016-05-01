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
import it.sapienza.pervasivesystems.smartmuseum.view.LoginActivity;

public class MainActivity extends AppCompatActivity implements RangingDetection {

    //This activity will range for beacons for some seconds to understand if the user is inside or outside
    //the museum;
    public static Ranging beaconsRanging;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //start ranging;
        this.beaconsRanging = new Ranging(this);
        Ranging.rangingDetection = this;
        this.beaconsRanging.initRanging();

        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Detecting your position. Please wait...");
        progressDialog.show();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        goToFirstActivity();
                        progressDialog.dismiss();
                    }
                }, 3000);

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
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

    private void goToFirstActivity() {
        //TODO: control if the user is already logged in and if he is inside or outside
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void beaconsDetected(ILCMessage message) {
        Log.i("MainActivity", message.getMessageText());
        List<Beacon> listOfBeaconsDetected = (List<Beacon>) message.getMessageObject();
        for(Beacon b: listOfBeaconsDetected) {
            Log.i("MainActivity", "Beacon detected: " + b.getMajor() + ":" + b.getMinor() + ", " + b.getMacAddress() + ", " + b.getRssi());
        }

        //stop ranging;
        Log.i("MainActivity", "Stop Ranging");
        this.beaconsRanging.stopRanging();

        SmartMuseumApp.isUserInsideMuseum = true;
    }
}
