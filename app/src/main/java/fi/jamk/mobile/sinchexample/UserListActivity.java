package fi.jamk.mobile.sinchexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.sinch.android.rtc.SinchError;

public class UserListActivity extends BaseActivity implements SinchService.StartFailedListener {

    private UserManager mUserManager = null;
    private User mAuthorizedUser = null;

    private UserList displayUserList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);

        displayUserList = new UserList();
        mUserManager = new UserManager();
        ListView listview = (ListView) findViewById(R.id.list);

        // Get authorized user:
        mAuthorizedUser = getAuthorizedUser();

        //Fetch users from mUserManager to list (exclude mAuthorizedUser)
        for(User u: mUserManager.getUserList()){
            if(!u.equals(mAuthorizedUser)){
                displayUserList.add(u);
            }
        }

        //Put list on Adapter
        ContactListAdapter adapter = new ContactListAdapter(this, displayUserList);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = displayUserList.get(position);
                showUserDetailed(user);
            }
        });
    }

    private User getAuthorizedUser(){
        User user = new User();
        Intent intent = getIntent();
        user.showName = intent.getStringExtra(Constants.SHOW_NAME);
        user.name = intent.getStringExtra(Constants.USER_ID);
        user.password = intent.getStringExtra(Constants.PASSWORD);
        for(User u : mUserManager.getUserList()){
            if(u.equals(user)){
                user.phone = u.phone;
                break;
            }
        }
        return user;
    }

    private void showUserDetailed(User user){
        Intent intent = new Intent(this, UserDetailedActivity.class);
        intent.putExtra(Constants.SHOW_NAME, user.showName);
        intent.putExtra(Constants.USER_ID, user.name);
        intent.putExtra(Constants.PHONE, user.phone);
        intent.putExtra(Constants.PASSWORD, user.password);
        intent.putExtra("auth_username", mAuthorizedUser.name);
        intent.putExtra("auth_phone", mAuthorizedUser.phone);
        startActivity(intent);
    }

    @Override
    protected void onServiceConnected() {
        getSinchServiceInterface().setStartListener(this);
        Toast.makeText(this, "Calling service working!", Toast.LENGTH_SHORT).show();
        if (!getSinchServiceInterface().isStarted()) {
            getSinchServiceInterface().startClient(mAuthorizedUser.phone);
        }
    }

    @Override
    public void onStartFailed(SinchError error) {
        Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStarted() {
        openCallActivity();
    }

    private void openCallActivity() {
        Intent mainActivity = new Intent(this, CallActivity.class);
        startActivity(mainActivity);
    }
}
