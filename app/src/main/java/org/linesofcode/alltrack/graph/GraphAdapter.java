package org.linesofcode.alltrack.graph;

import android.os.AsyncTask;
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
import static org.linesofcode.alltrack.graph.GraphToLineTransformer.toLineData;

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
public class GraphAdapter extends RecyclerView.Adapter<GraphAdapter.ViewHolder> {

    private final GraphService graphService;
    private List<Graph> allGraphs = new ArrayList<>();

    private final class LoadGraphsTask extends AsyncTask<Void, List<Graph>, List<Graph>> {
        private final GraphService graphService;
        private final GraphAdapter adapter;

        private LoadGraphsTask(GraphService graphService, GraphAdapter adapter) {
            this.graphService = graphService;
            this.adapter = adapter;
        }

        @Override
        protected List<Graph> doInBackground(Void... params) {
            return graphService.getAll();
        }

        @Override
        protected void onPostExecute(List<Graph> graphs) {
            allGraphs.clear();
            allGraphs.addAll(graphs);
            adapter.notifyDataSetChanged();
        }
    }

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
            title.setSelected(true);

            LineChart graphView = (LineChart) view.findViewById(R.id.graph);
            graphView.setData(toLineData(graph));
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
        new LoadGraphsTask(graphService, this).execute();
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
