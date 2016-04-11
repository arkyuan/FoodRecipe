package app.com.ark.android.foodrecipe;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by hp1 on 21-01-2015.
 */
public class IngredientsFragment extends Fragment {

    static final String INGREDIENT_LINES = "INGREDIENTS";
    static final String SERVINGS = "SERVINGS";
    String mIngredients;
    int mServings;
    TextView mIngredientContent;
    TextView mServingContent;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView =inflater.inflate(R.layout.ingredients,container,false);

        Bundle arguments = getArguments();
        if(arguments!=null){
            mIngredients = arguments.getString(IngredientsFragment.INGREDIENT_LINES);
            mServings = arguments.getInt(IngredientsFragment.SERVINGS);
        }

        mServingContent = (TextView) rootView.findViewById(R.id.serving);
        mServingContent.setText(getString(R.string.a11y_serving, mServings));
        mServingContent.setContentDescription(getString(R.string.a11y_serving, mServings));
        mIngredientContent = (TextView) rootView.findViewById(R.id.ingredient_lines);
        mIngredientContent.setText(mIngredients);
        mIngredientContent.setContentDescription(mIngredients);
        return rootView;
    }
}