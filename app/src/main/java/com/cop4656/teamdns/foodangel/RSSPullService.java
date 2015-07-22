package com.cop4656.teamdns.foodangel;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

/**
 * Created by ricardo on 7/17/15.
 */
public class RSSPullService extends IntentService {

    private static final String TAG = "RegIntentService";

    public RSSPullService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        /* This in case that multiple refresh operations occur simultaneously, execute them
        sequentially */
        try {
            /* Register for GCM, get Token */
            synchronized(TAG) {
                InstanceID instanceID = InstanceID.getInstance(this);
                String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                        GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                Log.i(TAG, "GCM Registration Token: " + token);

                SendRegistrationToServer(token);

                /* You should store a boolean that indicates whether the generated token has been
                 sent to your server. If the boolean is false, send the token to your server,
                 otherwise your server should have already received the token. */
                sharedPreferences.edit().putBoolean(FoodAngelPreferences.SENT_TOKEN_TO_SERVER, true).apply();
            }
        }
        catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            /* If an exception happens while fetching the new token or updating our registration data
             on a third-party server, this ensures that we'll attempt the update at a later time. */
            sharedPreferences.edit().putBoolean(FoodAngelPreferences.SENT_TOKEN_TO_SERVER, false).apply();
        }

        /* Notify UI that registration has completed, so the progress indicator can be hidden. */
        Intent registrationComplete = new Intent(FoodAngelPreferences.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void SendRegistrationToServer(String token) {
        /* Add implementation */
    }
}