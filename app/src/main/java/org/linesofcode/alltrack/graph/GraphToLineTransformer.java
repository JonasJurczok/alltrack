package org.linesofcode.alltrack.graph;

import android.graphics.Color;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GraphToLineTransformer {

    // TODO: add locale to make it work outside of DACH
    private static final SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    public static LineData toLineData(Graph graph) {
        ArrayList<String> xVals = new ArrayList<>();
        ArrayList<Entry> yVals = new ArrayList<>();

        for (int i = 0; i < graph.getDatapoints().size(); i++) {
            DataPoint dataPoint = graph.getDatapoints().get(i);
            yVals.add(new Entry(dataPoint.getValue(), i));
            xVals.add(format.format(dataPoint.getDatetime()));
        }
        LineDataSet line = new LineDataSet(yVals, graph.getName());
        line.setColor(Color.parseColor("#FFBB33"));

        return new LineData(xVals, line);
    }
}
