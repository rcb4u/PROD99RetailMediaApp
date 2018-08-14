package com.RSPL.MEDIA;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.sqlite.database.sqlite.SQLiteDatabase;

import java.io.File;

public class DataProvider extends ContentProvider {
    // Use ints to represent different queries


    private static final int RETAIL_MEDIA_CLICK = 1;
    private static final int RETAIL_MEDIA_CLICK_AD_PLAY_ID = 2;


    private static final int RETAIL_STORE = 3;
    private static final int RETAIL_STORE_ID = 4;

    private static final int RETAIL_VIDEODATA = 5;
    private static final int RETAIL_VIDEODATA_AD_PLAY_ID = 6;

    private static final int CARGILL_BANK_DETAILS = 7;
    private static final int CARGILL_BANK_DETAILS_AD_PLAY_ID = 8;


    private static final UriMatcher uriMatcher;

    static {
        // Add all our query types to our UriMatcher
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);


        uriMatcher.addURI(ArticleContract.CONTENT_AUTHORITY, ArticleContract.PATH_RETAIL_MEDIA_CLICK, RETAIL_MEDIA_CLICK);
        uriMatcher.addURI(ArticleContract.CONTENT_AUTHORITY, ArticleContract.PATH_RETAIL_MEDIA_CLICK + "/#", RETAIL_MEDIA_CLICK_AD_PLAY_ID);

        uriMatcher.addURI(ArticleContract.CONTENT_AUTHORITY, ArticleContract.PATH_RETAIL_STORE, RETAIL_STORE);
        uriMatcher.addURI(ArticleContract.CONTENT_AUTHORITY, ArticleContract.PATH_RETAIL_STORE + "/#", RETAIL_STORE_ID);


        uriMatcher.addURI(ArticleContract.CONTENT_AUTHORITY, ArticleContract.PATH_RETAIL_VIDEODATA, RETAIL_VIDEODATA);
        uriMatcher.addURI(ArticleContract.CONTENT_AUTHORITY, ArticleContract.PATH_RETAIL_VIDEODATA + "/#", RETAIL_VIDEODATA_AD_PLAY_ID);

