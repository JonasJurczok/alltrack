package org.linesofcode.alltrack;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.table.TableUtils;

import org.hamcrest.collection.IsEmptyCollection;
import org.linesofcode.alltrack.framework.persistence.DatabaseHelper;
import org.linesofcode.alltrack.graph.DataPoint;
import org.linesofcode.alltrack.graph.Graph;
import org.linesofcode.alltrack.graph.GraphService;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.assertThat;
import static org.linesofcode.alltrack.GraphGenerationUtil.generateSimpleTestGraph;

public class GraphServiceTest extends AndroidTestCase {
    private static final String PREFIX = "_test";

    private DatabaseHelper openHelper;
    private GraphService graphService;
    private RuntimeExceptionDao<Graph, Integer> graphDao;
    private RenamingDelegatingContext context;
    private RuntimeExceptionDao<DataPoint, Integer> dataPointDao;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        context = new RenamingDelegatingContext(getContext(), PREFIX);

        openHelper = new DatabaseHelper(context);
        graphDao = openHelper.getRuntimeExceptionDao(Graph.class);
        dataPointDao = openHelper.getRuntimeExceptionDao(DataPoint.class);
        graphService = new GraphService(graphDao, dataPointDao);
    }

    @Override
    protected void tearDown() throws Exception {
        openHelper.close();
        String[] databaseList = context.databaseList();
        for (String db : databaseList) {
            if (db.equals(openHelper.getDatabaseName())) {
                boolean databaseDeleted = context.deleteDatabase(openHelper.getDatabaseName());
                if (!databaseDeleted) {
                    throw new RuntimeException("Test database not deleted.");
                }
            }
        }
        super.tearDown();
    }

    public void testCreateGraphShouldPersistGraph() throws SQLException {
        final String graphName = "TestGraphCreate";
        Graph newGraph = graphService.createNewGraph(graphName);

        Graph dbGraph = graphDao.queryForId(newGraph.getId());

        assertThat(dbGraph, is(notNullValue()));
        assertThat(dbGraph.getName(), equalTo(graphName));
        assertThat(dbGraph.getDatapoints().size(), equalTo(newGraph.getDatapoints().size()));
    }

    public void testDeleteGraphShouldWork() throws SQLException {
        final String graphName = "TestGraphDelete";

        Graph graph = generateSimpleTestGraph(graphService, graphName);

        graphService.delete(graph);

        Graph dbGraph = graphDao.queryForId(graph.getId());

        assertThat(dbGraph, is(nullValue()));
    }

    public void testDeleteGraphShouldDeleteDataPoints() throws SQLException {
        final String graphName = "TestGraphDeleteDataPoints";

        Graph graph = generateSimpleTestGraph(graphService, graphName);

        graphService.delete(graph);

        List<DataPoint> dataPoints = dataPointDao.queryForEq("graph_id", graph.getId());

        assertThat(dataPoints, is(empty()));
    }

    public void testSaveShouldCreateNewDatapoints() {
        Graph graph = generateSimpleTestGraph(graphService, "testSaveShouldCreateNewDatapoints");

        final DataPoint newPoint = new DataPoint();
        newPoint.setGraph(graph);
        newPoint.setValue(-1234567);
        newPoint.setDatetime(Calendar.getInstance().getTime());
        graph.getDatapoints().add(newPoint);

        graphService.save(graph);

        List<DataPoint> dbPoints = dataPointDao.queryForEq("value", newPoint.getValue());
        assertThat(dbPoints.size(), is(1));
        DataPoint dbPoint = dbPoints.get(0);
        assertThat(dbPoint.getGraph(), is(graph));
    }

    public void testSaveShouldDeleteDataPoints() {
        Graph graph = generateSimpleTestGraph(graphService, "testSaveShouldDeleteDataPoints");

        Collection<DataPoint> graphPoints = graph.getDatapoints();
        int initialSize = graphPoints.size();
        DataPoint dataPoint = (DataPoint) graphPoints.toArray()[0];
        graphPoints.remove(dataPoint);

        graphService.save(graph);

        List<DataPoint> dataPoints = dataPointDao.queryForEq("graph_id", graph.getId());
        assertThat(dataPoints.size(), is(initialSize - 1));
        assertThat(dataPoints.size(), is(graph.getDatapoints().size()));

        assertThat(dataPoints.containsAll(graph.getDatapoints()), is(true));
        assertThat(graph.getDatapoints().containsAll(dataPoints), is(true));
    }

    public void testGetAllShouldHaveDatapointsOrdererdByTime() {
        assertThat("not implemented", true, is(false));
    }
}
