package app.com.ark.android.foodrecipe.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by ark on 3/19/2016.
 */
public class FoodRecipeContract {

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "app.com.ark.android.foodrecipe";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    // For instance, content://app.com.ark.android.foodrecipe/foodrecipe/ is a valid path for
    // looking at food data. content://app.com.ark.android.foodrecipe/givemeroot/ will fail,
    // as the ContentProvider hasn't been given any information on what to do with "givemeroot".
    // At least, let's hope not.  Don't be that dev, reader.  Don't be that dev.
    public static final String PATH_FOODRECIPE = "foodrecipe";

    /* Inner class that defines the table contents of the foodrecipe table */
    public static final class FoodRecipeEntry implements BaseColumns {

        public static final String TABLE_NAME = "Foodrecipe";

        //Column food recipe id
        public static final String COLUMN_FOOD_RECIPE_ID = "FoodRecipeId";

        //Column total time
        public static final String COLUMN_TOTAL_TIME = "Total_time";

        //Column hosted large image
        public static final String COLUMN_LARGE_IMAGE = "Large_image";

        //Column recipe name
        public static final String COLUMN_RECIPE_NAME = "Recipe_name";

        //Column source display name
        public static final String COLUMN_SOURCE_NAME = "Source_name";

        //Column ingredient Lines
        public static final String COLUMN_INGREDIENT_LINES = "Ingredient_line";

        //Column Nutrition
        public static final String COLUMN_NUTRITION = "Nutrition";

        //Column Direction
        public static final String COLUMN_RECIPE_DIRECTION = "Direction";

        //Column number of servings
        public static final String COLUMN_SERVINGS = "Servings";

        //Column Rate
        public static final String COLUMN_RATING = "Rating";

        //Column Favorite
        public static final String COLUMN_FAVORITE = "Favorite";

        //Column Search Query
        public static final String COLUMN_SEARCH_QUERY = "Search_query";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FOODRECIPE).build();

        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FOODRECIPE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FOODRECIPE;

        public static Uri buildFoodRecipeUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getFoodRecipeIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static Uri buildFoodRecipeId(String foodrecipeid){
            return CONTENT_URI.buildUpon().appendPath(foodrecipeid).build();
        }

    }
}
