package app.com.ark.android.foodrecipe.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import app.com.ark.android.foodrecipe.data.FoodRecipeContract.FoodRecipeEntry;

/**
 * Created by ark on 3/19/2016.
 */
public class FoodRecipeDbHelper extends SQLiteOpenHelper {

    //public static final String LOG_TAG = FoodRecipeDbHelper.class.getSimpleName();
    // If you change the database schema, you must increment the database version.

    public static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "foodrecipe.db";

    public FoodRecipeDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_FOODRECIPE_TABLE = "CREATE TABLE " + FoodRecipeContract.FoodRecipeEntry.TABLE_NAME + " (" +
                // Create a table to hold food recipes. A recipe consists of the string supplied in the
                FoodRecipeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FoodRecipeEntry.COLUMN_FOOD_RECIPE_ID + " TEXT UNIQUE NOT NULL, " +
                FoodRecipeEntry.COLUMN_TOTAL_TIME + " TEXT NULL, " +
                FoodRecipeEntry.COLUMN_LARGE_IMAGE + " BLOB NOT NULL, " +
                FoodRecipeEntry.COLUMN_RECIPE_NAME + " TEXT NULL, " +
                FoodRecipeEntry.COLUMN_SOURCE_NAME + " TEXT NULL, " +
                FoodRecipeEntry.COLUMN_INGREDIENT_LINES + " TEXT NULL, " +
                FoodRecipeEntry.COLUMN_NUTRITION + " TEXT NULL, " +
                FoodRecipeEntry.COLUMN_RECIPE_DIRECTION + " TEXT NULL, " +
                FoodRecipeEntry.COLUMN_SERVINGS + " INT NULL, " +
                FoodRecipeEntry.COLUMN_RATING + " DOUBLE NULL, " +
                FoodRecipeEntry.COLUMN_FAVORITE + " INT NULL, " +
                FoodRecipeEntry.COLUMN_SEARCH_QUERY + " INT NULL " +
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_FOODRECIPE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.

        //Log.w(LOG_TAG, "Upgrading database from version " + oldVersion + " to " +
        //        newVersion + ". OLD DATA WILL BE DESTROYED");
        // Drop the table
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FoodRecipeEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                FoodRecipeEntry.TABLE_NAME + "'");


        // re-create database
        onCreate(sqLiteDatabase);
    }
}