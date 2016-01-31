package org.linesofcode.alltrack.graph;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.linesofcode.alltrack.App;
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
public class CreateGraphActivity extends NavigatableBaseActivity {

    public static final String CREATE_GRAPH_ACTION_CODE = "org.linesofcode.alltrack.graph.CREATE";

    @Inject
    GraphService graphService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_detail_activity_layout);
        ((App) getApplication()).getObjectGraph().inject(this);

        initializeToolbar();

        initializeContent();
    }

    private void initializeContent() {
        Button okButton = (Button) findViewById(R.id.graph_detail_ok);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createGraph();
            }
        });

        Button cancelButton = (Button) findViewById(R.id.graph_detail_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED, new Intent(CREATE_GRAPH_ACTION_CODE));
                finish();
            }
        });

        EditText graphName = (EditText) findViewById(R.id.edit_graph_name);
        graphName.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_ENTER:
                            createGraph();
                            return true;
                    }
                }
                return false;
            }
        });
    }

    private void createGraph() {
        EditText graphEdit = (EditText) findViewById(R.id.edit_graph_name);
        String graphName = graphEdit.getText().toString();

        if (graphName.length() == 0) {
            Toast.makeText(this, R.string.graph_detail_graph_name_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        graphService.createNewGraph(graphName);
        setResult(Activity.RESULT_OK);
        finish();
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
