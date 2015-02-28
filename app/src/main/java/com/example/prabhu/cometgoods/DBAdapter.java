package com.example.prabhu.cometgoods;

/**
 * Created by Prabhu on 2/23/2015.
 */

import android.database.sqlite.SQLiteOpenHelper;



        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.util.Log;


// TO USE:
// Change the package (at top) to match your project.
// Search for "TODO", and make the appropriate changes.
public class DBAdapter {

    /////////////////////////////////////////////////////////////////////
    //	Constants & Data
    /////////////////////////////////////////////////////////////////////
    // For logging:
    private static final String TAG = "DBAdapter";

    // DB Fields
    public static final String KEY_ROWID = "_id";
    public static final int COL_ROWID = 0;
    /*
     * CHANGE 1:
     */
    // TODO: Setup your fields here:
    public static final String KEY_PID = "productid";
    public static final String KEY_PNAME = "productname";
    public static final String KEY_PPRICE = "productprice";
    public static final String KEY_PDESC = "productdescription";
    public static final String KEY_PTYPE = "producttype";
    public static final String KEY_PIMAGE = "productimage";
    public static final String KEY_PBARGAIN = "productbargain";

    // TODO: Setup your field numbers here (0 = KEY_ROWID, 1=...)
    public static final int COL_PID = 1;
    public static final int COL_PNAME = 2;
    public static final int COL_PPRICE = 3;
    public static final int COL_PDESC = 4;
    public static final int COL_PTYPE = 5;
    public static final int COL_PIMAGE = 6;
    public static final int COL_PBARGAIN = 7;


    public static final String[] ALL_KEYS = new String[] {KEY_ROWID, KEY_PID, KEY_PNAME, KEY_PPRICE,KEY_PDESC,KEY_PTYPE,KEY_PIMAGE,KEY_PBARGAIN};

    // DB info: it's name, and the table we are using (just one).
    public static final String DATABASE_NAME = "MyDb";
    public static final String DATABASE_TABLE = "mainTable";
    // Track DB version if a new version of your app changes the format.
    public static final int DATABASE_VERSION = 2;

    private static final String DATABASE_CREATE_SQL =
            "create table " + DATABASE_TABLE
                    + " (" + KEY_ROWID + " integer primary key autoincrement, "

			/*
			 * CHANGE 2:
			 */
                    // TODO: Place your fields here!
                    // + KEY_{...} + " {type} not null"
                    //	- Key is the column name you created above.
                    //	- {type} is one of: text, integer, real, blob
                    //		(http://www.sqlite.org/datatype3.html)
                    //  - "not null" means it is a required field (must be given a value).
                    // NOTE: All must be comma separated (end of line!) Last one must have NO comma!!
                    + KEY_PID + " text not null, "
                    + KEY_PNAME + " text not null, "
                    + KEY_PPRICE + " integer not null, "
                    + KEY_PDESC + " text not null, "
                    + KEY_PTYPE + " text not null, "
                    + KEY_PIMAGE + " text, "
                    + KEY_PBARGAIN + " int"


                    // Rest  of creation:
                    + ");";

    // Context of application who uses us.
    private final Context context;

    private DatabaseHelper myDBHelper;
    private SQLiteDatabase db;

    /////////////////////////////////////////////////////////////////////
    //	Public methods:
    /////////////////////////////////////////////////////////////////////

    public DBAdapter(Context ctx) {
        this.context = ctx;
        myDBHelper = new DatabaseHelper(context);
    }

    // Open the database connection.
    public DBAdapter open() {
        db = myDBHelper.getWritableDatabase();
        return this;
    }

    // Close the database connection.
    public void close() {
        myDBHelper.close();
    }

    // Add a new set of values to the database.
    public long insertRow(String pid,String pname, int pprice,String pdesc, String ptype,String pimage, int pbargain) {
		/*
		 * CHANGE 3:
		 */
        // TODO: Update data in the row with new fields.
        // TODO: Also change the function's arguments to be what you need!
        // Create row's data:
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_PID, pid);
        initialValues.put(KEY_PNAME, pname);
        initialValues.put(KEY_PPRICE, pprice);
        initialValues.put(KEY_PDESC, pdesc);
        initialValues.put(KEY_PTYPE, ptype);
        initialValues.put(KEY_PIMAGE, pimage);
        initialValues.put(KEY_PBARGAIN, pbargain);

        // Insert it into the database.
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    // Delete a row from the database, by rowId (primary key)
    public boolean deleteRow(long rowId) {
        String where = KEY_ROWID + "=" + rowId;
        return db.delete(DATABASE_TABLE, where, null) != 0;
    }

    public void deleteAll() {
        Cursor c = getAllRows();
        long rowId = c.getColumnIndexOrThrow(KEY_ROWID);
        if (c.moveToFirst()) {
            do {
                deleteRow(c.getLong((int) rowId));
            } while (c.moveToNext());
        }
        c.close();
    }

    // Return all data in the database.
    public Cursor getAllRows() {
        String where = null;
        Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    // Get a specific row (by rowId)
    public Cursor getRow(long rowId) {
        String where = KEY_ROWID + "=" + rowId;
        Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    // Change an existing row to be equal to new data.
    public boolean updateRow(int rowId,int pid,String pname, int pprice,String pdesc, String ptype,String pimage, int pbargain) {
        String where = KEY_ROWID + "=" + rowId;

		/*
		 * CHANGE 4:
		 */
        // TODO: Update data in the row with new fields.
        // TODO: Also change the function's arguments to be what you need!
        // Create row's data:
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_PID, pid);
        newValues.put(KEY_PNAME, pname);
        newValues.put(KEY_PPRICE, pprice);
        newValues.put(KEY_PDESC, pdesc);
        newValues.put(KEY_PTYPE, ptype);
        newValues.put(KEY_PIMAGE, pimage);
        newValues.put(KEY_PBARGAIN, pbargain);


        // Insert it into the database.
        return db.update(DATABASE_TABLE, newValues, where, null) != 0;
    }



    /////////////////////////////////////////////////////////////////////
    //	Private Helper Classes:
    /////////////////////////////////////////////////////////////////////

    /**
     * Private class which handles database creation and upgrading.
     * Used to handle low-level database access.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase _db) {
            _db.execSQL(DATABASE_CREATE_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading application's database from version " + oldVersion
                    + " to " + newVersion + ", which will destroy all old data!");

            // Destroy old database:
            _db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);

            // Recreate new database:
            onCreate(_db);
        }
    }
}
