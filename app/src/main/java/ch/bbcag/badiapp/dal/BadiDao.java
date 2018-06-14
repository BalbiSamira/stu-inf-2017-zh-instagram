package ch.bbcag.badiapp.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import java.util.ArrayList;
import java.util.List;
import ch.bbcag.badiapp.database.BadiSqlTable;
import ch.bbcag.badiapp.model.Badi;

public class BadiDao extends BaseDao {
    private static BadiDao instance;
    private BadiDao(Context context) {
        super(context);
    }

    public static synchronized BadiDao getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (instance == null) {
            instance = new BadiDao(context.getApplicationContext());
        }

        // If a db.close() was made before, we need to reopen the database
        if (instance != null && !instance.db.isOpen()) {
            instance.open();
        }
        return instance;
    }

    public List<Badi> getAll() {
        String[] projection = {
                BadiSqlTable._ID,
                BadiSqlTable.COLUMN_NAME,
                BadiSqlTable.COLUMN_ORT,
                BadiSqlTable.COLUMN_KANTON,
                BadiSqlTable.COLUMN_IS_FAVORITE
        };

        String sortOrder = String.format("%s ASC, %s ASC", BadiSqlTable.COLUMN_ORT, BadiSqlTable.COLUMN_NAME);

        Cursor cursor = db.query(
                BadiSqlTable.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        List<Badi> result = new ArrayList<>();
        while (cursor.moveToNext()) {
            Badi badi = new Badi();
            badi.setId(cursor.getInt(0));
            badi.setName(cursor.getString(1));
            badi.setOrt(cursor.getString(2));
            badi.setKanton(cursor.getString(3));
            badi.setIsFavorite(cursor.getInt(4) != 0);
            result.add(badi);
        }

        cursor.close();
        return result;
    }

    public int getFavoritesCount() {
        String[] projection = {
                BadiSqlTable._ID
        };

        String where = BadiSqlTable.COLUMN_IS_FAVORITE + " = ?";
        String[] args = {"1"};

        Cursor cursor = db.query(
                BadiSqlTable.TABLE_NAME,
                projection,
                where,
                args,
                null,
                null,
                null
        );

        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public long add(Badi badi) throws SQLException {
        ContentValues values = new ContentValues();
        values.put(BadiSqlTable._ID, badi.getId());
        values.put(BadiSqlTable.COLUMN_NAME, badi.getName());
        values.put(BadiSqlTable.COLUMN_KANTON, badi.getKanton());
        values.put(BadiSqlTable.COLUMN_ORT, badi.getOrt());
        long newId = db.insertOrThrow(BadiSqlTable.TABLE_NAME, null, values);
        return newId;
    }

    public List<Badi> getFavorites() {
        String[] projection = {
                BadiSqlTable._ID,
                BadiSqlTable.COLUMN_NAME,
                BadiSqlTable.COLUMN_ORT,
                BadiSqlTable.COLUMN_KANTON
        };

        String sortOrder = String.format("%s ASC, %s ASC", BadiSqlTable.COLUMN_ORT, BadiSqlTable.COLUMN_NAME);
        String where = BadiSqlTable.COLUMN_IS_FAVORITE + " = ?";
        String[] args = {"1"};

        Cursor cursor = db.query(
                BadiSqlTable.TABLE_NAME,
                projection,
                where,
                args,
                null,
                null,
                sortOrder
        );

        List<Badi> result = new ArrayList<>();
        while (cursor.moveToNext()) {
            Badi badi = new Badi();
            badi.setId(cursor.getInt(0));
            badi.setName(cursor.getString(1));
            badi.setOrt(cursor.getString(2));
            badi.setKanton(cursor.getString(3));
            badi.setIsFavorite(true);
            result.add(badi);
        }
        cursor.close();
        return result;
    }

    public void setIsFavorite(int badiId, boolean isFavorite) {
        ContentValues values = new ContentValues();
        values.put(BadiSqlTable.COLUMN_IS_FAVORITE, isFavorite ? 1 : 0);
        String where = BadiSqlTable._ID + " = ?";
        String[] args = {Integer.toString(badiId)};
        db.update(BadiSqlTable.TABLE_NAME, values, where, args);
    }

    public void update(Badi badi) {
        ContentValues values = new ContentValues();
        values.put(BadiSqlTable.COLUMN_NAME, badi.getName());
        values.put(BadiSqlTable.COLUMN_KANTON, badi.getKanton());
        values.put(BadiSqlTable.COLUMN_ORT, badi.getOrt());
        String where = BadiSqlTable._ID + " = ?";
        String[] args = {Integer.toString(badi.getId())};
        db.update(BadiSqlTable.TABLE_NAME, values, where, args);
    }


    public Badi getByIdOrNull(int id) {
        String[] projection = {
                BadiSqlTable._ID,
                BadiSqlTable.COLUMN_NAME,
                BadiSqlTable.COLUMN_ORT,
                BadiSqlTable.COLUMN_KANTON,
                BadiSqlTable.COLUMN_IS_FAVORITE
        };

        String where = BadiSqlTable._ID + " = ?";
        String[] args = {Integer.toString(id)};

        Cursor cursor = db.query(
                BadiSqlTable.TABLE_NAME,
                projection,
                where,
                args,
                null,
                null,
                null
        );

        if (cursor.getCount() == 0) {
            return null;
        }

        cursor.moveToFirst();
        Badi badi = new Badi();
        badi.setId(cursor.getInt(0));
        badi.setName(cursor.getString(1));
        badi.setOrt(cursor.getString(2));
        badi.setKanton(cursor.getString(3));
        badi.setIsFavorite(cursor.getInt(4) != 0);
        cursor.close();
        return badi;
    }
}
