package haui.nhom6.qlthuvien.database;

import android.content.ContentValues;
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

    public List<Sach> getSachByPage(int offset, int limit) {
        List<Sach> sachList = new ArrayList<>();
        db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT * FROM Sach LIMIT ? OFFSET ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(limit), String.valueOf(offset)});

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

    public int getTotalSachCount() {
        db = dbHelper.getReadableDatabase();
        String countQuery = "SELECT COUNT(*) FROM Sach";
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return count;
    }

    public long themSach(Sach sach) {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("maSach", sach.getMaSach());
        values.put("tenSach", sach.getTenSach());
        values.put("tacGia", sach.getTacGia());
        values.put("nhaXuatBan", sach.getNhaXuatBan());
        values.put("namXuatBan", sach.getNamXuatBan());
        values.put("trangThai", sach.getTrangThai());
        values.put("theLoai", sach.getTheLoai());
        values.put("gia", sach.getGia());
        values.put("soLuong", sach.getSoLuong());
        long result = db.insert("Sach", null, values);
        db.close();
        return result;
    }

    public long suaSach(Sach sach) {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tenSach", sach.getTenSach());
        values.put("tacGia", sach.getTacGia());
        values.put("nhaXuatBan", sach.getNhaXuatBan());
        values.put("namXuatBan", sach.getNamXuatBan());
        values.put("trangThai", sach.getTrangThai());
        values.put("theLoai", sach.getTheLoai());
        values.put("gia", sach.getGia());
        values.put("soLuong", sach.getSoLuong());
        long result = db.update("Sach", values, "maSach=?", new String[]{sach.getMaSach()});
        db.close();
        return result;
    }

    public int xoaSach(String maSach) {
        db = dbHelper.getWritableDatabase();
        int result = db.delete("Sach", "maSach=?", new String[]{maSach});
        db.close();
        return result;
    }
}