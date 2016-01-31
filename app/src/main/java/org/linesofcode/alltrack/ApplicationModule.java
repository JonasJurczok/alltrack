package org.linesofcode.alltrack;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import org.linesofcode.alltrack.framework.persistence.DatabaseHelper;
import org.linesofcode.alltrack.graph.AddValueActivitiy;
import org.linesofcode.alltrack.graph.DataPoint;
import org.linesofcode.alltrack.graph.Graph;
import org.linesofcode.alltrack.graph.GraphActivity;
import org.linesofcode.alltrack.graph.GraphAdapter;
import org.linesofcode.alltrack.graph.CreateGraphActivity;
import org.linesofcode.alltrack.graph.GraphService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

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
@Module(injects = {GraphActivity.class,
        GraphAdapter.class,
        GraphService.class,
        DatabaseHelper.class,
        SettingsActivity.class,
        CreateGraphActivity.class,
        AddValueActivitiy.class})
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
        RuntimeExceptionDao<DataPoint, Integer> dataPointDao = openHelper.getRuntimeExceptionDao(DataPoint.class);
        return new GraphService(graphDao, dataPointDao);
    }
}
