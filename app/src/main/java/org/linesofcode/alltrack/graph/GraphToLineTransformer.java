package org.linesofcode.alltrack.graph;

import android.graphics.Color;
import android.util.Log;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

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
public class GraphToLineTransformer {

    private static final String TAG = GraphToLineTransformer.class.getName();

    // TODO: add locale to make it work outside of DACH
    private static final SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    public static LineData toLineData(Graph graph) {

        Log.i(TAG, "Transforming graph [" + graph.getName() + "] with [" + graph.getDatapoints().size() + "] datapoints.");

        ArrayList<String> xVals = new ArrayList<>();
        ArrayList<Entry> yVals = new ArrayList<>();

        int i = 0;
        for (DataPoint dataPoint : graph.getDatapoints()) {
            Log.i(TAG, "Transforming datapoint #[" + i + "] with value [" + dataPoint.getValue() + "].");
            yVals.add(new Entry(dataPoint.getValue(), i));
            xVals.add(format.format(dataPoint.getDatetime()));
            i++;
        }
        LineDataSet line = new LineDataSet(yVals, graph.getName());
        line.setColor(Color.parseColor("#FFBB33"));

        Log.i(TAG, "Transformation finished.");

        return new LineData(xVals, line);
    }
}
