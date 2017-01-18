package com.satoaki.vertretungsplan;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

public class Welcome extends AppCompatActivity {
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    ImageView slider;
    NumberPicker numberPicker;
    ImageButton continueButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        numberPicker = (NumberPicker) findViewById(R.id.welcome_numberPicker);
        numberPicker.animate().alpha(0.0f).setDuration(1);
        numberPicker.setFocusable(false);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(S.ValidKlassen.length-1
        );



        slider= (ImageView)findViewById(R.id.Welcome_slider);
        slider.animate().translationYBy(500).setDuration(1);
        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                long t = System.currentTimeMillis();
                firsttime = S.init();
                while (t + 500 > System.currentTimeMillis()) {};
                if(firsttime){
                    doLogin();
                }else {doEnter();}
            }
        }).start();
    }

    private void doEnter() {
        Intent i = new Intent(getApplicationContext(), Main.class);
        startActivity(i);
    }

    private void doLogin() {
        TextView Version = (TextView)findViewById(R.id.Welcome_version);
        TextView Credits = (TextView)findViewById(R.id.WelcomeCredits);
        Credits.animate().alpha(0.0f).setDuration(1000);
        Version.animate().alpha(0.0f).setDuration(1000);
        mAnimation = new TranslateAnimation(
                TranslateAnimation.ABSOLUTE, 0f,
                TranslateAnimation.ABSOLUTE, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, -0.04f);
        mAnimation.setDuration(700);
        mAnimation.setRepeatCount(-1);
        mAnimation.setRepeatMode(Animation.REVERSE);
        slider.animate().translationYBy(-400).setDuration(500);
        slider.setAnimation(mAnimation);
        continueButton =(ImageButton)findViewById(R.id.imageButton);
        continueButton.animate().translationYBy(700).setDuration(1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                continueButton.setVisibility(View.VISIBLE);
            }
        });
        mContentView.setOnTouchListener(new View.OnTouchListener() {
            boolean touched = false;
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mAnimation.cancel();
                slider.animate().translationYBy(400).setDuration(400);
                if (!touched)
                    coninueLogin();
                touched = true;
                return false;
            }
        });
    }

    private void coninueLogin() {
        numberPicker.setFocusableInTouchMode(true);
        final ImageView logo = (ImageView)findViewById(R.id.WelcomeLogo);
        final TextView Title = (TextView)findViewById(R.id.VertretungenTitle);
        final TextView Title2 = (TextView)findViewById(R.id.WelcomeTitle);
        Title2.setTranslationX(-1000f);
        final TextView Sloagen = (TextView)findViewById(R.id.WelcomeSloagen);
        numberPicker.setFocusableInTouchMode(true);
        numberPicker.setDisplayedValues(S.ValidKlassen);
        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        numberPicker.setValue(1);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(numberPicker.getValue()!=0){
                    Log.d("State",String.valueOf(numberPicker.getValue())+" "+S.ValidKlassen[numberPicker.getValue()]);
                    S.p.add(new Person("Du",S.ValidKlassen[numberPicker.getValue()]));
                    doEnter();
                }
            }
        });
        continueButton.animate().translationY(0).setDuration(800);
        Title.animate().alpha(0f).translationX(500).setDuration(125);
        Sloagen.animate().translationYBy(-310).setDuration(1250);
        logo.animate().translationYBy(-310).setDuration(1250);
        numberPicker.animate().alpha(1f).translationY(-310).setDuration(1250);
        Title2.setAlpha(0f);
        Title2.setVisibility(View.VISIBLE);

        //anweisung.animate().alpha(1f).setDuration(200).setStartDelay(1300);
        Title2.animate().translationX(0).alpha(1f).setDuration(600).setStartDelay(700);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        delayedHide(0);
    }

    private void hide() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable,0);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable,0);
    }
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
    final String TAG = "main Welcome";
    static boolean firsttime;
    static boolean changeColor = true;
    static TranslateAnimation mAnimation;
}
