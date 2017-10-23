package fi.jamk.mobile.sinchexample;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sinch.android.rtc.MissingPermissionException;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.calling.Call;

public class UserDetailedActivity extends BaseActivity implements SinchService.StartFailedListener {

    private User mAuthorizedUser = null;
    private User mPickedUser = null;
    private TextView mUserName = null;
    private TextView mPhoneNumber = null;
    private Button mCallButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detailed);

        mAuthorizedUser = getAuthorizedUser();
        mPickedUser = getPickedUser();

        // Set up view elements:
        mUserName = (TextView) findViewById(R.id.username);
        mPhoneNumber = (TextView) findViewById(R.id.phone);
        mCallButton = (Button) findViewById(R.id.call);
        mCallButton.setEnabled(false);

        // Change data view:
        setView();

        mCallButton.setOnClickListener(buttonClickListener);
    }

    @Override
    protected void onServiceConnected() {
        getSinchServiceInterface().setStartListener(this);
        Toast.makeText(this, "Calling service working!", Toast.LENGTH_SHORT).show();
        if (!getSinchServiceInterface().isStarted()) {
            getSinchServiceInterface().startClient(mAuthorizedUser.phone);
        }
        mCallButton.setEnabled(true);
    }

    @Override
    public void onStartFailed(SinchError error) {
        Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStarted() {
        openCallActivity();
    }

    private void callButtonClicked() {
        try {
            Call call = getSinchServiceInterface().callUser(mPickedUser.phone);
            if (call == null) {
                // Service failed for some reason, show a Toast and abort
                Toast.makeText(this, "Service is not started. Try stopping the service and starting it again before "
                        + "placing a call.", Toast.LENGTH_LONG).show();
                return;
            }
            String callId = call.getCallId();
            Intent callScreen = new Intent(this, CallActivity.class);
            callScreen.putExtra(SinchService.CALL_ID, callId);
            startActivity(callScreen);
        } catch (MissingPermissionException e) {
            ActivityCompat.requestPermissions(this, new String[]{e.getRequiredPermission()}, 0);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "You may now place a call", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "This application needs permission to use your microphone to function properly.", Toast
                    .LENGTH_LONG).show();
        }
    }

    private void openCallActivity() {
        Intent mainActivity = new Intent(this, CallActivity.class);
        startActivity(mainActivity);
    }

    private User getAuthorizedUser(){
        User user = new User();
        Intent intent = getIntent();
        user.name = intent.getStringExtra("auth_username");
        user.phone = intent.getStringExtra("auth_phone");
        return user;
    }

    private User getPickedUser(){
        User user = new User();
        Intent intent = getIntent();
        user.name = intent.getStringExtra(Constants.USER_ID);
        user.password = intent.getStringExtra(Constants.PASSWORD);
        user.phone = intent.getStringExtra(Constants.PHONE);
        return user;
    }

    private void setView(){
        mUserName.setText(mPickedUser.name);
        mPhoneNumber.setText(mPickedUser.phone);
    }

    private View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.call:
                    callButtonClicked();
                    break;

            }
        }
    };

}
