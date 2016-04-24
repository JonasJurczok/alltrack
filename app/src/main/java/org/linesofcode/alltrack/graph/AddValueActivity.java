package org.linesofcode.alltrack.graph;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import org.linesofcode.alltrack.App;
import org.linesofcode.alltrack.R;
import org.linesofcode.alltrack.framework.navigation.NavigatableBaseActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

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
public class AddValueActivity extends NavigatableBaseActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private static final String TAG = AddValueActivity.class.getName();
    public static final String ADD_VALUE_ACTION_CODE = "org.linesofcode.alltrack.graph.ADD_VALUE";

    @Inject
    GraphService graphService;

    private static final String TIME_PATTERN = "HH:mm";
    private Calendar calendar;
    private DateFormat dateFormat;
    private SimpleDateFormat timeFormat;
    private TextView dateView;
    private TextView timeView;
    private Graph graph;
    private int position;
    private int graphId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_value_activity_layout);

        ((App) getApplication()).getComponent().inject(this);


        initializeToolbar();

        initializeContent();
    }

    private void initializeContent() {

        calendar = Calendar.getInstance();
        dateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
        timeFormat = new SimpleDateFormat(TIME_PATTERN, Locale.getDefault());

        dateView = (TextView) findViewById(R.id.add_value_date);
        timeView = (TextView) findViewById(R.id.add_value_time);

        Button okButton = (Button) findViewById(R.id.add_value_ok);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addValue();
            }
        });

        Button cancelButton = (Button) findViewById(R.id.add_value_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED, new Intent(ADD_VALUE_ACTION_CODE));
                finish();
            }
        });

        EditText value = (EditText) findViewById(R.id.add_value_value);
        value.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_ENTER:
                            addValue();
                            return true;
                    }
                }
                return false;
            }
        });

        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        timeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });

        updateDateTimeFields();

        Bundle extras = getIntent().getExtras();
        graphId = extras.getInt("graphId");
        position = extras.getInt("position");
        Log.d(TAG, "Starting addValue Activity for graph [" + graphId + "] in position [" + position + "].");
        graph = graphService.getById(graphId);

        selectInputMethod(graph.getType());
    }

    private void selectInputMethod(ValueType type) {
        Log.d(TAG, "Selecting input method for graph type [" + type + "].");
        switch (type) {

            case NUMBERS:
                setValueButtonState(View.GONE);
                break;
            case UNITS:
                setValueButtonState(View.VISIBLE);
                attachValueButtonHandler();
                break;
        }
    }

    private void attachValueButtonHandler() {
        View addOne = findViewById(R.id.add_value_add_one);
        View subOne = findViewById(R.id.add_value_sub_one);
        final EditText value = (EditText) findViewById(R.id.add_value_value);

        // TODO: improve listener implementation
        addOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = value.getText().toString();
                if (content.isEmpty()) {
                    content = "0";
                }
                Integer current = Integer.valueOf(content);
                current++;
                value.setText(current.toString());
            }
        });

        subOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = value.getText().toString();
                if (content.isEmpty()) {
                    content = "0";
                }
                Integer current = Integer.valueOf(content);
                current--;
                value.setText(current.toString());
            }
        });
    }

    private void setValueButtonState(int visibility) {
        View addOne = findViewById(R.id.add_value_add_one);
        View subOne = findViewById(R.id.add_value_sub_one);

        addOne.setVisibility(visibility);
        subOne.setVisibility(visibility);
    }

    private void addValue() {
        DataPoint dataPoint = new DataPoint();

        EditText valueView = (EditText) findViewById(R.id.add_value_value);
        Integer value = Integer.valueOf(valueView.getText().toString());
        dataPoint.setValue(value);

        dataPoint.setDatetime(calendar.getTime());

        dataPoint.setGraph(graph);
        graph.getDatapoints().add(dataPoint);

        Log.d(TAG, "Finishing adding value for graph [" + graphId + "] and position [" + position + "].");
        Intent result = new Intent(ADD_VALUE_ACTION_CODE);
        result.putExtra("org.linesofcode.alltrack.position", position);
        result.putExtra("org.linesofcode.alltrack.graphId", graphId);
        setResult(RESULT_OK, result);
        finish();
    }

    private void showDatePicker() {
        new DatePickerDialog(this, this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void showTimePicker() {
        new TimePickerDialog(this, this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        calendar.set(year, monthOfYear, dayOfMonth);
        updateDateTimeFields();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        updateDateTimeFields();
    }

    private void updateDateTimeFields() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dateView.setText(dateFormat.format(calendar.getTime()));
                timeView.setText(timeFormat.format(calendar.getTime()));
            }
        });
    }

    @Override
    protected DrawerLayout getMainDrawerLayout() {
        return (DrawerLayout) findViewById(R.id.mainLayout);
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

}
