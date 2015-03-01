package com.example.thanhnguyen.excelshortcuts;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;

public class MyDBHandler extends SQLiteOpenHelper{

    private Context mycontext;
    private static final String TAG = "MyDBHandler";
    private static final String DATABASE_NAME = "shortcuts.db";
    private static final String TABLE_SHORTCUTS = "shortcuts";

    private static final String COL_ID = "_id";
    private static final String COL_SHORTCUT_BUTTON_1 = "shortcutbutton1";
    private static final String COL_SHORTCUT_BUTTON_2 = "shortcutbutton2";
    private static final String COL_SHORTCUT_BUTTON_3 = "shortcutbutton3";
    private static final String COL_SHORTCUT_BUTTON_4 = "shortcutbutton4";
    private static final String COL_DESCRIPTION = "description";

    private String DATABASE_PATH = "data/data/"
            + "com.example.thanhnguyen.excelshortcuts"
            + "/databases/";

    public SQLiteDatabase shortcutDatabase;

    public MyDBHandler(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.mycontext = context;
    }

    /* Creates an empty database on the system and rewrite it with your own database */

    public void createDatabase() throws IOException {
        boolean dbExist = checkDatabase();
        SQLiteDatabase db_Read = null;
        if(!dbExist) {
            db_Read = this.getReadableDatabase();
            db_Read.close();
            try {
                copyDatabase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }
    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */

    private boolean checkDatabase() {
        return mycontext.getDatabasePath(DATABASE_NAME).exists();
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */

    private void copyDatabase() throws IOException {
        // open local db as the input stream
        InputStream myInput = mycontext.getAssets().open(DATABASE_NAME);

        // path to the just created empty db
        String outFileName = DATABASE_PATH + DATABASE_NAME;

        //open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        // transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0 , length);
        }

        // close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void openDatabase() throws SQLException {
        // open the database
        String myPath = DATABASE_PATH + DATABASE_NAME;
        shortcutDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    }

    @Override
    public synchronized void close() {
        if(shortcutDatabase != null) {
            shortcutDatabase.close();
        }
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        Log.i(TAG, CREATE_FTS_TABLE);
//        db.execSQL(CREATE_FTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHORTCUTS);
//        onCreate(db);
    }

    // get shortcut by its id
    Shortcut getShortcut(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.query(TABLE_SHORTCUTS, new String[] {
                                                     COL_ID
                                                    ,COL_SHORTCUT_BUTTON_1
                                                    ,COL_SHORTCUT_BUTTON_2
                                                    ,COL_SHORTCUT_BUTTON_3
                                                    ,COL_SHORTCUT_BUTTON_4
                                                    ,COL_DESCRIPTION}, COL_ID + "=?",
                                             new String[] {String.valueOf(id)}, null, null, null, null);
        if (c != null)
            c.moveToFirst();

        Shortcut shortcut = new Shortcut(Integer.parseInt(c.getString(0))
                                                         ,c.getString(1)
                                                         ,c.getString(2)
                                                         ,c.getString(3)
                                                         ,c.getString(4)
                                                         ,c.getString(5));
        return shortcut;
    }

    // get all shortcuts
    public ArrayList<Shortcut> getAllShortcuts () {
        ArrayList<Shortcut> shortcutList = new ArrayList<Shortcut>();

        String query = "SELECT * FROM " + TABLE_SHORTCUTS + " WHERE 1 ORDER BY description;";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(query,null);

        /* loop through rows and add to list*/
        if (c.moveToFirst()) {
            do {
                Shortcut shortcut = new Shortcut();
                shortcut.set_id(Integer.parseInt(c.getString(0)));
                shortcut.setShortcutButton1(c.getString(1));
                shortcut.setShortcutButton2(c.getString(2));
                shortcut.setShortcutButton3(c.getString(3));
                shortcut.setShortcutButton4(c.getString(4));
                shortcut.setDescription(c.getString(5));
                /* add shortcut to list */
                shortcutList.add(shortcut);
            } while (c.moveToNext());
        }

        return shortcutList;
    }


}
