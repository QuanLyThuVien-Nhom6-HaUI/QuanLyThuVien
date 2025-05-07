package haui.nhom6.qlthuvien.database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import haui.nhom6.qlthuvien.model.Sach;
import java.util.ArrayList;
import java.util.List;


public class SachDAO {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public SachDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    @SuppressLint("Range")
    public List<Sach> getAllSach() {
        List<Sach> sachList = new ArrayList<>();
        db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT * FROM Sach";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Sach sach = new Sach();
                sach.setMaSach(cursor.getString(cursor.getColumnIndex("maSach")));
                sach.setTenSach(cursor.getString(cursor.getColumnIndex("tenSach")));
                sach.setTacGia(cursor.getString(cursor.getColumnIndex("tacGia")));
                sach.setNhaXuatBan(cursor.getString(cursor.getColumnIndex("nhaXuatBan")));
                sach.setNamXuatBan(cursor.getInt(cursor.getColumnIndex("namXuatBan")));
                sach.setTrangThai(cursor.getString(cursor.getColumnIndex("trangThai")));
                sach.setTheLoai(cursor.getString(cursor.getColumnIndex("theLoai")));
                sach.setGia(cursor.getInt(cursor.getColumnIndex("gia")));
                sach.setSoLuong(cursor.getInt(cursor.getColumnIndex("soLuong")));
                sachList.add(sach);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return sachList;
    }
}