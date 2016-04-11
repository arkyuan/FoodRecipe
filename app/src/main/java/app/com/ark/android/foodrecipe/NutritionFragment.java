package app.com.ark.android.foodrecipe;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

/**
 * Created by hp1 on 21-01-2015.
 */
public class NutritionFragment extends Fragment {

    static final String NUTRITION = "NUTRITION";

    TableLayout mNutrition_table;
    TextView mCalories;
    TextView mCalories_fat;
    TextView mTotal_fat;
    TextView mTotal_fat_dv;
    TextView mSaturated_fat;
    TextView mSaturated_fat_dv;
    TextView mTrans_fat;
    TextView mTrans_fat_dv;
    TextView mCholesterol;
    TextView mCholesterol_dv;
    TextView mSodium;
    TextView mSodium_dv;
    TextView mPotassium;
    TextView mPotassium_dv;
    TextView mTotal_carbohydrate;
    TextView mTotal_carbohydrate_dv;
    TextView mDietary_fiber;
    TextView mDietary_fiber_dv;
    TextView mSugars;
    TextView mProtein;
    TextView mProtein_dv;
    TextView mVitamin_a;
    TextView mVitamin_a_dv;
    TextView mVitamin_c;
    TextView mVitamin_c_dv;
    TextView mCalcium;
    TextView mCalcium_dv;
    TextView mIron;
    TextView mIron_dv;

    TextView mEmptyView;

