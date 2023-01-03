package com.canhmai.theweatherapi.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.canhmai.theweatherapi.callback.OnBroadcastCallback;

public class BroadCast extends BroadcastReceiver {

    private OnBroadcastCallback onBroadCallback;


    public BroadCast(OnBroadcastCallback onBroadCallback) {

        this.onBroadCallback = onBroadCallback;
    }




    @Override
    public void onReceive(Context context, Intent intent) {
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {


            if (isNetworkAvailable(context)) {
                try {

                    onBroadCallback.returnNetworkState(true);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

            } else {
                try {
                    onBroadCallback.returnNetworkState(false);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

        } else {

            Log.i("TAG", "onReceive: " + intent.getAction());
        }



    }

    private boolean isNetworkAvailable(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        }
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }


}
