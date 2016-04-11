package app.com.ark.android.foodrecipe;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by ark on 4/5/2016.
 */
public class FoodRecipeApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(
                                Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(
                                Stetho.defaultInspectorModulesProvider(this))
                        .build());
                // Your normal application code here.  See SampleDebugApplication for Stetho initialization.
    }
}
