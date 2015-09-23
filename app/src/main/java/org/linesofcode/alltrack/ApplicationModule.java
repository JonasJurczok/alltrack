package org.linesofcode.alltrack;

import org.linesofcode.alltrack.graph.GraphAdapter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(injects = {MainActivity.class})
public class ApplicationModule {

    @Provides
    @Singleton
    public GraphAdapter providesGraphAdapter(String[] datasource) {
        return new GraphAdapter(datasource);
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
