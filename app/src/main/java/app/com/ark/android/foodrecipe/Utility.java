package app.com.ark.android.foodrecipe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

/**
 * Created by ark on 3/25/2016.
 */
public class Utility {

    public static final String ACTION_DATA_UPDATED = "app.com.ark.android.foodrecipe.ACTION_DATA_UPDATED";

    public static final int STATUS_OK = 0;
    public static final int STATUS_SERVER_DOWN = 1;
    public static final int STATUS_DATA_INVALID = 2;
    private static int mEmptyStatus;

    public static boolean getFavorite(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String displayFavoriteOnlyKey = context.getString(R.string.pref_show_favorite_key);
        return prefs.getBoolean(displayFavoriteOnlyKey,
                Boolean.parseBoolean(context.getString(R.string.pref_show_favorite_default)));
    }

    public static Boolean checkInternetConnection(Context context){
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static void updateWidgets(Context context) {
        // Setting the package ensures that only components in our app will receive the broadcast
        Intent dataUpdatedIntent = new Intent(ACTION_DATA_UPDATED)
                .setPackage(context.getPackageName());
        context.sendBroadcast(dataUpdatedIntent);
    }

    public static void setEmptyStatus(int emptyStatus) {
        mEmptyStatus = emptyStatus;
    }

    public static int getEmptyStatus(){
        return mEmptyStatus;
    }
}
