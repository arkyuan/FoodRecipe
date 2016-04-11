package app.com.ark.android.foodrecipe.retrofit;

import java.util.List;

/**
 * Created by ark on 4/2/2016.
 */
public class FoodRecipeDetail {

        public List<Nutrition> nutritionEstimates;
        public String totalTime;
        public List<Image> images;
        public String name;
        public Source source;
        public String id;
        public List<String> ingredientLines;
        public String numberOfServings;
        public String totalTimeInSeconds;
        public Attributes attributes;
        public double rating;

    public class Nutrition {
        public String attribute;
        public String description;
        public String value;
        public Unit unit;
    }

    public class Unit{
        public String id;
        public String name;
        public String abbreviation;
        public String plural;
        public String pluralAbbreviation;
    }

    public class Image {
        public String hostedSmallUrl;
        public String hostedMediumUrl;
        public String hostedLargeUrl;
    }

    public class Source{
        public String sourceDisplayName;
        public String sourceSiteUrl;
        public String sourceRecipeUrl;
    }

    public class Attributes {
        public List<String> course;
        public List<String> cuisine;
    }


}
