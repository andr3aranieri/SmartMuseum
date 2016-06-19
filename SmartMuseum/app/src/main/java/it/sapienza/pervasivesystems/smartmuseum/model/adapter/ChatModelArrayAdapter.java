package it.sapienza.pervasivesystems.smartmuseum.model.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import it.sapienza.pervasivesystems.smartmuseum.R;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.ChatModel;
import it.sapienza.pervasivesystems.smartmuseum.view.Utilities;

/**
 * Created by andrearanieri on 12/06/16.
 */
public class ChatModelArrayAdapter extends ArrayAdapter<ChatModel>  {

    Context context;
    int layoutResourceId;
    ArrayList<ChatModel> chatModels = new ArrayList<ChatModel>();

    public ChatModelArrayAdapter(Context context, int layoutResourceId, ArrayList<ChatModel> cm) {
        super(context, layoutResourceId, cm);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.chatModels = cm;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View item = convertView;
        ChatWrapper chatWrapper = null;

        if (item == null) {

            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            item = inflater.inflate(layoutResourceId, parent, false);

            chatWrapper = new ChatWrapper();
            chatWrapper.message = (TextView) item.findViewById(R.id.cha_message);
            chatWrapper.userName = (TextView) item.findViewById(R.id.cha_username);
            chatWrapper.timeStamp = (TextView) item.findViewById(R.id.cha_timestamp);

            item.setTag(chatWrapper);

        } else {
            chatWrapper = (ChatWrapper) item.getTag();
        }

        final ChatModel chatModel = chatModels.get(position);

        chatWrapper.message.setText(chatModel.getMessage());
        chatWrapper.userName.setText(chatModel.getUserName());
        chatWrapper.timeStamp.setText(new Utilities().formatDateToString(chatModel.getTimeStamp(), "dd/MM/yyyy HH:mm"));
//        chatWrapper.timeStamp.setText(chatModel.getsTimeStamp());

        return item;

    }

    /**
     * Wrapper class that has to fill the question items
     */
    static class ChatWrapper {
        TextView message;
        TextView userName;
        TextView timeStamp;
    }
}