        uriMatcher.addURI(ArticleContract.CONTENT_AUTHORITY, ArticleContract.PATH_CARGILL_BANK_DETAILS, CARGILL_BANK_DETAILS);
        uriMatcher.addURI(ArticleContract.CONTENT_AUTHORITY, ArticleContract.PATH_CARGILL_BANK_DETAILS + "/#", CARGILL_BANK_DETAILS_AD_PLAY_ID);


    }

    private SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        System.loadLibrary("sqliteX");

        File db_path = new File("/data/data/" + Activity_Installation.class.getPackage().getName() + "/databases/Db");
        db_path.getParentFile().mkdirs();
        db = SQLiteDatabase.openOrCreateDatabase(db_path, null);          //Opens database in writable mode.
        db.execSQL("PRAGMA key='Anaconda'");
        // this.db = DBhelper.getInstance(getContext()).getDb();
        return true;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        // Find the MIME type of the results... multiple results or a single result
        switch (uriMatcher.match(uri)) {

            case RETAIL_MEDIA_CLICK:
                return ArticleContract.RetailMedia.CONTENT_TYPE_RETAIL_MEDIA_CLICK;

            case RETAIL_MEDIA_CLICK_AD_PLAY_ID:
                return ArticleContract.RetailMedia.CONTENT_ITEM_TYPE_RETAIL_MEDIA_CLICK;

            case RETAIL_STORE:
                return ArticleContract.RetailStore.CONTENT_TYPE_RETAIL_STORE;

            case RETAIL_STORE_ID:
                return ArticleContract.RetailStore.CONTENT_ITEM_TYPE_RETAIL_STORE;

            case RETAIL_VIDEODATA:
                return ArticleContract.RetailVideodata.CONTENT_TYPE_RETAIL_VIDEODATA;

            case RETAIL_VIDEODATA_AD_PLAY_ID:
                return ArticleContract.RetailVideodata.CONTENT_ITEM_TYPE_RETAIL_VIDEODATA;

            case CARGILL_BANK_DETAILS:
                return ArticleContract.CargillBankDetails.CONTENT_TYPE_CARGILL_BANK_DETAILS;

            case CARGILL_BANK_DETAILS_AD_PLAY_ID:
                return ArticleContract.CargillBankDetails.CONTENT_ITEM_TYPE_CARGILL_BANK_DETAILS;

            default:
                throw new IllegalArgumentException("Invalid URI!");
        }
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor c = null;
        switch (uriMatcher.match(uri)) {
            // Query for multiple article results

            case RETAIL_MEDIA_CLICK:
                try {

                    c = db.query(ArticleContract.RetailMedia.TABLENAME_RETAIL_MEDIA_CLICK,
                            projection,
                            selection, selectionArgs,
                            null,
                            null,
                            sortOrder);
                    break;
                } catch (NumberFormatException num) {
                    num.fillInStackTrace();
                }


            case RETAIL_MEDIA_CLICK_AD_PLAY_ID:
                try {
                    long ad_play_media_click = ContentUris.parseId(uri);
                    c = db.query(ArticleContract.RetailMedia.TABLENAME_RETAIL_MEDIA_CLICK,
                            projection,
                            ArticleContract.RetailMedia.AD_PLAY + "=?",
                            new String[]{String.valueOf(ad_play_media_click)},
                            null,
                            null,
                            sortOrder);
                    break;
                } catch (NumberFormatException num) {
                    num.printStackTrace();
                }

            case RETAIL_STORE:
                try {

                    c = db.query(ArticleContract.RetailStore.TABLENAME_RETAIL_STORE,
                            projection,
                            selection, selectionArgs,
                            null,
                            null,
                            sortOrder);
                    break;
                } catch (NumberFormatException num) {
                    num.fillInStackTrace();
                }


            case RETAIL_STORE_ID:
                try {
                    long store_id_retail_store = ContentUris.parseId(uri);
                    c = db.query(ArticleContract.RetailStore.TABLENAME_RETAIL_STORE,
                            projection,
                            ArticleContract.RetailStore.STORE_MEDIA_ID + "=?",
                            new String[]{String.valueOf(store_id_retail_store)},
                            null,
                            null,
                            sortOrder);
                    break;
                } catch (NumberFormatException num) {
                    num.fillInStackTrace();
                }

            case RETAIL_VIDEODATA:
                try {

                    c = db.query(ArticleContract.RetailVideodata.TABLENAME_RETAIL_VIDEODATA,
                            projection,
                            selection, selectionArgs,
                            null,
                            null,
                            sortOrder);
                    break;
                } catch (NumberFormatException num) {
                    num.fillInStackTrace();
                }


            case RETAIL_VIDEODATA_AD_PLAY_ID:
                try {
                    long ad_play_retail_videodata = ContentUris.parseId(uri);
                    c = db.query(ArticleContract.RetailVideodata.TABLENAME_RETAIL_VIDEODATA,
                            projection,
                            ArticleContract.RetailMedia.AD_PLAY + "=?",
                            new String[]{String.valueOf(ad_play_retail_videodata)},
                            null,
                            null,
                            sortOrder);
                    break;
                } catch (NumberFormatException num) {
                    num.printStackTrace();
                }

            case CARGILL_BANK_DETAILS:
                try {

                    c = db.query(ArticleContract.CargillBankDetails.TABLENAME_CARGILL_BANK_DETAILS,
                            projection,
                            selection, selectionArgs,
                            null,
                            null,
                            sortOrder);
                    break;
                } catch (NumberFormatException num) {
                    num.fillInStackTrace();
                }


            case CARGILL_BANK_DETAILS_AD_PLAY_ID:
                try {
                    long ad_play_cargill_bank = ContentUris.parseId(uri);
                    c = db.query(ArticleContract.CargillBankDetails.TABLENAME_CARGILL_BANK_DETAILS,
                            projection,
                            ArticleContract.CargillBankDetails.AD_PLAY + "=?",
                            new String[]{String.valueOf(ad_play_cargill_bank)},
                            null,
                            null,
                            sortOrder);
                    break;
                } catch (NumberFormatException num) {
                    num.printStackTrace();
                }
        }

        // Tell the cursor to register a content observer to observe changes to the
        // URI or its descendants.

        try {

            assert getContext() != null;
            c.setNotificationUri(getContext().getContentResolver(), uri);
        } catch (NullPointerException n) {
            n.fillInStackTrace();
        }
        return c;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Uri returnUri;
        long _id;
        long ad_play_media_click, store_id_retail_store, ad_play_retail_videodata, ad_play_cargill_bank;

        switch (uriMatcher.match(uri)) {

            case RETAIL_MEDIA_CLICK:
                ad_play_media_click = db.insert(ArticleContract.RetailMedia.TABLENAME_RETAIL_MEDIA_CLICK, null, values);
                returnUri = ContentUris.withAppendedId(ArticleContract.RetailMedia.CONTENT_URI_RETAIL_MEDIA_CLICK, ad_play_media_click);
                break;
            case RETAIL_STORE:
                store_id_retail_store = db.insert(ArticleContract.RetailStore.TABLENAME_RETAIL_STORE, null, values);
                returnUri = ContentUris.withAppendedId(ArticleContract.RetailStore.CONTENT_URI_RETAIL_STORE, store_id_retail_store);
                break;
            case RETAIL_VIDEODATA:
                ad_play_retail_videodata = db.insert(ArticleContract.RetailVideodata.TABLENAME_RETAIL_VIDEODATA, null, values);
                returnUri = ContentUris.withAppendedId(ArticleContract.RetailVideodata.CONTENT_URI_RETAIL_VIDEODATA, ad_play_retail_videodata);
                break;

            case CARGILL_BANK_DETAILS:
                ad_play_cargill_bank = db.insert(ArticleContract.CargillBankDetails.TABLENAME_CARGILL_BANK_DETAILS, null, values);
                returnUri = ContentUris.withAppendedId(ArticleContract.CargillBankDetails.CONTENT_URI_CARGILL_BANK_DETAILS, ad_play_cargill_bank);
                break;

            default:
                throw new IllegalArgumentException("Invalid URI!");
        }

        // Notify any observers to update the UI
        assert getContext() != null;
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int rows;
        switch (uriMatcher.match(uri)) {

            case RETAIL_MEDIA_CLICK_AD_PLAY_ID:
                rows = db.update(ArticleContract.RetailMedia.TABLENAME_RETAIL_MEDIA_CLICK, values, selection, selectionArgs);
                break;

            case RETAIL_STORE_ID:
                rows = db.update(ArticleContract.RetailStore.TABLENAME_RETAIL_STORE, values, selection, selectionArgs);
                break;

            case RETAIL_VIDEODATA_AD_PLAY_ID:
                rows = db.update(ArticleContract.RetailVideodata.TABLENAME_RETAIL_VIDEODATA, values, selection, selectionArgs);
                break;

            case CARGILL_BANK_DETAILS_AD_PLAY_ID:
                rows = db.update(ArticleContract.CargillBankDetails.TABLENAME_CARGILL_BANK_DETAILS, values, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Invalid URI!");
        }

        // Notify any observers to update the UI
        if (rows != 0) {
            assert getContext() != null;
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rows;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int rows;
        switch (uriMatcher.match(uri)) {
           /* case LOCAL_PROD_CPG:
               rows = db.delete(ArticleContract.STRPRODLOCAL.NAME, selection, selectionArgs);
                break;
            case LOCAL_PROD_ID_CPG:
               rows = db.delete(ArticleContract.SalesMaster.NAME_MASTER, selection, selectionArgs);
                break;*/
            default:
                throw new IllegalArgumentException("Invalid URI!");
        }


    }
}