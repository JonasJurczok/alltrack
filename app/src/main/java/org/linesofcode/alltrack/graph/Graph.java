package org.linesofcode.alltrack.graph;

import com.j256.ormlite.dao.EagerForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@DatabaseTable(tableName = "graph")
public class Graph {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false, uniqueIndex = true)
    private String name;

    // TODO: add icon support

    // TODO: defensive copies
    @ForeignCollectionField(orderColumnName = "datetime")
    private Collection<DataPoint> datapoints;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<DataPoint> getDatapoints() {
        return datapoints;
    }

    public void setDatapoints(Collection<DataPoint> datapoints) {
        this.datapoints = datapoints;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Graph graph = (Graph) o;

        if (getId() != graph.getId()) return false;
        return getName().equals(graph.getName());

    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + getName().hashCode();
        return result;
    }
}
