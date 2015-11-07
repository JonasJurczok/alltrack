package org.linesofcode.alltrack.graph;

import android.graphics.Color;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import javax.inject.Provider;
import java.util.ArrayList;
import java.util.Random;

public class LineDataProvider implements Provider<LineData> {
    @Override
    public LineData get() {
        Random random = new Random();


        ArrayList<String> xVals = new ArrayList<>();
        ArrayList<Entry> yVals = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            yVals.add(new Entry(random.nextInt(10), i));
            xVals.add(String.valueOf(i));

        }
        LineDataSet line = new LineDataSet(yVals, "testdata");
        line.setAxisDependency(YAxis.AxisDependency.LEFT);
        line.setColor(Color.parseColor("#FFBB33"));

        LineData data = new LineData(xVals, line);

        return data;
    }
}
