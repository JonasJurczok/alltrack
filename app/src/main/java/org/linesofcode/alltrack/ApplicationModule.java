package org.linesofcode.alltrack;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

import org.linesofcode.alltrack.graph.GraphAdapter;
import org.linesofcode.alltrack.graph.GraphModule;
import org.linesofcode.alltrack.graph.GraphService;

import javax.inject.Singleton;

@Module(injects = {MainActivity.class, GraphAdapter.class, GraphService.class})
public class ApplicationModule {

    @Provides
    @Singleton
    public GraphAdapter providesGraphAdapter(GraphService graphService) {
        return new GraphAdapter(graphService);
    }

    @Provides
    @Singleton
    public GraphService graphService() {
        return new GraphService();
    }

}
