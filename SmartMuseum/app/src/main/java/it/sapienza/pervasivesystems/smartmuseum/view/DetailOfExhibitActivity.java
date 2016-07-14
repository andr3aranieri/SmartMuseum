package it.sapienza.pervasivesystems.smartmuseum.view;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import it.sapienza.pervasivesystems.smartmuseum.R;
import it.sapienza.pervasivesystems.smartmuseum.SmartMuseumApp;
import it.sapienza.pervasivesystems.smartmuseum.business.InternalStorage.FileSystemBusiness;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.ExhibitModel;
import it.sapienza.pervasivesystems.smartmuseum.view.slack.gui.MainChatActivity;

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
    private ExhibitModel exhDtl;
    private Toolbar toolbar;
//    String mp3 = "http://www.stephaniequinn.com/Music/Allegro%20from%20Duet%20in%20C%20Major.mp3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_of_exhibit);

        Intent mIntent = getIntent();

        exhDtl= (ExhibitModel) mIntent.getSerializableExtra("exhibitModel");

        if(exhDtl == null)
            return;

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

        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);

        try {
            getInit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.createThreadChatNotification();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            try {
                new FileSystemBusiness(this).deleteFile(SmartMuseumApp.localLoginFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(this, LoginActivity.class);
            this.startActivity(intent);
        }
        if(id == R.id.action_ask) {
            SmartMuseumApp.newMessageRead = true;
            Intent intent = new Intent(this, MainChatActivity.class);
            this.startActivity(intent);
        }

        if(id == R.id.action_back) {
            super.onBackPressed();
        }

        if (id == R.id.exhibition_list) {
            Intent intent = new Intent(this, ListOfUHExhibitsActivity.class);
            this.startActivity(intent);
        }

        if (id == R.id.work_of_art_list) {
            Intent intent = new Intent(this, ListOfUHObjectsActivity.class);
            this.startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }

    public void getInit() throws IOException {
        seek_bar = (SeekBar) findViewById(R.id.musicSeekBar);
        play_button = (Button) findViewById(R.id.playPauseButton);

        musicDuration = (TextView) findViewById(R.id.musicDuration);
        musicCurrentLoc = (TextView) findViewById(R.id.musicCurrentLoc);


        play_button.setOnClickListener(this);
        player = new MediaPlayer();
        player.setDataSource(exhDtl.getAudioURL());
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

            String totalTime = String.format("%d : %d",
                    TimeUnit.MILLISECONDS.toMinutes(totalDuration),
                    TimeUnit.MILLISECONDS.toSeconds(totalDuration) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(totalDuration))
            );

            String curTime = String.format("%d : %d",
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

    private void createThreadChatNotification() {
        final DetailOfExhibitActivity parentActivity = this;
        final Handler handler=new Handler();
        handler.post(new Runnable(){
            @Override
            public void run() {
                // upadte textView here
                handler.postDelayed(this,5000); // set time here to refresh textView

                MenuItem item = toolbar.getMenu().findItem(R.id.action_ask);

                if(SmartMuseumApp.newMessage) {
                    SmartMuseumApp.newMessageRead = false;
                    item.setIcon(R.drawable.ic_chat_notification2);
                }
                else {
                    if(!SmartMuseumApp.newMessageRead)
                        item.setIcon(R.drawable.ic_chat_notification2);
                    else
                        item.setIcon(R.drawable.chat_icon);
                }
            }
        });
    }
}

