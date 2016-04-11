package app.com.ark.android.foodrecipe;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by hp1 on 21-01-2015.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created
    String mIngredients; //ingredients required
    int mServing; // number of servings
    String mNutritionData; // nutrition values
    private String mRecipeUrlData; //cooking directions


    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapter(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
    }

    public void setIngredientData(String ingredientData){
        mIngredients=ingredientData;
    }

    public void setServingData(int servingData){
        mServing=servingData;
    }
    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {

        if(position == 0) // if the position is 0 we are returning the First tab
        {
            Bundle args=new Bundle();
            args.putString(IngredientsFragment.INGREDIENT_LINES, mIngredients);
            args.putInt(IngredientsFragment.SERVINGS, mServing);
            IngredientsFragment tab1 = new IngredientsFragment();
            tab1.setArguments(args);
            return tab1;
        } else if(position == 1){ // if the position is 1 we are returning the second tab
            Bundle args = new Bundle();
            args.putString(NutritionFragment.NUTRITION, mNutritionData);
            NutritionFragment tab2 = new NutritionFragment();
            tab2.setArguments(args);
            return tab2;
        } else if(position==2){ //returning the 3rd tab
            //Bundle args = new Bundle();
            //args.putString(DirectionsFragment.DIRECTIONURL, mRecipeUrlData);
            DirectionsFragment tab3 = new DirectionsFragment();
            //tab3.setArguments(args);
            return tab3;
        }
        return null;

    }

    @Override
    public int getItemPosition(Object object) {
        if(object instanceof DirectionsFragment) {
            DirectionsFragment f = (DirectionsFragment) object;
            if (f != null) {
                f.onTab(mRecipeUrlData);
            }
        }
        return super.getItemPosition(object);
    }
// This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return NumbOfTabs;
    }

    public void setNutritionData(String nutritionData) {
        mNutritionData = nutritionData;
    }

    public void setRecipeUrlData(String recipeUrlData) {
        this.mRecipeUrlData = recipeUrlData;
    }
}