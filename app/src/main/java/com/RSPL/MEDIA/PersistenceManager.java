package com.RSPL.MEDIA;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by rspl-rahul on 28/3/16.
 */
public class PersistenceManager {


    private static final String LOCAL_PREFERENCES = "retail-perf";
    private static final String SESSION_ID_KEY = "AccessToken";
    private static final String TIME_KEY = "Time";
    private static final String REFRESH_TOKEN_KEY = "RefreshToken";

    private static final String STORE_ID_KEY = "Store_Id";


    public static void saveSessionId(Context context, String storeId) {
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(LOCAL_PREFERENCES,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SESSION_ID_KEY, storeId);
        editor.commit();
    }

    public static void saveRefreshToken(Context context, String refreshtoken) {
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(LOCAL_PREFERENCES,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(REFRESH_TOKEN_KEY, refreshtoken);
        editor.commit();
    }

    public static String getRefreshTokenId(Context context) {
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(
                LOCAL_PREFERENCES,
                Context.MODE_PRIVATE);
        return sharedPreferences.getString(REFRESH_TOKEN_KEY, "refresh-token-not-saved-yet");
    }

    public static String getSessionId(Context context) {
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(
                LOCAL_PREFERENCES,
                Context.MODE_PRIVATE);
        return sharedPreferences.getString(SESSION_ID_KEY, "session-id-not-saved-yet");
    }

    public static void saveTime(Context context, String storeId) {
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(LOCAL_PREFERENCES,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TIME_KEY, storeId);
        editor.commit();
    }

    public static String getTime(Context context) {
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(
                LOCAL_PREFERENCES,
                Context.MODE_PRIVATE);
        return sharedPreferences.getString(TIME_KEY, "0");
    }

    public static void saveStoreId(Context context, String storeId) {
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(LOCAL_PREFERENCES,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(STORE_ID_KEY, storeId);
        editor.commit();
    }

    public static String getStoreId(Context context) {
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(
                LOCAL_PREFERENCES,
                Context.MODE_PRIVATE);
        return sharedPreferences.getString(STORE_ID_KEY, "store-id-not-saved-yet");
    }
}


