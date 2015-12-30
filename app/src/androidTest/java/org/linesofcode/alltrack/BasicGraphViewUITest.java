package org.linesofcode.alltrack;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.linesofcode.alltrack.graph.Graph;
import org.linesofcode.alltrack.graph.GraphAdapter;
import org.linesofcode.alltrack.graph.GraphService;

import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.linesofcode.alltrack.GraphGenerationUtil.generateSimpleTestGraph;

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

        String graphName = "SimpleLineGraph";
        Graph graph = generateSimpleTestGraph(graphService, graphName);
        updateGraphView();

        try {
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
        } finally {
            graphService.delete(graph);
        }
    }

    public void updateGraphView() {
        App app = (App) rule.getActivity().getApplication();
        app.getObjectGraph().get(GraphAdapter.class).updateGraphs();
    }
}