    String mNutrition;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.nutrition,container,false);

        Bundle arguments = getArguments();
        if(arguments!=null){
            mNutrition = arguments.getString(NutritionFragment.NUTRITION);
        }

        if(mNutrition==null){
            mNutrition_table = (TableLayout) rootView.findViewById(R.id.nutrition_table);
            mNutrition_table.setVisibility(View.GONE);
            mEmptyView = (TextView) rootView.findViewById(R.id.nutrition_empty);
            mEmptyView.setVisibility(View.VISIBLE);
        }else {
            mCalories = (TextView) rootView.findViewById(R.id.calories);
            mCalories_fat = (TextView) rootView.findViewById(R.id.calories_from_fat);
            mTotal_fat = (TextView) rootView.findViewById(R.id.total_fat);
            mTotal_fat_dv = (TextView) rootView.findViewById(R.id.total_fat_dv);
            mSaturated_fat = (TextView) rootView.findViewById(R.id.saturated_fat);
            mSaturated_fat_dv = (TextView) rootView.findViewById(R.id.saturated_fat_dv);
            mTrans_fat = (TextView) rootView.findViewById(R.id.trans_fat);
            mTrans_fat_dv = (TextView) rootView.findViewById(R.id.trans_fat_dv);
            mCholesterol = (TextView) rootView.findViewById(R.id.cholesterol);
            mCholesterol_dv = (TextView) rootView.findViewById(R.id.cholesterol_dv);
            mSodium = (TextView) rootView.findViewById(R.id.sodium);
            mSodium_dv = (TextView) rootView.findViewById(R.id.sodium_dv);
            mPotassium = (TextView) rootView.findViewById(R.id.potassium);
            mPotassium_dv = (TextView) rootView.findViewById(R.id.potassium_dv);
            mTotal_carbohydrate = (TextView) rootView.findViewById(R.id.total_carbohydrate);
            mTotal_carbohydrate_dv = (TextView) rootView.findViewById(R.id.carbohydrate_dv);
            mDietary_fiber = (TextView) rootView.findViewById(R.id.dietary_fiber);
            mDietary_fiber_dv = (TextView) rootView.findViewById(R.id.dietary_fiber_dv);
            mSugars = (TextView) rootView.findViewById(R.id.sugars);
            mProtein = (TextView) rootView.findViewById(R.id.protein);
            mProtein_dv = (TextView) rootView.findViewById(R.id.protein_dv);
            mVitamin_a = (TextView) rootView.findViewById(R.id.vitamin_a);
            mVitamin_a_dv = (TextView) rootView.findViewById(R.id.vitamin_a_dv);
            mVitamin_c = (TextView) rootView.findViewById(R.id.vitamin_c);
            mVitamin_c_dv = (TextView) rootView.findViewById(R.id.vitamin_c_dv);
            mCalcium = (TextView) rootView.findViewById(R.id.calcium);
            mCalcium_dv = (TextView) rootView.findViewById(R.id.calcium_dv);
            mIron = (TextView) rootView.findViewById(R.id.iron);
            mIron_dv = (TextView) rootView.findViewById(R.id.iron_dv);

            List<NutritionEst> items = new Vector<NutritionEst>();

            String[] nutrition_list = mNutrition.split(getString(R.string.nutrition_separator));
            for(String s : nutrition_list){
                items.add(getNutrition(s));
            }

            for(NutritionEst n : items){
                if(n.attribute.equals(getString(R.string.fat_kcal))){
                    mCalories_fat.setText(getString(R.string.a11y_fat_kcal, n.getValue(),n.getUnit()));
                    mCalories_fat.setContentDescription(getString(R.string.a11y_fat_kcal, n.getValue(),n.getUnit()));
                } else if(n.attribute.equals(getString(R.string.k))){
                    mPotassium.setText(getString(R.string.a11y_k, n.getValue(), n.getUnit()));
                    mPotassium.setContentDescription(getString(R.string.a11y_fat_kcal, n.getValue(), n.getUnit()));

                    int K_dv = (int) ((n.getValue()/3.5)*100);
                    String S_K_dv = String.valueOf(K_dv)+"%";
                    mPotassium_dv.setText(S_K_dv);
                    mPotassium_dv.setContentDescription(S_K_dv);

                } else if(n.attribute.equals(getString(R.string.fasat))){
                    mSaturated_fat.setText(getString(R.string.a11y_fasat, n.getValue(), n.getUnit()));
                    mSaturated_fat.setContentDescription(getString(R.string.a11y_fasat, n.getValue(), n.getUnit()));

                    int fasat_dv = (int) ((n.getValue()/20)*100);
                    String S_fasat_dv = String.valueOf(fasat_dv)+"%";
                    mSaturated_fat_dv.setText(S_fasat_dv);
                    mSaturated_fat_dv.setContentDescription(S_fasat_dv);

                } else if(n.attribute.equals(getString(R.string.fe))){
                    mIron.setText(getString(R.string.a11y_fe));
                    mIron.setContentDescription(getString(R.string.a11y_fe));

                    int fe_dv = (int) ((n.getValue()/0.018)*100);
                    String S_fe_dv = String.valueOf(fe_dv)+"%";
                    mIron_dv.setText(S_fe_dv);
                    mIron_dv.setContentDescription(S_fe_dv);
                } else if(n.attribute.equals(getString(R.string.sugar))){
                    mSugars.setText(getString(R.string.a11y_sugar, n.getValue(),n.getUnit()));
                    mSugars.setContentDescription(getString(R.string.a11y_sugar, n.getValue(), n.getUnit()));
                } else if(n.attribute.equals(getString(R.string.fibtg))){
                    mDietary_fiber.setText(getString(R.string.a11y_fibtg, n.getValue(), n.getUnit()));
                    mDietary_fiber.setContentDescription(getString(R.string.a11y_fibtg, n.getValue(), n.getUnit()));

                    int fibtg = (int) ((n.getValue()/25)*100);
                    String S_fibtg = String.valueOf(fibtg)+"%";
                    mDietary_fiber_dv.setText(S_fibtg);
                    mDietary_fiber_dv.setContentDescription(S_fibtg);
                } else if(n.attribute.equals(getString(R.string.procnt))){
                    mProtein.setText(getString(R.string.a11y_procnt, n.getValue(), n.getUnit()));
                    mProtein.setContentDescription(getString(R.string.a11y_procnt, n.getValue(), n.getUnit()));

                    int procnt_dv = (int) ((n.getValue()/50)*100);
                    String S_procnt_dv = String.valueOf(procnt_dv)+"%";
                    mProtein_dv.setText(S_procnt_dv);
                    mProtein_dv.setContentDescription(S_procnt_dv);
                } else if(n.attribute.equals(getString(R.string.chocdf))){
                    mTotal_carbohydrate.setText(getString(R.string.a11y_chocdf, n.getValue(), n.getUnit()));
                    mTotal_carbohydrate.setContentDescription(getString(R.string.a11y_chocdf, n.getValue(), n.getUnit()));

                    int chocdf_dv = (int) ((n.getValue()/300)*100);
                    String S_chocdf_dv = String.valueOf(chocdf_dv)+"%";
                    mTotal_carbohydrate_dv.setText(S_chocdf_dv);
                    mTotal_carbohydrate_dv.setContentDescription(S_chocdf_dv);
                } else if(n.attribute.equals(getString(R.string.ca))){
                    mCalcium.setText(getString(R.string.a11y_ca));
                    mCalcium.setContentDescription(getString(R.string.a11y_ca));

                    int ca_dv = (int) ((n.getValue()/1) * 100);
                    String S_ca_dv = String.valueOf(ca_dv)+"%";
                    mCalcium_dv.setText(S_ca_dv);
                    mCalcium_dv.setContentDescription(S_ca_dv);
                } else if(n.attribute.equals(getString(R.string.vita_iu))){
                    mVitamin_a.setText(getString(R.string.a11y_vita_iu));
                    mVitamin_a.setContentDescription(getString(R.string.a11y_vita_iu));

                    int vita_iu_dv = (int) ((n.getValue()/5000) *100);
                    String S_vita_iu_dv = String.valueOf(vita_iu_dv)+"%";
                    mVitamin_a_dv.setText(S_vita_iu_dv);
                    mVitamin_a_dv.setContentDescription(S_vita_iu_dv);
                } else if(n.attribute.equals(getString(R.string.enerc_kcal))){
                    mCalories.setText(getString(R.string.a11y_enerc_kcal, n.getValue(),n.getUnit()));
                    mCalories.setContentDescription(getString(R.string.a11y_enerc_kcal, n.getValue(), n.getUnit()));
                } else if(n.attribute.equals(getString(R.string.vitc))){
                    mVitamin_c.setText(getString(R.string.a11y_vitc));
                    mVitamin_c.setContentDescription(getString(R.string.a11y_vitc));

                    int vitc_dv = (int) ((n.getValue()/0.06)*100);
                    String S_vitc_dv = String.valueOf(vitc_dv)+"%";
                    mVitamin_c_dv.setText(S_vitc_dv);
                    mVitamin_a_dv.setContentDescription(S_vitc_dv);
                } else if(n.attribute.equals(getString(R.string.fat))){
                    mTotal_fat.setText(getString(R.string.a11y_fat, n.getValue(), n.getUnit()));
                    mTotal_fat.setContentDescription(getString(R.string.a11y_fat, n.getValue(), n.getUnit()));

                    int fat_dv = (int) ((n.getValue()/65)*100);
                    String S_fat_dv = String.valueOf(fat_dv)+"%";
                    mTotal_fat_dv.setText(S_fat_dv);
                    mTotal_fat_dv.setContentDescription(S_fat_dv);
                } else if(n.attribute.equals(getString(R.string.na))){
                    mSodium.setText(getString(R.string.a11y_na, n.getValue(), n.getUnit()));
                    mSodium.setContentDescription(getString(R.string.a11y_na, n.getValue(), n.getUnit()));

                    int na_dv = (int) ((n.getValue()/2.4)*100);
                    String S_na_dv = String.valueOf(na_dv)+"%";
                    mSodium_dv.setText(S_na_dv);
                    mSodium_dv.setContentDescription(S_na_dv);
                } else if(n.attribute.equals(getString(R.string.fatrn))){
                    mTrans_fat.setText(getString(R.string.a11y_fatrn, n.getValue(),n.getUnit()));
                    mTrans_fat.setContentDescription(getString(R.string.a11y_fatrn, n.getValue(), n.getUnit()));
                } else if(n.attribute.equals(getString(R.string.chole))){
                    mCholesterol.setText(getString(R.string.a11y_chole, n.getValue(), n.getUnit()));
                    mCholesterol.setContentDescription(getString(R.string.a11y_chole, n.getValue(),n.getUnit()));

                    int chole_dv = (int) ((n.getValue()/0.3)*100);
                    String S_chole_dv = String.valueOf(chole_dv)+"%";
                    mCholesterol_dv.setText(S_chole_dv);
                    mCholesterol_dv.setContentDescription(S_chole_dv);
                }
            }

        }


        return rootView;
    }

    public NutritionEst getNutrition(String s){
        List<String> s_list = Arrays.asList(s.split(getString(R.string.nutrition_attribute_separator)));
        String att = s_list.get(0);
        List<String> v_list = Arrays.asList(s_list.get(1).split(getString(R.string.nutrition_value_unit_separator)));
        double value = Double.parseDouble(v_list.get(0));
        String unit = v_list.get(1);

        NutritionEst nutritionEst = new NutritionEst(att,value,unit);
        return nutritionEst;
    }

    private class NutritionEst{


        String attribute;
        double value;
        String unit;

        public NutritionEst(String attri,double val, String uni){
            attribute=attri;
            value=val;
            unit=uni;
        }

        public String getAttribute() {
            return attribute;
        }

        public void setAttribute(String attribute) {
            this.attribute = attribute;
        }

        public double getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }



    }
}