package app.com.ark.android.foodrecipe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;

import app.com.ark.android.foodrecipe.data.FoodRecipeContract;

/**
 * Created by ark on 3/14/2016.
 */
public class FoodViewAdapter extends RecyclerView.Adapter<FoodViewAdapter.FoodAdapterViewHolder> {

    private Cursor mCursor;
    final private Context mContext;
    final private FoodAdapterOnClickHandler mClickHandler;
    final private View mEmptyView;
    final private ItemChoiceManager mICM;




    public FoodViewAdapter(Context context, FoodAdapterOnClickHandler dh, View emptyView, int choiceMode){
        mContext = context;
        mEmptyView = emptyView;
        mClickHandler = dh;
        mICM = new ItemChoiceManager(this);
        mICM.setChoiceMode(choiceMode);
    }
    @Override
    public FoodAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_food, parent, false);
        view.setFocusable(true);
        return new FoodAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FoodAdapterViewHolder foodrecipeAdapterViewHolder, int position) {
        mCursor.moveToPosition(position);


        // Read Large Image from cursor
        byte[] large_image = mCursor.getBlob(Constant.COL_FOOD_RECIPE_LARGE_IMAGE);
        ByteArrayInputStream imageStream = new ByteArrayInputStream(large_image);
        Bitmap theImage= BitmapFactory.decodeStream(imageStream);
        foodrecipeAdapterViewHolder.mFoodImageView.setImageBitmap(theImage);

        // this enables better animations. even if we lose state due to a device rotation,
        // the animator can use this to re-find the original view
        foodrecipeAdapterViewHolder.mFoodImageView.setTransitionName(mContext.getString(R.string.detail_icon_transition_name) + position);

        // Read Recipe Title from cursor
        String recipe_title = mCursor.getString(Constant.COL_FOOD_RECIPE_NAME);
        foodrecipeAdapterViewHolder.mFoodTitleView.setText(recipe_title);
        foodrecipeAdapterViewHolder.mFoodTitleView.setContentDescription(mContext.getString(R.string.a11y_recipe_title, recipe_title));

        // Read Recipe source name from cursor
        String source_title = mCursor.getString(Constant.COL_FOOD_SOURCE_NAME);
        foodrecipeAdapterViewHolder.mFoodChefnameview.setText(source_title);
        foodrecipeAdapterViewHolder.mFoodChefnameview.setContentDescription(mContext.getString(R.string.a11y_source_title, source_title));

        //Read Favorite
        int favorite = mCursor.getInt(Constant.COL_FOOD_RECIPE_FAVORITE);
        boolean fav = (favorite!=0);
        foodrecipeAdapterViewHolder.mFavorite.setPressed(fav);


    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        mICM.onRestoreInstanceState(savedInstanceState);
    }

    public void onSaveInstanceState(Bundle outState) {
        mICM.onSaveInstanceState(outState);
    }

    public int getSelectedItemPosition() {
        return mICM.getSelectedItemPosition();
    }

    public void selectView(RecyclerView.ViewHolder viewHolder) {
        if ( viewHolder instanceof FoodAdapterViewHolder ) {
            FoodAdapterViewHolder vfh = (FoodAdapterViewHolder)viewHolder;
            vfh.onClick(vfh.itemView);
        }
    }

    @Override
    public int getItemCount() {
        if ( null == mCursor ) return 0;
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
        mEmptyView.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    public Cursor getCursor() {
        return mCursor;
    }



    public class FoodAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public final ImageView mFoodImageView;
        public final TextView mFoodTitleView;
        public final TextView mFoodChefnameview;
        public final ImageButton mFavorite;

        public FoodAdapterViewHolder(View view){
            super(view);
            mFoodImageView = (ImageView) view.findViewById(R.id.list_item_icon);
            mFoodTitleView = (TextView) view.findViewById(R.id.list_item_Food_title);
            mFoodChefnameview = (TextView) view.findViewById(R.id.list_item_Chef_name);
            mFavorite = (ImageButton) view.findViewById(R.id.List_item_favorite);
            mFoodImageView.setOnClickListener(this);
            mFavorite.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if(v.getId()==mFoodImageView.getId()) {
                int adapterPosition = getAdapterPosition();
                mCursor.moveToPosition(adapterPosition);
                int foodRecipeIDColumnIndex = mCursor.getColumnIndex(FoodRecipeContract.FoodRecipeEntry.COLUMN_FOOD_RECIPE_ID);
                mClickHandler.onClick(mCursor.getString(foodRecipeIDColumnIndex), this,adapterPosition);
                mICM.onClick(this);
                //Toast.makeText(v.getContext(), "ITEM PRESSED = " + mCursor.getString(foodRecipeIDColumnIndex), Toast.LENGTH_SHORT).show();
            }

            if(v.getId()==mFavorite.getId()){
                int adapterPosition = getAdapterPosition();
                //move cursor to selected position
                mCursor.moveToPosition(adapterPosition);
                int foodRecipeFavoriteColumnIndex = mCursor.getColumnIndex(FoodRecipeContract.FoodRecipeEntry.COLUMN_FAVORITE);
                int foodRecipeIDColumnIndex = mCursor.getColumnIndex(FoodRecipeContract.FoodRecipeEntry.COLUMN_FOOD_RECIPE_ID);
                //old favorite value
                int favorite_old = mCursor.getInt(foodRecipeFavoriteColumnIndex);
                //toggle the old value
                int favorite_new = Math.abs(favorite_old-1);
                String foodId = mCursor.getString(foodRecipeIDColumnIndex);
                Uri uri = FoodRecipeContract.FoodRecipeEntry.buildFoodRecipeId(foodId);

                //update the value into the database
                ContentValues updatedValues = new ContentValues();
                updatedValues.put(FoodRecipeContract.FoodRecipeEntry.COLUMN_FAVORITE, favorite_new);

                mContext.getContentResolver().update(
                        uri, updatedValues, FoodRecipeContract.FoodRecipeEntry.COLUMN_FOOD_RECIPE_ID + "= ?",
                        new String[]{uri.getLastPathSegment()});

                Utility.updateWidgets(mContext);

                //boolean fav = (favorite_new!=0);
                //set the new favorite value
                //mFavorite.setPressed(fav);
            }

        }
    }

    public static interface FoodAdapterOnClickHandler {
        void onClick(String foodId, FoodAdapterViewHolder vh,int select_position);
    }
}
