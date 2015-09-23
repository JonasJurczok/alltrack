package org.linesofcode.alltrack.graph;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

public class GraphAdapter extends RecyclerView.Adapter<GraphAdapter.ViewHolder> {

    private String[] dataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView view;

        public ViewHolder(TextView view) {
            super(view);
            this.view = view;
        }

        public void setText(final String text) {
            Log.d("ViewHolder", "ViewHolder setting text to [" + text + "].");
            view.setText(text);
        }
    }

    public GraphAdapter(String[] dataset) {
        this.dataset = dataset;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("GraphAdapter", "onCreateViewHolder with viewType [" + viewType + "].");
        TextView textView = new TextView(parent.getContext());
        return new ViewHolder(textView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d("GraphAdapter", "onBindViewHolder for position [" + position + "].");
        holder.setText(dataset[position]);
    }

    @Override
    public int getItemCount() {
        Log.d("GraphAdapter", "getItemCount");
        return dataset.length;
    }
}
