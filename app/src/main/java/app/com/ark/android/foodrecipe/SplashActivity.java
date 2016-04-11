package app.com.ark.android.foodrecipe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * Created by ark on 3/13/2016.
 */
public class SplashActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashing_screen);

        final ImageView iv = (ImageView) findViewById(R.id.apilogo);
        final Animation an = AnimationUtils.loadAnimation(getBaseContext(),R.anim.rotate);

        //fade out animation
        final Animation fadeout = AnimationUtils.loadAnimation(getBaseContext(),R.anim.abc_fade_out);

        //start animation
        iv.startAnimation(an);

        //animation listener
        an.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                //fade out animation
                iv.startAnimation(fadeout);

                //end splashing screen activity
                finish();

                //start main activity
                Intent i = new Intent(getBaseContext(),MainFoodActivity.class);
                startActivity(i);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
}
