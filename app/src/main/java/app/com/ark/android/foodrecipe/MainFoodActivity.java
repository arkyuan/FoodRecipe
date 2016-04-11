package app.com.ark.android.foodrecipe;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import app.com.ark.android.foodrecipe.data.MySuggestionProvider;
import app.com.ark.android.foodrecipe.widget.FavoriteWidgetRemoteViewsService;

public class MainFoodActivity extends AppCompatActivity implements MainFoodFragment.Callback {

    //private static final String LOG_TAG = DetailFoodFragment.class.getSimpleName();
    private boolean mTwoPane;
    private static String DETAILFOODFRAGMENT_TAG = "DFFTAG";
    private SearchView mSearchView = null;
    public static int selected_food_id;
    public static int selected_position;

    //InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Uri contentUri = getIntent() != null ? getIntent().getData() : null;
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //mInterstitialAd = new InterstitialAd(this);
        //mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");

        //mInterstitialAd.setAdListener(new AdListener() {
        //    @Override
        //    public void onAdClosed() {
        //        requestNewInterstitial();
         //   }
       // });

       // requestNewInterstitial();

        selected_food_id =(int) getIntent().getDoubleExtra(FavoriteWidgetRemoteViewsService.SELECTED_FOOD_RECIPE_ID,0);
        selected_position = getIntent().getIntExtra(FavoriteWidgetRemoteViewsService.SELECTED_POSITION,0);
        //Log.d(LOG_TAG, "Reached MainActivity onCreate" + selected_food_id);

        if(findViewById(R.id.food_detail_container)!=null){
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (savedInstanceState == null) {
                DetailFoodFragment fragment = new DetailFoodFragment();
                if (contentUri != null) {
                    Bundle args = new Bundle();
                    args.putParcelable(DetailFoodFragment.DETAIL_URI, contentUri);
                    fragment.setArguments(args);
                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.food_detail_container, fragment, DETAILFOODFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
            //getSupportActionBar().setElevation(0f);
        }

        //MainFoodFragment mainfoodFragment = ((MainFoodFragment) getSupportFragmentManager().findFragmentById(R.id.frag_food_main));
        //if (contentUri != null) {
        //    mainfoodFragment.setInitialSelectedDate(
        //            WeatherContract.WeatherEntry.getDateFromUri(contentUri));
        //}

        handleIntent(getIntent());


    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                    MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE);
            suggestions.saveRecentQuery(query, null);

            //TextView tv = (TextView) findViewById(R.id.testview);
            //tv.setText(query);
            String queryAPI = query.replace(" ","+");
            MainFoodFragment MFF = (MainFoodFragment) getSupportFragmentManager().findFragmentById(R.id.frag_food_main);
            MFF.onFoodChanged(queryAPI);
            mSearchView.clearFocus();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        mSearchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        mSearchView.setQueryRefinementEnabled(true); //query refinement for search suggestions

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(Uri foodRecipeUri, FoodViewAdapter.FoodAdapterViewHolder vh,int select_position) {
        if(mTwoPane){
            Bundle args = new Bundle();
            args.putParcelable(DetailFoodFragment.DETAIL_URI, foodRecipeUri);

            DetailFoodFragment fragment = new DetailFoodFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.food_detail_container,fragment,DETAILFOODFRAGMENT_TAG)
                    .commit();
        }else {
            if (foodRecipeUri != null) {
                Intent intent = new Intent(this, DetailFoodActivity.class)
                        .setData(foodRecipeUri)
                        .putExtra(getString(R.string.selection_position), select_position);
                ActivityOptionsCompat activityOptions =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                                vh.mFoodImageView, getString(R.string.detail_icon_transition_name)+select_position);
                ActivityCompat.startActivity(this, intent, activityOptions.toBundle());
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //Log.v(LOG_TAG,"selected id: "+selected_food_id);
        outState.putInt("Selected_food", selected_food_id);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        //Log.v(LOG_TAG,"selected id: "+savedInstanceState.getInt("Selected_match"));
        selected_food_id = savedInstanceState.getInt("Selected_match");
        super.onRestoreInstanceState(savedInstanceState);
    }

//    private void requestNewInterstitial() {
//        AdRequest adRequest = new AdRequest.Builder()
//                .addTestDevice("SEE_YOUR_LOGCAT_TO_GET_YOUR_DEVICE_ID")
//                .build();
//
//        mInterstitialAd.loadAd(adRequest);
//    }


}
