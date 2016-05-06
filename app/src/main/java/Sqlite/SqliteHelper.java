package Sqlite;

/**
 * Created by apple on 9/22/15.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by apple on 9/20/15.
 */
public class SqliteHelper extends SQLiteOpenHelper
{

    private static String DB_PATH;
    private static String DB_NAME;
    private Context context;
    static SQLiteDatabase db;
    private static final int DB_VERSION = 1;

    public SqliteHelper(Context context, String DATABASE_NAME)
    {
        super(context, DATABASE_NAME, null, DB_VERSION);
        this.context = context;
        this.DB_NAME = DATABASE_NAME;
        DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {

    }

    @Override
    public synchronized void close()
    {
        if (db != null)
            db.close();
        super.close();
    }

    //check if database doesn't exist, copy into databses folder
    public boolean checkDataBase()
    {
        boolean checkdb = false;
        try
        {
            String myPath = context.getFilesDir().getAbsolutePath().replace("files", "databases") + File.separator + DB_NAME;
            File dbfile = new File(myPath);
            checkdb = dbfile.exists();
        }
        catch (SQLiteException e)
        {
            System.out.println("Database doesn't exist");
        }

        return checkdb;
    }

    //Create databse for my app
    public void createDataBase() throws IOException
    {
        if (!checkDataBase())
        {
            getWritableDatabase();
            copyDataBase();
        }
    }

    public ArrayList<HashMap<String, String>> getAll(String table)
    {
        return excuteQuery("select * from " + table);
    }

    //get databse for my app
    public ArrayList<HashMap<String, String>> excuteQuery(String sql)
    {
//sql = "SELECT name FROM sqlite_master WHERE type='table'";
        ArrayList<HashMap<String, String>> listData = new ArrayList<HashMap<String, String>>();
        if (db != null)
        {
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor != null)
            {
                String[] columnNames = cursor.getColumnNames();
                cursor.moveToFirst();
                while (!cursor.isAfterLast())
                {
                    HashMap<String, String> item = new HashMap<>();
                    for (int i = 0; i < columnNames.length; ++i)
                    {
                        item.put(columnNames[i], cursor.getString(i));
                    }
                    listData.add(item);
                    cursor.moveToNext();
                }
                cursor.close();
            }
        }
        return listData;
    }


    public boolean excute(String sql)
    {
        try
        {
            db.execSQL(sql);
            Log.d("ERROR", "\n\n\nGOOD");
            return true;
        }
        catch (Exception e)
        {
            Log.d("ERROR", "\n\n\nERROR");
            e.printStackTrace();
            return false;
        }
    }

    public void insert(String table, double lat, double lng)
    {
       boolean a = excute(String.format("insert into %s values ('%s', '%s')", table, Double.toString(lat), Double.toString(lng)));
    }
    public void insert(String table, String place, String address)
    {
        boolean a = excute(String.format("insert into %s values ('%s', '%s')", table, place, address));
    }
    public void delete(String table, double lat, double lng)
    {
        db.delete(table, "Lat = ? and Lng = ?", new String[]{Double.toString(lat), Double.toString(lng)});
    }

    public void delete(String table, String place)
    {
        db.delete(table, "Place = ?", new String[]{ place });
    }

    private void copyDataBase() throws IOException
    {
        //Open your local db as the input stream
        InputStream myInput = context.getAssets().open(DB_NAME);
        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;
        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);
        //transfer bytes from the input file to the output file
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0)
        {
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void openDataBase() throws SQLException
    {
        String myPath = DB_PATH + DB_NAME;
        //String myPath = context.getFilesDir().getAbsolutePath().replace("files", "databases")+File.separator + DB_NAME;
        db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }
}
