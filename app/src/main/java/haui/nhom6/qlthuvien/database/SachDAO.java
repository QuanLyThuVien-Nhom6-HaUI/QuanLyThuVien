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

    public SachDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public List<Sach> getAllSach() {
        List<Sach> sachList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Sach", null);

        int maSachIndex = cursor.getColumnIndex("maSach");
        int tenSachIndex = cursor.getColumnIndex("tenSach");
        int tacGiaIndex = cursor.getColumnIndex("tacGia");
        int nhaXuatBanIndex = cursor.getColumnIndex("nhaXuatBan");
        int namXuatBanIndex = cursor.getColumnIndex("namXuatBan");
        int trangThaiIndex = cursor.getColumnIndex("trangThai");
        int theLoaiIndex = cursor.getColumnIndex("theLoai");
        int giaIndex = cursor.getColumnIndex("gia");
        int soLuongIndex = cursor.getColumnIndex("soLuong");

        while (cursor.moveToNext()) {
            Sach sach = new Sach();
            if (maSachIndex >= 0) sach.setMaSach(cursor.getString(maSachIndex));
            if (tenSachIndex >= 0) sach.setTenSach(cursor.getString(tenSachIndex));
            if (tacGiaIndex >= 0) sach.setTacGia(cursor.getString(tacGiaIndex));
            if (nhaXuatBanIndex >= 0) sach.setNhaXuatBan(cursor.getString(nhaXuatBanIndex));
            if (namXuatBanIndex >= 0) sach.setNamXuatBan(cursor.getInt(namXuatBanIndex));
            if (trangThaiIndex >= 0) sach.setTrangThai(cursor.getString(trangThaiIndex));
            if (theLoaiIndex >= 0) sach.setTheLoai(cursor.getString(theLoaiIndex));
            if (giaIndex >= 0) sach.setGia(cursor.getInt(giaIndex));
            if (soLuongIndex >= 0) sach.setSoLuong(cursor.getInt(soLuongIndex));
            sachList.add(sach);
        }

        cursor.close();
        db.close();
        return sachList;
    }

    public void updateSoLuongSach(String maSach, int soLuongMoi) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("soLuong", soLuongMoi);
        db.update("Sach", values, "maSach = ?", new String[]{maSach});
        db.close();
    }
}