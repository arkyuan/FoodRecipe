package app.com.ark.android.foodrecipe.retrofit;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by ark on 4/2/2016.
 */
public interface GetFoodRecipeApi {
    @GET("/v1/api/recipes")
    FoodRecipe getFoodRecipe(
            @Query("_app_id") String appID,
            @Query("_app_key") String appKey,
            @Query("q") String searchParam
    );

    @GET("/v1/api/recipe/{recipeId}")
    FoodRecipeDetail getFoodDetails(
            @Path("recipeId") String foodId,
            @Query("_app_id") String appID,
            @Query("_app_key") String appKey
    );

}
