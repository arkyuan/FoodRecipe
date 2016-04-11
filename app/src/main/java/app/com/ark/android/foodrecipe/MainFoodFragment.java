package app.com.ark.android.foodrecipe;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import app.com.ark.android.foodrecipe.data.FoodRecipeContract;

/**
 * Created by ark on 3/14/2016.
 */
public class MainFoodFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,OnTaskCompleted{

    private FoodViewAdapter mFoodAdapter;
    private static final int FOOD_LOADER =0;
    private int mChoiceMode;
    FetchFoodTask mFoodTask;
    RecyclerView mFood_Recipe_entry;
    String mSearch_query=null;
    private long mInitialSelectedDate = -1;


    public MainFoodFragment(){

    }

    @Override
    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(activity, attrs, savedInstanceState);
        TypedArray a = activity.obtainStyledAttributes(attrs, R.styleable.MainFoodFragment,
                0, 0);
        mChoiceMode = a.getInt(R.styleable.MainFoodFragment_android_choiceMode, AbsListView.CHOICE_MODE_NONE);

        a.recycle();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mFood_Recipe_entry) {
            mFood_Recipe_entry.clearOnScrollListeners();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mFoodAdapter.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        AdView mAdView = (AdView) rootView.findViewById(R.id.adView);
        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);

        mFood_Recipe_entry = (RecyclerView) rootView.findViewById(R.id.recyclerview_foodlist);

        final int columns = getResources().getInteger(R.integer.gallery_columns);
        // Set the layout manager
        mFood_Recipe_entry.setLayoutManager(new GridLayoutManager(getContext(), columns));

        View emptyView = rootView.findViewById(R.id.recyclerview_foodlist_empty);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mFood_Recipe_entry.setHasFixedSize(true);

        // The ForecastAdapter will take data from a source and
        // use it to populate the RecyclerView it's attached to.
        mFoodAdapter = new FoodViewAdapter(getActivity(), new FoodViewAdapter.FoodAdapterOnClickHandler() {

            @Override
            public void onClick(String foodId, FoodViewAdapter.FoodAdapterViewHolder vh,int select_position) {
                ((Callback) getActivity())
                        .onItemSelected(FoodRecipeContract.FoodRecipeEntry.buildFoodRecipeId(
                                        foodId),
                                vh,
                                select_position
                        );
            }

        }, emptyView,mChoiceMode);

        mFood_Recipe_entry.setAdapter(mFoodAdapter);

        final AppBarLayout appbarView = (AppBarLayout)rootView.findViewById(R.id.appbar);
        if (null != appbarView) {
            ViewCompat.setElevation(appbarView, 0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mFood_Recipe_entry.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        if (0 == mFood_Recipe_entry.computeVerticalScrollOffset()) {
                            appbarView.setElevation(0);
                        } else {
                            appbarView.setElevation(appbarView.getTargetElevation());
                        }
                    }
                });
            }
        }


        if (savedInstanceState != null) {
            mFoodAdapter.onRestoreInstanceState(savedInstanceState);
        }

        mFoodTask = new FetchFoodTask(getActivity(),"chicken+soup",this);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        restartLoader();


    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        // We hold for transition here just in-case the activity
        // needs to be re-created. In a standard return transition,
        // this doesn't actually make a difference.
        //if ( mHoldForTransition ) {
        //    getActivity().supportPostponeEnterTransition();
        //}
        getLoaderManager().initLoader(FOOD_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {


        boolean displayFavoriteOnly = Utility.getFavorite(getActivity());

        String sortOrder=null;
        String selection = null;
        String[] selctionArgs=null;
        if(displayFavoriteOnly){
            //sortOrder = FoodRecipeContract.FoodRecipeEntry.COLUMN_RATING + " DESC";
            selection = FoodRecipeContract.FoodRecipeEntry.COLUMN_FAVORITE + " = ?";
            selctionArgs= new String[]{"1"};
        }
        else if(mSearch_query!=null){
            //sortOrder = FoodRecipeContract.FoodRecipeEntry.COLUMN_RATING + " DESC";
            selection = FoodRecipeContract.FoodRecipeEntry.COLUMN_SEARCH_QUERY + " = ?";
            selctionArgs = new String[]{mSearch_query};
        }
        return new CursorLoader(getActivity(),
                FoodRecipeContract.FoodRecipeEntry.CONTENT_URI,
                Constant.FOODRECIPE_COLUMNS,
                selection,
                selctionArgs,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if(data!=null) {
            if (data.moveToFirst() || Utility.getFavorite(getActivity())) {
                mFoodAdapter.swapCursor(data);
                // .setRefreshing(false);
            } else {
                //onFoodChanged();
            }
        }
        updateEmptyView();

    }

    public void restartLoader(){
        getLoaderManager().restartLoader(FOOD_LOADER, null, this);
    }

    public void onFoodChanged(String search_query) {
        mSearch_query = search_query;
        getActivity().getContentResolver().delete(FoodRecipeContract.FoodRecipeEntry.CONTENT_URI,
                FoodRecipeContract.FoodRecipeEntry.COLUMN_SEARCH_QUERY + " != ? AND " + FoodRecipeContract.FoodRecipeEntry.COLUMN_FAVORITE + " = ? ",
                new String[]{mSearch_query, "0"});
        if(mFoodTask.getStatus()!= AsyncTask.Status.RUNNING) {
            mFoodTask= new FetchFoodTask(getActivity(),search_query,this);
            mFoodTask.execute(); // Fetch Food Async Task
            restartLoader();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onTaskCompleted() {
        updateEmptyView();
    }

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(Uri foodRecipeUri, FoodViewAdapter.FoodAdapterViewHolder vh,int select_position);
    }


    private void updateEmptyView() {
        if(mFoodAdapter.getItemCount()==0){
            TextView tv = (TextView) getView().findViewById(R.id.recyclerview_foodlist_empty);
            if(tv!=null){
                // if cursor is empty, why? do we have an invalid ?
                int message = R.string.empty_food_list;
                switch (Utility.getEmptyStatus()) {
                    case Utility.STATUS_SERVER_DOWN:
                        message = R.string.empty_food_list_server_down;
                        break;
                    case Utility.STATUS_DATA_INVALID:
                        message = R.string.empty_food_invaid;
                        break;
                    default:
                        if (!Utility.checkInternetConnection(getActivity()) ) {
                            message = R.string.empty_food_list_no_network;
                        }
                }
                tv.setText(message);
            }
        }
    }
}
