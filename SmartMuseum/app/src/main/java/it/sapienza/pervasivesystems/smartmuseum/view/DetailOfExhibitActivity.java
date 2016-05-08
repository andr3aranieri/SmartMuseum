package it.sapienza.pervasivesystems.smartmuseum.view;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.concurrent.TimeUnit;
import java.io.IOException;

import it.sapienza.pervasivesystems.smartmuseum.R;
import it.sapienza.pervasivesystems.smartmuseum.model.db.ExhibitDB;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.ExhibitModel;

public class DetailOfExhibitActivity extends AppCompatActivity implements View.OnClickListener, MediaPlayer.OnCompletionListener, SeekBar.OnSeekBarChangeListener {

    private ImageView image;
    private TextView title;
    private TextView desc;
    private TextView period;
    private TextView openingHours;
    private TextView location;

    private SeekBar seek_bar;
    private Button play_button;
    private MediaPlayer player;
    private TextView musicCurrentLoc;
    private TextView musicDuration;
    private Handler seekHandler = new Handler();
    private Utilities utils;
    String mp3 = "http://www.stephaniequinn.com/Music/Allegro%20from%20Duet%20in%20C%20Major.mp3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_of_exhibit);

        Intent mIntent = getIntent();
        int exhibitId = mIntent.getIntExtra("exhibitId", 1);

        ExhibitModel exhDtl = (ExhibitModel) new ExhibitDB().getExhibitDetail(exhibitId);

        image = (ImageView) findViewById(R.id.exh_dtl_image);
        title = (TextView) findViewById(R.id.exh_dtl_title);
        desc = (TextView) findViewById(R.id.exh_dtl_desc);
        period = (TextView) findViewById(R.id.exh_dtl_period);
        openingHours = (TextView) findViewById(R.id.exh_dtl_opening_hours);
        location = (TextView) findViewById(R.id.exh_dtl_location);

        Picasso.with(this).load(exhDtl.getImage()).into(image);
        title.setText(exhDtl.getTitle());
        desc.setText(exhDtl.getLongDescription());
        period.setText(exhDtl.getPeriod());
        openingHours.setText(exhDtl.getOpeningHour());
        location.setText(exhDtl.getLocation());

        try {
            getInit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getInit() throws IOException {
        seek_bar = (SeekBar) findViewById(R.id.musicSeekBar);
        play_button = (Button) findViewById(R.id.playPauseButton);

        musicDuration = (TextView) findViewById(R.id.musicDuration);
        musicCurrentLoc = (TextView) findViewById(R.id.musicCurrentLoc);


        play_button.setOnClickListener(this);
        player = new MediaPlayer();
        player.setDataSource(mp3);
        player.prepare();
        utils = new Utilities();

        seek_bar.setOnSeekBarChangeListener(this);
        player.setOnCompletionListener(this);

        seek_bar.setMax(player.getDuration());
        seek_bar.setClickable(false);

    }

    Runnable run = new Runnable() {
        @Override
        public void run() {
            int totalDuration = player.getDuration();
            int currentDuration = player.getCurrentPosition();

            // Updating progress bar
            int progress = (int) (utils.getProgressPercentage(currentDuration, totalDuration));
            //Log.d("Progress", ""+progress);
            seek_bar.setProgress(progress);

            // Running this thread after 100 milliseconds
            seekHandler.postDelayed(this, 1000);

            String totalTime = String.format("%d : , %d",
                    TimeUnit.MILLISECONDS.toMinutes(totalDuration),
                    TimeUnit.MILLISECONDS.toSeconds(totalDuration) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(totalDuration))
            );

            String curTime = String.format("%d : , %d",
                    TimeUnit.MILLISECONDS.toMinutes(currentDuration),
                    TimeUnit.MILLISECONDS.toSeconds(currentDuration) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(currentDuration))
            );


            musicDuration.setText(totalTime);
            musicCurrentLoc.setText(curTime);

        }
    };

    @Override
    public void onClick(View view) {
        if (player.isPlaying()) {
            player.pause();
            play_button.setBackgroundResource(R.drawable.pause_icon_pink);

        } else {
            player.start();
            play_button.setBackgroundResource(R.drawable.play_icon_pink);

            seekHandler.postDelayed(run, 1000);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        seekHandler.removeCallbacks(run);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }
}

class Utilities {

    /**
     * Function to convert milliseconds time to
     * Timer Format
     * Hours:Minutes:Seconds
     */
    public String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }

    /**
     * Function to get Progress percentage
     *
     * @param currentDuration
     * @param totalDuration
     */
    public int getProgressPercentage(long currentDuration, long totalDuration) {
        Double percentage = (double) 0;

        long currentSeconds = (int) (currentDuration / 1000);
        long totalSeconds = (int) (totalDuration / 1000);

        // calculating percentage
        percentage = (((double) currentSeconds) / totalSeconds) * 100;

        // return percentage
        return percentage.intValue();
    }

    /**
     * Function to change progress to timer
     *
     * @param progress      -
     * @param totalDuration returns current duration in milliseconds
     */
    public int progressToTimer(int progress, int totalDuration) {
        int currentDuration = 0;
        totalDuration = (int) (totalDuration / 1000);
        currentDuration = (int) ((((double) progress) / 100) * totalDuration);

        // return current duration in milliseconds
        return currentDuration * 1000;
    }
}