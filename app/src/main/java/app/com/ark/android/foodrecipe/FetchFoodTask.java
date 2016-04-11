package app.com.ark.android.foodrecipe;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Vector;

import app.com.ark.android.foodrecipe.data.FoodRecipeContract.FoodRecipeEntry;
import app.com.ark.android.foodrecipe.retrofit.FoodRecipe;
import app.com.ark.android.foodrecipe.retrofit.FoodRecipeDetail;
import app.com.ark.android.foodrecipe.retrofit.GetFoodRecipeApi;
import retrofit.RestAdapter;
/**
 * Created by ark on 3/26/2016.
 */
public class FetchFoodTask extends AsyncTask<Void,Void,Void> {

    private OnTaskCompleted mListener;

    //Base URL
    private static final String API_URL = "http://api.yummly.com";

    private final Context mContext;
    private final String mSearchParam;

    RestAdapter mRestAdapter;

    public FetchFoodTask(Context mContext, String search_param,OnTaskCompleted listener) {
        this.mContext = mContext;
        mSearchParam = search_param;
        mListener=listener;
    }

    @Override
    protected void onPreExecute() {
        mRestAdapter = new RestAdapter.Builder()
                .setEndpoint(API_URL)
                .build();
    }

    private byte[] getImage(String url){
        try {
            URL imageUrl = new URL(url);
            URLConnection ucon = imageUrl.openConnection();

            InputStream is = ucon.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);

            ByteArrayOutputStream baf = new ByteArrayOutputStream();
            //We create an array of bytes
            byte[] data = new byte[500];
            int current = 0;

            while((current = bis.read(data,0,data.length)) != -1){
                baf.write(data, 0, current);
            }

            return baf.toByteArray();
        } catch (Exception e) {
            Log.d("ImageManager", "Error: " + e.toString());
            return null;
        }

    }

    @Override
    protected Void doInBackground(Void... params) {
        FoodRecipe foods = null;
        FoodRecipeDetail food_detail=null;
        GetFoodRecipeApi methods = mRestAdapter.create(GetFoodRecipeApi.class);
        //create the request to API and open the connection
        if(Utility.checkInternetConnection(mContext)){
            //GetFoodApi methods = mRestAdapter.create(GetFoodApi.class);
            //foods= methods.getFoods(mSearchParam,BuildConfig.EDAMAM_APP_ID, BuildConfig.EDAMAM_API_KEY);

            foods = methods.getFoodRecipe(BuildConfig.YUMMLY_APP_ID, BuildConfig.YUMMLY_API_KEY,mSearchParam);
        } else {
            cancel(true);
        }

        if(foods!=null) {
            getFoodDetail(foods,methods,mSearchParam);
        } else {
            Utility.setEmptyStatus(Utility.STATUS_SERVER_DOWN);
        }
        //Log.v(LOG_TAG, "Movie JSON String: " + foodJsonRawString);

        return null;
    }

    private void getFoodDetail(FoodRecipe foods, GetFoodRecipeApi methods,String searchParam) {
        // Insert the new movie information into the database
        Vector<ContentValues> cVVector = new Vector<ContentValues>();

        for(FoodRecipe.MatchSet match : foods.matches){
            FoodRecipeDetail food_detail = methods.getFoodDetails(match.id,BuildConfig.YUMMLY_APP_ID, BuildConfig.YUMMLY_API_KEY);

            if(food_detail.id!=null) {
                ContentValues CV = getContentValuesFromFoodRecipeDetail(food_detail, match.sourceDisplayName,searchParam);
                cVVector.add(CV);
            }
        }

        int inserted = 0;
        // add to database
        if ( cVVector.size() > 0 ) {
            // call bulkInsert to add the movieEntries to the database here
            ContentValues[] cvArrary = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArrary);
            inserted = mContext.getContentResolver().bulkInsert(FoodRecipeEntry.CONTENT_URI,cvArrary);

            if(inserted==0){
                Utility.setEmptyStatus(Utility.STATUS_DATA_INVALID);
            }
            else {
                Utility.setEmptyStatus(Utility.STATUS_OK);
            }
        }
        //Log.d(LOG_TAG, "FetchFoodTask Complete. " + inserted + " Inserted");
    }

    private ContentValues getContentValuesFromFoodRecipeDetail(FoodRecipeDetail food_detail,String source_name, String searchParam) {
        ContentValues foodDetailValues = new ContentValues();

        foodDetailValues.put(FoodRecipeEntry.COLUMN_FOOD_RECIPE_ID,food_detail.id);
        foodDetailValues.put(FoodRecipeEntry.COLUMN_TOTAL_TIME,food_detail.totalTime);

        //Fetch pictures
        byte[] image_data = getImage(food_detail.images.get(0).hostedLargeUrl);
        foodDetailValues.put(FoodRecipeEntry.COLUMN_LARGE_IMAGE,image_data);
        foodDetailValues.put(FoodRecipeEntry.COLUMN_RECIPE_NAME,food_detail.name);
        foodDetailValues.put(FoodRecipeEntry.COLUMN_SOURCE_NAME,source_name);
        String ingredient_total=null;
        for(String s : food_detail.ingredientLines){
            if(ingredient_total==null){
                ingredient_total = "\u2022 "+s;
            }else {
                ingredient_total = ingredient_total+"\n\n\u2022 "+s;
            }
        }
        if(ingredient_total!=null) {
            foodDetailValues.put(FoodRecipeEntry.COLUMN_INGREDIENT_LINES, ingredient_total);
        }

        String nutrition_value=null;
        for(FoodRecipeDetail.Nutrition n : food_detail.nutritionEstimates){
            boolean add_nutrition=false;
            if(n.attribute.equals(mContext.getString(R.string.fat_kcal))){
                add_nutrition=true;
            } else if (n.attribute.equals(mContext.getString(R.string.k))){
                add_nutrition=true;
            } else if(n.attribute.equals(mContext.getString(R.string.fasat))){
                add_nutrition=true;
            } else if(n.attribute.equals(mContext.getString(R.string.fe))){
                add_nutrition=true;
            } else if(n.attribute.equals(mContext.getString(R.string.sugar))){
                add_nutrition=true;
            } else if(n.attribute.equals(mContext.getString(R.string.fibtg))){
                add_nutrition=true;
            } else if(n.attribute.equals(mContext.getString(R.string.procnt))){
                add_nutrition=true;
            }else if(n.attribute.equals(mContext.getString(R.string.chocdf))){
                add_nutrition=true;
            }else if(n.attribute.equals(mContext.getString(R.string.ca))){
                add_nutrition=true;
            }else if(n.attribute.equals(mContext.getString(R.string.vita_iu))){
                add_nutrition=true;
            }else if(n.attribute.equals(mContext.getString(R.string.enerc_kcal))){
                add_nutrition=true;
            }else if(n.attribute.equals(mContext.getString(R.string.vitc))){
                add_nutrition=true;
            }else if(n.attribute.equals(mContext.getString(R.string.fat))){
                add_nutrition=true;
            } else if(n.attribute.equals(mContext.getString(R.string.na))){
                add_nutrition=true;
            } else if(n.attribute.equals(mContext.getString(R.string.fatrn))){
                add_nutrition=true;
            } else if(n.attribute.equals(mContext.getString(R.string.chole))){
                add_nutrition=true;
            }

            if(add_nutrition){
                if(nutrition_value==null) {
                    nutrition_value = n.attribute + mContext.getString(R.string.nutrition_attribute_separator) + n.value + mContext.getString(R.string.nutrition_value_unit_separator) + n.unit.abbreviation;
                } else {
                    nutrition_value = nutrition_value + mContext.getString(R.string.nutrition_separator)+n.attribute + mContext.getString(R.string.nutrition_attribute_separator) + n.value + mContext.getString(R.string.nutrition_value_unit_separator) + n.unit.abbreviation;
                }
            }
        }
        foodDetailValues.put(FoodRecipeEntry.COLUMN_NUTRITION,nutrition_value);
        foodDetailValues.put(FoodRecipeEntry.COLUMN_RECIPE_DIRECTION,food_detail.source.sourceRecipeUrl);
        foodDetailValues.put(FoodRecipeEntry.COLUMN_SERVINGS,food_detail.numberOfServings);
        foodDetailValues.put(FoodRecipeEntry.COLUMN_RATING,food_detail.rating);
        foodDetailValues.put(FoodRecipeEntry.COLUMN_FAVORITE,0);
        foodDetailValues.put(FoodRecipeEntry.COLUMN_SEARCH_QUERY,searchParam);




        return foodDetailValues;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        mListener.onTaskCompleted();
    }
}
