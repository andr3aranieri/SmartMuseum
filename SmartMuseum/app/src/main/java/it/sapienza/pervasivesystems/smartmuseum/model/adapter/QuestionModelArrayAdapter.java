package it.sapienza.pervasivesystems.smartmuseum.model.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import it.sapienza.pervasivesystems.smartmuseum.R;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.QuestionModel;
import it.sapienza.pervasivesystems.smartmuseum.view.Utilities;
import it.sapienza.pervasivesystems.smartmuseum.view.slack.ChatActivity;

/**
 * Created by andrearanieri on 03/06/16.
 */
public class QuestionModelArrayAdapter extends ArrayAdapter<QuestionModel> {

    Context context;
    int layoutResourceId;
    ArrayList<QuestionModel> questionModels = new ArrayList<QuestionModel>();

    public QuestionModelArrayAdapter(Context context, int layoutResourceId, ArrayList<QuestionModel> qm) {
        super(context, layoutResourceId, qm);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.questionModels = qm;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View item = convertView;
        QuestionWrapper questionWrapper = null;

        if (item == null) {

            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            item = inflater.inflate(layoutResourceId, parent, false);

            questionWrapper = new QuestionWrapper();
            questionWrapper.title = (TextView) item.findViewById(R.id.que_title);
            questionWrapper.timeStamp = (TextView) item.findViewById(R.id.que_timestamp);
            questionWrapper.detailBtn = (Button) item.findViewById(R.id.detailBtn);

            item.setTag(questionWrapper);

        } else {
            questionWrapper = (QuestionWrapper) item.getTag();
        }

        final QuestionModel questionModel = questionModels.get(position);

        questionWrapper.title.setText(questionModel.getTitle());
        questionWrapper.timeStamp.setText(new Utilities().formatDateToString(questionModel.getTimeStamp(), "dd/MM/yyyy HH:mm"));

        questionWrapper.detailBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = null;
                intent = new Intent(context, ChatActivity.class);
                intent.putExtra("channelToLoad", questionModel.getChannelName());
                context.startActivity(intent);
            }

        });

        return item;

    }

    /**
     * Wrapper class that has to fill the question items
     */
    static class QuestionWrapper {
        TextView title;
        TextView timeStamp;
        Button detailBtn;
    }
}
