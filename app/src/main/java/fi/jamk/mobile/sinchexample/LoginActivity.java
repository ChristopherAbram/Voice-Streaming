package fi.jamk.mobile.sinchexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    private TextView mUser = null;
    private TextView mRecipient = null;

    private Button mLogin = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Setting view instances:
        mUser = (TextView) findViewById(R.id.callerId);
        mRecipient = (TextView) findViewById(R.id.recipientId);
        mLogin = (Button) findViewById(R.id.loginButton);

        // Bind actions:
        setLoginClickListener();
    }

    private void setLoginClickListener(){
        if(mLogin != null){
            mLogin.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    String callerId = mUser.getText().toString();
                    String recipientId = mRecipient.getText().toString();

                    // Start calling activity:
                    sendIntent(callerId, recipientId);
                }
            });
        }
    }

    private void sendIntent(String caller, String recipient){
        Intent intent = new Intent(getApplicationContext(), CallActivity.class);
        intent.putExtra(Constants.CALLER_ID, caller);
        intent.putExtra(Constants.RECIPIENT_ID, recipient);
        startActivity(intent);
    }

}
