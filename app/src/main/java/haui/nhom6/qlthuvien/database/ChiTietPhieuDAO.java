package haui.nhom6.qlthuvien.database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

public class ChiTietPhieuDAO {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public ChiTietPhieuDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    @SuppressLint("Range")
    public List<String[]> getAllChiTietPhieu() {
        List<String[]> chiTietPhieuList = new ArrayList<>();
        db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT * FROM ChiTietPhieu";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                String[] chiTiet = new String[3];
                chiTiet[0] = cursor.getString(cursor.getColumnIndex("maPhieu"));
                chiTiet[1] = cursor.getString(cursor.getColumnIndex("maSach"));
                chiTiet[2] = cursor.getString(cursor.getColumnIndex("soLuong"));
                chiTietPhieuList.add(chiTiet);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return chiTietPhieuList;
    }
}