package it.sapienza.pervasivesystems.smartmuseum.view.slack;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import it.sapienza.pervasivesystems.smartmuseum.R;
import it.sapienza.pervasivesystems.smartmuseum.SmartMuseumApp;
import it.sapienza.pervasivesystems.smartmuseum.business.interlayercommunication.ILCMessage;
import it.sapienza.pervasivesystems.smartmuseum.business.slack.SlackBusiness;
import it.sapienza.pervasivesystems.smartmuseum.business.visits.VisitBusiness;
import it.sapienza.pervasivesystems.smartmuseum.model.adapter.QuestionModelArrayAdapter;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.QuestionModel;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.UserModel;

public class QuestionsActivity extends AppCompatActivity implements QuestionsAsyncResponse {

    private SlackBusiness slackBusiness = new SlackBusiness();
    private ListView listView;
    private QuestionModelArrayAdapter questionModelArrayAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        new QuestionsAsync(this, SmartMuseumApp.loggedUser).execute();
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
