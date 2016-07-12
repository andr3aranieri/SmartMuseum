package it.sapienza.pervasivesystems.smartmuseum.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.sapienza.pervasivesystems.smartmuseum.R;
import it.sapienza.pervasivesystems.smartmuseum.SmartMuseumApp;
import it.sapienza.pervasivesystems.smartmuseum.business.InternalStorage.FileSystemBusiness;
import it.sapienza.pervasivesystems.smartmuseum.business.cryptography.SHA1Business;
import it.sapienza.pervasivesystems.smartmuseum.business.interlayercommunication.ILCMessage;
import it.sapienza.pervasivesystems.smartmuseum.business.slack.SlackBusiness;
import it.sapienza.pervasivesystems.smartmuseum.model.db.UserDB;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.UserModel;
import it.sapienza.pervasivesystems.smartmuseum.view.slack.ChatAsync;
import it.sapienza.pervasivesystems.smartmuseum.view.slack.ChatAsyncResponse;

public class LoginActivity extends AppCompatActivity implements LoginAsyncResponse, ChatAsyncResponse {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private ProgressDialog progressDialog;


    @Bind(R.id.input_email)
    EditText _emailText;
    @Bind(R.id.input_password)
    EditText _passwordText;
    @Bind(R.id.btn_login)
    Button _loginButton;
    @Bind(R.id.link_signup)
    TextView _signupLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });

        this.progressDialog = new ProgressDialog(LoginActivity.this, R.style.AppTheme_Dark_Dialog);
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed("Login failed");
            return;
        }

        _loginButton.setEnabled(false);

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        /*******Start login async task******/
        new LoginAsync(this, email, password).execute();
        /************************/

        if(SmartMuseumApp.isUserInsideMuseum)
            Log.i("LoginActivity", "The User is inside");
        else
            Log.i("LoginActivity", "The User is outside");

        this.progressDialog.setIndeterminate(true);
        this.progressDialog.setMessage("Authenticating...");
        this.progressDialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        finish();
        this.goToFirstActivity();
    }

    public void onLoginFailed(String msg) {
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    private void goToFirstActivity() {

        System.out.println("*****************gotofirstactivity************" + SmartMuseumApp.isUserInsideMuseum);


        Intent intent = null;
        //if the user is inside of the museum, the list of exhibits will be called
        if(SmartMuseumApp.isUserInsideMuseum) {
            intent = new Intent(this, ListOfExhibitsActivity.class);
        } else { // otherwise the history of users exhibits list will be called
            intent = new Intent(this, ListOfUHObjectsActivity.class);
        }
    }

    @Override
    public void processFinish(ILCMessage message) {
        Log.i("LoginActivity", message.getMessageText());
        this.progressDialog.dismiss();
        switch (message.getMessageType()) {
            case SUCCESS:
                //write logged user to file;
                try {
                    new FileSystemBusiness(this).writeUserToFile(SmartMuseumApp.localLoginFile, (UserModel) message.getMessageObject());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //the first time the user opens the app, we Open Slack Session after the user clicks on the login button, if he didn't already open the signup page;
                if(SmartMuseumApp.slackSession == null && SmartMuseumApp.loggedUser != null) {
                    new ChatAsync(this, SmartMuseumApp.loggedUser, SlackBusiness.SlackCommand.OPEN_SESSION, "", "").execute();

                    //show progress popup
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Connecting to Slack... Please Wait.");
                    progressDialog.show();
                } else {
                    this.goToFirstActivity();
                }

                break;
            case ERROR:
                onLoginFailed(message.getMessageText());
                break;
        }
    }

    @Override
    public void sessionOpened(ILCMessage message) {
        Log.i("CHATACTIVITY", message.getMessageText());

        //hide progress popup;
        this.progressDialog.dismiss();
        this.goToFirstActivity();
    }

    @Override
    public void sessionClosed(ILCMessage message) {
        Log.i("CHATACTIVITY", message.getMessageText());
    }

    @Override
    public void messagesDownloaed(ILCMessage message) {
    }

    @Override
    public void messageSent(ILCMessage message) {

    }

    @Override
    public void channelCreated(ILCMessage message) {

    }

    @Override
    public void channelListFetched(ILCMessage message) {

    }
}

/***********************************************************************/
/* Async Task to retrieve data from neo4j rest ws */

interface LoginAsyncResponse {
    void processFinish(ILCMessage message);
}

class LoginAsync extends AsyncTask<Void, Integer, String> {
    public LoginAsyncResponse delegate = null;

    private String email, password;
    private UserModel userModel = null;
    private ILCMessage message = new ILCMessage();
    private UserDB userDB = new UserDB();

    public LoginAsync(LoginAsyncResponse d, String e, String p) {
        this.email = e;
        this.password = p;
        this.delegate = d;
    }

    protected void onPreExecute() {
        Log.d("LoginAsync", "On pre Exceute......");
    }

    protected String doInBackground(Void... arg0) {
        Log.d("LoginAsync", "On doInBackground...");

        this.userModel = this.userDB.getUserByEmail(this.email);
        try {
            if (this.userModel == null) {
                this.userModel = null;
                this.message.setMessageType(ILCMessage.MessageType.ERROR);
                this.message.setMessageText("The email that you entered is incorrect. Please try again");
                this.message.setMessageObject(this.userModel);
            } else if (!userModel.getPassword().trim().equalsIgnoreCase(SHA1Business.SHA1(this.password.trim()))) {
                this.userModel = null;
                this.message.setMessageType(ILCMessage.MessageType.ERROR);
                this.message.setMessageText("The password that you entered is incorrect. Please try again");
                this.message.setMessageObject(this.userModel);
            } else {
                //success
                this.message.setMessageType(ILCMessage.MessageType.SUCCESS);
                this.message.setMessageText("Login successfully done. Welcome back to SmartMuseum!");
                this.message.setMessageObject(this.userModel);

                SmartMuseumApp.loggedUser = this.userModel;
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return "You are at PostExecute";
    }

    protected void onProgressUpdate(Integer... a) {
        Log.d("LoginAsync", "You are in progress update ... " + a[0]);
    }

    protected void onPostExecute(String result) {
        this.delegate.processFinish(this.message);
        Log.d("LoginAsync", "FINISHED Async Task, invoked activity postback method");
    }
}
