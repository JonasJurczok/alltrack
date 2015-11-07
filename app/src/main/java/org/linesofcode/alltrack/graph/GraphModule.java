package org.linesofcode.alltrack.graph;

import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module(injects = {GraphAdapter.class})
public class GraphModule {

    @Provides
    @Singleton
    public LineDataProvider providesLineProvider() {
        return new LineDataProvider();
    }

    @Provides
    @Singleton
    public GraphAdapter providesGraphAdapter(String[] datasource, LineDataProvider lineProvider) {
        return new GraphAdapter(datasource, lineProvider);
    }

    @Provides
    @Singleton
    public String[] providesDataSource() {
        String[] data = new String[100];

        for (int i = 0; i < 100; i++) {
            data[i] = "Teststring " + i;
        }
        return data;
    }

}
