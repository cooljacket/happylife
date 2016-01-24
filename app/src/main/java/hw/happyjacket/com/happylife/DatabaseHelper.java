package hw.happyjacket.com.happylife;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jacket on 2016/1/21.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String CREATE_DB = "create table Accounts ("
            + "id integer primary key autoincrement, "
            + "name varchar(100), "
            + "tag varchar(100), "
            + "price real, "
            + "time char(14), "
            + "kind int2)";

    private Context m_Context;

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        m_Context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DB);
        //Toast.makeText(m_Context, "create success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*db.execSQL("drop table if exists Accounts");
        onCreate(db);*/
    }
}