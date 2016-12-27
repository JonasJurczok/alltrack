package org.linesofcode.eatr.day;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import org.linesofcode.eatr.App;
import org.linesofcode.eatr.R;
import org.linesofcode.eatr.framework.navigation.NavigatableBaseActivity;

import javax.inject.Inject;

public class GraphActivity extends NavigatableBaseActivity {

    private static final String TAG = GraphActivity.class.getName();
    public static final Integer CREATE_GRAPH_INTENT_CODE = 1;
    public static final Integer ADD_VALUE_INTENT_CODE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_activity_layout);

        ((App) getApplication()).getComponent().inject(this);

        initializeToolbar();

        inizializeContent();
    }

    private void inizializeContent() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //recyclerView.setAdapter(graphAdapter);
        //graphAdapter.setClickListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "clicked the floating action button.");
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "Received onActivityResult for request [" + requestCode + "] and result [" + resultCode + "]");
    }

    private void initializeToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout mainLayout = (DrawerLayout) findViewById(R.id.mainLayout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mainLayout, toolbar, R.string.navigation_open, R.string.navigation_close);
        mainLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setActiveMenuItem(R.id.nav_data_series);
    }

    @Override
    protected DrawerLayout getMainDrawerLayout() {
        return (DrawerLayout) findViewById(R.id.mainLayout);
    }
}
