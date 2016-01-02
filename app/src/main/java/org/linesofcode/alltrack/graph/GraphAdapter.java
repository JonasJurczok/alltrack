package org.linesofcode.alltrack.graph;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.LineData;
import org.linesofcode.alltrack.R;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM;

public class GraphAdapter extends RecyclerView.Adapter<GraphAdapter.ViewHolder> {

    private final GraphService graphService;
    private List<Graph> allGraphs = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private View view;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
        }

        public void setGraph(final Graph graph) {
            Log.d("ViewHolder", "ViewHolder setting line to [" + graph.getName() + "].");

            TextView title = (TextView) view.findViewById(R.id.title);
            title.setText(graph.getName());

            LineChart graphView = (LineChart) view.findViewById(R.id.graph);
            graphView.setData(GraphToLineTransformer.toLineData(graph));
            graphView.setDescription("");
            graphView.setDrawGridBackground(false);
            graphView.setDrawBorders(false);
            XAxis xAxis = graphView.getXAxis();
            xAxis.setPosition(BOTTOM);
            xAxis.setLabelRotationAngle(45);
            graphView.invalidate();
        }
    }

    @Inject
    public GraphAdapter(GraphService graphService) {
        this.graphService = graphService;
        updateGraphs();
    }

    public void updateGraphs() {
        allGraphs = graphService.getAll();
        notifyItemRangeInserted(0, allGraphs.size());
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
        holder.setGraph(allGraphs.get(position));
    }

    @Override
    public int getItemCount() {
        Log.d("GraphAdapter", "getItemCount");
        return allGraphs.size();
    }
}