package org.linesofcode.alltrack.graph;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.echo.holographlibrary.Line;
import com.echo.holographlibrary.LineGraph;
import com.echo.holographlibrary.LinePoint;
import org.linesofcode.alltrack.R;

public class GraphAdapter extends RecyclerView.Adapter<GraphAdapter.ViewHolder> {

    private String[] dataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private View view;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
        }

        public void setText(final Line line) {
            Log.d("ViewHolder", "ViewHolder setting line to [" + line.toString() + "].");
            LineGraph graphView = (LineGraph) view.findViewById(R.id.graph);
            graphView.removeAllLines();
            graphView.addLine(line);
            graphView.setRangeY(0, 10);
            graphView.setLineToFill(0);
        }
    }

    public GraphAdapter(String[] dataset) {
        this.dataset = dataset;
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
        holder.setText(generateRandomLine());
    }

    private Line generateRandomLine() {
        Line line = new Line();
        line.addPoint(new LinePoint(0, 0));
        line.addPoint(new LinePoint(1, 1));
        line.addPoint(new LinePoint(2, 2));

        return line;
    }

    @Override
    public int getItemCount() {
        Log.d("GraphAdapter", "getItemCount");
        return dataset.length;
    }
}
