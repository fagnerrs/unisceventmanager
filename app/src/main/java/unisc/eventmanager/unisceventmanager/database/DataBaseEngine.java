package unisc.eventmanager.unisceventmanager.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by FAGNER on 28/03/2015
 */
public class DataBaseEngine extends SQLiteOpenHelper
{
    private static String m_DatabaseName = "enterprise.db";
    private static int m_Version = 1;

    public DataBaseEngine(Context context) {
        super(context, m_DatabaseName, null, m_Version);


    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        deleteTables(db);
        createTables(db);
    }

    public void createTables(SQLiteDatabase db)
    {

        db.execSQL(new StringBuilder().append(" create table evento ")
                .append(" ( id numeric,   ")
                .append("  descricao text,   ")
                .append("  data_inicial numeric,   ")
                .append("  data_final numeric )   ")
                .toString());

        db.execSQL(new StringBuilder().append(" create table encontro ")
                .append(" ( id numeric,   ")
                .append("  evento_id numeric,   ")
                .append("  descricao text,   ")
                .append("  data_inicial numeric,   ")
                .append("  data_final numeric )  ")
                .toString());

    }

    public void deleteTables(SQLiteDatabase db)
    {
        db.execSQL(new StringBuilder().append(" drop table if exists evento ")
                .toString());
        db.execSQL(new StringBuilder().append(" drop table if exists encontro ")
                .toString());
    }

}
