package org.linesofcode.alltrack.graph;


import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GraphService {

    private static final String TAG = GraphService.class.getName();

    private final Dao<Graph, Integer> graphDao;
    private final Dao<DataPoint, Integer> dataPointDao;

    public GraphService(Dao<Graph, Integer> graphStringDao, Dao<DataPoint, Integer> dataPointDao) {

        this.graphDao = graphStringDao;
        this.dataPointDao = dataPointDao;
    }

    public List<Graph> getAll() {

        try {
            List<Graph> results = graphDao.queryBuilder().orderBy("name", true).query();
            Log.d("GraphService", "Found [" + results.size() + "] graphs to return to the caller.");

            return results;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // TODO: add tests that points are also gone.
    public void delete(Graph graph) {

        try {
            List<Integer> pointIds = new ArrayList<>();
            for (DataPoint dataPoint : graph.getDatapoints()) {
                pointIds.add(dataPoint.getId());
            }

            DeleteBuilder<DataPoint, Integer> pointDelete = dataPointDao.deleteBuilder();
            pointDelete.where().in("id", pointIds);
            dataPointDao.delete(pointDelete.prepare());

            graphDao.delete(graph);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(Graph  graph) {
        try {
            graphDao.createOrUpdate(graph);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Graph createNewGraph(String name) {

        try {
            Graph result = new Graph();
            result.setName(name);
            ForeignCollection<DataPoint> datapoints = graphDao.getEmptyForeignCollection("datapoints");
            result.setDatapoints(datapoints);

            graphDao.createOrUpdate(result);

            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
