package app.com.ark.android.foodrecipe.widget;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.io.ByteArrayInputStream;

import app.com.ark.android.foodrecipe.Constant;
import app.com.ark.android.foodrecipe.R;
import app.com.ark.android.foodrecipe.data.FoodRecipeContract;

/**
 * Created by ark on 4/9/2016.
 */
public class FavoriteWidgetRemoteViewsService extends RemoteViewsService {

    //public final String LOG_TAG = FavoriteWidgetRemoteViewsService.class.getSimpleName();
    public static final String SELECTED_FOOD_RECIPE_ID = "selectedFid";
    public static final String SELECTED_POSITION = "selectedpos";

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            private Cursor data = null;

            @Override
            public void onCreate() {

            }

            @Override
            public void onDataSetChanged() {
                if (data != null) {
                    data.close();
                }
                // This method is called by the app hosting the widget (e.g., the launcher)
                // However, our ContentProvider is not exported so it doesn't have access to the
                // data. Therefore we need to clear (and finally restore) the calling identity so
                // that calls use our process and permission
                final long identityToken = Binder.clearCallingIdentity();
                String selection = FoodRecipeContract.FoodRecipeEntry.COLUMN_FAVORITE + " = ?";
                String[] selctionArgs= new String[]{"1"};
                data = getContentResolver().query(FoodRecipeContract.FoodRecipeEntry.CONTENT_URI,
                        Constant.FOODRECIPE_COLUMNS,
                        selection,
                        selctionArgs,
                        null);
                Binder.restoreCallingIdentity(identityToken);

            }

            @Override
            public void onDestroy() {
                if (data != null) {
                    data.close();
                    data = null;
                }
            }

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if (position == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(position)) {
                    return null;
                }

                RemoteViews views = new RemoteViews(getPackageName(),
                        R.layout.widget_favorite_list_item);



                String food_id = data.getString(Constant.COL_FOOD_RECIPE_ID);

                //Read Favorite
                int favorite = data.getInt(Constant.COL_FOOD_RECIPE_FAVORITE);
                boolean fav = (favorite!=0);
                //views.setBoolean(R.id.widget_item_favorite, null, fav);
                //views.setContentDescription(R.id.widget_item_favorite, getString(R.string.content_desc_favorite) + fav);

                // Read Recipe Title from cursor
                String recipe_title = data.getString(Constant.COL_FOOD_RECIPE_NAME);
                views.setTextViewText(R.id.widget_item_Food_title, recipe_title);
                views.setContentDescription(R.id.widget_item_Food_title, recipe_title);

                // Read Recipe source name from cursor
                String source_title = data.getString(Constant.COL_FOOD_SOURCE_NAME);
                views.setTextViewText(R.id.widget_item_Chef_name, source_title);
                views.setContentDescription(R.id.widget_item_Chef_name, source_title);

                //read image
                byte[] image_url = data.getBlob(Constant.COL_FOOD_RECIPE_LARGE_IMAGE);
                ByteArrayInputStream posterimageStream = new ByteArrayInputStream(image_url);
                Bitmap foodImage = BitmapFactory.decodeStream(posterimageStream);
                views.setImageViewBitmap(R.id.widget_item_icon, foodImage);
                views.setContentDescription(R.id.widget_item_icon, getString(R.string.a11y_food_icon, recipe_title));


                Uri foodRecipeUri = FoodRecipeContract.FoodRecipeEntry.buildFoodRecipeId(food_id);

                final Intent fillInIntent = new Intent();
                fillInIntent.putExtra(SELECTED_FOOD_RECIPE_ID, food_id);
                fillInIntent.putExtra(SELECTED_POSITION, position);
                fillInIntent.setData(foodRecipeUri);
                views.setOnClickFillInIntent(R.id.widget_list_item, fillInIntent);
                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.widget_favorite_list_item);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                if (data.moveToPosition(position))
                    return data.getLong(Constant.COL_FOD_ID);
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }
}
