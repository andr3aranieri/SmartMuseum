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

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.sapienza.pervasivesystems.smartmuseum.R;
import it.sapienza.pervasivesystems.smartmuseum.business.SHA1Business;
import it.sapienza.pervasivesystems.smartmuseum.business.interlayercommunication.ILCMessage;
import it.sapienza.pervasivesystems.smartmuseum.model.db.UserDB;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.UserModel;

public class LoginActivity extends AppCompatActivity implements LoginAsyncResponse {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

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
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.
        /*******Start login async task******/
        new LoginAsync(this, email, password).execute();
        /************************/

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
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
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

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

    @Override
    public void processFinish(ILCMessage message) {
        Log.i("LoginActivity", message.getMessageText());

        switch(message.getMessageType()) {
            case SUCCESS:
                //update UI;
                break;
            case ERROR:
                //update UI;
                break;
        }
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
