package org.linesofcode.alltrack;

import org.linesofcode.alltrack.graph.DataPoint;
import org.linesofcode.alltrack.graph.Graph;
import org.linesofcode.alltrack.graph.GraphService;

import java.util.Calendar;

public class GraphGenerationUtil {

    /**
     * Generates a simple graph with 5 datapoints with values form 0 to 4 and the date between
     * the first and 5th of the month.
     * @param graphService the GraphService instance to use
     * @param name the name of the newly created graph
     * @return a persisted dummy graph
     */
    public static Graph generateSimpleTestGraph(final GraphService graphService, final String name) {
        Graph graph = graphService.createNewGraph("Test Linegraph");

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
