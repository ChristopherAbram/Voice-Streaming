package fi.jamk.mobile.sinchexample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import static fi.jamk.mobile.sinchexample.R.id.user;

public class LoginActivity extends Activity {

    private TextView mUser = null;
    private TextView mPassword = null;
    private ProgressBar mProgressBar = null;
    private Button mLogin = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Setting view instances:
        mUser = (TextView) findViewById(user);
        mPassword = (TextView) findViewById(R.id.password);
        mLogin = (Button) findViewById(R.id.loginButton);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        hideProgressBar();

        // Bind actions:
        setLoginClickListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideProgressBar();
    }

    private void setLoginClickListener(){
        if(mLogin != null){

            mLogin.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    mLogin.setEnabled(false);
                    showProgressBar();
                    String username = mUser.getText().toString();
                    String password = mPassword.getText().toString();

                    if(username.isEmpty() || password.isEmpty()) {
                        hideProgressBar();
                        // Some validations:
                        Toast.makeText(getBaseContext(), "Username or Password is empty.", Toast.LENGTH_LONG).show();
                    }
                    else {
                        User user = new User(username, password);
                        UserManager mUserManager = new UserManager();
                        if(mUserManager.authorize(user)) {
                            // Start list activity:
                            sendIntent(user);
                            hideProgressBar();
                        }
                        else {
                            hideProgressBar();
                            Toast.makeText(getBaseContext(), "Such user does not exist.", Toast.LENGTH_LONG).show();
                        }
                    }
                    mLogin.setEnabled(true);
                }
            });
        }
    }

    private void sendIntent(User user){
        Intent intent = new Intent(getApplicationContext(), UserListActivity.class);
        intent.putExtra(Constants.USER_ID, user.name);
        intent.putExtra(Constants.PASSWORD, user.password);
        startActivity(intent);
    }

    private void showProgressBar(){
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar(){
        mProgressBar.setVisibility(View.INVISIBLE);
    }
}
