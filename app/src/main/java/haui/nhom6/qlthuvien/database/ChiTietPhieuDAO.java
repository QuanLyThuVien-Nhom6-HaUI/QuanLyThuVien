package haui.nhom6.qlthuvien.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class ChiTietPhieuDAO {
    private DatabaseHelper dbHelper;

    public ChiTietPhieuDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public long insertChiTietPhieu(String maPhieu, String maSach, int soLuong) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("maPhieu", maPhieu);
        values.put("maSach", maSach);
        values.put("soLuong", soLuong);
        long result = db.insert("ChiTietPhieu", null, values);
        db.close();
        return result;
    }
}