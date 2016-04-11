package app.com.ark.android.foodrecipe;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;

import app.com.ark.android.foodrecipe.SlidingTab.SlidingTabLayout;
import app.com.ark.android.foodrecipe.data.FoodRecipeContract;

/**
 * Created by ark on 3/14/2016.
 */
public class DetailFoodFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private Uri mUri;
    private int mPosition;
    private ShareActionProvider mShareActionProvider;
    private String mFoodId;
    private int mFavoriteValue;
    static final String DETAIL_URI = "URI";
    static final String SELECTOR_POSITION = "POSITION";
    private static final String FOOD_SHARE_HASHTAG = " #FoodRecipeApp";
    static final String DETAIL_TRANSITION_ANIMATION = "DTA";
    private boolean mTransitionAnimation;
    private static final int DETAIL_LOADER =0;

    //tabs
    ViewPager mPager;
    ViewPagerAdapter mPagerAdapter;
    SlidingTabLayout mTabs;
    CharSequence mTitles[] = {"Ingredients","Nutrition","Directions"};
    int mNumboftabs = 3;

    private ImageView mImageView;
    private ImageButton mFavorite;

    public DetailFoodFragment() {
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if(arguments!=null){
            mUri = arguments.getParcelable(DetailFoodFragment.DETAIL_URI);
            mPosition = arguments.getInt(DetailFoodFragment.SELECTOR_POSITION);
            mTransitionAnimation = arguments.getBoolean(DetailFoodFragment.DETAIL_TRANSITION_ANIMATION, false);
        }

        View rootView = inflater.inflate(R.layout.fragment_detail_start, container, false);
        mImageView = (ImageView) rootView.findViewById(R.id.backdrop);
        mFavorite = (ImageButton) rootView.findViewById(R.id.btn_favorite);

        mFavorite.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //old favorite value
                    int favorite_old = mFavoriteValue;
                    //toggle the old value
                    int favorite_new = Math.abs(favorite_old - 1);

                    //update the value into the database
                    ContentValues updatedValues = new ContentValues();
                    updatedValues.put(FoodRecipeContract.FoodRecipeEntry.COLUMN_FAVORITE, favorite_new);

                    getContext().getContentResolver().update(
                            mUri, updatedValues, FoodRecipeContract.FoodRecipeEntry.COLUMN_FOOD_RECIPE_ID + "= ?",
                            new String[]{mUri.getLastPathSegment()});

                    mFavoriteValue = favorite_new;

                    Utility.updateWidgets(getContext());
                }
                return true;
            }


        });

        // Assigning ViewPager View and setting the adapter
        mPager = (ViewPager) rootView.findViewById(R.id.pager);
        // Assiging the Sliding Tab Layout View
        mTabs = (SlidingTabLayout) rootView.findViewById(R.id.tabs);

        return rootView;
    }

    private Intent createShareFoodRecipeIntent(){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "http://www.yummly.com/recipe/" + mFoodId + FOOD_SHARE_HASHTAG);
        return shareIntent;
    }

    private void finishCreatingMenu(Menu menu) {
        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.action_share);
        menuItem.setIntent(createShareFoodRecipeIntent());
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        if (getActivity() instanceof DetailFoodActivity) {
            // Inflate the menu; this adds items to the action bar if it is present.
            inflater.inflate(R.menu.detailfragment, menu);
            finishCreatingMenu(menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:{
                ActivityCompat.finishAfterTransition(getActivity());
            }

                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void startPostponedEnterTransition() {

        mImageView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mImageView.getViewTreeObserver().removeOnPreDrawListener(this);
                getActivity().startPostponedEnterTransition();
                return true;
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //Log.v(LOG_TAG, "In onCreateLoader");
        //Now create and return a CursorLoader tha will take care of
        //creating a Cursor for the data being displayed
        if(mUri!=null) {
            return new CursorLoader(
                    getActivity(),
                    mUri,
                    Constant.FOODRECIPE_COLUMNS,
                    null,
                    null,
                    null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        //Log.v(LOG_TAG, "In onLoadFinished");
        if(data!=null && data.moveToFirst()) {
            //food recipe name
            String recipe_name = data.getString(Constant.COL_FOOD_RECIPE_NAME);
            mFoodId = data.getString(Constant.COL_FOOD_RECIPE_ID);


            AppCompatActivity activity = (AppCompatActivity) getActivity();
            final Toolbar toolbarView = (Toolbar) getView().findViewById(R.id.toolbar);
            CollapsingToolbarLayout collapsingToolbar =
                    (CollapsingToolbarLayout) getView().findViewById(R.id.collapsing_toolbar);


            // We need to start the enter transition after the data has loaded
            if (mTransitionAnimation) {
                //image
                mImageView.setTransitionName(getString(R.string.detail_icon_transition_name) + mPosition);

                activity.supportStartPostponedEnterTransition();

                if (null != toolbarView) {
                    activity.setSupportActionBar(toolbarView);
                    activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
                    activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    collapsingToolbar.setTitle(recipe_name);
                    collapsingToolbar.setContentDescription(getString(R.string.a11y_food, recipe_name));
                }

            } else {
                if (null != toolbarView) {
                    Menu menu = toolbarView.getMenu();
                    if (null != menu) menu.clear();
                    toolbarView.inflateMenu(R.menu.detailfragment);
                    //finishCreatingMenu(toolbarView.getMenu());
                }
            }


            //startPostponedEnterTransition();
            byte[] image_url = data.getBlob(Constant.COL_FOOD_RECIPE_LARGE_IMAGE);
            ByteArrayInputStream posterimageStream = new ByteArrayInputStream(image_url);
            Bitmap foodImage = BitmapFactory.decodeStream(posterimageStream);
            mImageView.setImageBitmap(foodImage);
            mImageView.setContentDescription(getString(R.string.a11y_food_icon, recipe_name));

            //Read Favorite
            int favorite = data.getInt(Constant.COL_FOOD_RECIPE_FAVORITE);
            mFavoriteValue=favorite;
            boolean fav = (favorite!=0);
            mFavorite.setPressed(fav);
            mFavorite.setContentDescription(getString(R.string.content_desc_favorite) + fav);

            // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
            mPagerAdapter = new ViewPagerAdapter(getFragmentManager(),mTitles,mNumboftabs);
            mPagerAdapter.setIngredientData(data.getString(Constant.COL_FOOD_RECIPE_INGREDIENT_LINES));
            mPagerAdapter.setServingData(data.getInt(Constant.COL_FOOD_RECIPE_SERVINGS));
            mPagerAdapter.setNutritionData(data.getString(Constant.COL_FOOD_RECIPE_NUTRITION));
            //mPagerAdapter.setRecipeUrlData(data.getString(Constant.COL_FOOD_RECIPE_DIRECTION));

            mPager.setAdapter(mPagerAdapter);

            mTabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

            // Setting Custom Color for the Scroll bar indicator of the Tab View
            mTabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
                @Override
                public int getIndicatorColor(int position) {
                    return ContextCompat.getColor(getContext(), R.color.colorAccent);
                }
            });

            final String recipe_url = data.getString(Constant.COL_FOOD_RECIPE_DIRECTION);
                    // Setting the ViewPager For the SlidingTabsLayout
                    mTabs.setViewPager(mPager);

            mTabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    if(position==2){ //need to only trigger the "loading" dialog when page changed to recipe view
                        mPagerAdapter.setRecipeUrlData(recipe_url);
                        mPagerAdapter.notifyDataSetChanged();
                    }
                    //Log.d("test", "position = " + position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }

            });
        }

        // If onCreateOptionsMenu has already happened, we need to update the share intent now.
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareFoodRecipeIntent());
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
