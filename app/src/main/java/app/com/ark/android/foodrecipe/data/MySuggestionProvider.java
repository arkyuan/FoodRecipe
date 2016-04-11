package app.com.ark.android.foodrecipe.data;

import android.content.SearchRecentSuggestionsProvider;

/**
 * Created by ark on 3/18/2016.
 */
public class MySuggestionProvider extends SearchRecentSuggestionsProvider {
    public final static String AUTHORITY = "app.com.ark.android.foodrecipe.data.MySuggestionProvider";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public MySuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }

}
