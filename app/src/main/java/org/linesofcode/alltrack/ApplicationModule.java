package org.linesofcode.alltrack;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import org.linesofcode.alltrack.framework.persistence.DatabaseHelper;
import org.linesofcode.alltrack.graph.DataPoint;
import org.linesofcode.alltrack.graph.Graph;
import org.linesofcode.alltrack.graph.GraphAdapter;
import org.linesofcode.alltrack.graph.GraphService;

import java.sql.SQLException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(injects = {MainActivity.class, GraphAdapter.class, GraphService.class, DatabaseHelper.class})
public class ApplicationModule {

    private final Context context;

    public ApplicationModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    public GraphAdapter providesGraphAdapter(GraphService graphService) {
        return new GraphAdapter(graphService);
    }

    @Provides
    @Singleton
    public GraphService graphService() {
        OrmLiteSqliteOpenHelper openHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
        RuntimeExceptionDao<Graph, Integer> graphDao = openHelper.getRuntimeExceptionDao(Graph.class);
        //Dao<Graph, Integer> graphDao = openHelper.getDao(Graph.class);
        RuntimeExceptionDao<DataPoint, Integer> dataPointDao = openHelper.getRuntimeExceptionDao(DataPoint.class);
        return new GraphService(graphDao, dataPointDao);
    }
}
