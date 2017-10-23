package fi.jamk.mobile.sinchexample;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;
import com.sinch.android.rtc.calling.CallListener;

import java.util.List;

public class CallActivity extends AppCompatActivity {

    private SinchClient mSinchClient = null;

    private Call mCall;
    private Button mButton = null;
    private TextView mCallState = null;

    private String mCallerId = null;
    private String mRecipientId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        // Get credentials:
        getIntentExtras();




        mCallState = (TextView) findViewById(R.id.callState);

        mButton = (Button) findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCall == null) {
                    mCall = mSinchClient.getCallClient().callUser(mRecipientId);
                    mCall.addCallListener(new SinchCallListener());
                    mButton.setText("Hang Up");
                } else {
                    mCall.hangup();
                    mCall = null;
                    mButton.setText("Call");
                }
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        // Build and start sinch client:
        buildSinchClient();
        startClient();
    }

    @Override
    protected void onStop(){
        super.onStop();
        terminateClient();
    }

    private void getIntentExtras(){
        Intent intent = getIntent();
        mCallerId = intent.getStringExtra(Constants.CALLER_ID);
        mRecipientId = intent.getStringExtra(Constants.RECIPIENT_ID);
    }

    private void buildSinchClient(){
        mSinchClient = Sinch.getSinchClientBuilder()
                .context(this)
                .userId(mCallerId)
                .applicationKey(Constants.Sinch.APP_KEY)
                .applicationSecret(Constants.Sinch.APP_SECRET)
                .environmentHost(Constants.Sinch.ENVIRONMENT)
                .build();
    }

    private void terminateClient(){
        if (isStarted()) {
            mSinchClient.terminate();
            mSinchClient = null;
        }
    }

    private boolean isStarted() {
        return (mSinchClient != null && mSinchClient.isStarted());
    }

    private void startClient(){
        mSinchClient.setSupportCalling(true);
        mSinchClient.startListeningOnActiveConnection();
        mSinchClient.start();
        mSinchClient.getCallClient().addCallClientListener(new SinchCallClientListener());
    }

    private class SinchCallListener implements CallListener {
        @Override
        public void onCallEnded(Call endedCall) {
            mCall = null;
            mButton.setText("Call");
            setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
            mCallState.setText("");
        }
        @Override
        public void onCallEstablished(Call establishedCall) {
            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
            mCallState.setText("Connected");
        }
        @Override
        public void onCallProgressing(Call progressingCall) {
            mCallState.setText("Ringing");
        }
        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> pushPairs) {
            //don't worry about this right now
        }
    }

    private class SinchCallClientListener implements CallClientListener {
        @Override
        public void onIncomingCall(CallClient callClient, Call incomingCall) {
            mCall = incomingCall;
            mCall.answer();
            mCall.addCallListener(new SinchCallListener());
            mButton.setText("Hang up");
        }
    }
}
