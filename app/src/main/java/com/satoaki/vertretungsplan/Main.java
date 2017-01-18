package com.satoaki.vertretungsplan;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

public class Main extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    static final String TAG = "main Main";
    FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.content_main,new addFaecher()).commit();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_menu, menu);
        MenuItem item = menu.findItem(R.id.actionBar_addFach);
        item.setVisible(false);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_ueber) {
        } else if (id == R.id.nav_settings) {
            fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.content_main,new Einstellungen()).commit();
        } else if (id == R.id.nav_richtlinien) {

        } else if (id == R.id.nav_hud) {
            fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.content_main,new Vertretungen()).commit();

        } else if (id == R.id.nav_faecherVerwalten) {
            fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.content_main,new addFaecher()).commit();

        } else if (id == R.id.nav_credits) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
