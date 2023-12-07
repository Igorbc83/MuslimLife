package all.muslimlife.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.net.InetAddress;

public class InternetConnectionService extends Service {

    private ConnectivityManager connectivityManager;
    private InternetConnectionObserver internetConnectionObserver;

    private final IBinder binder = new LocalBinder();

    ConnectivityManager.NetworkCallback networkCallback;

    private boolean isConnected = true;

    public class LocalBinder extends Binder {
        public InternetConnectionService getService() {
            return InternetConnectionService.this;
        }
    }

    public interface InternetConnectionObserver {
        void onInternetConnectionChanged(boolean isConnected);
    }

    public void setInternetConnectionObserver(InternetConnectionObserver observer) {
        internetConnectionObserver = observer;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerNetworkCallback();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void registerNetworkCallback() {
        networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                checkInternetConnection();
            }

            @Override
            public void onLost(Network network) {
                notifyInternetConnectionChanged(false);
            }
        };

        connectivityManager.registerDefaultNetworkCallback(networkCallback);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.unregisterNetworkCallback(networkCallback);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public boolean isConnectedToInternet() {
        if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) &&
                        isInternetAvailable();
            } else {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                return activeNetworkInfo != null && activeNetworkInfo.isConnected() &&
                        isInternetAvailable();
            }
        }
        return false;
    }

    public void checkInternetConnectionAsync() {
        new InternetConnectionTask().execute();
    }

    private class InternetConnectionTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            return isConnectedToInternet();
        }

        @Override
        protected void onPostExecute(Boolean isConnected) {
            notifyInternetConnectionChanged(isConnected);
        }
    }

    private boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            return !ipAddr.equals("");
        } catch (IOException e) {
            Log.e("InternetConnection", "Error checking internet connection", e);
            return false;
        }
    }

    private void checkInternetConnection() {
        isConnected = isInternetAvailable();
        notifyInternetConnectionChanged(isConnected);
    }

    private void notifyInternetConnectionChanged(boolean isConnected) {
        if (internetConnectionObserver != null) {
            internetConnectionObserver.onInternetConnectionChanged(isConnected);
        }
    }

    public boolean isConnected() {
        return isConnected;
    }
}




