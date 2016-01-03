package org.linesofcode.alltrack.framework.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import org.linesofcode.alltrack.SettingsActivity;
import org.linesofcode.alltrack.GraphActivity;
import org.linesofcode.alltrack.R;

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
 */public abstract class NavigatableBaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);
    }

    protected void setActiveMenuItem(int itemId) {
        navigationView.setCheckedItem(itemId);
    }

    /**
     * This method should find the main drawer layout used to house the navigation drawer.
     * As this might be different in every activity the activity itself has to provide it.
     *
     * @return the DrawerLayout used to draw the navigation.
     */
    protected abstract DrawerLayout getMainDrawerLayout();

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        DrawerLayout mainLayout = getMainDrawerLayout();
        mainLayout.closeDrawers();

        switch(menuItem.getItemId()) {
            case R.id.nav_data_series:
                navigateToDataSeries();
                break;
            case R.id.nav_settings:
                navigateToConfigDataSeries();
            default:
                return false;
        }
        return true;
    }

    private void navigateToConfigDataSeries() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void navigateToDataSeries() {
        Intent intent = new Intent(this, GraphActivity.class);
        startActivity(intent);
    }
}
