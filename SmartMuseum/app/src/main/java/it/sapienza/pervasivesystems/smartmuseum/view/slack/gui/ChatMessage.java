package it.sapienza.pervasivesystems.smartmuseum.view.slack.gui;

/**
 * Created by andrearanieri on 27/06/16.
 */
public class ChatMessage {
    public boolean left;
    public String message;

    public ChatMessage(boolean left, String message) {
        super();
        this.left = left;
        this.message = message;
    }
}
