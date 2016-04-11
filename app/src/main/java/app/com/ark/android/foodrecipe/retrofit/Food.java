package app.com.ark.android.foodrecipe.retrofit;

import java.util.List;

/**
 * Created by ark on 3/27/2016.
 */
public class Food {

    public List<Hitset> hits;

    public class Hitset{
        public Recipe recipe;
    }

    public class Recipe {
        public String uri;
        public String label;
        public String image;
        public String source;
        public String url;
        public String shareAs;
        public String yield;
        public List<Ingredients> ingredients;
        public String calories;
        public String totalWeight;
        public TotalNutrients totalNutrients;
        public TotalDaily totalDaily;
    }

    public class Ingredients {
        public String text;
    }

    public class TotalNutrients{
        public Nutrients ENERC_KCAL;
        public Nutrients FAT;
        public Nutrients FASAT;
        public Nutrients FATRN;
        public Nutrients FAMS;
        public Nutrients FAPU;
        public Nutrients CHOCDF;
        public Nutrients FIBTG;
        public Nutrients SUGAR;
        public Nutrients PROCNT;
        public Nutrients CHOLE;
        public Nutrients NA;
        public Nutrients CA;
        public Nutrients MG;
        public Nutrients K;
        public Nutrients FE;
        public Nutrients ZN;
        public Nutrients P;
        public Nutrients VITA_RAE;
        public Nutrients VITC;
        public Nutrients THIA;
        public Nutrients RIBF;
        public Nutrients NIA;
        public Nutrients VITB6A;
        public Nutrients FOL;
        public Nutrients VITB12;
        public Nutrients VITD;
        public Nutrients TOCPHA;
        public Nutrients VITK1;

    }

    public class TotalDaily{
        public Nutrients ENERC_KCAL;
        public Nutrients FAT;
        public Nutrients FASAT;
        public Nutrients CHOCDF;
        public Nutrients FIBTG;
        public Nutrients PROCNT;
        public Nutrients CHOLE;
        public Nutrients NA;
        public Nutrients CA;
        public Nutrients MG;
        public Nutrients K;
        public Nutrients FE;
        public Nutrients ZN;
        public Nutrients P;
        public Nutrients VITA_RAE;
        public Nutrients VITC;
        public Nutrients THIA;
        public Nutrients RIBF;
        public Nutrients NIA;
        public Nutrients VITB6A;
        public Nutrients FOL;
        public Nutrients VITB12;
        public Nutrients VITD;
        public Nutrients TOCPHA;
        public Nutrients VITK1;

    }

    public class Nutrients{
        public String label;
        public String quantity;
        public String unit;
    }

}
