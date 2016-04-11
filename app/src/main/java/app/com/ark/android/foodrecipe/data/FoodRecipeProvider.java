package app.com.ark.android.foodrecipe.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by ark on 3/19/2016.
 */
public class FoodRecipeProvider extends ContentProvider{

    //private static final String LOG_TAG = FoodRecipeProvider.class.getSimpleName();

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private FoodRecipeDbHelper mOpenHelper;

    // Codes for the UriMatcher //////
    static final int FOOD = 100;
    static final int FOOD_WITH_ID = 101;


    static UriMatcher buildUriMatcher() {
        // 1) The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case. Add the constructor below.
        final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final  String authority = FoodRecipeContract.CONTENT_AUTHORITY;
        // 2) Use the addURI function to match each of the types.  Use the constants from
        // FoodRecipeContract to help define the types to the UriMatcher.
        // # followed by a number
        // * followed by any string
        // */other/#   followed by a string followed by "other" follwed by a number
        sURIMatcher.addURI(authority, FoodRecipeContract.PATH_FOODRECIPE, FOOD);
        sURIMatcher.addURI(authority, FoodRecipeContract.PATH_FOODRECIPE+"/*", FOOD_WITH_ID);

        // 3) Return the new matcher!
        return sURIMatcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new FoodRecipeDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            // "list food recipe"
            case FOOD: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FoodRecipeContract.FoodRecipeEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }
            // "food recipe with id"
            case FOOD_WITH_ID: {
                String foodId = FoodRecipeContract.FoodRecipeEntry.getFoodRecipeIdFromUri(uri);
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FoodRecipeContract.FoodRecipeEntry.TABLE_NAME,
                        projection,
                        FoodRecipeContract.FoodRecipeEntry.COLUMN_FOOD_RECIPE_ID + " = ?",
                        new String[]{foodId},
                        null,
                        null,
                        sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public String getType(Uri uri) {
        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match){
            case FOOD:{
                return FoodRecipeContract.FoodRecipeEntry.CONTENT_DIR_TYPE;
            }
            case FOOD_WITH_ID:{
                return FoodRecipeContract.FoodRecipeEntry.CONTENT_ITEM_TYPE;
            }
            default:{
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match) {
            case FOOD: {

                long _id = db.insert(FoodRecipeContract.FoodRecipeEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = FoodRecipeContract.FoodRecipeEntry.buildFoodRecipeUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        //return uri with _id not food id
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int numDeleted;

        if(null==selection){
            selection="1";
        }

        switch(match){
            case FOOD: {
                numDeleted = db.delete(
                        FoodRecipeContract.FoodRecipeEntry.TABLE_NAME, selection, selectionArgs);
                // reset _ID
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        FoodRecipeContract.FoodRecipeEntry.TABLE_NAME + "'");
                break;
            }
            case FOOD_WITH_ID: {
                String foodrecipeId = FoodRecipeContract.FoodRecipeEntry.getFoodRecipeIdFromUri(uri);
                numDeleted = db.delete(FoodRecipeContract.FoodRecipeEntry.TABLE_NAME,
                        FoodRecipeContract.FoodRecipeEntry.COLUMN_FOOD_RECIPE_ID + " = ?",
                        new String[]{foodrecipeId});
                // reset _ID
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        FoodRecipeContract.FoodRecipeEntry.TABLE_NAME + "'");
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if(numDeleted!=0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rowsUpdate;

        if (values == null){
            throw new IllegalArgumentException("Cannot have null content values");
        }

        final int match = sUriMatcher.match(uri);

        switch (match) {
            case FOOD: {
                rowsUpdate = db.update(FoodRecipeContract.FoodRecipeEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            }
            case FOOD_WITH_ID:{
                String foodrecipeId = FoodRecipeContract.FoodRecipeEntry.getFoodRecipeIdFromUri(uri);
                rowsUpdate = db.update(FoodRecipeContract.FoodRecipeEntry.TABLE_NAME,
                        values,
                        FoodRecipeContract.FoodRecipeEntry.COLUMN_FOOD_RECIPE_ID + " = ?",
                        new String[] {foodrecipeId});
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if(rowsUpdate!=0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdate;
    }


    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case FOOD: {
                // allows for multiple transactions
                db.beginTransaction();

                // keep track of successful inserts
                int returnCount = 0;

                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(FoodRecipeContract.FoodRecipeEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }

                    if (returnCount > 0) {
                        db.setTransactionSuccessful();
                    }
                } finally {
                    db.endTransaction();
                }
                if (returnCount > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return returnCount;
            }
            default:
                return super.bulkInsert(uri, values);
        }
    }


    // http://developer.android.com/reference/android/content/ContentProvider.html#shutdown()
    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}
