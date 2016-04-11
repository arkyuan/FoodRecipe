package app.com.ark.android.foodrecipe;

import app.com.ark.android.foodrecipe.data.FoodRecipeContract;

/**
 * Created by ark on 3/21/2016.
 */
public class Constant {

    public static final String[] FOODRECIPE_COLUMNS = {
            FoodRecipeContract.FoodRecipeEntry._ID,
            FoodRecipeContract.FoodRecipeEntry.COLUMN_FOOD_RECIPE_ID,
            FoodRecipeContract.FoodRecipeEntry.COLUMN_TOTAL_TIME,
            FoodRecipeContract.FoodRecipeEntry.COLUMN_LARGE_IMAGE,
            FoodRecipeContract.FoodRecipeEntry.COLUMN_RECIPE_NAME,
            FoodRecipeContract.FoodRecipeEntry.COLUMN_SOURCE_NAME,
            FoodRecipeContract.FoodRecipeEntry.COLUMN_INGREDIENT_LINES,
            FoodRecipeContract.FoodRecipeEntry.COLUMN_NUTRITION,
            FoodRecipeContract.FoodRecipeEntry.COLUMN_RECIPE_DIRECTION,
            FoodRecipeContract.FoodRecipeEntry.COLUMN_SERVINGS,
            FoodRecipeContract.FoodRecipeEntry.COLUMN_RATING,
            FoodRecipeContract.FoodRecipeEntry.COLUMN_FAVORITE,
            FoodRecipeContract.FoodRecipeEntry.COLUMN_SEARCH_QUERY
    };

    // These indices are tied to MOVIE_COLUMNS.  If MOVIE_COLUMNS changes, these
    // must change.
    public static final int COL_FOD_ID=0;
    public static final int COL_FOOD_RECIPE_ID =1;
    public static final int COL_FOOD_RECIPE_TOT_TIME =2;
    public static final int COL_FOOD_RECIPE_LARGE_IMAGE=3;
    public static final int COL_FOOD_RECIPE_NAME =4;
    public static final int COL_FOOD_SOURCE_NAME =5;
    public static final int COL_FOOD_RECIPE_INGREDIENT_LINES=6;
    public static final int COL_FOOD_RECIPE_NUTRITION=7;
    public static final int COL_FOOD_RECIPE_DIRECTION=8;
    public static final int COL_FOOD_RECIPE_SERVINGS=9;
    public static final int COL_FOOD_RECIPE_RATING=10;
    public static final int COL_FOOD_RECIPE_FAVORITE=11;
    public static final int COL_FOOD_RECIPE_SEARCH_QUERY=12;

}
