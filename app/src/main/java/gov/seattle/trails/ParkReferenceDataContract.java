package gov.seattle.trails;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Craig on 7/12/2016.
 */
public class ParkReferenceDataContract {

    public ParkReferenceDataContract() {};


    /*
    define table contents
     */
    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "parks";
        public static final String COLUMN_PARK_NAME = "parkName";
        public static final String COLUMN_PMAID = "pmaid";
        public static final String MARKER_ID = "markerID";
    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ", ";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
            FeedEntry._ID + " INTEGER PRIMARY KEY," +
            FeedEntry.COLUMN_PARK_NAME + TEXT_TYPE + COMMA_SEP +
            FeedEntry.COLUMN_PMAID + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.MARKER_ID + TEXT_TYPE + " )";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;

    public static abstract class ParkReferenceDataHelper extends SQLiteOpenHelper {

        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "ParkReference.db";

        public ParkReferenceDataHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //discard data and start over every time for online data
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }

        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }


}
