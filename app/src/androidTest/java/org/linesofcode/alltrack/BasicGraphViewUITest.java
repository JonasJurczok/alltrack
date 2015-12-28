package org.linesofcode.alltrack;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.linesofcode.alltrack.graph.DataPoint;
import org.linesofcode.alltrack.graph.Graph;
import org.linesofcode.alltrack.graph.GraphAdapter;
import org.linesofcode.alltrack.graph.GraphService;

import java.util.Calendar;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class BasicGraphViewUITest {

    private GraphService graphService;

    @Rule
    @SuppressWarnings("unchecked")
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule(MainActivity.class);

    @Before
    public void setUp() {
        App app = (App) rule.getActivity().getApplication();
        graphService = app.getObjectGraph().get(GraphService.class);
    }

    // TODO: improve clenaup (currently each test has to care).

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

        Graph graph = generateLinearTestGraph();
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

                LineChart graph = (LineChart) graphView.findViewById(R.id.graph);
                LineDataSet lineDataSet = graph.getLineData().getDataSets().get(0);

                List<Entry> yVals = lineDataSet.getYVals();

                assertThat("Wrong number of yVals returned.", yVals.size(), is(5));

            }
        });

        graphService.delete(graph);
    }

    public void updateGraphView() {
        App app = (App) rule.getActivity().getApplication();
        app.getObjectGraph().get(GraphAdapter.class).updateGraphs();
    }

    public Graph generateLinearTestGraph() {

        // There seems to be a trick on how to use realm correctly. The github repo has a threading example.
        // it seems usefull to add a listener to the realm...

        // Options:
        // - put a layer between realm and the app
        // - create a special "database thread"
        // - transform the test graph creation to a ui based execution, using existing methods to create graphs via the ui.

        Graph graph = graphService.createNewGraph("Test Linegraph");

        //graphService.save(graph);

        Calendar startDate = Calendar.getInstance();

        for (int i = 0; i < 5; i++) {
            DataPoint dataPoint = new DataPoint();
            dataPoint.setValue(i);
            dataPoint.setGraph(graph);

            startDate.set(Calendar.DAY_OF_MONTH, i+1);
            dataPoint.setDatetime(startDate.getTime());

            graph.getDatapoints().add(dataPoint);
        }

        return graph;
    }
}
