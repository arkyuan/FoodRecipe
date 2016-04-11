package app.com.ark.android.foodrecipe;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by ark on 4/6/2016.
 */
public class DetailFoodActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {
            //create the detail fragment and add it to the activity
            //using a fragment transaction.

            Bundle argments = new Bundle();
            argments.putParcelable(DetailFoodFragment.DETAIL_URI, getIntent().getData());
            argments.putInt(DetailFoodFragment.SELECTOR_POSITION, getIntent().getIntExtra(getString(R.string.selection_position), 0));
            argments.putBoolean(DetailFoodFragment.DETAIL_TRANSITION_ANIMATION, true);

            DetailFoodFragment fragment = new DetailFoodFragment();
            fragment.setArguments(argments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.food_detail_container, fragment)
                    .commit();
            // Being here means we are in animation mode
            supportPostponeEnterTransition();
        }
    }
}
