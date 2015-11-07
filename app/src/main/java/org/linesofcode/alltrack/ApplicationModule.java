package org.linesofcode.alltrack;

import dagger.Module;
import org.linesofcode.alltrack.graph.GraphModule;

@Module(injects = {MainActivity.class}, includes = {GraphModule.class})
public class ApplicationModule {
}
