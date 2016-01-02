package org.linesofcode.alltrack;

import org.linesofcode.alltrack.graph.DataPoint;
import org.linesofcode.alltrack.graph.Graph;
import org.linesofcode.alltrack.graph.GraphService;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import static org.linesofcode.alltrack.GraphGenerationUtil.GraphOrdering.ASC;

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
public class GraphGenerationUtil {

    public enum GraphOrdering {
        ASC,
        DESC,
        RANDOM
    }

    /**
     * Generates a simple graph with 5 datapoints with values form 0 to 4 and the date between
     * the first and 5th of the month.
     * @param graphService the GraphService instance to use
     * @param name the name of the newly created graph
     * @return a persisted dummy graph
     */
    public static Graph generateSimpleTestGraph(final GraphService graphService, final String name) {
/*        Graph graph = graphService.createNewGraph(name);

        Calendar startDate = Calendar.getInstance();

        for (int i = 0; i < 5; i++) {
            DataPoint dataPoint = new DataPoint();
            dataPoint.setValue(i);
            dataPoint.setGraph(graph);

            startDate.set(Calendar.DAY_OF_MONTH, i+1);
            dataPoint.setDatetime(startDate.getTime());

            graph.getDatapoints().add(dataPoint);
        }

        return graph;*/

        return generateGraph(graphService, name, 5, ASC);
    }

    public static Graph generateGraph(final GraphService graphService, final String name,
                                      final int dataPointCount, final GraphOrdering ordering) {

        if (ordering == null) {
            throw new IllegalArgumentException("Ordering has to be set.");
        }

        Graph graph = graphService.createNewGraph(name);

        Calendar startDate = Calendar.getInstance();

        Random random = new Random();

        for (int i = 1; i <= dataPointCount; i++) {

            int nextValue = 0;

            switch (ordering) {
                case ASC:
                    nextValue = i;
                    startDate.set(Calendar.DAY_OF_MONTH, i);
                    break;
                case DESC:
                    nextValue = dataPointCount - i;
                    startDate.set(Calendar.DAY_OF_MONTH, i);
                    break;
                case RANDOM:
                    random.nextInt(10);
                    startDate.set(Calendar.DAY_OF_MONTH, random.nextInt(28));
                    break;
            }
            Date nextDateTime = startDate.getTime();

            DataPoint dataPoint = new DataPoint();
            dataPoint.setValue(nextValue);
            dataPoint.setGraph(graph);

            dataPoint.setDatetime(nextDateTime);

            graph.getDatapoints().add(dataPoint);
        }

        return graph;
    }
}
