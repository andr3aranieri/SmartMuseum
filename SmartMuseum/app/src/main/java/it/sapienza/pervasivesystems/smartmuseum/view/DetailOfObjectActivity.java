package it.sapienza.pervasivesystems.smartmuseum.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import it.sapienza.pervasivesystems.smartmuseum.R;
import it.sapienza.pervasivesystems.smartmuseum.SmartMuseumApp;
import it.sapienza.pervasivesystems.smartmuseum.business.exhibits.ExhibitBusiness;
import it.sapienza.pervasivesystems.smartmuseum.business.exhibits.WorkofartBusiness;
import it.sapienza.pervasivesystems.smartmuseum.business.interlayercommunication.ILCMessage;
import it.sapienza.pervasivesystems.smartmuseum.business.visits.VisitBusiness;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.UserModel;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.VisitWorkofartModel;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.WorkofartModel;

public class DetailOfObjectActivity extends AppCompatActivity implements View.OnClickListener, MediaPlayer.OnCompletionListener, SeekBar.OnSeekBarChangeListener, WorkofartVisitAsyncResponse {

    private ImageView image;
    private TextView title;
    private TextView desc;

    private View timeStampLayout;
    private TextView timeStampText;

    private SeekBar seek_bar;
    private Button play_button;
    private MediaPlayer player;
    private TextView musicCurrentLoc;
    private TextView musicDuration;
    private Handler seekHandler = new Handler();
    private Utilities utils;
    private WorkofartModel objectDtl;
    private String visitTimeStamp;
//    String mp3 = "http://www.stephaniequinn.com/Music/Allegro%20from%20Duet%20in%20C%20Major.mp3";

    private ProgressDialog progressDialog;
    private WorkofartBusiness workofartBusiness = new WorkofartBusiness();
    private ExhibitBusiness exhibitBusiness = new ExhibitBusiness();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_of_object);

        Intent mIntent = getIntent();

        objectDtl = (WorkofartModel) mIntent.getSerializableExtra("workOfArtModel");
        image = (ImageView) findViewById(R.id.obj_dtl_image);
        title = (TextView) findViewById(R.id.obj_dtl_title);
        desc = (TextView) findViewById(R.id.obj_dtl_desc);

        Picasso.with(this).load(objectDtl.getImage()).into(image);
        title.setText(objectDtl.getTitle());
        desc.setText(objectDtl.getLongDescription());

        //show timestamp visit;
        timeStampLayout = (View) findViewById(R.id.exh_timestamp_layout2);
        timeStampText = (TextView) findViewById(R.id.exh_timestamp2);

        visitTimeStamp = (String) mIntent.getSerializableExtra("visitTimestamp");
        if(visitTimeStamp != null && !visitTimeStamp.trim().equals("")) {
            timeStampLayout.setVisibility(View.VISIBLE);
            timeStampText.setText(visitTimeStamp);
        }
        else {
            timeStampLayout.setVisibility(View.GONE);
        }

        try {
            getInit();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(SmartMuseumApp.saveVisit) {
            this.progressDialog = new ProgressDialog(DetailOfObjectActivity.this, R.style.AppTheme_Dark_Dialog);
            this.progressDialog.setIndeterminate(true);
            this.progressDialog.setMessage("Loading Data... Please wait.");
            this.progressDialog.show();
            new WorkofartVisitAsync(this, objectDtl, SmartMuseumApp.loggedUser).execute();
        }
    }

    public void getInit() throws IOException {
        seek_bar = (SeekBar) findViewById(R.id.musicSeekBar);
        play_button = (Button) findViewById(R.id.playPauseButton);

        musicDuration = (TextView) findViewById(R.id.musicDuration);
        musicCurrentLoc = (TextView) findViewById(R.id.musicCurrentLoc);


        play_button.setOnClickListener(this);
        player = new MediaPlayer();
        player.setDataSource(objectDtl.getAudioURL());
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

    @Override
    public void visitSaved(ILCMessage message) {
        this.progressDialog.dismiss();

        //in case of success, we update the 2 hashmaps containing the user history;
        if(message.getMessageType() == ILCMessage.MessageType.SUCCESS) {
            VisitWorkofartModel visitWorkofartModel = (VisitWorkofartModel) message.getMessageObject();
            WorkofartModel workofartModel = visitWorkofartModel.getWorkofartModel();
            String key = this.workofartBusiness.getWorkofartHashmapKey(this.exhibitBusiness.getExhibitHashmapKey(workofartModel.getExhibitModel()), workofartModel);

            //we add the visit in the today visits;
            SmartMuseumApp.todayVisitedWorksofart.put(key, visitWorkofartModel);

            //we update the visit in the total visits;
            if(SmartMuseumApp.totalVisitedWorksofart.containsKey(key)) {
                SmartMuseumApp.totalVisitedWorksofart.remove(key);
                SmartMuseumApp.totalVisitedWorksofart.put(key, visitWorkofartModel);
            }
        }
    }
}

interface WorkofartVisitAsyncResponse {
    void visitSaved(ILCMessage message);
}

class WorkofartVisitAsync extends AsyncTask<Void, Integer, String> {
    private WorkofartVisitAsyncResponse delegate;
    private ILCMessage message = new ILCMessage();
    private WorkofartModel workofartModel;
    private UserModel userModel;
    private VisitBusiness visitBusiness = new VisitBusiness();
    private WorkofartBusiness workofartBusiness = new WorkofartBusiness();
    private ExhibitBusiness exhibitBusiness = new ExhibitBusiness();

    public WorkofartVisitAsync(WorkofartVisitAsyncResponse d, WorkofartModel woa, UserModel um) {
        this.delegate = d;
        this.workofartModel = woa;
        this.userModel = um;
    }

    @Override
    protected String doInBackground(Void... voids) {
        if(!SmartMuseumApp.todayVisitedWorksofart.containsKey(this.workofartBusiness.getWorkofartHashmapKey(this.exhibitBusiness.getExhibitHashmapKey(this.workofartModel.getExhibitModel()), this.workofartModel))) {
            Date tStamp = new Date();
            if (this.visitBusiness.insertWorkofartVisit(tStamp, this.workofartModel, this.userModel)) {
                this.message.setMessageType(ILCMessage.MessageType.SUCCESS);
                this.message.setMessageText("Work of art visit saved");
                VisitWorkofartModel visitWorkofartModel = new VisitWorkofartModel();
                visitWorkofartModel.setTimestamp(tStamp);
                visitWorkofartModel.setWorkofartModel(this.workofartModel);
                this.message.setMessageObject(visitWorkofartModel);
            } else {
                this.message.setMessageType(ILCMessage.MessageType.ERROR);
                this.message.setMessageText("Error in saving work of art visit");
                this.message.setMessageObject(false);
            }
        }
        else {
            this.message.setMessageType(ILCMessage.MessageType.ERROR);
            this.message.setMessageText("Visit already saved");
            this.message.setMessageObject(false);
        }

        return null;
    }

    protected void onPostExecute(String result) {
        this.delegate.visitSaved(this.message);
    }
}

