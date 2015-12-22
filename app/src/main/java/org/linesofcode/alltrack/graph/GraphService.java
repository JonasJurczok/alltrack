package org.linesofcode.alltrack.graph;


import android.util.Log;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class GraphService {

    public List<Graph> getAll() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Graph> result = realm.where(Graph.class).findAllSorted("name");
        Log.d("GraphService", "Found [" + result.size() + "] graphs to return to the caller.");
        return result;
    }
}
