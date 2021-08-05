package com.cropcircle.filmcircle.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NetworkChangeReceiver extends BroadcastReceiver {
    private OnNetworkChangeListener listener;

    public NetworkChangeReceiver(OnNetworkChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int status = NetworkUtils.getConnectivityStatusString(context);
        if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
            if (status == NetworkUtils.NETWORK_STATUS_NOT_CONNECTED) {
               // new ForceExitPause(context).execute();
                listener.onNetworkNotConnected(status);
                Log.d("Network Status", "inactive");
            } else {
                listener.onNetworkConnected(status);
                Log.d("Network Status", "active: " + status);
            }
        }
    }

    public interface OnNetworkChangeListener{
        void onNetworkConnected(int status);
        void onNetworkNotConnected(int status);
    }
}
