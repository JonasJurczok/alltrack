package org.linesofcode.alltrack.graph;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "datapoint")
public class DataPoint{

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private Date datetime;

    @DatabaseField
    private int value;

    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private Graph graph;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataPoint dataPoint = (DataPoint) o;

        if (getId() != dataPoint.getId()) return false;
        if (getValue() != dataPoint.getValue()) return false;
        if (!getDatetime().equals(dataPoint.getDatetime())) return false;
        return getGraph().equals(dataPoint.getGraph());

    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + getDatetime().hashCode();
        result = 31 * result + getValue();
        result = 31 * result + getGraph().hashCode();
        return result;
    }
}
