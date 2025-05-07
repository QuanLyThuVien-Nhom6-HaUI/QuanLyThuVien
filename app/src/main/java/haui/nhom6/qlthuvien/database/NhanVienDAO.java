package haui.nhom6.qlthuvien.database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import haui.nhom6.qlthuvien.model.NhanVien;
import java.util.ArrayList;
import java.util.List;



public class NhanVienDAO {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public NhanVienDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    @SuppressLint("Range")
    public List<NhanVien> getAllNhanVien() {
        List<NhanVien> nhanVienList = new ArrayList<>();
        db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT * FROM NhanVien";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                NhanVien nhanVien = new NhanVien();
                nhanVien.setMaNhanVien(cursor.getString(cursor.getColumnIndex("maNhanVien")));
                nhanVien.setTenNhanVien(cursor.getString(cursor.getColumnIndex("tenNhanVien")));
                nhanVien.setNgaySinh(cursor.getString(cursor.getColumnIndex("ngaySinh")));
                nhanVien.setQueQuan(cursor.getString(cursor.getColumnIndex("queQuan")));
                nhanVien.setSoDienThoai(cursor.getString(cursor.getColumnIndex("soDienThoai")));
                nhanVienList.add(nhanVien);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return nhanVienList;
    }
}