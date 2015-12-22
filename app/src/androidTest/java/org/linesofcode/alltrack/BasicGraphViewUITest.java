package org.linesofcode.alltrack;

import android.app.Application;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.linesofcode.alltrack.graph.DataPoint;
import org.linesofcode.alltrack.graph.Graph;
import org.linesofcode.alltrack.graph.GraphAdapter;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.Realm;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class BasicGraphViewUITest {

    private Realm realm;

    @Rule
    @SuppressWarnings("unchecked")
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule(MainActivity.class);

    @Before
    public void setUp() {
        App app = (App) rule.getActivity().getApplication();

        realm = Realm.getDefaultInstance();
    }

    @After
    public void cleanupRealm() {
        Log.i("BasicGraphViewUITest", "Cleaning up realm...");
        realm.beginTransaction();
        realm.allObjects(DataPoint.class).clear();
        Log.i("BasicGraphViewUITest", "DataPoints deleted.");
        realm.allObjects(Graph.class).clear();
        Log.i("BasicGraphViewUITest", "Graphs deleted.");
        realm.commitTransaction();
        Log.i("BasicGraphViewUITest", "Cleanup completed.");

        realm.close();
    }

    /**
     * TESTMAP:
     *
     * inject graphs into the system to verify ui
     * line graph should be displayed
     * bar graph should be displayed
     *
     *
     */

    @Test
    public void showSimpleGraph() {

        generateLinearTestGraph();
        updateGraphView();

        onView(withId(R.id.recyclerView)).perform(new ViewAction() {
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

                RecyclerView recyclerView = (RecyclerView) view;

                View graphView = recyclerView.getChildAt(0);

                //View graphView = view.findViewById(R.id.graphView);

                LineChart graph = (LineChart) graphView.findViewById(R.id.graph);
                LineDataSet lineDataSet = graph.getLineData().getDataSets().get(0);

                List<Entry> yVals = lineDataSet.getYVals();

                assertThat("Wrong number of yVals returned.", yVals.size(), is(4));

            }
        });

        /*onView(withText("Test Linegraph")).perform(new ViewAction() {
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

                LineChart graph = (LineChart) view.findViewById(R.id.graph);
                LineDataSet lineDataSet = graph.getLineData().getDataSets().get(0);

                List<Entry> yVals = lineDataSet.getYVals();

                assertThat("Wrong number of yVals returned.", yVals.size(), is(4));

            }
        });*/
    }

    public void updateGraphView() {
        App app = (App) rule.getActivity().getApplication();
        app.getObjectGraph().get(GraphAdapter.class).updateGraphs();
    }

    public void generateLinearTestGraph() {
        realm.beginTransaction();

        // the test failes because the realm object used here is the same that is used in the app
        // to access the objects. But the Thread is different. Therefore realm.io is rejecting access
        // to the objects.

        // There seems to be a trick on how to use realm correctly. The github repo has a threading example.
        // it seems usefull to add a listener to the realm...

        // Options:
        // - put a layer between realm and the app
        // - create a special "database thread"
        // - transform the test graph creation to a ui based execution, using existing methods to create graphs via the ui.

        Graph graph = realm.createObject(Graph.class);
        graph.setName("Test Linegraph");

        Calendar startDate = Calendar.getInstance();

        for (int i = 0; i < 5; i++) {
            DataPoint dataPoint = realm.createObject(DataPoint.class);
            dataPoint.setValue(i);
            startDate.set(Calendar.DAY_OF_MONTH, i);
            dataPoint.setDatetime(startDate.getTime());
            graph.getDatapoints().add(dataPoint);
        }
        realm.commitTransaction();
    }
}
