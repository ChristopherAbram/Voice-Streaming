package fi.jamk.mobile.sinchexample;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.sinch.android.rtc.SinchError;

public class UserListActivity extends BaseActivity implements SinchService.StartFailedListener {

    private UserManager mUserManager = null;
    private User mAuthorizedUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);

        // Get authorized user:
        mAuthorizedUser = getAuthorizedUser();

        // TODO: Make user list, get user list from UserManager and...

    }

    private User getAuthorizedUser(){
        User user = new User();
        Intent intent = getIntent();
        user.name = intent.getStringExtra(Constants.USER_ID);
        user.password = intent.getStringExtra(Constants.PASSWORD);
        return user;
    }

    @Override
    protected void onServiceConnected() {
        getSinchServiceInterface().setStartListener(this);
        Toast.makeText(this, "Calling service working!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStartFailed(SinchError error) {
        Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStarted() {

    }
}
