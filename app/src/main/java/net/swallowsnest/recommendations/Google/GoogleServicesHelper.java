package net.swallowsnest.recommendations.Google;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;

import static com.google.android.gms.internal.a.C;
import static com.google.android.gms.internal.a.w;
import static com.google.android.gms.internal.b.ca;

/**
 * Created by marshas on 1/15/17.
 */

public class GoogleServicesHelper implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {

    private static final int REQUEST_CODE_RESOLUTION = -100;
    private static final int REQUEST_CODE_AVAILABILITY = -101;

    public interface GoogleServicesListener {
        public void onConnected();
        public void onDisconnected();
    }

    private Activity activity;
    private GoogleServicesListener listener;
    private GoogleApiClient apiClient;

public GoogleServicesHelper(Activity activity, GoogleServicesListener listener) {
    this.listener = listener;
    this.activity = activity;

    this.apiClient = new GoogleApiClient.Builder(activity)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(Plus.API, Plus.PlusOptions.builder()
                    .setServerClientId("746070181807-q5p4eih042qkoeibme2pv2rd87p1pkkr.apps.googleusercontent.com")
            .build())
            .build();}


    public void connect() {
        if (isGooglePlayServicesAvailable()){
        apiClient.connect();}
        else {
            listener.onDisconnected();
        }
    }

    public void disconnect() {
        if (isGooglePlayServicesAvailable()) {
            apiClient.disconnect();
        } else {
            listener.onDisconnected();
        }
    }

    private boolean isGooglePlayServicesAvailable() {
        int availablilty = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);

        switch (availablilty) {
            case ConnectionResult.SUCCESS:
                return true;
            case ConnectionResult.SERVICE_DISABLED:
            case ConnectionResult.SERVICE_INVALID:
            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
                GooglePlayServicesUtil.getErrorDialog(availablilty, activity, REQUEST_CODE_AVAILABILITY).show();
                return false;
            default:
                return false;
        }
    }
        @Override
        public void onConnected (Bundle bundle){
            listener.onConnected();
        }

        @Override
        public void onConnectionSuspended ( int i){
            listener.onDisconnected();
        }

        @Override
        public void onConnectionFailed (ConnectionResult connectionResult){
            if (connectionResult.hasResolution()) {
                try {
                    connectionResult.startResolutionForResult(activity, REQUEST_CODE_RESOLUTION);
                } catch (android.content.IntentSender.SendIntentException e){
                    connect();
                }
            } else
                listener.onDisconnected();
        }

    public void handleActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == REQUEST_CODE_AVAILABILITY || requestCode == REQUEST_CODE_RESOLUTION){
            if (resultCode == Activity.RESULT_OK){
                connect();
            } else {
                listener.onDisconnected();
            }
            }
        }

    }


