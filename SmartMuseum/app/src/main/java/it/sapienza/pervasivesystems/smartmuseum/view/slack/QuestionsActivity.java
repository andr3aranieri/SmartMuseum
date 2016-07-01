package it.sapienza.pervasivesystems.smartmuseum.view.slack;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.sapienza.pervasivesystems.smartmuseum.R;
import it.sapienza.pervasivesystems.smartmuseum.SmartMuseumApp;
import it.sapienza.pervasivesystems.smartmuseum.business.cryptography.SHA1Business;
import it.sapienza.pervasivesystems.smartmuseum.business.interlayercommunication.ILCMessage;
import it.sapienza.pervasivesystems.smartmuseum.business.slack.SlackBusiness;
import it.sapienza.pervasivesystems.smartmuseum.business.visits.VisitBusiness;
import it.sapienza.pervasivesystems.smartmuseum.model.adapter.QuestionModelArrayAdapter;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.QuestionModel;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.UserModel;
import it.sapienza.pervasivesystems.smartmuseum.view.slack.gui.MainChatActivity;

public class QuestionsActivity extends AppCompatActivity implements QuestionsAsyncResponse, ChatAsyncResponse {

    private SlackBusiness slackBusiness = new SlackBusiness();
    private ListView listView;
    private QuestionModelArrayAdapter questionModelArrayAdapter = null;
    private Button buttonNewQuestion;
    private EditText questionText;
    private ProgressDialog progressDialog;
    private String newChannelName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        this.questionText = (EditText) findViewById(R.id.txtQuestion);
        this.buttonNewQuestion = (Button) findViewById(R.id.btnNewQuestion);
        this.buttonNewQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                newQuestion();
            }
        });

        new QuestionsAsync(this, SmartMuseumApp.loggedUser).execute();
    }

    private void newQuestion() {
        Log.i("NEWQUESTION", this.questionText.getText().toString());
        SmartMuseumApp.newQuestionText = this.questionText.getText().toString();
        try {
            this.newChannelName = SHA1Business.SHA1(SmartMuseumApp.loggedUser.getEmail() + new Date().toString());
        }
        catch(Exception ex) {
            ex.printStackTrace();
            this.newChannelName = "";
        }
        new ChatAsync(this, SmartMuseumApp.loggedUser, SlackBusiness.SlackCommand.CREATE_CHANNEL, this.newChannelName, "").execute();
        this.showProgressPopup("Sending Question. Please wait...");
    }

    private void showProgressPopup(String message) {
        //show progress popup
        this.progressDialog = new ProgressDialog(QuestionsActivity.this,
                R.style.AppTheme_Dark_Dialog);
        this.progressDialog.setIndeterminate(true);
        this.progressDialog.setMessage(message);
        this.progressDialog.show();
    }

    @Override
    public void processFinish(ILCMessage message) {
        Log.i("QUESTIONDB", message.getMessageText());

        ArrayList<QuestionModel> questions = (ArrayList<QuestionModel>) message.getMessageObject();

        if(questions != null) {
            // Getting a reference to listview of activity_item_of_exhibits layout file
            questionModelArrayAdapter = new QuestionModelArrayAdapter(this, R.layout.activity_item_of_questions, questions);

            listView = (ListView) findViewById(R.id.listview);
            listView.setItemsCanFocus(false);
            listView.setAdapter(questionModelArrayAdapter);
        }
    }

    @Override
    public void sessionOpened(ILCMessage message) {

    }

    @Override
    public void sessionClosed(ILCMessage message) {

    }

    @Override
    public void messagesDownloaed(ILCMessage message) {

    }

    @Override
    public void messageSent(ILCMessage message) {

    }

    @Override
    public void channelCreated(ILCMessage message) {
        Log.i("QuestionActivity", message.getMessageText());
        this.progressDialog.dismiss();

        Intent intent = null;
        intent = new Intent(this, MainChatActivity.class);
        intent.putExtra("channelToLoad", this.newChannelName);
        this.startActivity(intent);
    }
}

interface QuestionsAsyncResponse {
    void processFinish(ILCMessage message);
}

class QuestionsAsync extends AsyncTask<Void, Integer, String> {
    private UserModel userModel;
    private VisitBusiness visitBusiness = new VisitBusiness();
    private ILCMessage message = new ILCMessage();
    private QuestionsActivity delegate;
    private SlackBusiness slackBusiness = new SlackBusiness();

    public QuestionsAsync(QuestionsActivity d, UserModel um) {
        this.delegate = d;
        this.userModel = um;
    }

    protected String doInBackground(Void... arg0) {
        List<QuestionModel> questionModels = this.slackBusiness.getUserQuestions(SmartMuseumApp.loggedUser);
        if(questionModels != null) {
            this.message.setMessageType(ILCMessage.MessageType.SUCCESS);
            this.message.setMessageText("Retrieve user questions OK");
            this.message.setMessageObject(questionModels);
        }
        else {
            this.message.setMessageType(ILCMessage.MessageType.ERROR);
            this.message.setMessageText("Retrieve user questions KO");
            this.message.setMessageObject(null);
        }

        return this.message.getMessageText();
    }

    protected void onPostExecute(String result) {
        this.delegate.processFinish(this.message);
    }
}
