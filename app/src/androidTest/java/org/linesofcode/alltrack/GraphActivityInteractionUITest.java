package org.linesofcode.alltrack;

import android.app.Activity;
import android.support.annotation.NonNull;
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
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.linesofcode.alltrack.framework.DisableAnimationsRule;
import org.linesofcode.alltrack.framework.persistence.DatabaseHelper;
import org.linesofcode.alltrack.graph.AddValueActivitiy;
import org.linesofcode.alltrack.graph.DataPoint;
import org.linesofcode.alltrack.graph.Graph;
import org.linesofcode.alltrack.graph.GraphActivity;
import org.linesofcode.alltrack.graph.GraphAdapter;
import org.linesofcode.alltrack.graph.GraphService;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
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
import static java.lang.Math.abs;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
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
public class GraphActivityInteractionUITest {

    private GraphService graphService;
    private RuntimeExceptionDao<Graph, Integer> graphDao;
    private RuntimeExceptionDao<DataPoint, Integer> dataPointDao;
    private Graph graphForTest = null;

    @ClassRule
    public static DisableAnimationsRule disableAnimationsRule = new DisableAnimationsRule();

    @Rule
    @SuppressWarnings("unchecked")
    public ActivityTestRule<GraphActivity> rule = new ActivityTestRule(GraphActivity.class);

    @Before
    public void setUp() {
        App app = (App) rule.getActivity().getApplication();
        graphService = app.getComponent().getGraphService();

        DatabaseHelper openHelper = new DatabaseHelper(app);
        graphDao = openHelper.getRuntimeExceptionDao(Graph.class);

        dataPointDao = openHelper.getRuntimeExceptionDao(DataPoint.class);
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

        onView(withId(R.id.graph_detail_cancel)).perform(click());
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

    @Test
    public void clickingAGraphShouldOpenAddValueActivity() {
        String graphName = "clickingAGraphShouldOpenAddValueActivity";
        graphForTest = createGraph(graphName);

        takeScreenshot(graphName + "_before_click");
        onView(withText(graphName)).perform(click());

        takeScreenshot(graphName + "_before_assert");
        onView(withText(R.string.add_value_value_field_title)).check(matches(isDisplayed()));
    }

    @Test
    public void addValueShouldTakeCurrentTimeAsDefault() {
        String graphName = "addValueShouldTakeCurrentTimeAsDefault";
        graphForTest = createGraph(graphName);

        addValue(graphName, 5);

        List<DataPoint> dataPoints = dataPointDao.queryForEq("graph_id", graphForTest.getId());

        assertThat(dataPoints.size(), is(1));

        DataPoint datapoint = dataPoints.get(0);

        assertThat(datapoint.getValue(), is(5));
        long current = System.currentTimeMillis() - (1000 * 60);
        Date reference = new Date(current);
        assertThat(datapoint.getDatetime().after(reference), is(true));
    }

    @Test
    public void addValueWithRandomDateTimeShouldWork() {
        String graphName = "addValueWithRandomDateTimeShouldWork";
        graphForTest = createGraph(graphName);

        long currentTime = System.currentTimeMillis();
        Date dateOfGraph = new Date(currentTime - 1000 * 60 * 60);
        addValue(graphName, 5, dateOfGraph);

        List<DataPoint> dataPoints = dataPointDao.queryForEq("graph_id", graphForTest.getId());

        assertThat(dataPoints.size(), is(1));

        DataPoint datapoint = dataPoints.get(0);

        assertThat(datapoint.getValue(), is(5));
        assertThat(abs(datapoint.getDatetime().getTime() - dateOfGraph.getTime()), is(Matchers.lessThan(10000l)));
    }

    @Test
    public void addValueShouldRefreshGraphAndMakeValueVisible() throws InterruptedException {
        String graphName = "addValueShouldRefreshGraphAndMakeValueVisible";
        graphForTest = createGraph(graphName);

        Thread.sleep(5000);

        addValue(graphName, 5);

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

                assertThat("Wrong number of yVals returned.", yVals.size(), is(1));

                Entry entry = yVals.get(0);
                assertThat(entry.getVal(), is(5f));
            }
        });

    }

    private Graph createGraph(String graphName) {
        onView(withId(R.id.fab)).perform(click());

        takeScreenshot("CreateGraph_" + graphName + "_before_graph_name");
        onView(withId(R.id.edit_graph_name)).perform(typeText(graphName + "\n"));
        takeScreenshot("CreateGraph_" + graphName + "_after_confirm");
        List<Graph> graphs = graphDao.queryForEq("name", graphName);

        assertThat(graphs.size(), is(1));

        return graphs.get(0);
    }

    private void addValue(String graphName, Integer value) {
        addValue(graphName, value, new Date());
    }

    private void addValue(String graphName, Integer value, Date date) {
        takeScreenshot(graphName + "_add_value_before_click");
        onView(withText(graphName)).perform(click());

        takeScreenshot(graphName + "_add_value_before_add");
        onView(withId(R.id.add_value_value)).perform(typeText(value.toString()));
        onView(withId(R.id.add_value_value)).perform(closeSoftKeyboard());

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        AddValueActivitiy addValueActivity = (AddValueActivitiy) getCurrentActivity();
        addValueActivity.onDateSet(null, cal.get(YEAR), cal.get(MONTH), cal.get(DAY_OF_MONTH));
        addValueActivity.onTimeSet(null, cal.get(HOUR_OF_DAY), cal.get(MINUTE));

        takeScreenshot(graphName + "_add_value_before_submit");
        onView(withId(R.id.add_value_ok)).perform(click());

        takeScreenshot(graphName + "_add_value_after_submit");
    }

    private void takeScreenshot(final String tag) {
        Activity activity = getCurrentActivity();
        Spoon.screenshot(activity, tag);
    }

    @NonNull
    private Activity getCurrentActivity() {
        final Activity[] activity = new Activity[1];
        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                Collection<Activity> resumedActivities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED);
                if (resumedActivities.isEmpty()) {
                    Log.e(GraphActivityInteractionUITest.class.getName(), "No currently running activies found.");
                    return;
                }
                activity[0] = Iterables.getOnlyElement(resumedActivities);
            }
        });
        return activity[0];
    }

    public void updateGraphView() {
        App app = (App) rule.getActivity().getApplication();
        app.getComponent().getGraphAdapter().updateGraphs();
    }
}
