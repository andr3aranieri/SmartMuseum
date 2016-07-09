package it.sapienza.pervasivesystems.smartmuseum.business.user;

import it.sapienza.pervasivesystems.smartmuseum.model.db.UserDB;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.SlackChannelModel;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.UserModel;

/**
 * Created by andrearanieri on 05/07/16.
 */
public class UserBusiness {
    private UserDB userDB = new UserDB();

    public boolean createUser(UserModel user, SlackChannelModel channel) {
        boolean ret = false;
        ret = this.userDB.createUser(user);
        if(ret) {
            ret = this.userDB.connectChannelToUser(user, channel);
        }
        return ret;
    }

    public UserModel getUserByEmail(String email) {
        return this.userDB.getUserByEmail(email);
    }
}
