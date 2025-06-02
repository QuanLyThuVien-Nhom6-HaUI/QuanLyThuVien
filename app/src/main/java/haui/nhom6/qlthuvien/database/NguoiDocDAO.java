package haui.nhom6.qlthuvien.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import haui.nhom6.qlthuvien.model.NguoiDoc;

import java.util.ArrayList;
import java.util.List;

public class NguoiDocDAO {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public NguoiDocDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    @SuppressLint("Range")
    public List<NguoiDoc> getAllNguoiDoc() {
        List<NguoiDoc> nguoiDocList = new ArrayList<>();
        db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT * FROM NguoiDoc";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                NguoiDoc nguoiDoc = new NguoiDoc();
                nguoiDoc.setMaNguoiDoc(cursor.getString(cursor.getColumnIndex("maNguoiDoc")));
                nguoiDoc.setTenNguoiDoc(cursor.getString(cursor.getColumnIndex("tenNguoiDoc")));
                nguoiDoc.setSoDienThoai(cursor.getString(cursor.getColumnIndex("soDienThoai")));
                nguoiDoc.setGioiTinh(cursor.getString(cursor.getColumnIndex("gioiTinh")));
                nguoiDoc.setCccd(cursor.getString(cursor.getColumnIndex("cccd")));
                nguoiDoc.setDiaChi(cursor.getString(cursor.getColumnIndex("diaChi")));
                nguoiDocList.add(nguoiDoc);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return nguoiDocList;
    }

    @SuppressLint("Range")
    public List<NguoiDoc> getNguoiDocByPage(int offset, int limit) {
        List<NguoiDoc> nguoiDocList = new ArrayList<>();
        db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT * FROM NguoiDoc LIMIT ? OFFSET ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(limit), String.valueOf(offset)});

        if (cursor.moveToFirst()) {
            do {
                NguoiDoc nguoiDoc = new NguoiDoc();
                nguoiDoc.setMaNguoiDoc(cursor.getString(cursor.getColumnIndex("maNguoiDoc")));
                nguoiDoc.setTenNguoiDoc(cursor.getString(cursor.getColumnIndex("tenNguoiDoc")));
                nguoiDoc.setSoDienThoai(cursor.getString(cursor.getColumnIndex("soDienThoai")));
                nguoiDoc.setGioiTinh(cursor.getString(cursor.getColumnIndex("gioiTinh")));
                nguoiDoc.setCccd(cursor.getString(cursor.getColumnIndex("cccd")));
                nguoiDoc.setDiaChi(cursor.getString(cursor.getColumnIndex("diaChi")));
                nguoiDocList.add(nguoiDoc);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return nguoiDocList;
    }

    public int getTotalNguoiDocCount() {
        db = dbHelper.getReadableDatabase();
        String countQuery = "SELECT COUNT(*) FROM NguoiDoc";
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return count;
    }

    @SuppressLint("Range")
    public List<NguoiDoc> timKiemNguoiDoc(String tuKhoa) {
        List<NguoiDoc> ketQua = new ArrayList<>();
        db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT * FROM NguoiDoc WHERE tenNguoiDoc LIKE ? OR maNguoiDoc LIKE ? OR soDienThoai LIKE ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{"%" + tuKhoa + "%", "%" + tuKhoa + "%", "%" + tuKhoa + "%"});

        if (cursor.moveToFirst()) {
            do {
                NguoiDoc nguoiDoc = new NguoiDoc();
                nguoiDoc.setMaNguoiDoc(cursor.getString(cursor.getColumnIndex("maNguoiDoc")));
                nguoiDoc.setTenNguoiDoc(cursor.getString(cursor.getColumnIndex("tenNguoiDoc")));
                nguoiDoc.setSoDienThoai(cursor.getString(cursor.getColumnIndex("soDienThoai")));
                nguoiDoc.setGioiTinh(cursor.getString(cursor.getColumnIndex("gioiTinh")));
                nguoiDoc.setCccd(cursor.getString(cursor.getColumnIndex("cccd")));
                nguoiDoc.setDiaChi(cursor.getString(cursor.getColumnIndex("diaChi")));
                ketQua.add(nguoiDoc);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return ketQua;
    }

    public long themNguoiDoc(NguoiDoc nguoiDoc) {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("maNguoiDoc", nguoiDoc.getMaNguoiDoc());
        values.put("tenNguoiDoc", nguoiDoc.getTenNguoiDoc());
        values.put("soDienThoai", nguoiDoc.getSoDienThoai());
        values.put("gioiTinh", nguoiDoc.getGioiTinh());
        values.put("cccd", nguoiDoc.getCccd());
        values.put("diaChi", nguoiDoc.getDiaChi());
        long result = db.insert("NguoiDoc", null, values);
        db.close();
        return result;
    }

    public long suaNguoiDoc(NguoiDoc nguoiDoc) {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tenNguoiDoc", nguoiDoc.getTenNguoiDoc());
        values.put("soDienThoai", nguoiDoc.getSoDienThoai());
        values.put("gioiTinh", nguoiDoc.getGioiTinh());
        values.put("cccd", nguoiDoc.getCccd());
        values.put("diaChi", nguoiDoc.getDiaChi());
        long result = db.update("NguoiDoc", values, "maNguoiDoc=?", new String[]{nguoiDoc.getMaNguoiDoc()});
        db.close();
        return result;
    }

    public int xoaNguoiDoc(String maNguoiDoc) {
        db = dbHelper.getWritableDatabase();
        int result = db.delete("NguoiDoc", "maNguoiDoc=?", new String[]{maNguoiDoc});
        db.close();
        return result;
    }
}
