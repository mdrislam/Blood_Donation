package com.mristudio.blooddonation.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.mristudio.blooddonation.R;
import com.mristudio.blooddonation.fragment.WelcomeScreenFragmentOne;
import com.mristudio.blooddonation.fragment.WelcomeScreenFragmentThree;
import com.mristudio.blooddonation.fragment.WelcomeScreenFragmentTwo;
import com.mristudio.blooddonation.utility.IntroPrefManager;

public class WelcomeActivity extends AppCompatActivity implements Animation.AnimationListener {
    private static final String TAG = "WelcomeActivity";
    public static ViewPager pager;
    private IntroPrefManager prefManager;
    private ImageView splashScreen;
    Animation animFadeIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        pager = (ViewPager) findViewById(R.id.view_pager);
        splashScreen = (ImageView) findViewById(R.id.splashScreen);
        prefManager = new IntroPrefManager(this);

        Log.e(TAG,"State ");
        prefManager.setFirstTimeLaunch(true);
        /**
         * cheak is it First Launch Or Not
         * */
        if (!prefManager.isFirstTimeLaunch()) {
            splashScreen.setVisibility(View.VISIBLE);
            pager.setVisibility(View.GONE);
            /*load the animation*/
            animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(),
                    R.anim.animation_fade_in);
            animFadeIn.setAnimationListener(this);
            splashScreen.startAnimation(animFadeIn);
        } else {
            splashScreen.setVisibility(View.GONE);
            pager.setVisibility(View.VISIBLE);
            setViewPager();
        }

    }


    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }


    /**
     * SetCurrent ViewPager in Activity
     */
    private void setViewPager() {

        FragmentManager fm = getSupportFragmentManager();
        ViewPagerAdapter adapter = new ViewPagerAdapter(fm);
        pager.setAdapter(adapter);

    }

    /**
     * Set Current ViewPager Item
     */
    public static void setCurrentItem(int selectedPosition) {
        pager.setCurrentItem(selectedPosition, true);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        launchHomeScreen();
        this.finish();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    /**
     * If The Application is 2nd Time Launch then the Method is Called and then cheak the user is loging or not
     * if the user loging State is true then Launch MainActivity otherwise Launch Loging Activity
     */
    private void launchHomeScreen() {
        startActivity(new Intent(this, MainActivity.class));
        finish();

    }

    /**
     * View Pager Adapter Class
     */
    private class ViewPagerAdapter extends FragmentPagerAdapter {
        public ViewPagerAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new WelcomeScreenFragmentOne();
                case 1:
                    return new WelcomeScreenFragmentTwo();
                case 2:
                    return new WelcomeScreenFragmentThree();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}