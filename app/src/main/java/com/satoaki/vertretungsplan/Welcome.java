package com.satoaki.vertretungsplan;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Welcome extends AppCompatActivity {
    static boolean firsttime;
    static boolean changeColor = true;
    static TranslateAnimation mAnimation;
    final String TAG = "main Welcome";
    private final Handler mHideHandler = new Handler();
    AutoCompleteTextView Inp_Klasse;
    ImageView slider;
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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Inp_Klasse = (AutoCompleteTextView) findViewById(R.id.Welcome_inp_klasse);
        Inp_Klasse.setVisibility(View.INVISIBLE);
        Inp_Klasse.setFocusable(false);
        slider= (ImageView)findViewById(R.id.Welcome_slider);
        slider.animate().translationYBy(500);
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
                while (t + 500 > System.currentTimeMillis()) {}
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
        slider.animate().translationYBy(-400).setDuration(500);

        mAnimation = new TranslateAnimation(
                TranslateAnimation.ABSOLUTE, 0f,
                TranslateAnimation.ABSOLUTE, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, -0.04f);
        mAnimation.setDuration(700);
        mAnimation.setRepeatCount(-1);
        mAnimation.setRepeatMode(Animation.REVERSE);
        slider.setAnimation(mAnimation);

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
        ImageView logo = (ImageView)findViewById(R.id.WelcomeLogo);
        final TextView Title = (TextView)findViewById(R.id.WelcomeTitle);
        TextView Sloagen = (TextView)findViewById(R.id.WelcomeSloagen);
        logo.animate().translationYBy(-310).setDuration(1000);
        Title.animate().translationYBy(300).alpha(0.0f).setDuration(500).setListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                Title.setText("Willkommen");
                Title.animate().alpha(1.0f).translationYBy(-610).setDuration(1000).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                    }
                });
            }
        });
        Sloagen.animate().translationYBy(-310).setDuration(1000);
        Inp_Klasse.setVisibility(View.VISIBLE);
        Inp_Klasse.setFocusable(true);
        Inp_Klasse.setFocusableInTouchMode(true);
        Inp_Klasse.animate().translationYBy(-320).setDuration(1000);
        Inp_Klasse.animate().alpha(1.0f).setDuration(1000);
        changeColor = true;
        new Thread(new Runnable() {
            public void run() {
                while (changeColor){
                    while (Inp_Klasse.hasFocus()){
                        try{
                            getWindow().setStatusBarColor(Color.WHITE);
                        }catch (Exception e){Log.i(TAG, "Failed to chance Statusbarcolor");
                        }
                        changeColor = false;
                    }
                }
            }
        }).start();
        Inp_Klasse.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(i==KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN){
                    boolean found = false;
                    if(Inp_Klasse.getText().toString().toLowerCase().contains("ks1"))
                            Inp_Klasse.setText("Ks-1");
                    if(Inp_Klasse.getText().toString().toLowerCase().contains("ks2"))
                        Inp_Klasse.setText("Ks-2");
                    for(int b=0;b<S.ValidKlassen.length;b++)
                        if (!found && S.ValidKlassen[b].toLowerCase().contains(Inp_Klasse.getText().toString().toLowerCase())) {
                            S.p.add(new Person("Du", S.ValidKlassen[b]));
                            found = true;
                            doEnter();
                            changeColor = false;
                        }
                    if (!found){
                        Inp_Klasse.setText("");
                        Toast.makeText(getApplicationContext(),"die Klasse ist nicht vorhanden!",Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        });

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
}
