package org.linesofcode.alltrack.graph;

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

import org.linesofcode.alltrack.App;
import org.linesofcode.alltrack.AppComponent;
import org.linesofcode.alltrack.DaggerAppComponent;
import org.linesofcode.alltrack.R;
import org.linesofcode.alltrack.framework.navigation.NavigatableBaseActivity;

import javax.inject.Inject;

/**
 * Copyright 2015 Jonas Jurczok (jonasjurczok@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class GraphActivity extends NavigatableBaseActivity implements GraphAdapterClickListener {

    private static final String TAG = GraphActivity.class.getName();
    public static final Integer CREATE_GRAPH_INTENT_CODE = 1;
    public static final Integer ADD_VALUE_INTENT_CODE = 2;


    @Inject
    GraphAdapter graphAdapter;

    @Inject
    GraphService graphService;

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

        recyclerView.setAdapter(graphAdapter);
        graphAdapter.setClickListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createGraph();
            }
        });
    }

    protected void createGraph() {
        Intent graphDetailIntent = new Intent(this, CreateGraphActivity.class);
        startActivityForResult(graphDetailIntent, CREATE_GRAPH_INTENT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "Received onActivityResult for request [" + requestCode + "] and result [" + resultCode + "]");
        if (requestCode == CREATE_GRAPH_INTENT_CODE) {
            if (resultCode == RESULT_OK) {
                Log.d(TAG, "updating graphs...");
                graphAdapter.updateGraphs();
            }
        } else if (requestCode == ADD_VALUE_INTENT_CODE) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                int position = extras.getInt("org.linesofcode.alltrack.position");
                int graphId = extras.getInt("org.linesofcode.alltrack.graphId");
                Log.d(TAG, "Value added. Updating graph [" + graphId + "] for position [" + position + "].");
                graphAdapter.updateGraph(graphId, position);
            }
        }
    }

    @Override
    public void onClick(Graph graph, int position) {
        Intent intent = new Intent(this, AddValueActivitiy.class);
        intent.putExtra("graphId", graph.getId());
        intent.putExtra("position", position);
        startActivityForResult(intent, ADD_VALUE_INTENT_CODE);
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
        setActiveMenuItem(R.id.nav_data_series);
    }

    @Override
    protected DrawerLayout getMainDrawerLayout() {
        return (DrawerLayout) findViewById(R.id.mainLayout);
    }
}
