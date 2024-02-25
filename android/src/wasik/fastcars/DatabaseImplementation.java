package wasik.fastcars;

import static wasik.fastcars.ColumnsNames.LAPS;
import static wasik.fastcars.ColumnsNames.LAPTIME;
import static wasik.fastcars.ColumnsNames.MAPNAME;
import static wasik.fastcars.ColumnsNames.RACETIME;
import static wasik.fastcars.ColumnsNames.TABLE;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class DatabaseImplementation implements DatabaseInterface {

    private Context context;
    DatabaseHelper db;

    ArrayList<FiveElementTuple> data;

    public DatabaseImplementation(Context context) {
        this.context = context;
        try {
            db = new DatabaseHelper(context);
        }catch (Exception e){
            System.out.println("SQLERROR");
        }
        data = new ArrayList<>();
    }

    @Override
    public void insertIntoDatabase(FiveElementTuple fiveElementTuple) {
        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(RACETIME, fiveElementTuple.raceTime);
        cv.put(LAPS, fiveElementTuple.numberOfLaps);
        cv.put(LAPTIME, fiveElementTuple.bestLapTime);
        cv.put(MAPNAME, fiveElementTuple.mapName);

        long result = sqLiteDatabase.insert(TABLE, null, cv);
    }


    @Override
    public ArrayList<FiveElementTuple> getFromDatabase() {
        String query = "SELECT * FROM racetimes";
        SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();
        Cursor cursor = null;
        if (sqLiteDatabase != null) {
            cursor = sqLiteDatabase.rawQuery(query, null);
        }
        assert cursor != null;
        if(cursor.getCount() == 0){
            return null;
        }
        data.clear();
        while(cursor.moveToNext()){
            FiveElementTuple tmp = new FiveElementTuple(0, "", "", 0, "");
            tmp.id = cursor.getInt(0);
            tmp.mapName = cursor.getString(1);
            tmp.raceTime = cursor.getString(2);
            tmp.numberOfLaps = cursor.getInt(3);
            tmp.bestLapTime = cursor.getString(4);
            data.add(tmp);
        }
        return data;
    }

    @Override
    public void resetDatabase() {
        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM racetimes");

    }
}