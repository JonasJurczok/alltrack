package org.linesofcode.eatr.framework.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

import static com.j256.ormlite.table.TableUtils.createTable;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String LOGTAG = DatabaseHelper.class.getName();

    private static final String DB_NAME = "eatr_database.db";
    private static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        Log.i(LOGTAG, "Table creation finished.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }
}
