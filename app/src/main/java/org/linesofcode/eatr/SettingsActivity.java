package org.linesofcode.eatr;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;

import org.linesofcode.eatr.framework.navigation.NavigatableBaseActivity;

public class SettingsActivity extends NavigatableBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity_layout);

        ((App) getApplication()).getComponent().inject(this);

        initializeToolbar();

        inizializeContent();
    }

    private void inizializeContent() {
    }

    private void initializeToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout mainLayout = (DrawerLayout) findViewById(R.id.mainLayout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mainLayout, toolbar, R.string.navigation_open, R.string.navigation_close);
        mainLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setActiveMenuItem(R.id.nav_settings);
    }

    @Override
    protected DrawerLayout getMainDrawerLayout() {
        return (DrawerLayout) findViewById(R.id.mainLayout);
    }

}
