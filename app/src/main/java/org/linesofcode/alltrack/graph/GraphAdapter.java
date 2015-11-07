package org.linesofcode.alltrack.graph;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import org.linesofcode.alltrack.R;

import javax.inject.Inject;

public class GraphAdapter extends RecyclerView.Adapter<GraphAdapter.ViewHolder> {

    private LineDataProvider lineProvider;

    private String[] dataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private View view;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
        }

        public void setText(final LineData line) {
            Log.d("ViewHolder", "ViewHolder setting line to [" + line.toString() + "].");
            LineChart graphView = (LineChart) view.findViewById(R.id.graph);
            graphView.setData(line);
            graphView.setDescription("");
            graphView.setDrawGridBackground(false);
            graphView.setDrawBorders(false);
            graphView.invalidate();
        }
    }

    @Inject
    public GraphAdapter(String[] dataset, LineDataProvider lineProvider) {
        this.dataset = dataset;
        this.lineProvider = lineProvider;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("GraphAdapter", "onCreateViewHolder with viewType [" + viewType + "].");
        View graphView = LayoutInflater.from(parent.getContext()).inflate(R.layout.graph_view, parent, false);

        parent.addView(graphView);
        return new ViewHolder(graphView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d("GraphAdapter", "onBindViewHolder for position [" + position + "].");
        holder.setText(lineProvider.get());
    }

    @Override
    public int getItemCount() {
        Log.d("GraphAdapter", "getItemCount");
        return dataset.length;
    }
}
