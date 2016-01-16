package org.linesofcode.alltrack.graph;


import android.util.Log;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
public class GraphService {

    private static final String TAG = GraphService.class.getName();

    private final RuntimeExceptionDao<Graph, Integer> graphDao;
    private final RuntimeExceptionDao<DataPoint, Integer> dataPointDao;
    private PreparedQuery<Graph> allOrderByName;

    public GraphService(RuntimeExceptionDao<Graph, Integer> graphStringDao, RuntimeExceptionDao<DataPoint, Integer> dataPointDao) {

        this.graphDao = graphStringDao;
        this.dataPointDao = dataPointDao;

        try {
            allOrderByName = graphDao.queryBuilder().orderBy("name", true).prepare();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Graph> getAll() {

        List<Graph> results = graphDao.query(allOrderByName);
        Log.d(TAG, "Found [" + results.size() + "] graphs to return to the caller.");
        return results;
    }

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

    public void save(Graph graph) {
        graphDao.createOrUpdate(graph);
    }

    public Graph createNewGraph(String name) {
        Graph result = new Graph();
        result.setName(name);
        ForeignCollection<DataPoint> datapoints = graphDao.getEmptyForeignCollection("datapoints");
        result.setDatapoints(datapoints);

        graphDao.createOrUpdate(result);

        return result;
    }
}
