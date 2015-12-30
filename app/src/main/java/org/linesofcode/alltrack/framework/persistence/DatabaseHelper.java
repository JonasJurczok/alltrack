package org.linesofcode.alltrack.framework.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;

import org.linesofcode.alltrack.graph.DataPoint;
import org.linesofcode.alltrack.graph.Graph;

import java.sql.SQLException;

import static com.j256.ormlite.table.TableUtils.createTable;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String LOGTAG = DatabaseHelper.class.getName();

    private static final String DB_NAME = "alltrack_database.db";
    private static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            Log.i(LOGTAG, "Creating Datapoint table.");
            createTable(connectionSource, DataPoint.class);
            Log.i(LOGTAG, "Creating Graph table.");
            createTable(connectionSource, Graph.class);
            Log.i(LOGTAG, "Table creation finished.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }
}
