package fi.jamk.mobile.sinchexample;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.sinch.android.rtc.ClientRegistration;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.SinchClientListener;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;

public class SinchService extends Service {

    private SinchClient mSinchClient = null;
    private StartFailedListener mListener = null;
    private Call mCall = null;

    private String mUserId = null;
    public static final String CALL_ID = "CALL_ID";
    static final String TAG = SinchService.class.getSimpleName();

    public SinchService() {}

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        terminateClient();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new SinchServiceInterface();
    }

    private void buildSinchClient(String userId){
        mUserId = userId;
        mSinchClient = Sinch.getSinchClientBuilder()
                .context(this)
                .userId(userId)
                .applicationKey(Constants.Sinch.APP_KEY)
                .applicationSecret(Constants.Sinch.APP_SECRET)
                .environmentHost(Constants.Sinch.ENVIRONMENT)
                .build();
    }

    private void startClient(){
        mSinchClient.setSupportCalling(true);
        mSinchClient.startListeningOnActiveConnection();
        mSinchClient.addSinchClientListener(new MySinchClientListener());
        mSinchClient.getCallClient().setRespectNativeCalls(false);
        mSinchClient.getCallClient().addCallClientListener(new SinchCallClientListener());
        mSinchClient.start();
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

    public class SinchServiceInterface extends Binder {

        public Call callPhoneNumber(String phoneNumber) {
            return mSinchClient.getCallClient().callPhoneNumber(phoneNumber);
        }

        public Call callUser(String userId) {
            if (mSinchClient == null) {
                return null;
            }
            return mSinchClient.getCallClient().callUser(userId);
        }

        public String getUserName() {
            return mUserId;
        }

        public boolean isStarted() {
            return SinchService.this.isStarted();
        }

        public void startClient(String userName) {
            buildSinchClient(userName);
            SinchService.this.startClient();
        }

        public void stopClient() {
            terminateClient();
        }

        public void setStartListener(StartFailedListener listener) {
            mListener = listener;
        }

        public Call getCall(String callId) {
            return mSinchClient.getCallClient().getCall(callId);
        }
    }

    public interface StartFailedListener {
        void onStartFailed(SinchError error);

        void onStarted();
    }

    private class MySinchClientListener implements SinchClientListener {

        @Override
        public void onClientFailed(SinchClient client, SinchError error) {
            if (mListener != null) {
                mListener.onStartFailed(error);
            }
            mSinchClient.terminate();
            mSinchClient = null;
        }

        @Override
        public void onClientStarted(SinchClient client) {
            Log.d(TAG, "SinchClient started");
            if (mListener != null) {
                mListener.onStarted();
            }
        }

        @Override
        public void onClientStopped(SinchClient client) {
            Log.d(TAG, "SinchClient stopped");
        }

        @Override
        public void onLogMessage(int level, String area, String message) {
            switch (level) {
                case Log.DEBUG:
                    Log.d(area, message);
                    break;
                case Log.ERROR:
                    Log.e(area, message);
                    break;
                case Log.INFO:
                    Log.i(area, message);
                    break;
                case Log.VERBOSE:
                    Log.v(area, message);
                    break;
                case Log.WARN:
                    Log.w(area, message);
                    break;
            }
        }

        @Override
        public void onRegistrationCredentialsRequired(SinchClient client,
                                                      ClientRegistration clientRegistration) {
        }
    }

    private class SinchCallClientListener implements CallClientListener {
        @Override
        public void onIncomingCall(CallClient callClient, Call incomingCall) {
            Intent intent = new Intent(SinchService.this, IncomingCallScreenActivity.class);
            intent.putExtra(CALL_ID, incomingCall.getCallId());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            SinchService.this.startActivity(intent);
        }
    }
}
