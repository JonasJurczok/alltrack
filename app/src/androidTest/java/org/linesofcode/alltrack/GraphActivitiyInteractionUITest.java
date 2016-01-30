package org.linesofcode.alltrack;

import android.app.Activity;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.core.deps.guava.collect.Iterables;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;
import android.util.Log;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.squareup.spoon.Spoon;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.linesofcode.alltrack.framework.DisableAnimationsRule;
import org.linesofcode.alltrack.framework.persistence.DatabaseHelper;
import org.linesofcode.alltrack.graph.Graph;
import org.linesofcode.alltrack.graph.GraphActivity;
import org.linesofcode.alltrack.graph.GraphAdapter;
import org.linesofcode.alltrack.graph.GraphService;

import java.util.Collection;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.linesofcode.alltrack.GraphGenerationUtil.generateSimpleTestGraph;

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
public class GraphActivitiyInteractionUITest {

    private GraphService graphService;
    private RuntimeExceptionDao<Graph, Integer> graphDao;
    private Graph graphForTest = null;

    @ClassRule
    public static DisableAnimationsRule disableAnimationsRule = new DisableAnimationsRule();

    @Rule
    @SuppressWarnings("unchecked")
    public ActivityTestRule<GraphActivity> rule = new ActivityTestRule(GraphActivity.class);

    @Before
    public void setUp() {
        App app = (App) rule.getActivity().getApplication();
        graphService = app.getObjectGraph().get(GraphService.class);

        DatabaseHelper openHelper = new DatabaseHelper(app);
        graphDao = openHelper.getRuntimeExceptionDao(Graph.class);
    }

    @After
    public void tearDown() {
        if (graphForTest != null) {
            graphService.delete(graphForTest);
        }
    }

    /**
     * TESTMAP:
     *
     * line graph should be displayed
     * bar graph should be displayed
     */

    @Test
    public void showSimpleGraph() {

        String graphName = "SimpleLineGraph";
        graphForTest = generateSimpleTestGraph(graphService, graphName);
        updateGraphView();

        takeScreenshot("showSimpleGraph_before_asserts");

        onView(withText(graphName)).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isDisplayed();
            }

            @Override
            public String getDescription() {
                return "Check for display of graph view.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                assertThat("No view found", view, is(notNullValue()));

                View parent = (View) view.getParent();
                LineChart graph = (LineChart) parent.findViewById(R.id.graph);
                LineDataSet lineDataSet = graph.getLineData().getDataSets().get(0);

                List<Entry> yVals = lineDataSet.getYVals();

                assertThat("Wrong number of yVals returned.", yVals.size(), is(5));
            }
        });
    }

    @Test
    public void addNewGraphShouldCreateNewGraph() {
        String graphName = "addNewGraphShouldCreateNewGraph";

        graphForTest = createGraph(graphName);

        assertThat(graphForTest, is(not(nullValue())));
        assertThat(graphForTest.getName(), is(graphName));
    }

    @Test
    public void newlyAddedGraphShouldBeDisplayedImmediately() {
        String graphName = "newlyAddedGraphShouldBeDisplayedImmediately";
        graphForTest = createGraph(graphName);

        onView(withText(graphName)).check(matches(isDisplayed()));
    }

    @Test
    public void clickOnCancelShouldNotCreateNewGraph() {
        String graphName = "clickOnCancelShouldNotCreateNewGraph";

        onView(withId(R.id.fab)).perform(click());

        takeScreenshot("clickOnCancelShouldNotCreateNewGraph_before_graph_name");
        onView(withId(R.id.edit_graph_name)).perform(typeText(graphName));
        takeScreenshot("clickOnCancelShouldNotCreateNewGraph_after_graph_name");
        onView(withId(R.id.edit_graph_name)).perform(closeSoftKeyboard());
        takeScreenshot("clickOnCancelShouldNotCreateNewGraph_after_close_keyboard");

        onView(withId(R.id.cancel)).perform(click());
        takeScreenshot("clickOnCancelShouldNotCreateNewGraph_after_cancel_clicked");
        List<Graph> graphs = graphDao.queryForEq("name", graphName);

        assertThat(graphs.size(), is(0));
    }

    @Test
    public void hittingEnterInGraphNameFieldShouldCreateNewGraph() {
        String graphName = "hittingEnterInGraphNameFieldShouldCreateNewGraph";
        onView(withId(R.id.fab)).perform(click());

        takeScreenshot("hittingEnterInGraphNameFieldShouldCreateNewGraph_before_graph_name");
        onView(withId(R.id.edit_graph_name)).perform(typeText(graphName + "\n"));
        takeScreenshot("hittingEnterInGraphNameFieldShouldCreateNewGraph_after_graph_name");

        List<Graph> graphs = graphDao.queryForEq("name", graphName);
        assertThat(graphs.size(), is(1));
        graphForTest = graphs.get(0);
    }

    public Graph createGraph(String graphName) {
        onView(withId(R.id.fab)).perform(click());

        takeScreenshot("CreateGraph_" + graphName + "_before_graph_name");
        onView(withId(R.id.edit_graph_name)).perform(typeText(graphName));
        takeScreenshot("CreateGraph_" + graphName + "_after_graph_name");
        onView(withId(R.id.edit_graph_name)).perform(closeSoftKeyboard());
        takeScreenshot("CreateGraph_" + graphName + "_after_close_keyboard");
        onView(withId(R.id.ok)).perform(click());
        takeScreenshot("CreateGraph_" + graphName + "_after_confirm");
        List<Graph> graphs = graphDao.queryForEq("name", graphName);

        assertThat(graphs.size(), is(1));

        return graphs.get(0);
    }

    private void takeScreenshot(final String tag) {
        final Activity[] activity = new Activity[1];
        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                Collection<Activity> resumedActivities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED);
                if (resumedActivities.isEmpty()) {
                    Log.e(GraphActivitiyInteractionUITest.class.getName(), "No currently running activies found.");
                    return;
                }


                activity[0] = Iterables.getOnlyElement(resumedActivities);
            }
        });
        Spoon.screenshot(activity[0], tag);
    }

    public void updateGraphView() {
        App app = (App) rule.getActivity().getApplication();
        app.getObjectGraph().get(GraphAdapter.class).updateGraphs();
    }
}
