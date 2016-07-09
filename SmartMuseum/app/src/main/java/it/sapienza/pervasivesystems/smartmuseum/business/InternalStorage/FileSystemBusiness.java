package it.sapienza.pervasivesystems.smartmuseum.business.InternalStorage;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import it.sapienza.pervasivesystems.smartmuseum.model.entity.SlackChannelModel;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.UserModel;

/**
 * Created by andrearanieri on 06/07/16.
 */
public class FileSystemBusiness {

    private Context context;

    public FileSystemBusiness(Context c) {
        this.context = c;
    }

    public UserModel readUserFromFile(String fileName) {
        UserModel userModel;
        try {
            FileInputStream fis = this.context.openFileInput(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = "";
            String[] array;
            userModel = new UserModel();
            SlackChannelModel slackChannelModel = new SlackChannelModel();
            while ((line = bufferedReader.readLine()) != null) {
                array = line.split("\t");
                switch (array[0]) {
                    case "name":
                        userModel.setName(array[1]);
                        break;
                    case "email":
                        userModel.setEmail(array[1]);
                        break;
                    case "profileimage":
                        userModel.setProfileImage(array[1]);
                        break;
                    case "channelname":
                        slackChannelModel.setChannelName(array[1]);
                        break;
                    case "channelid":
                        slackChannelModel.setChannelId(array[1]);
                        break;
                }
            }
            userModel.setSlackChannel(slackChannelModel);
            inputStreamReader.close();
        }
        catch(IOException ex) {
            userModel = null;
        }
        return userModel;
    }

    public boolean writeUserToFile(String filename, UserModel userModel) throws IOException {
        boolean ret = false;

        FileOutputStream fos = this.context.openFileOutput(filename, Context.MODE_PRIVATE);
        String line = "";
        line += "name\t" + userModel.getName() + "\n";
        line += "email\t" + userModel.getEmail() + "\n";
        line += "profileimage\t" + userModel.getProfileImage() + "\n";
        line += "channelname\t" + userModel.getSlackChannel().getChannelName() + "\n";
        line += "channelid\t" + userModel.getSlackChannel().getChannelId() + "\n";

        fos.write(line.getBytes());
        fos.close();
        ret = true;
        return ret;
    }

    //when the user logs out, we delete its local file so the next time we'll prompt him the login activity;
    public boolean deleteFile(String fileName) throws IOException {
        boolean ret = false;
        this.context.deleteFile(fileName);
        return ret;
    }
}
